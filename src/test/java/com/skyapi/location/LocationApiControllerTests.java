package com.skyapi.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherapicommon.dto.LocationDTO;
import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapiservice.controller.LocationApiController;
import com.skyapi.weatherapiservice.exception.LocationNotFoundException;
import com.skyapi.weatherapiservice.service.LocationService;

import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

@WebMvcTest(LocationApiController.class)
public class LocationApiControllerTests {

    private static final String END_POINT_PATH = "/v1/locations";
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean LocationService locationService;

    @Test
    public void testAddShouldReturn400BadRequest() throws Exception {
        LocationDTO location = new LocationDTO();
        String bodyContent = objectMapper.writeValueAsString(location);
        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAddShouldReturn201Created() throws Exception{
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryName("US");
        location.setCountryCode("United States of America");
        location.setEnabled(true);

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCode(location.getCode());
        locationDTO.setCityName(location.getCityName());
        locationDTO.setRegionName(location.getRegionName());
        locationDTO.setCountryCode(location.getCountryCode());
        locationDTO.setCountryName(location.getCountryName());
        locationDTO.setEnabled(location.isEnabled());

        Mockito.when(locationService.add(location)).thenReturn(location);
        String bodyContent = objectMapper.writeValueAsString(locationDTO);
        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is("NYC_USA"))) // compare , primary key class ro bayad method hash va equal ro barash faal konim
                .andExpect(MockMvcResultMatchers.jsonPath("$.city_name" , CoreMatchers.is("New York City")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.region_name",CoreMatchers.is("New York")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country_name",CoreMatchers.is("US")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country_code",CoreMatchers.is("United States of America")))
                .andExpect(MockMvcResultMatchers.header().string("Location","/v1/locations/NYC_USA"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void testListShouldReturn204NoContent() throws Exception{
        Mockito.when(locationService.list()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testListShouldReturn200Ok() throws Exception{
        Location location1 = new Location();
        location1.setCode("NYC_USA");
        location1.setCityName("New York City");
        location1.setRegionName("New York");
        location1.setCountryName("US");
        location1.setCountryCode("United States of America");
        location1.setEnabled(true);

        Location location2 = new Location();
        location2.setCode("LACA_USA");
        location2.setCityName("Los Angeles");
        location2.setRegionName("California");
        location2.setCountryName("US");
        location2.setCountryCode("United States of America");
        location2.setEnabled(true);

        Location location3 = new Location();
        location3.setCode("Deli_IN");
        location3.setCityName("New Deli");
        location3.setRegionName("Deli");
        location3.setCountryName("India");
        location3.setCountryCode("IN");
        location3.setEnabled(true);

        Mockito.when(locationService.list()).thenReturn(List.of(location1,location2,location3));

        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].code", CoreMatchers.is("NYC_USA"))) // compare , primary key class ro bayad method hash va equal ro barash faal konim
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].city_name" , CoreMatchers.is("New York City")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].region_name",CoreMatchers.is("New York")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].country_name",CoreMatchers.is("US")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].country_code",CoreMatchers.is("United States of America")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].code", CoreMatchers.is("LACA_USA"))) // compare , primary key class ro bayad method hash va equal ro barash faal konim
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].city_name" , CoreMatchers.is("Los Angeles")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].region_name",CoreMatchers.is("California")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].country_name",CoreMatchers.is("US")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].country_code",CoreMatchers.is("United States of America")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].code", CoreMatchers.is("Deli_IN"))) // compare , primary key class ro bayad method hash va equal ro barash faal konim
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].city_name" , CoreMatchers.is("New Deli")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].region_name",CoreMatchers.is("Deli")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].country_name",CoreMatchers.is("India")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].country_code",CoreMatchers.is("IN")))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void testGetShouldReturn404MethodNotFound() throws Exception{
        String requestURI = END_POINT_PATH + "/ABCD";
        mockMvc.perform(MockMvcRequestBuilders.get(requestURI))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetShouldReturn405MethodNotAllowed() throws Exception{
        String requestURI = END_POINT_PATH + "/ABCD";
        mockMvc.perform(MockMvcRequestBuilders.post(requestURI))
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetShouldReturn200Ok() throws Exception{
        String code = "NYC_USA";
        String requestURI = END_POINT_PATH + "/" + code;

        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryName("US");
        location.setCountryCode("United States of America");
        location.setEnabled(true);

        Mockito.when(locationService.get(code)).thenReturn(location);

        mockMvc.perform(MockMvcRequestBuilders.get(requestURI))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is("NYC_USA"))) // compare , primary key class ro bayad method hash va equal ro barash faal konim
                .andExpect(MockMvcResultMatchers.jsonPath("$.city_name" , CoreMatchers.is("New York City")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.region_name",CoreMatchers.is("New York")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country_name",CoreMatchers.is("US")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country_code",CoreMatchers.is("United States of America")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception{
        LocationDTO location = new LocationDTO();
        location.setCode("ABCD");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryName("US");
        location.setCountryCode("United States of America");
        location.setEnabled(true);

        Mockito.when(locationService.update(Mockito.any())).thenThrow(new LocationNotFoundException("No location found"));
        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(MockMvcRequestBuilders.put(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequest() throws Exception{
        LocationDTO location = new LocationDTO();
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryName("US");
        location.setCountryCode("United States of America");
        location.setEnabled(true);

        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(MockMvcRequestBuilders.put(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateShouldReturn200Ok() throws Exception{
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryName("US");
        location.setCountryCode("United States Of America");
        location.setEnabled(true);

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCode(location.getCode());
        locationDTO.setCityName(location.getCityName());
        locationDTO.setRegionName(location.getRegionName());
        locationDTO.setCountryCode(location.getCountryCode());
        locationDTO.setCountryName(location.getCountryName());
        locationDTO.setEnabled(location.isEnabled());

        Mockito.when(locationService.update(location)).thenReturn(location);
        String bodyContent = objectMapper.writeValueAsString(locationDTO);

        mockMvc.perform(MockMvcRequestBuilders.put(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is("NYC_USA"))) // compare , primary key class ro bayad method hash va equal ro barash faal konim
                .andExpect(MockMvcResultMatchers.jsonPath("$.city_name" , CoreMatchers.is("New York City")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.region_name",CoreMatchers.is("New York")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country_name",CoreMatchers.is("US")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country_code",CoreMatchers.is("United States Of America")))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void testDeleteShouldReturn404NotFound() throws Exception{
        String code = "LACA_USA";
        String requestURI = END_POINT_PATH + "/" + code;

        Mockito.doThrow(LocationNotFoundException.class).when(locationService).delete(code); // chon az noe void hast nemishe mesl halat haye ghabl benevisim

        mockMvc.perform(MockMvcRequestBuilders.delete(requestURI))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test void testDeleteShouldReturn204NotFound() throws Exception{
        String code = "LACA_USA";
        String requestURI = END_POINT_PATH + "/" + code;

        Mockito.doNothing().when(locationService).delete(code); // chon az noe void hast nemishe mesl halat haye ghabl benevisim

        mockMvc.perform(MockMvcRequestBuilders.delete(requestURI))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testValidateRequestBodyLocationCodeNotNull() throws Exception{
        LocationDTO location = new LocationDTO();
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryName("US");
        location.setCountryCode("United States of America");
        location.setEnabled(true);

        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) //400
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]", CoreMatchers.is("Location code cannot be null"))) // compare , primary key class ro bayad method hash va equal ro barash faal konim
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void testValidateRequestBodyLocationCodeLength() throws Exception{
        LocationDTO location = new LocationDTO();
        location.setCode("");
        //location.setCode("United United United United");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryName("US");
        location.setCountryCode("United States of America");
        location.setEnabled(true);

        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) //400
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]", CoreMatchers.is("Location code must have 3-12 characters"))) // compare , primary key class ro bayad method hash va equal ro barash faal konim
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void testValidateRequestBodyAddLocationAllFieldsInvalid() throws Exception{
        LocationDTO location = new LocationDTO();
        location.setRegionName("");

        String bodyContent = objectMapper.writeValueAsString(location);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) //400
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        Assertions.assertThat(responseBody).contains("Location code cannot be null");
        Assertions.assertThat(responseBody).contains("Region name must have 3-128 characters");
        Assertions.assertThat(responseBody).contains("Country name cannot be null");
        Assertions.assertThat(responseBody).contains("City name cannot be null");
        Assertions.assertThat(responseBody).contains("Country code cannot be null");

        //System.out.println(responseBody);
    }

}
