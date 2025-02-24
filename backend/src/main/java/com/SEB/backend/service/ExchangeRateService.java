package com.SEB.backend.service;

import com.SEB.backend.dto.ExchangeRateDTO;
import com.SEB.backend.dto.ExchangeRateHistoryDTO;
import com.SEB.backend.entity.ExchangeRate;
import com.SEB.backend.exception.ExchangeRateServiceException;
import com.SEB.backend.mapper.ExchangeRateHistoryMapper;
import com.SEB.backend.mapper.ExchangeRateMapper;
import com.SEB.backend.model.Currency;
import com.SEB.backend.parser.ExchangeRateXmlParser;
import com.SEB.backend.repository.ExchangeRateRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);

    private final WebClient webClient;
    private final ExchangeRateXmlParser exchangeRateXmlParser;
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateMapper exchangeRateMapper;
    private final ExchangeRateHistoryMapper exchangeRateHistoryMapper;

    @Value("${exchange.rate.api.history-endpoint}")
    private String historyEndpoint;

    @Value("${exchange.rate.api.daily-endpoint}")
    private String dailyEndpoint;


    @PostConstruct
    @Transactional
    public void init() {
        try {
            long count = exchangeRateRepository.count();
            if (count == 0) {
                logger.info("Initializing exchange rate database with 90 days historical data");
                List<ExchangeRate> rates = fetchLast90DaysRates();
                exchangeRateRepository.saveAll(rates);
                logger.info("Successfully populated database with {} exchange rates", rates.size());
            }
        } catch (Exception e) {
            logger.error("Failed to initialize exchange rates database", e);
            throw new ExchangeRateServiceException("Failed to initialize exchange rates", e);
        }
    }

    /**
     * Returns all exchange rates for the most recent date in the database
     * @return List of ExchangeRate objects
     */
    @Transactional(readOnly = true)
    public List<ExchangeRateDTO> getAllExchangeRates() {
        try {
            Optional<LocalDate> latestDate = exchangeRateRepository.findMaxDate();
            if (latestDate.isEmpty()) {
                logger.warn("No exchange rates found in database");
                return Collections.emptyList();
            }

            return exchangeRateMapper.toDTOList(exchangeRateRepository.findByDate(latestDate.get()));
        } catch (Exception e) {
            logger.error("Error retrieving latest exchange rates", e);
            throw new ExchangeRateServiceException("Failed to retrieve latest exchange rates", e);
        }
    }

    /**
     * Returns exchange rate history for a specific currency over the last 90 days
     * @param currency The currency to get history for
     * @return List of ExchangeRate objects sorted by date
     */
    @Transactional(readOnly = true)
    public ExchangeRateHistoryDTO getExchangeRateHistory(Currency currency) {
        try {
            if (currency == null) throw new IllegalArgumentException("Currency not specified");
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(90);
            return exchangeRateHistoryMapper.toDTO(exchangeRateRepository.findByCurrencyAndDateBetweenOrderByDateDesc(currency, startDate, endDate));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid currency provided: {}", currency, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving exchange rate history for currency: {}", currency, e);
            throw new ExchangeRateServiceException("Failed to retrieve exchange rate history for " + currency, e);
        }
    }

    private List<ExchangeRate> fetchLast90DaysRates() {
        try {
            String xmlResponse = webClient.get()
                    .uri(historyEndpoint)  // Now using the property
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (xmlResponse == null || xmlResponse.isEmpty())
                throw new ExchangeRateServiceException("Received empty response from exchange rate API");

            return exchangeRateXmlParser.parse(xmlResponse);
        } catch (WebClientException e) {
            throw new ExchangeRateServiceException("Failed to fetch exchange rates from API", e);
        }
    }

    @Transactional
    public void fetchAndSaveLatestRates() {
        try {
            String xmlResponse = webClient.get()
                    .uri(dailyEndpoint)  // Now using the property
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (xmlResponse == null || xmlResponse.isEmpty()) {
                throw new ExchangeRateServiceException("Received empty response from exchange rate API");
            }

            List<ExchangeRate> newRates = exchangeRateXmlParser.parse(xmlResponse);
            exchangeRateRepository.saveAll(newRates);
        } catch (WebClientException e) {
            throw new ExchangeRateServiceException("Failed to fetch exchange rates from API", e);
        }
    }
}
