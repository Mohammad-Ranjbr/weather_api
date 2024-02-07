package com.skyapi.weatherapiservice.controller;

import com.skyapi.weatherapicommon.dto.LocationDTO;
import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapiservice.exception.LocationNotFoundException;
import com.skyapi.weatherapiservice.service.LocationService;
import jakarta.validation.Valid;
//import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/v1/locations")
public class LocationApiController {

    private final LocationService locationService;
    private final ModelMapper modelMapper;

    @Autowired
    public LocationApiController(LocationService locationService , ModelMapper modelMapper){
        this.locationService = locationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<LocationDTO> addLocation(@RequestBody  @Valid LocationDTO dto){
        Location addedLocation =locationService.add(dto2Entity(dto));
        URI uri = URI.create("/v1/locations/" + addedLocation.getCode()); // redirect url
        return ResponseEntity.created(uri).body(entity2DTO(addedLocation));
    }

    @GetMapping
    public ResponseEntity<?> locations(){
        List<Location> locations = locationService.list();
        if(locations.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(listEntity2ListDTO(locations), HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getLocation(@PathVariable("code") /*@NotNull(message = "Location code must not be null")*/ String code){
        Location location = locationService.get(code);
        if(location == null){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(entity2DTO(location),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateLocation(@RequestBody @Valid LocationDTO dto){
        try{
            Location updatedLocation = locationService.update(dto2Entity(dto));
            return ResponseEntity.ok(entity2DTO(updatedLocation));
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

    private LocationDTO entity2DTO(Location entity){
        return modelMapper.map(entity,LocationDTO.class);
    }

    private Location dto2Entity(LocationDTO dto){
        return modelMapper.map(dto,Location.class);
    }

    private List<LocationDTO> listEntity2ListDTO(List<Location> listEntity){
        return listEntity.stream().map(this::entity2DTO).collect(Collectors.toList());
    }

}
