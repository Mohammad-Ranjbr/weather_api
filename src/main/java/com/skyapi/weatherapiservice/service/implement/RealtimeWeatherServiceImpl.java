package com.skyapi.weatherapiservice.service.implement;

import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapicommon.model.RealtimeWeather;
import com.skyapi.weatherapiservice.exception.LocationNotFoundException;
import com.skyapi.weatherapiservice.repository.LocationRepository;
import com.skyapi.weatherapiservice.repository.RealtimeWeatherRepository;
import com.skyapi.weatherapiservice.service.RealtimeWeatherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RealtimeWeatherServiceImpl implements RealtimeWeatherService {

    private final LocationRepository locationRepository;
    private final RealtimeWeatherRepository realtimeWeatherRepository;

    @Autowired
    public RealtimeWeatherServiceImpl(RealtimeWeatherRepository realtimeWeatherRepository , LocationRepository locationRepository){
        this.locationRepository = locationRepository;
        this.realtimeWeatherRepository = realtimeWeatherRepository;
    }

    @Override
    public RealtimeWeather getByLocation(Location location) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode,cityName);
        if(realtimeWeather == null){
            throw new LocationNotFoundException(countryCode,cityName);
        }
        return realtimeWeather;
    }

    @Override
    public RealtimeWeather getByLocationCode(String locationCode) {
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);
        if(realtimeWeather == null){
            throw new LocationNotFoundException(locationCode);
        }
        return realtimeWeather;
    }

    @Override
    public RealtimeWeather update(String locationCode, RealtimeWeather realtimeWeather) {
        Location location = locationRepository.findByCode(locationCode);
        if(location == null){
            throw new LocationNotFoundException(locationCode);
        }
        realtimeWeather.setLocation(location);
        realtimeWeather.setLastUpdate(new Date());

        if(location.getRealtimeWeather() == null){
            location.setRealtimeWeather(realtimeWeather);
            Location updatedLocation = locationRepository.save(location);
            return updatedLocation.getRealtimeWeather();
        }
        return realtimeWeatherRepository.save(realtimeWeather);
    }

}
