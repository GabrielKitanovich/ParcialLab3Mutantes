package org.example.exceptions;

public class DnaAlreadyExistsException extends RuntimeException {
    public DnaAlreadyExistsException(String message) {
        super(message);
    }
}
