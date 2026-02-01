package com.azeauj.takitransfer.core.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.core.Conditions;
import org.zalando.logbook.core.DefaultHttpLogWriter;
import org.zalando.logbook.core.DefaultSink;
import org.zalando.logbook.core.StatusAtLeastStrategy;
import org.zalando.logbook.json.JsonHttpLogFormatter;

@Configuration
public class LoggerConfiguration {
    @Bean
    @Scope("prototype")
    public Logger logger(InjectionPoint injectionPoint) {
        MethodParameter methodParameter = injectionPoint.getMethodParameter();
        Class<?> klass;
        if (methodParameter == null) {
            klass = injectionPoint.getField().getDeclaringClass();
        } else {
            klass = methodParameter.getContainingClass();
        }
        return LoggerFactory.getLogger(klass);
    }

    @Bean
    public Logbook logbook() {
        return Logbook.builder()
                .sink(
                        new DefaultSink(
                                new JsonHttpLogFormatter(),
                                new DefaultHttpLogWriter()
                        )
                ).strategy(new StatusAtLeastStrategy(HttpStatus.OK.value()))
                .build();
    }
}
