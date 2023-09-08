package com.example.dirtestservice.validator;

import com.example.dirtestservice.validator.impl.UrlValidatorImpl;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UrlValidatorTest {

    @Test
    public void testValidUrl() {
        assertThat(new UrlValidatorImpl().isValid("http://localhost:8000",
                new ConstraintValidatorContextImpl(null, null, null, null, null, null)))
                .isEqualTo(true);
    }

    @Test
    public void testInvalidUrl() {
        assertThat(new UrlValidatorImpl().isValid("localhost:8000",
                new ConstraintValidatorContextImpl(null, null, null, null, null, null)))
                .isEqualTo(false);
    }
}
