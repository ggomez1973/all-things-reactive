package com.ggomez1973.coffee;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoSuchCoffeeException extends ResponseStatusException {


    public NoSuchCoffeeException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
