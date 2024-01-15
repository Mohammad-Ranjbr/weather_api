package com.skyapi.weatherapiservice.service;

import com.skyapi.weatherapicommon.dto.RealtimeWeatherDTO;
import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapicommon.model.RealtimeWeather;

public interface RealtimeWeatherService {

    RealtimeWeather getByLocation(Location location);
    RealtimeWeather getByLocationCode(String locationCode);
    RealtimeWeather update(String locationCode , RealtimeWeather realtimeWeather);

}
