package com.SEB.backend.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Configuration
public class WebClientConfig {

    @Value("${exchange.rate.api.base-url}") // Inject value from properties
    private String exchangeRateApiBaseUrl;

    @Bean
    public WebClient webClient(WebClient.Builder builder) throws SSLException {
        // Disable SSL verification (use only in development!)
        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        HttpClient httpClient = HttpClient.create()
                .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

        return builder
                .baseUrl(exchangeRateApiBaseUrl) // Use configured base URL
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
