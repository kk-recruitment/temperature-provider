package com.recruitment.temperatureprovider.temperature;

import com.recruitment.temperatureprovider.temperature.exception.CityNotFoundException;
import com.recruitment.temperatureprovider.temperature.model.YearlyAverageTemp;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TemperatureServiceTest {
    private final TemperatureDataProvider temperatureDataProvider = mock(TemperatureDataProvider.class);
    private final TemperatureService temperatureService = new TemperatureService(temperatureDataProvider);

    @Test
    void shouldReturnYearlyAverageTemperature() {
        //given
        var warsawName = "Warszawa";
        var yearlyAverageTempList = List.of(new YearlyAverageTemp(2018, 27.4), new YearlyAverageTemp(2019, 16.1));
        when(temperatureDataProvider.getYearlyAverageTemperatureListData(warsawName)).thenReturn(yearlyAverageTempList);

        //when
        var result = temperatureService.getYearlyAverageTemperatureList(warsawName);

        //then
        assertThat(result).isEqualTo(yearlyAverageTempList);
    }

    @Test
    void shouldThrowExceptionWhenDataForCityAreNotExistInFile() {
        //given
        var invalidCity = "invalidCity";
        when(temperatureDataProvider.getYearlyAverageTemperatureListData(invalidCity)).thenReturn(emptyList());

        //when & then
        assertThatThrownBy(() -> temperatureService.getYearlyAverageTemperatureList(invalidCity))
                .isExactlyInstanceOf(CityNotFoundException.class)
                .hasMessage("Could not find data for city: %s".formatted(invalidCity));
    }
}