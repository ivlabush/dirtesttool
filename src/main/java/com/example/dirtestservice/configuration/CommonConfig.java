package com.example.dirtestservice.configuration;

import com.example.dirtestservice.dto.TaskResultDto;
import com.example.dirtestservice.entity.TaskResultEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommonConfig {

    private final RunConfig configuration;

    @Bean
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        TypeMap<TaskResultEntity, TaskResultDto> taskResultTypeMap
                = mapper.createTypeMap(TaskResultEntity.class, TaskResultDto.class);

        taskResultTypeMap.addMappings(m -> m.map(src -> src.getTask().getId(), TaskResultDto::setTaskId));

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
                        ((HttpStatusCodeException) throwable).getStatusCode().value(),
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
