package com.recruitment.temperatureprovider.temperature;

import com.recruitment.temperatureprovider.temperature.exception.CityNotFoundException;
import com.recruitment.temperatureprovider.temperature.model.YearlyAverageTemp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemperatureService {
    private final TemperatureDataProvider temperatureDataProvider;

    public List<YearlyAverageTemp> getYearlyAverageTemperatureList(String city) {
        return Optional.of(temperatureDataProvider.getYearlyAverageTemperatureListData(city))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new CityNotFoundException(city));
    }
}
