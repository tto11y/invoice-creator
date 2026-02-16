package com.example.invoice.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(InvoicePdfProperties.class)
public class InvoiceConfiguration {
}
