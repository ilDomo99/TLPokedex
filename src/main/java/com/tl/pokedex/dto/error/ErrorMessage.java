package com.tl.pokedex.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorMessage {
    private String timestamp;
    private Integer status;
    private String error;
    private String path;
    private String message;
}
