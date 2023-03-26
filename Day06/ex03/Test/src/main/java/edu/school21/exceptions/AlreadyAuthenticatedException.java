package edu.school21.exceptions;

public class AlreadyAuthenticatedException extends RuntimeException{
    public AlreadyAuthenticatedException(){

    }
    public AlreadyAuthenticatedException(String msg){
        super(msg);
    }
}
