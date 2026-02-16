package com.teatown.software.invoice.application.opentelemetry;

import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmClassLoadingMeterConventions;
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmCpuMeterConventions;
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmMemoryMeterConventions;
import io.micrometer.core.instrument.binder.jvm.convention.otel.OpenTelemetryJvmThreadMeterConventions;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.OpenTelemetryServerRequestObservationConvention;

import java.util.List;

/**
 * {@link <a href="https://spring.io/blog/2025/11/18/opentelemetry-with-spring-boot#exporting-metrics">Spring - Exporting Metrics</a>}
 *
 * <p>The Micrometer team has also added so-called observation conventions for OpenTelemetry.</p>
 * <p>Signals in OpenTelemetry adhere to the OpenTelemetry Semantic Convention, and the observation conventions in Micrometer implement the stable parts of the OpenTelemetry Semantic Convention.</p>
 * <p>To use them in Spring Boot, you have to define some configuration (see Spring Beans below)</p>
 *
 */
@Configuration(proxyBeanMethods = false)
public class ObservationConventionsConfig {

    @Bean
    public OpenTelemetryServerRequestObservationConvention openTelemetryServerRequestObservationConvention() {
        return new OpenTelemetryServerRequestObservationConvention();
    }

    @Bean
    public ProcessorMetrics processorMetrics(final OpenTelemetryJvmCpuMeterConventions openTelemetryJvmCpuMeterConventions) {
        // you can add custom tags here, if needed
        //  e.g., Tags.of("environment", "production")
        return new ProcessorMetrics(List.of(), openTelemetryJvmCpuMeterConventions);
    }

    @Bean
    public OpenTelemetryJvmCpuMeterConventions openTelemetryJvmCpuMeterConventions() {
        return new OpenTelemetryJvmCpuMeterConventions(Tags.empty());
    }

    @Bean
    public JvmMemoryMetrics jvmMemoryMetrics(final OpenTelemetryJvmMemoryMeterConventions openTelemetryJvmMemoryMeterConventions) {
        // you can add custom tags here, if needed
        //  e.g., Tags.of("environment", "production")
        return new JvmMemoryMetrics(List.of(), openTelemetryJvmMemoryMeterConventions);
    }

    @Bean
    public OpenTelemetryJvmMemoryMeterConventions openTelemetryJvmMemoryMeterConventions() {
        return new OpenTelemetryJvmMemoryMeterConventions(Tags.empty());
    }

    @Bean
    public JvmThreadMetrics jvmThreadMetrics(final OpenTelemetryJvmThreadMeterConventions openTelemetryJvmThreadMeterConventions) {
        // you can add custom tags here, if needed
        //  e.g., Tags.of("environment", "production")
        return new JvmThreadMetrics(List.of(), openTelemetryJvmThreadMeterConventions);
    }

    @Bean
    public OpenTelemetryJvmThreadMeterConventions openTelemetryJvmThreadMeterConventions() {
        return new OpenTelemetryJvmThreadMeterConventions(Tags.empty());
    }

    @Bean
    public ClassLoaderMetrics classLoaderMetrics() {
        return new ClassLoaderMetrics(new OpenTelemetryJvmClassLoadingMeterConventions());
    }

}