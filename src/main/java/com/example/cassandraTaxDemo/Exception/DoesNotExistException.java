package com.example.cassandraTaxDemo.Exception;

public class DoesNotExistException extends RuntimeException{

    public DoesNotExistException(String message){
        super(message);
    }

}
