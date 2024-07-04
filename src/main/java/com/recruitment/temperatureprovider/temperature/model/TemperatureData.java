package com.recruitment.temperatureprovider.temperature.model;

import java.time.LocalDateTime;

public record TemperatureData(String city, LocalDateTime timestamp, double temperature) {
}
