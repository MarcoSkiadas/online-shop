package org.example.backend.exceptions;

public class InvalidIdException extends RuntimeException {

    public InvalidIdException(String message) {
        super(message);
    }
}
