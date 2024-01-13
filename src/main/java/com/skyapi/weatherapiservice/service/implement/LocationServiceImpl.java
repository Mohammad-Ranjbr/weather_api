package com.skyapi.weatherapiservice.service.implement;

import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapiservice.exception.LocationNotFoundException;
import com.skyapi.weatherapiservice.repository.LocationRepository;
import com.skyapi.weatherapiservice.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional // chon to repository interface az update query estefade kardim
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

    @Override
    public List<Location> list() {
        return locationRepository.findUntrashed();
    }

    @Override
    public Location get(String code) {
        return locationRepository.findByCode(code);
    }

    @Override
    public Location update(Location locationInRequest) throws LocationNotFoundException{
        String code = locationInRequest.getCode();
        Location locationDB = locationRepository.findByCode(code);
        if(locationDB == null){
            throw new LocationNotFoundException("No location found with the given code "+code);
        }
        locationDB.setCityName(locationInRequest.getCityName());
        locationDB.setRegionName(locationInRequest.getRegionName());
        locationDB.setCountryCode(locationInRequest.getCountryCode());
        locationDB.setCountryName(locationInRequest.getCountryName());
        locationDB.setEnabled(locationInRequest.isEnabled());
        return locationRepository.save(locationDB);
    }

    @Override
    public void delete(String code) throws LocationNotFoundException{
        Location location = locationRepository.findByCode(code);
        if(location == null){
            throw new LocationNotFoundException("No location found with the given code "+code);
        }
        locationRepository.trashByCode(code);
    }

}
