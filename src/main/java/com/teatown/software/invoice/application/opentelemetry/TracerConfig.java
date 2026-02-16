package com.teatown.software.invoice.application.opentelemetry;

import io.micrometer.tracing.Tracer;
import org.springframework.boot.micrometer.tracing.opentelemetry.autoconfigure.otlp.OtlpTracingConnectionDetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * <p>To enable tracing via OTLP, the Micrometer implementation requires the property management.opentelemetry.tracing.export.otlp.endpoint to be set in the application properties.</p>
 * <p>Otherwise, the bean {@link OtlpTracingConnectionDetails} will not be created,
 * and as a consequence neither {@link io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter} nor {@link io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter} will be instantiated.</p>
 *
 * <p>NOTE: This configuration serves solely the purpose of debugging and documenting. That's why it's only active in conjunction with the dev profile.</p>
 * */
@Profile("dev")
@Configuration
public class TracerConfig {

    private Tracer tracer;

    public TracerConfig(Tracer tracer) {
        this.tracer = tracer;
    }
}
