package com.SEB.backend.scheduler;

import com.SEB.backend.service.ExchangeRateService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateUpdateJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateUpdateJob.class);

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateUpdateJob(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public void execute(JobExecutionContext context) {
        try {
            logger.info("Fetching and updating exchange rates...");
            exchangeRateService.fetchAndSaveLatestRates();
            logger.info("Exchange rates updated successfully.");
        } catch (Exception e) {
            logger.error("Error updating exchange rates", e);
        }
    }
}
