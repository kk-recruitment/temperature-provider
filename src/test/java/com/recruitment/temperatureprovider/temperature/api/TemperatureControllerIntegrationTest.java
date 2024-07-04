package com.recruitment.temperatureprovider.temperature.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TemperatureControllerIntegrationTest {
    private static final String AVERAGE_TEMP_PATH = "/temperature/average/%s";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnYearlyAverageTemperature() throws Exception {
        //given & when & then
        mockMvc.perform(MockMvcRequestBuilders.get(AVERAGE_TEMP_PATH.formatted("Warszawa"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].year").exists())
                .andExpect(jsonPath("$[0].averageTemperature").exists());
    }

    @Test
    void shouldThrowExceptionWhenDataForCityAreNotExistInFile() throws Exception {
        //given
        var invalidCity = "invalidCity";

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.get(AVERAGE_TEMP_PATH.formatted(invalidCity))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Could not find data for city: %s".formatted(invalidCity)));
    }
}