package com.SEB.backend.controller;

import com.SEB.backend.dto.ExchangeRateDTO;
import com.SEB.backend.dto.ExchangeRateHistoryDTO;
import com.SEB.backend.entity.ExchangeRate;
import com.SEB.backend.model.Currency;
import com.SEB.backend.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exchange-rates")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public ResponseEntity<List<ExchangeRateDTO>> getLatestRates() {
        return ResponseEntity.ok(exchangeRateService.getAllExchangeRates());
    }

    @GetMapping("/{currency}/history")
    public ResponseEntity<ExchangeRateHistoryDTO> getHistory(@PathVariable Currency currency) {
        return ResponseEntity.ok(exchangeRateService.getExchangeRateHistory(currency));
    }
}