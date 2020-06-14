package com.pohorilyi.vmi.exception;

/**
 * Thrown when repository can't find entity by specified params.
 */
public class DrinkValidationException extends RuntimeException {

    public DrinkValidationException() {
        super();
    }

    public DrinkValidationException(String message) {
        super(message);
    }
}
