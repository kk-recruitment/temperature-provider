package com.recruitment.temperatureprovider.temperature;

import com.recruitment.temperatureprovider.temperature.exception.DataException;
import com.recruitment.temperatureprovider.temperature.exception.FileReadingException;
import com.recruitment.temperatureprovider.temperature.model.TemperatureData;
import com.recruitment.temperatureprovider.temperature.model.YearlyAverageTemp;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class TemperatureDataProvider {
    private static final String FILE_PATH = "src/main/resources/example_file.csv";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    List<YearlyAverageTemp> getYearlyAverageTemperatureListData(String city) {
        try (var lines = Files.lines(Paths.get(FILE_PATH))) {
            return lines.map(this::mapToTemperatureData)
                    .filter(data -> city.equals(data.city()))
                    .collect(Collectors.groupingBy(
                            data -> data.timestamp().getYear(),
                            Collectors.collectingAndThen(
                                    Collectors.averagingDouble(TemperatureData::temperature),
                                    TemperatureDataProvider::roundDouble
                            )))
                    .entrySet().stream()
                    .map(entry -> new YearlyAverageTemp(entry.getKey(), entry.getValue()))
                    .sorted(Comparator.comparingInt(YearlyAverageTemp::year))
                    .toList();
        } catch (Exception e) {
            throw new FileReadingException(e.getMessage());
        }
    }

    private TemperatureData mapToTemperatureData(String line) {
        var data = line.split(";");
        var city = data[0];
        var timestamp = getTimestamp(data[1]);
        var temperature = getTemperature(data[2]);
        return new TemperatureData(city, timestamp, temperature);
    }

    private static double roundDouble(Double average) {
        return BigDecimal.valueOf(average).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    private static double getTemperature(String temperatureValue) {
        try {
            return Double.parseDouble(temperatureValue);
        } catch (Exception e) {
            throw DataException.invalidTemperature(temperatureValue);
        }
    }

    private static LocalDateTime getTimestamp(String timestampValue) {
        try {
            return LocalDateTime.parse(timestampValue, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        } catch (Exception e) {
            throw DataException.invalidTimestamp(timestampValue);
        }
    }
}
