package com.ashish.tasknotificationsystem.Exception;

public class InvalidJwtException extends RuntimeException{
    public InvalidJwtException(String message){
        super(message);
    }
}
