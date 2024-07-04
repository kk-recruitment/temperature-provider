package com.recruitment.temperatureprovider.temperature.exception;

public class FileReadingException extends RuntimeException {
    private static final String MESSAGE = "Cannot proper read file: %s";

    public FileReadingException(String message) {
        super(MESSAGE.formatted(message));
    }
}
