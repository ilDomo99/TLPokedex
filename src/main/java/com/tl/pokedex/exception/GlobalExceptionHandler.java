package com.tl.pokedex.exception;

import com.tl.pokedex.constant.DateConstant;
import com.tl.pokedex.constant.ErrorConstant;
import com.tl.pokedex.dto.error.ErrorMessage;
import com.tl.pokedex.dto.error.ValidationErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles generic runtime exceptions and returns a standardized {@link ErrorMessage} response.
     *
     * @param ex the exception that was thrown
     * @param request the HTTP request during which the exception occurred
     * @return a ResponseEntity containing an {@link ErrorMessage} with details about the error
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleGenericException(Exception ex, HttpServletRequest request) {
        String uri = request.getRequestURI();

        Instant now = Instant.now();

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern(DateConstant.TIMESTAMP_DATE_FORMAT)
                .withZone(ZoneOffset.UTC);

        String timestamp = formatter.format(now);

        ErrorMessage errorMessage = new ErrorMessage(
                timestamp,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                uri,
                ErrorConstant.GENERIC_EXCEPTION_MESSAGE
        );

        log.error(ErrorConstant.GENERIC_EXCEPTION_LOG, ex);

        return ResponseEntity.internalServerError().body(errorMessage);
    }

    /**
     * Handles {@link ConstraintViolationException} by mapping the validation errors and constructing
     * a standardized {@link ErrorMessage} response.
     *
     * @param ex the {@link ConstraintViolationException} instance containing details about validation errors
     * @param request the {@link HttpServletRequest} during which the validation error occurred, used for extracting request URI
     * @return a {@link ResponseEntity} containing an {@link ErrorMessage} that describes the validation errors and their context
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {

        final String uri = request.getRequestURI();

        List<ValidationErrorMessage> validationErrorMessages = ex.getConstraintViolations().stream()
                .map(constraintViolation -> {

                    String classWithError = String.valueOf(constraintViolation.getRootBeanClass());

                    String[] methodAndInvalidParameter = String.valueOf(constraintViolation.getPropertyPath())
                            .split("\\.");

                    String method = methodAndInvalidParameter[0];
                    String invalidParameter = methodAndInvalidParameter[1];
                    String invalidValue = String.valueOf(constraintViolation.getInvalidValue());
                    String reason = constraintViolation.getMessage();

                    return new ValidationErrorMessage(classWithError, method, invalidParameter, invalidValue, reason);
                })
                .toList();

        validationErrorMessages
                .forEach(validationErrorMessage -> log.error(ErrorConstant.BAD_REQUEST_INVALID_VALUE_LOG,
                        uri,
                        validationErrorMessage.getClassWithError(),
                        validationErrorMessage.getMethod(),
                        validationErrorMessage.getInvalidParameter(),
                        validationErrorMessage.getInvalidValue(),
                        validationErrorMessage.getReason(),
                        ex));

        String badRequestMessage = validationErrorMessages.stream()
                .map(validationErrorMessage -> ErrorConstant.BAD_REQUEST_INVALID_VALUE_MESSAGE.formatted(
                        validationErrorMessage.getInvalidValue(),
                        validationErrorMessage.getReason()
                ))
                .collect(Collectors.joining("\n"));

        Instant now = Instant.now();

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern(DateConstant.TIMESTAMP_DATE_FORMAT)
                .withZone(ZoneOffset.UTC);

        String timestamp = formatter.format(now);

        ErrorMessage errorMessage = new ErrorMessage(
                timestamp,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                uri,
                badRequestMessage
        );

        return ResponseEntity.badRequest().body(errorMessage);
    }
}
