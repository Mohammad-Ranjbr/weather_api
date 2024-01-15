package com.skyapi.weatherapicommon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RealtimeWeatherDTO {

    private int temperature;
    private int humidity;
    private int precipitation;
    @JsonProperty("wind_speed")
    private int windSpeed;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty("last_updated")
    private Date lastUpdate;
    private String location;

}
