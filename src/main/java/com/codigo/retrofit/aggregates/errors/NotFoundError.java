package com.codigo.retrofit.aggregates.errors;

public class NotFoundError extends RuntimeException{

    public NotFoundError(String message){
        super(message);
    }

}
