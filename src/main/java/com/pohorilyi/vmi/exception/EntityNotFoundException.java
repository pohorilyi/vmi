package com.pohorilyi.vmi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when repository can't find entity by specified params.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Drink")
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
        super();
    }
}
