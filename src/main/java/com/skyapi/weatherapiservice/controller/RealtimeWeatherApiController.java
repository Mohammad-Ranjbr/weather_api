package com.skyapi.weatherapiservice.controller;

import com.skyapi.weatherapicommon.dto.RealtimeWeatherDTO;
import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapicommon.model.RealtimeWeather;
import com.skyapi.weatherapicommon.utility.CommonUtility;
import com.skyapi.weatherapiservice.exception.GeolocationException;
import com.skyapi.weatherapiservice.exception.LocationNotFoundException;
import com.skyapi.weatherapiservice.service.GeolocationService;
import com.skyapi.weatherapiservice.service.RealtimeWeatherService;

import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/realtime")
public class RealtimeWeatherApiController {

    private final ModelMapper modelMapper;
    private final GeolocationService geolocationService;
    private final RealtimeWeatherService realtimeWeatherService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherApiController.class);
    @Autowired
    public RealtimeWeatherApiController(GeolocationService geolocationService , RealtimeWeatherService realtimeWeatherService ,
                                        ModelMapper modelMapper){
        this.modelMapper = modelMapper;
        this.geolocationService = geolocationService;
        this.realtimeWeatherService = realtimeWeatherService;
    }

    @GetMapping
    public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request){
        String ipAddress = CommonUtility.getIPAddress(request);

        try{
            Location locationFromIP = geolocationService.getLocation(ipAddress);
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIP);
            RealtimeWeatherDTO realtimeWeatherDTO = modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
            return new ResponseEntity<>(realtimeWeatherDTO, HttpStatus.OK);
        } catch(GeolocationException geolocationException){
            LOGGER.error(geolocationException.getMessage(),geolocationException);
            return  ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException locationNotFoundException){
            LOGGER.error(locationNotFoundException.getMessage(),locationNotFoundException);
            return ResponseEntity.notFound().build();
        }
    }

}
