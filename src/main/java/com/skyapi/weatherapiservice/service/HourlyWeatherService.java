package com.skyapi.weatherapiservice.service;

import com.skyapi.weatherapicommon.model.HourlyWeather;
import com.skyapi.weatherapicommon.model.Location;

import java.util.List;

public interface HourlyWeatherService {

    List<HourlyWeather> getByLocation(Location location,int currentHour);
    List<HourlyWeather> getByLocationCode(String locationCode,int currentHour);
    List<HourlyWeather> updateByLocationCode(String locationCode , List<HourlyWeather> hourlyForecastInRequest);

}
