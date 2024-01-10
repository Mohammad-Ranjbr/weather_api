package com.skyapi.weatherapiservice.service.implement;

import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapiservice.repository.LocationRepository;
import com.skyapi.weatherapiservice.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }
    @Override
    public Location add(Location location) {
        return locationRepository.save(location);
    }
}
