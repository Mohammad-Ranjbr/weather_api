package com.skyapi.weatherapiservice.controller;

import com.skyapi.weatherapicommon.model.HourlyWeather;
import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapicommon.utility.CommonUtility;
import com.skyapi.weatherapiservice.exception.GeolocationException;
import com.skyapi.weatherapiservice.exception.LocationNotFoundException;
import com.skyapi.weatherapiservice.service.GeolocationService;
import com.skyapi.weatherapiservice.service.HourlyWeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/hourly")
public class HourlyWeatherApiController {

    private final HourlyWeatherService hourlyWeatherService;
    private final GeolocationService geolocationService;

    public HourlyWeatherApiController(HourlyWeatherService hourlyWeatherService , GeolocationService geolocationService){
        this.hourlyWeatherService = hourlyWeatherService;
        this.geolocationService = geolocationService;
    }

    @GetMapping
    public ResponseEntity<?> listHourlyForecastByIPAddress(HttpServletRequest httpServletRequest){
        String ipAddress = CommonUtility.getIPAddress(httpServletRequest);

        try{
            int currentHour = Integer.parseInt(httpServletRequest.getHeader("X-Current-Hour"));

            Location locationFromIP = geolocationService.getLocation(ipAddress);
            List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocation(locationFromIP,currentHour);

            if(hourlyForecast == null){
                return ResponseEntity.noContent().build();
            }

            return new ResponseEntity<>(hourlyForecast, HttpStatus.OK);
        }
        catch (NumberFormatException | GeolocationException geolocationException){
            return ResponseEntity.badRequest().build();
        }
        catch (LocationNotFoundException locationNotFoundException){
            return ResponseEntity.notFound().build();
        }
    }

}
