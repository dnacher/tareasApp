package com.software.tareasApp.exceptions;

public class TareasAppException extends RuntimeException{

    private int code;

    public TareasAppException(String message) {
        super(message);
    }

    public TareasAppException(int code, String message) {
        super(message);
        this.code = code;
    }

}
