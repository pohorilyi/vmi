package com.pohorilyi.vmi.exception;

/**
 * Thrown when repository can't find entity by specified params.
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(){
        super();
    }
}
