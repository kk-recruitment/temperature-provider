package com.recruitment.temperatureprovider.temperature;

import com.recruitment.temperatureprovider.temperature.exception.DataException;
import com.recruitment.temperatureprovider.temperature.exception.FileReadingException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class TemperatureDataProvider {
    private static final String FILE_PATH = "src/main/resources/example_file.csv";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    List<YearlyAverageTemp> getYearlyAverageTemperatureListData(String city) {
        try (var lines = Files.lines(Paths.get(FILE_PATH))) {
            Map<Integer, YearlyTemperatureData> yearToYearlyTempDataMap = new HashMap<>();
            lines.map(line -> line.split(";"))
                    .filter(data -> city.equals(data[0]))
                    .forEach(data -> {
                        var year = getTimestamp(data[1]).getYear();
                        var temperature = getTemperature(data[2]);
                        var yearlyTempData = yearToYearlyTempDataMap.getOrDefault(year, new YearlyTemperatureData());
                        yearlyTempData.addTemperature(temperature);
                        yearToYearlyTempDataMap.put(year, yearlyTempData);
                    });

            return yearToYearlyTempDataMap.entrySet().stream()
                    .map(this::mapToYearlyAverageTemperature)
                    .sorted(Comparator.comparingInt(YearlyAverageTemp::year))
                    .toList();
        } catch (Exception e) {
            throw new FileReadingException(e.getMessage());
        }
    }

    private YearlyAverageTemp mapToYearlyAverageTemperature(Map.Entry<Integer, YearlyTemperatureData> entry) {
        return new YearlyAverageTemp(entry.getKey(), entry.getValue().getAverageTemperature());
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

    private static class YearlyTemperatureData {
        private double totalTemperature = 0;
        private int numberOfLogs = 0;

        void addTemperature(double temperature) {
            totalTemperature += temperature;
            numberOfLogs++;
        }

        public double getAverageTemperature() {
            return BigDecimal.valueOf(totalTemperature).divide(BigDecimal.valueOf(numberOfLogs), 1, RoundingMode.HALF_UP).doubleValue();
        }
    }
}
