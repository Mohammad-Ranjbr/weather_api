package com.skyapi.weatherapiservice.exception;

public class LocationNotFoundException extends RuntimeException{

    public LocationNotFoundException(String message) {
        super(message);
    }

}
