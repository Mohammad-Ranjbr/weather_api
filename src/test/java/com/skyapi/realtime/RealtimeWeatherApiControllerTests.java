package com.skyapi.realtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapicommon.model.RealtimeWeather;
import com.skyapi.weatherapiservice.controller.RealtimeWeatherApiController;
import com.skyapi.weatherapiservice.exception.GeolocationException;
import com.skyapi.weatherapiservice.exception.LocationNotFoundException;
import com.skyapi.weatherapiservice.service.GeolocationService;
import com.skyapi.weatherapiservice.service.RealtimeWeatherService;
import org.hamcrest.CoreMatchers;
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
        Location location = new Location().code("DEHLI_IN");
        location.setCode("SFCA_USA");
        location.setCityName("San Francisco");
        location.setRegionName("California");
        location.setCountryName("United States Of America");
        location.setCountryCode("US");
        LocationNotFoundException ex = new LocationNotFoundException(location.getCode());

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        Mockito.when(realtimeWeatherService.getByLocation(location)).thenThrow(ex);

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

        String expectedLocation = location.getCityName() + ", " +  location.getRegionName() + ", " + location.getCountryName();

        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location", CoreMatchers.is(expectedLocation)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetByLocationCodeShouldReturnStatus404NotFound() throws Exception{
        String locationCode = "ABCD";
        Mockito.when(realtimeWeatherService.getByLocationCode(locationCode)).thenThrow(LocationNotFoundException.class);
        String requestURI = END_POINT_PATH + "/" + locationCode;

        mockMvc.perform(MockMvcRequestBuilders.get(requestURI))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetByLocationCodeShouldReturnStatus200OK() throws Exception{
        String locationCode = "SFCA_USA";

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

        Mockito.when(realtimeWeatherService.getByLocationCode(locationCode)).thenReturn(realtimeWeather);

        String requestURI = END_POINT_PATH + "/" + locationCode;
        String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName();

        mockMvc.perform(MockMvcRequestBuilders.get(requestURI))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location",CoreMatchers.is(expectedLocation)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequest() throws Exception{
        String locationCode = "ABC_US";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(-200);
        realtimeWeather.setHumidity(132);
        realtimeWeather.setPrecipitation(143);
        realtimeWeather.setStatus("Sn");
        realtimeWeather.setWindSpeed(240);

        Mockito.when(realtimeWeatherService.update(locationCode,realtimeWeather)).thenThrow(new LocationNotFoundException("No location found"));
        String bodyContent = objectMapper.writeValueAsString(realtimeWeather);

        mockMvc.perform(MockMvcRequestBuilders.put(requestURI).contentType("application/json").content(bodyContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception{
        String locationCode = "ABC_US";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(-20);
        realtimeWeather.setHumidity(13);
        realtimeWeather.setPrecipitation(14);
        realtimeWeather.setStatus("Snowy");
        realtimeWeather.setWindSpeed(24);
        realtimeWeather.setLocationCode(locationCode);

        LocationNotFoundException ex = new LocationNotFoundException(locationCode);
        Mockito.when(realtimeWeatherService.update(locationCode,realtimeWeather)).thenThrow(ex);
        String bodyContent = objectMapper.writeValueAsString(realtimeWeather);

        mockMvc.perform(MockMvcRequestBuilders.put(requestURI).contentType("application/json").content(bodyContent))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception{
        String locationCode = "SFCA_USA";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        Location location = new Location();
        location.setCode(locationCode);
        location.setCityName("San Francisco");
        location.setRegionName("California");
        location.setCountryName("United States Of America");
        location.setCountryCode("US");

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(-20);
        realtimeWeather.setHumidity(13);
        realtimeWeather.setPrecipitation(14);
        realtimeWeather.setStatus("Snowy");
        realtimeWeather.setWindSpeed(24);
        realtimeWeather.setLastUpdate(new Date());

        realtimeWeather.setLocation(location);
        location.setRealtimeWeather(realtimeWeather);

        Mockito.when(realtimeWeatherService.update(locationCode,realtimeWeather)).thenReturn(realtimeWeather);
        String bodyContent = objectMapper.writeValueAsString(realtimeWeather);

        String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName();

        mockMvc.perform(MockMvcRequestBuilders.put(requestURI).contentType("application/json").content(bodyContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.location", CoreMatchers.is(expectedLocation)))
                .andDo(MockMvcResultHandlers.print());
    }

}
