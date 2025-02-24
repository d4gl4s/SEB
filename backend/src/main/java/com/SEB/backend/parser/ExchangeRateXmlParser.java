package com.SEB.backend.parser;

import com.SEB.backend.entity.ExchangeRate;
import com.SEB.backend.model.Currency;
import com.SEB.backend.model.ExchangeRateEnvelope;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExchangeRateXmlParser {
    private final XmlMapper xmlMapper;

    public ExchangeRateXmlParser() {
        this.xmlMapper = new XmlMapper();
    }

    public List<ExchangeRate> parse(String xml) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try {
            ExchangeRateEnvelope envelope = xmlMapper.readValue(xml, ExchangeRateEnvelope.class);

            // Get the first date cube (assuming we're dealing with a single day's data)
            List<ExchangeRateEnvelope.Cube.DateCube> dateCubes = envelope.getCube().getDateCubes();

            for (ExchangeRateEnvelope.Cube.DateCube dateCube : dateCubes) {
                LocalDate date = LocalDate.parse(dateCube.getTime());
                for (ExchangeRateEnvelope.Cube.CurrencyCube currencyCube : dateCube.getCurrencyCubes()) {
                    try {
                        Currency currency = Currency.valueOf(currencyCube.getCurrency());
                        BigDecimal rate = new BigDecimal(currencyCube.getRate());
                        exchangeRates.add(new ExchangeRate(currency, date, rate));
                    } catch (IllegalArgumentException e) {
                        // Skip unknown currencies
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse XML response", e);
        }
        return exchangeRates;
    }
}
