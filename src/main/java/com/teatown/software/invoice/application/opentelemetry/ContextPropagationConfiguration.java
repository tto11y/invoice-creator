package com.teatown.software.invoice.application.opentelemetry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.support.ContextPropagatingTaskDecorator;

@Configuration(proxyBeanMethods = false)
public class ContextPropagationConfiguration {

    /**
     *
     * {@link <a href="https://spring.io/blog/2025/11/18/opentelemetry-with-spring-boot#beware-the-context">Spring - Beware the context</a>}
     *
     * <p>One thing you'll note if you're dealing with methods that switch threads, e.g., @Async annotated methods or the use of Spring's AsyncTaskExecutor,
     * is that the context is lost in the new thread. The lost context affects logs,
     * which don't include the trace ID anymore, and traces, which lose spans.
     * </p>
     *
     * <p>
     * The context is lost because it is stored inside a ThreadLocal, which doesn't get transferred into the new thread.
     * </p>
     * <p>
     *     However, the solution is quite simple:
     *     </p>
     * <ul>
     *   <li>Use the ContextPropagatingTaskDecorator in the AsyncTaskExecutor (which is also used for @Async annotated methods). </li>
     *   <li>The ContextPropagatingTaskDecorator uses Micrometer's Context Propagation API to make sure that the trace context is transferred to new threads.</li>
     *   <li>Installing the ContextPropagatingTaskDecorator is simple: just define a @Bean method, like below</li>
     * </ul>
     *
     * <p>Spring Boot's auto-configuration looks for TaskDecorator beans and installs them into the AsyncTaskExecutor.</p>
     * <p>With the ContextPropagatingTaskDecorator in place, the context is now transferred to new threads, fixing lost trace IDs in logs and lost spans.</p>
     * */
    @Bean
    public ContextPropagatingTaskDecorator contextPropagatingTaskDecorator() {
        return new ContextPropagatingTaskDecorator();
    }

}
