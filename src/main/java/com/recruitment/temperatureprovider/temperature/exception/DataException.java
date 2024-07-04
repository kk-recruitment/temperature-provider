package com.recruitment.temperatureprovider.temperature.exception;

public class DataException extends RuntimeException {
    private static final String INVALID_TIMESTAMP_MESSAGE = "Invalid timestamp value: %s";
    private static final String INVALID_TEMP_MESSAGE = "Invalid temperature value: %s";

    public DataException(String message) {
        super(message);
    }

    public static DataException invalidTimestamp(String timestamp) {
        return new DataException(INVALID_TIMESTAMP_MESSAGE.formatted(timestamp));
    }

    public static DataException invalidTemperature(String temperature) {
        return new DataException(INVALID_TEMP_MESSAGE.formatted(temperature));
    }
}
