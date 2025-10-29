package com.tl.pokedex.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PokedexGenericException extends ResponseStatusException {
    public PokedexGenericException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
