package com.pohorilyi.vmi;

/**
 * Thrown when repository can't find entity by specified params.
 */
class EntityNotFoundException extends RuntimeException {

    EntityNotFoundException(){
        super();
    }
}
