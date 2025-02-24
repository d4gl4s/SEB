package com.SEB.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ExchangeRateDTO {
    private String currencyCode;
    private String currencyName;
    private String flag;
    private String symbol;
    private BigDecimal rate;
    private LocalDate date;
}