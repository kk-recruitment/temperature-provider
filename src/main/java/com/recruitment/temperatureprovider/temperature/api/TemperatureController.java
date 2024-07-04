package com.recruitment.temperatureprovider.temperature.api;

import com.recruitment.temperatureprovider.temperature.TemperatureService;
import com.recruitment.temperatureprovider.temperature.api.dto.YearlyAverageTempDto;
import com.recruitment.temperatureprovider.temperature.model.YearlyAverageTemp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/temperature")
@RequiredArgsConstructor
class TemperatureController {
    private final TemperatureService temperatureService;

    @GetMapping("/average/{city}")
    List<YearlyAverageTempDto> getYearlyAverageTemperatureList(@PathVariable String city) {
        return temperatureService.getYearlyAverageTemperatureList(city).stream()
                .map(this::toDto)
                .toList();
    }

    private YearlyAverageTempDto toDto(YearlyAverageTemp yearlyAverageTemp) {
        return new YearlyAverageTempDto(yearlyAverageTemp.year(), yearlyAverageTemp.averageTemperature());
    }
}
