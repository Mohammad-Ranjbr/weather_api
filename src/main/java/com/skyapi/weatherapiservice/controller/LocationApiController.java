package com.skyapi.weatherapiservice.controller;

import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapiservice.exception.LocationNotFoundException;
import com.skyapi.weatherapiservice.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/locations")
public class LocationApiController {

    private final LocationService locationService;

    @Autowired
    public LocationApiController(LocationService locationService){
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<Location> addLocation(@RequestBody  @Valid Location location){
        Location addedLocation =locationService.add(location);
        URI uri = URI.create("/v1/locations/" + location.getCode()); // redirect url
        return ResponseEntity.created(uri).body(addedLocation);
    }

    @GetMapping
    public ResponseEntity<?> locations(){
        List<Location> locations = locationService.list();
        if(locations.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getLocation(@PathVariable("code") String code){
        Location location = locationService.get(code);
        if(location == null){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(location,HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateLocation(@RequestBody @Valid Location location){
        try{
            Location updatedLocation = locationService.update(location);
            return ResponseEntity.ok(updatedLocation);
        } catch (LocationNotFoundException locationNotFoundException){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteLocation(@PathVariable("code") String code){
        try{
            locationService.delete(code);
            return ResponseEntity.noContent().build();
        } catch (LocationNotFoundException locationNotFoundException){
            return ResponseEntity.notFound().build();
        }
    }

}
