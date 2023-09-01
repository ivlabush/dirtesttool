package com.example.dirtestservice.validator.impl;

import com.example.dirtestservice.validator.Url;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class UrlValidatorImpl implements ConstraintValidator<Url, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            new URL(value).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
