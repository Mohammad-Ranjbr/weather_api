package com.skyapi.weatherapiservice.service;

import com.skyapi.weatherapicommon.model.Location;

import java.util.List;

public interface LocationService {

    Location add(Location location);
    List<Location> list();
    Location get(String code);
    Location update(Location locationInRequest);
    void delete(String code);

}
