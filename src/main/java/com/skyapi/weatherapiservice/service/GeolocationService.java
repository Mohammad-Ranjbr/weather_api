package com.skyapi.weatherapiservice.service;

import com.skyapi.weatherapicommon.model.Location;

public interface GeolocationService {

    Location getLocation(String ipAddress);

}
