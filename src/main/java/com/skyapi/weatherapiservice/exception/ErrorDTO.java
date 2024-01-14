package com.skyapi.weatherapiservice.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ErrorDTO {

    private Date timestamp;
    private int status;
    private String path;
    private String error;

}
