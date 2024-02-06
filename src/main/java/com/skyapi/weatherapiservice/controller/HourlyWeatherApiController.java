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
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
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
                                                  @RequestBody @Valid List<HourlyWeatherDTO> dtoList) throws BadRequestException{
        if(dtoList.isEmpty()){
            throw new BadRequestException("Hourly forecast data cannot be empty");
        }

        dtoList.forEach(System.out::println);
        List<HourlyWeather> hourlyWeatherList = listDTO2ListEntity(dtoList);
        hourlyWeatherList.forEach(System.out::println);

        try {
            List<HourlyWeather> updateHourlyWeather = hourlyWeatherService.updateByLocationCode(location_code, hourlyWeatherList);
            return new ResponseEntity<>(listEntity2DTO(updateHourlyWeather),HttpStatus.OK);
        }
        catch (LocationNotFoundException locationNotFoundException){
            return ResponseEntity.notFound().build();
        }

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

    private List<HourlyWeather> listDTO2ListEntity(List<HourlyWeatherDTO> dtoList){
        List<HourlyWeather> listEntity = new ArrayList<>();

        dtoList.forEach(dto -> {
            listEntity.add(modelMapper.map(dto,HourlyWeather.class));
        });
        return listEntity;
    }

}
