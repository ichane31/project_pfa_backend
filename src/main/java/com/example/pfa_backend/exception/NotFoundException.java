package com.example.pfa_backend.exception;

public class NotFoundException extends Exception{
    public NotFoundException(String message){
        super(message);
    }

    public NotFoundException() {}
}
