package com.ridango.game.exception;

/**
 * Thrown when no cocktail is fetched.
 */
public class NoCocktailFoundException extends RuntimeException {
    private final String message;
    private final Throwable cause;

    public NoCocktailFoundException(String message) {
        this(message, null);
    }

    public NoCocktailFoundException(String message, Throwable cause) {
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
