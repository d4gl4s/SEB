package com.SEB.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ExchangeRateHistoryDTO {
    private String currencyCode;
    private String currencyName;
    private String flag;
    private String symbol;
    private List<ExchangeRateHistoryEntry> entries;
}