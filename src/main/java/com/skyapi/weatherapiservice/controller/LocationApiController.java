package com.skyapi.weatherapiservice.controller;

import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapiservice.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/v1/locations")
public class LocationApiController {

    private final LocationService locationService;

    @Autowired
    public LocationApiController(LocationService locationService){
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<Location> addLocation(@RequestBody Location location){
        Location addedLocation =locationService.add(location);
        URI uri = URI.create("/v1/locations/" + location.getCode());
        return ResponseEntity.created(uri).body(addedLocation);
    }

}
