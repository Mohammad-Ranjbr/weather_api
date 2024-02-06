package com.skyapi.weatherapiservice.service.implement;

import com.skyapi.weatherapicommon.model.HourlyWeather;
import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapiservice.exception.LocationNotFoundException;
import com.skyapi.weatherapiservice.repository.HourlyWeatherRepository;
import com.skyapi.weatherapiservice.repository.LocationRepository;
import com.skyapi.weatherapiservice.service.HourlyWeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HourlyWeatherServiceImpl implements HourlyWeatherService {

    private final HourlyWeatherRepository hourlyWeatherRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public HourlyWeatherServiceImpl(HourlyWeatherRepository hourlyWeatherRepository , LocationRepository locationRepository){
        this.hourlyWeatherRepository = hourlyWeatherRepository;
        this.locationRepository = locationRepository;
    }
    @Override
    public List<HourlyWeather> getByLocation(Location location, int currentHour) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        Location locationInDB = locationRepository.findByCountryCodeAndCityName(countryCode,cityName);
        if(locationInDB == null){
            throw new LocationNotFoundException("No location found with the given country code and city name");
        }

        return hourlyWeatherRepository.findByLocationCode(locationInDB.getCode(),currentHour);
    }

    @Override
    public List<HourlyWeather> getByLocationCode(String locationCode, int currentHour) {
        Location locationInDB = locationRepository.findByCode(locationCode);
        if(locationInDB == null){
            throw new LocationNotFoundException("No location found with the given code");
        }
        return hourlyWeatherRepository.findByLocationCode(locationCode,currentHour);
    }

    @Override
    public List<HourlyWeather> updateByLocationCode(String locationCode, List<HourlyWeather> hourlyForecastInRequest) throws
            LocationNotFoundException {
        Location location = locationRepository.findByCode(locationCode);
        if(location == null){
            throw new LocationNotFoundException("No location found with the given code: " + locationCode);
        }
        return hourlyWeatherRepository.saveAll(hourlyForecastInRequest);
    }

}
