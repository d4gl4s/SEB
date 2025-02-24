package com.SEB.backend.config;

import com.SEB.backend.scheduler.ExchangeRateUpdateJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Value("${scheduler.cron.expression}") // Inject from properties file
    private String cronExpression;

    @Bean
    public JobDetail exchangeRateJobDetail() {
        return JobBuilder.newJob(ExchangeRateUpdateJob.class)
                .withIdentity("exchangeRateJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger exchangeRateJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(exchangeRateJobDetail())
                .withIdentity("exchangeRateTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)) // Runs daily at 16:00 CET
                .build();
    }
}
