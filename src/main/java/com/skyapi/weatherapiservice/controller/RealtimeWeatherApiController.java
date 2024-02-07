package com.skyapi.weatherapiservice.controller;

import com.skyapi.weatherapicommon.dto.RealtimeWeatherDTO;
import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapicommon.model.RealtimeWeather;
import com.skyapi.weatherapicommon.utility.CommonUtility;
import com.skyapi.weatherapiservice.exception.GeolocationException;

import com.skyapi.weatherapiservice.service.GeolocationService;
import com.skyapi.weatherapiservice.service.RealtimeWeatherService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return new ResponseEntity<>(entity2DTO(realtimeWeather), HttpStatus.OK);
        } catch(GeolocationException geolocationException){
            LOGGER.error(geolocationException.getMessage(),geolocationException);
            return  ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("locationCode") String location_code){
        RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(location_code);
        return new ResponseEntity<>(entity2DTO(realtimeWeather),HttpStatus.OK);
    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateRealtimeWeather(@PathVariable("locationCode") String locationCode ,
                                                   @RequestBody @Valid RealtimeWeather realtimeWeatherInRequest){
        realtimeWeatherInRequest.setLocationCode(locationCode);
        RealtimeWeather updatedRealtimeWeather = realtimeWeatherService.update(locationCode,realtimeWeatherInRequest);
        return new ResponseEntity<>(entity2DTO(updatedRealtimeWeather),HttpStatus.OK);
    }

    private RealtimeWeatherDTO entity2DTO(RealtimeWeather realtimeWeather){
        return modelMapper.map(realtimeWeather,RealtimeWeatherDTO.class);
    }


}
