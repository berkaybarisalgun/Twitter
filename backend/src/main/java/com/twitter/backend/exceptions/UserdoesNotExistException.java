package com.twitter.backend.exceptions;

public class UserdoesNotExistException extends RuntimeException{

    public UserdoesNotExistException(){
        super("The user you are looking for does not exist");
    }
}
