package com.skyapi.realtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapicommon.model.RealtimeWeather;
import com.skyapi.weatherapiservice.controller.RealtimeWeatherApiController;
import com.skyapi.weatherapiservice.exception.GeolocationException;
import com.skyapi.weatherapiservice.exception.LocationNotFoundException;
import com.skyapi.weatherapiservice.service.GeolocationService;
import com.skyapi.weatherapiservice.service.RealtimeWeatherService;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

@WebMvcTest(RealtimeWeatherApiController.class)
public class RealtimeWeatherApiControllerTests {

    private static final String END_POINT_PATH = "/v1/realtime";

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean GeolocationService geolocationService;
    @MockBean RealtimeWeatherService realtimeWeatherService;

    @Test
    public void testGetShouldReturnStatus400BadRequest() throws Exception{
        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenThrow(GeolocationException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetShouldReturnStatus404NotFound() throws Exception{
        Location location = new Location();
        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        Mockito.when(realtimeWeatherService.getByLocation(location)).thenThrow(LocationNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetShouldReturnStatus200Found() throws Exception{
        Location location = new Location();
        location.setCode("SFCA_USA");
        location.setCityName("San Francisco");
        location.setRegionName("California");
        location.setCountryName("United States Of America");
        location.setCountryCode("US");

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(2);
        realtimeWeather.setHumidity(32);
        realtimeWeather.setPrecipitation(43);
        realtimeWeather.setStatus("Snowy");
        realtimeWeather.setWindSpeed(24);
        realtimeWeather.setLastUpdate(new Date());

        realtimeWeather.setLocation(location);
        location.setRealtimeWeather(realtimeWeather);

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        Mockito.when(realtimeWeatherService.getByLocation(location)).thenReturn(realtimeWeather);

        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andDo(MockMvcResultHandlers.print());
    }

}
