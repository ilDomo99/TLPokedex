package com.tl.pokedex.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationErrorMessage {
    private String classWithError;
    private String method;
    private String invalidParameter;
    private String invalidValue;
    private String reason;
}
