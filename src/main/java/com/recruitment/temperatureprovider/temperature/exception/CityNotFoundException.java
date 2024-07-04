package com.recruitment.temperatureprovider.temperature.exception;

public class CityNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Could not find data for city: %s";

    public CityNotFoundException(String city) {
        super(MESSAGE.formatted(city));
    }
}
