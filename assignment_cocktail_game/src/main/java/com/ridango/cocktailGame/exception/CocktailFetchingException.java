package com.ridango.cocktailGame.exception;

public class CocktailFetchingException extends RuntimeException {
    private final String message;
    private final Throwable cause;

    public CocktailFetchingException(String message) {
        this(message, null);
    }

    public CocktailFetchingException(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
