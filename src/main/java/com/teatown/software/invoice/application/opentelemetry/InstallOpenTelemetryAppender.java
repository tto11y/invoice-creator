package com.teatown.software.invoice.application.opentelemetry;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * InstallOpenTelemetryAppender lets the OpenTelemetryAppender know which OpenTelemetry API instance to use
 * */
@Component
class InstallOpenTelemetryAppender implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstallOpenTelemetryAppender.class);

    private final OpenTelemetry openTelemetry;

    @Autowired
    InstallOpenTelemetryAppender(final OpenTelemetry openTelemetry) {
        this.openTelemetry = openTelemetry;
    }

    @Override
    public void afterPropertiesSet() {

        LOGGER.info("installing OpenTelemetry API instance ({}) in OpenTelemetryAppender", this.openTelemetry.toString());
        OpenTelemetryAppender.install(this.openTelemetry);
    }

}
