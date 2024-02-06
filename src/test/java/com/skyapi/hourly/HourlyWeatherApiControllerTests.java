package com.skyapi.hourly;  // Power Bi

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherapicommon.dto.HourlyWeatherDTO;
import com.skyapi.weatherapicommon.model.HourlyWeather;
import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapiservice.controller.HourlyWeatherApiController;
import com.skyapi.weatherapiservice.exception.GeolocationException;
import com.skyapi.weatherapiservice.exception.LocationNotFoundException;
import com.skyapi.weatherapiservice.service.GeolocationService;
import com.skyapi.weatherapiservice.service.HourlyWeatherService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

@WebMvcTest(HourlyWeatherApiController.class)
public class HourlyWeatherApiControllerTests {

    private static final String END_POINT_PATH = "/v1/hourly";
    private static final String X_CURRENT_HOUR = "X-Current-Hour";

    @Autowired private MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean private HourlyWeatherService hourlyWeatherService;
    @MockBean private GeolocationService geolocationService;

    @Test
    public void testGetByIPShouldReturn400BadRequestBecauseNoHeaderXCurrentHour() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetByIPShouldReturn404BadRequestBecauseGeolocationException() throws Exception{
        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenThrow(GeolocationException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH).header(X_CURRENT_HOUR,"7"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetByIPShouldReturn204NoContent() throws Exception{
        int currentHour = 7;
        Location location = new Location().code("DEHLI_IN");
        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        Mockito.when(hourlyWeatherService.getByLocation(location,currentHour)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH).header(X_CURRENT_HOUR,String.valueOf(currentHour)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetByIPShouldReturn200OK() throws Exception{
        int currentHour = 7;

        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryName("US");
        location.setCountryCode("United States of America");

        HourlyWeather forecast1 = new HourlyWeather()
                .location(location)
                .hourOfDay(10)
                .temperature(13)
                .precipitation(70)
                .status("Cloudy");

        HourlyWeather forecast2 = new HourlyWeather()
                .location(location)
                .hourOfDay(11)
                .temperature(14)
                .precipitation(71)
                .status("Cloudy");

        String expectedLocation = location.toString();

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        Mockito.when(hourlyWeatherService.getByLocation(location,currentHour)).thenReturn(List.of(forecast1,forecast2));
        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH).header(X_CURRENT_HOUR,String.valueOf(currentHour)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location", CoreMatchers.is(expectedLocation)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hourly_forecast[0].hour_of_day",CoreMatchers.is(10)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetByLocationCodeShouldReturn400BadRequest() throws Exception{
        String locationCode = "DEHLI_IN";
        String requestURI = END_POINT_PATH + "/" + locationCode;
        mockMvc.perform(MockMvcRequestBuilders.get(requestURI))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetByCodeShouldReturn404NotFound() throws Exception{
        int currentHour = 9;
        String locationCode = "DEHLI_IN";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        Mockito.when(hourlyWeatherService.getByLocationCode(locationCode,currentHour)).thenThrow(LocationNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(requestURI).header(X_CURRENT_HOUR,String.valueOf(currentHour)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetByCodeShouldReturn204NoContent1() throws Exception{
        int currentHour = 7;
        String locationCode = "DEHLI_IN";
        String requestURI = END_POINT_PATH + "/" + locationCode;
        Mockito.when(hourlyWeatherService.getByLocationCode(locationCode,currentHour)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get(requestURI).header(X_CURRENT_HOUR,String.valueOf(currentHour)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetByCodeShouldReturn200OK() throws Exception{
        int currentHour = 7;
        String locationCode = "DEHLI_IN";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        Location location = new Location();
        location.setCode(locationCode);
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryName("US");
        location.setCountryCode("United States of America");

        HourlyWeather forecast1 = new HourlyWeather()
                .location(location)
                .hourOfDay(10)
                .temperature(13)
                .precipitation(70)
                .status("Cloudy");

        HourlyWeather forecast2 = new HourlyWeather()
                .location(location)
                .hourOfDay(11)
                .temperature(14)
                .precipitation(71)
                .status("Cloudy");

        String expectedLocation = location.toString();

        Mockito.when(hourlyWeatherService.getByLocationCode(locationCode,currentHour)).thenReturn(List.of(forecast1,forecast2));
        mockMvc.perform(MockMvcRequestBuilders.get(requestURI).header(X_CURRENT_HOUR,String.valueOf(currentHour)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location", CoreMatchers.is(expectedLocation)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hourly_forecast[0].hour_of_day",CoreMatchers.is(10)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequestBecauseNoData() throws Exception {
        String requestURI = END_POINT_PATH + "/DEHLI_IN";
        List<HourlyWeatherDTO> dtoList = Collections.emptyList();
        String requestBody = objectMapper.writeValueAsString(dtoList);

        mockMvc.perform(MockMvcRequestBuilders.put(requestURI).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]",CoreMatchers.is("Hourly forecast data cannot be empty")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequestBecauseInvalidData() throws Exception {
        String requestURI = END_POINT_PATH + "/DEHLI_IN";

        HourlyWeatherDTO dto1 = new HourlyWeatherDTO()
                .hourOfDay(10)
                .temperature(133)
                .precipitation(70)
                .status("Cloudy");

        HourlyWeatherDTO dto2 = new HourlyWeatherDTO()
                .hourOfDay(11)
                .temperature(15)
                .precipitation(60)
                .status("Cloudy");

        List<HourlyWeatherDTO> dtoList = List.of(dto1,dto2);

        String requestBody = objectMapper.writeValueAsString(dtoList);

        mockMvc.perform(MockMvcRequestBuilders.put(requestURI).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]",CoreMatchers.containsString("Temperature must be in the range")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception {
        String locationCode = "DEHLI_IN";
        String requestURI = END_POINT_PATH + "/" + locationCode;

        HourlyWeatherDTO dto1 = new HourlyWeatherDTO()
                .hourOfDay(10)
                .temperature(13)
                .precipitation(70)
                .status("Cloudy");

        List<HourlyWeatherDTO> dtoList = List.of(dto1);

        String requestBody = objectMapper.writeValueAsString(dtoList);

        Mockito.when(hourlyWeatherService.updateByLocationCode(Mockito.eq(locationCode),Mockito.anyList())).thenThrow(LocationNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.put(requestURI).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

}
