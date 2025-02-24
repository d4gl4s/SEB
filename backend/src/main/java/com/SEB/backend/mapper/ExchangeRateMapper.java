package com.SEB.backend.mapper;

import com.SEB.backend.dto.ExchangeRateDTO;
import com.SEB.backend.entity.ExchangeRate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExchangeRateMapper {
    public ExchangeRateDTO toDTO(ExchangeRate exchangeRate) {
        return ExchangeRateDTO.builder()
                .currencyCode(exchangeRate.getCurrency().name())
                .currencyName(exchangeRate.getCurrency().getCurrencyName())
                .flag(exchangeRate.getCurrency().getFlag())
                .symbol(exchangeRate.getCurrency().getSymbol())
                .rate(exchangeRate.getRate())
                .date(exchangeRate.getDate())
                .build();
    }

    public List<ExchangeRateDTO> toDTOList(List<ExchangeRate> exchangeRates) {
        return exchangeRates.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}