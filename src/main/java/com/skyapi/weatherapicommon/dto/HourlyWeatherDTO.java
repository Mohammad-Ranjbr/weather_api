package com.skyapi.weatherapicommon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HourlyWeatherDTO {

    @JsonProperty("hour_of_day")
    private int hourOfDay;
    private int temperature;
    private int precipitation;
    private String status;

}
