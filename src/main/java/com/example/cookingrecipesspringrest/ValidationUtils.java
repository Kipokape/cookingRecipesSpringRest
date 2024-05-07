package com.example.cookingrecipesspringrest;


import com.example.cookingrecipesspringrest.exception.ServiceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;


@Service
public class ValidationUtils {

    private final Validator validator;

    @Autowired
    public ValidationUtils(LocalValidatorFactoryBean localValidatorFactoryBean) {
        validator = localValidatorFactoryBean.getValidator();
    }

    public <T> void validation(T req) {
        if (req != null) {
            Set<ConstraintViolation<T>> result = validator.validate(req);
            if (!result.isEmpty()) {
                String resultValidations = result.stream()
                        .map(ConstraintViolation::getMessage)
                        .reduce((s, s2) -> s + ". " + s2).orElse("");
                throw new ServiceException(resultValidations);
            }
        }
    }

}
