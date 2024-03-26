package com.twitter.backend.exceptions;

public class EmailAlreadyTakenException extends RuntimeException {
    public EmailAlreadyTakenException() {
        super("The email provided is already in use");
    }
}
