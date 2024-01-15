package com.skyapi.weatherapiservice.controller;

import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapicommon.model.RealtimeWeather;
import com.skyapi.weatherapicommon.utility.CommonUtility;
import com.skyapi.weatherapiservice.exception.GeolocationException;
import com.skyapi.weatherapiservice.exception.LocationNotFoundException;
import com.skyapi.weatherapiservice.service.GeolocationService;
import com.skyapi.weatherapiservice.service.RealtimeWeatherService;

import jakarta.servlet.http.HttpServletRequest;
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

    private final GeolocationService geolocationService;
    private final RealtimeWeatherService realtimeWeatherService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherApiController.class);
    @Autowired
    public RealtimeWeatherApiController(GeolocationService geolocationService , RealtimeWeatherService realtimeWeatherService){
        this.geolocationService = geolocationService;
        this.realtimeWeatherService = realtimeWeatherService;
    }

    @GetMapping
    public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request){
        String ipAddress = CommonUtility.getIPAddress(request);

        try{
            Location locationFromIP = geolocationService.getLocation(ipAddress);
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIP);
            return new ResponseEntity<>(realtimeWeather, HttpStatus.OK);
        } catch(GeolocationException geolocationException){
            LOGGER.error(geolocationException.getMessage(),geolocationException);
            return  ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException locationNotFoundException){
            LOGGER.error(locationNotFoundException.getMessage(),locationNotFoundException);
            return ResponseEntity.notFound().build();
        }
    }

}
