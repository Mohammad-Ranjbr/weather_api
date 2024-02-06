package com.skyapi.weatherapiservice.controller;

import com.skyapi.weatherapicommon.dto.HourlyWeatherDTO;
import com.skyapi.weatherapicommon.dto.HourlyWeatherListDTo;
import com.skyapi.weatherapicommon.model.HourlyWeather;
import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapicommon.utility.CommonUtility;
import com.skyapi.weatherapiservice.exception.BadRequestException;
import com.skyapi.weatherapiservice.exception.GeolocationException;
import com.skyapi.weatherapiservice.exception.LocationNotFoundException;
import com.skyapi.weatherapiservice.service.GeolocationService;
import com.skyapi.weatherapiservice.service.HourlyWeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/v1/hourly")
public class HourlyWeatherApiController {

    private final HourlyWeatherService hourlyWeatherService;
    private final GeolocationService geolocationService;

    private final ModelMapper modelMapper;

    public HourlyWeatherApiController(HourlyWeatherService hourlyWeatherService , GeolocationService geolocationService,
                                      ModelMapper modelMapper){
        this.hourlyWeatherService = hourlyWeatherService;
        this.geolocationService = geolocationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> listHourlyForecastByIPAddress(HttpServletRequest httpServletRequest){
        String ipAddress = CommonUtility.getIPAddress(httpServletRequest);

        try{
            int currentHour = Integer.parseInt(httpServletRequest.getHeader("X-Current-Hour"));

            Location locationFromIP = geolocationService.getLocation(ipAddress);
            List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocation(locationFromIP,currentHour);

            if(hourlyForecast.isEmpty()){
                return ResponseEntity.noContent().build();
            }

            return new ResponseEntity<>(listEntity2DTO(hourlyForecast), HttpStatus.OK);
        }
        catch (NumberFormatException | GeolocationException geolocationException){
            return ResponseEntity.badRequest().build();
        }
        catch (LocationNotFoundException locationNotFoundException){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> listHourlyForecastByLocationCode(HttpServletRequest httpServletRequest,
                                                              @PathVariable("locationCode") String location_code){

        try{
            int currentHour = Integer.parseInt(httpServletRequest.getHeader("X-Current-Hour"));

            List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocationCode(location_code,currentHour);

            if(hourlyForecast.isEmpty()){
                return ResponseEntity.noContent().build();
            }

            return new ResponseEntity<>(listEntity2DTO(hourlyForecast), HttpStatus.OK);
        }
        catch (NumberFormatException | GeolocationException geolocationException){
            return ResponseEntity.badRequest().build();
        }
        catch (LocationNotFoundException locationNotFoundException){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateHourlyForecast(@PathVariable("locationCode") String location_code,
                                                  @RequestBody List<HourlyWeatherDTO> dtoList){
        if(dtoList.isEmpty()){
            throw new BadRequestException("Hourly forecast data cannot be empty");
        }
        return ResponseEntity.accepted().build();
    }

    private HourlyWeatherListDTo listEntity2DTO(List<HourlyWeather> hourlyForecast){
        Location location = hourlyForecast.get(0).getId().getLocation();

        HourlyWeatherListDTo listDTo = new HourlyWeatherListDTo();
        listDTo.setLocation(location.toString());

        hourlyForecast.forEach(hourlyWeather -> {
            HourlyWeatherDTO hourlyWeatherDTO = modelMapper.map(hourlyWeather , HourlyWeatherDTO.class);
            listDTo.addWeatherHourlyDTO(hourlyWeatherDTO);
        });

        return listDTo;
    }

}
