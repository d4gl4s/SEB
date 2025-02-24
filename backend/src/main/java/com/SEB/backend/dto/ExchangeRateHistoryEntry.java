package com.SEB.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ExchangeRateHistoryEntry {
    private LocalDate date;
    private BigDecimal rate;
}