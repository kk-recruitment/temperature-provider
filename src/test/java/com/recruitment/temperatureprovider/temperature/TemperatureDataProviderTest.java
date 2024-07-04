package com.recruitment.temperatureprovider.temperature;

import com.recruitment.temperatureprovider.temperature.exception.FileReadingException;
import com.recruitment.temperatureprovider.temperature.model.YearlyAverageTemp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

class TemperatureDataProviderTest {
    private static final String WARSAW_NAME = "Warszawa";

    private final MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class);
    private final TemperatureDataProvider temperatureDataProvider = new TemperatureDataProvider();

    @AfterEach
    void tearDown() {
        mockedFiles.close();
    }

    @Test
    void shouldReturnYearlyAverageTemperature() {
        //given
        mockedFiles.when(() -> Files.lines(any(Path.class))).thenReturn(Stream.of(getSampleData().split("\n")));

        //when
        var result = temperatureDataProvider.getYearlyAverageTemperatureListData(WARSAW_NAME);

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0))
                .returns(2018, YearlyAverageTemp::year)
                .returns(27.4, YearlyAverageTemp::averageTemperature);
        assertThat(result.get(1))
                .returns(2019, YearlyAverageTemp::year)
                .returns(16.1, YearlyAverageTemp::averageTemperature);
    }

    @ParameterizedTest
    @MethodSource("invalidDataProvider")
    void shouldThrowException(String data, Class<RuntimeException> exceptionClass, String exceptionMessage) {
        //given
        mockedFiles.when(() -> Files.lines(any(Path.class))).thenReturn(Stream.of(data.split("\n")));

        //when & then
        assertThatThrownBy(() -> temperatureDataProvider.getYearlyAverageTemperatureListData(WARSAW_NAME))
                .isExactlyInstanceOf(exceptionClass)
                .hasMessage(exceptionMessage);
    }

    private static Stream<Arguments> invalidDataProvider() {
        return Stream.of(
                Arguments.of("Warszawa;invalidTimestamp;9.97", FileReadingException.class,
                        "Cannot proper read file: Invalid timestamp value: invalidTimestamp"),
                Arguments.of("Warszawa;2018-09-19 05:17:32.619;invalidTemp", FileReadingException.class,
                        "Cannot proper read file: Invalid temperature value: invalidTemp"));
    }

    private static String getSampleData() {
        return """
                Warszawa;2018-09-19 05:17:32.619;9.97
                Warszawa;2018-10-20 18:44:42.468;39.02
                Warszawa;2018-11-21 11:17:42.318;33.27
                Warszawa;2019-03-08 01:02:27.102;10.3
                Warszawa;2019-05-09 17:56:13.119;10.23
                Warszawa;2019-07-10 11:44:15.938;27.78
                Kraków;2020-10-11 17:53:40.689;34.09
                Kraków;2020-10-12 21:52:05.182;38.93
                Kraków;2020-10-13 21:33:53.559;-4.23
                """;
    }
}