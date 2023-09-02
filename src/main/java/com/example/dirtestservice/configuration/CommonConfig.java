package com.example.dirtestservice.configuration;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommonConfig {

    private final RunConfiguration configuration;

    @Bean
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return mapper;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        StopConfig config = configuration.getStop();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(config.getBackoff());
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        ExceptionClassifierRetryPolicy retryPolicy = new ExceptionClassifierRetryPolicy();
        retryPolicy.setExceptionClassifier(configureStatusCodeBasedRetryPolicy(config));
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

    private Classifier<Throwable, RetryPolicy> configureStatusCodeBasedRetryPolicy(StopConfig config) {
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(1 + config.getRetries());
        NeverRetryPolicy neverRetryPolicy = new NeverRetryPolicy();

        return throwable -> {
            if (throwable instanceof HttpStatusCodeException) {
                return getRetryPolicyForStatus(config,
                        ((HttpStatusCodeException)throwable).getStatusCode().value(),
                        simpleRetryPolicy,
                        neverRetryPolicy);
            }
            return neverRetryPolicy;
        };
    }

    private RetryPolicy getRetryPolicyForStatus(StopConfig config,
                                                int httpStatusCode,
                                                SimpleRetryPolicy simpleRetryPolicy,
                                                NeverRetryPolicy neverRetryPolicy) {
        if (config.getCodes().contains(httpStatusCode)) {
            return simpleRetryPolicy;
        }
        return neverRetryPolicy;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
