package com.tl.pokedex.util;

import com.tl.pokedex.constant.ErrorConstant;
import com.tl.pokedex.exception.PokedexGenericException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ValidationUtil {
    private final Validator validator;

    public ValidationUtil(Validator validator) {
        this.validator = validator;
    }

    public <T> void isValidOrFail(Class<?> objectToValidateClass, T objectToValidate){
        if(objectToValidate == null){
            throwValidationError(objectToValidateClass, ErrorConstant.OBJECT_CANNOT_BE_NULL_MESSAGE);
        }

        Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);

        boolean hasViolation = !violations.isEmpty();

        if(hasViolation){
            String errorCauses = violations.stream()
                    .map(violation -> {
                        String message = violation.getMessage();
                        Path property = violation.getPropertyPath();

                        return String.format("[%s]: [%s]", property, message);
                    })
                    .collect(Collectors.joining(", "));

            throwValidationError(objectToValidateClass, errorCauses);
        }
    }

    private void throwValidationError(Class<?> objectToValidateClass, String errorCause){
        String className = objectToValidateClass != null ? objectToValidateClass.getSimpleName() : "?";

        String errorMessage = ErrorConstant.VALIDATION_ERROR_MESSAGE
                .formatted(className, errorCause);

        throw new PokedexGenericException(errorMessage);
    }
}
