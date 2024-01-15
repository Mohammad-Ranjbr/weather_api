package com.skyapi.weatherapiservice.service.implement;

import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapicommon.model.RealtimeWeather;
import com.skyapi.weatherapiservice.exception.LocationNotFoundException;
import com.skyapi.weatherapiservice.repository.RealtimeWeatherRepository;
import com.skyapi.weatherapiservice.service.RealtimeWeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RealtimeWeatherServiceImpl implements RealtimeWeatherService {

    private final RealtimeWeatherRepository realtimeWeatherRepository;

    @Autowired
    public RealtimeWeatherServiceImpl(RealtimeWeatherRepository realtimeWeatherRepository){
        this.realtimeWeatherRepository = realtimeWeatherRepository;
    }

    @Override
    public RealtimeWeather getByLocation(Location location) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode,cityName);
        if(realtimeWeather == null){
            throw new LocationNotFoundException("No location found with the given country code and city name");
        }
        return realtimeWeather;
    }

}
