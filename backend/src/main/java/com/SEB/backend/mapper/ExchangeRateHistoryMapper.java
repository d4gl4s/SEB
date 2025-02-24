package com.SEB.backend.mapper;

import com.SEB.backend.dto.ExchangeRateHistoryDTO;
import com.SEB.backend.dto.ExchangeRateHistoryEntry;
import com.SEB.backend.entity.ExchangeRate;
import com.SEB.backend.model.Currency;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExchangeRateHistoryMapper {

    public ExchangeRateHistoryDTO toDTO(List<ExchangeRate> exchangeRates) {
        if (exchangeRates == null || exchangeRates.isEmpty()) return null;

        // Get the first exchange rate to extract currency information
        Currency currency = exchangeRates.getFirst().getCurrency();

        // Convert exchange rates to history entries
        List<ExchangeRateHistoryEntry> entries = exchangeRates.stream()
                .map(rate -> ExchangeRateHistoryEntry.builder()
                        .date(rate.getDate())
                        .rate(rate.getRate())
                        .build())
                .sorted(Comparator.comparing(ExchangeRateHistoryEntry::getDate))
                .collect(Collectors.toList());

        // Build the DTO with currency information and sorted entries
        return ExchangeRateHistoryDTO.builder()
                .currencyCode(currency.name())  // Assuming Currency is an enum
                .currencyName(currency.getCurrencyName())
                .flag(currency.getFlag())
                .symbol(currency.getSymbol())
                .entries(entries)
                .build();
    }
}