package com.recruitment.temperatureprovider.temperature.api;

import com.recruitment.temperatureprovider.temperature.exception.CityNotFoundException;
import com.recruitment.temperatureprovider.temperature.exception.FileReadingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
@ResponseBody
class GlobalExceptionHandler {

    @ExceptionHandler(CityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    RestExceptionMessage handleCityNotFoundException(CityNotFoundException ex) {
        return new RestExceptionMessage(LocalDateTime.now(), ex.getMessage());
    }

    @ExceptionHandler(FileReadingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    RestExceptionMessage handleFileReadingException(FileReadingException ex) {
        log.debug("Cannot proper read file, exception: {}", ex.getMessage());
        return new RestExceptionMessage(LocalDateTime.now(), ex.getMessage());
    }

    private record RestExceptionMessage(LocalDateTime timestamp, String message) {
    }
}
