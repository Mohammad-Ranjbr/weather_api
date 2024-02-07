package com.skyapi.weatherapiservice.exception;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException(String locationCode) {
        super("No location found with the given code "+locationCode);
    }

}
