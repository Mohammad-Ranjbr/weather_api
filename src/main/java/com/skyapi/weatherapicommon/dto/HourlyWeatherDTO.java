package com.skyapi.weatherapicommon.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HourlyWeatherDTO {

    private int hourOfDay;
    private int temperature;
    private int precipitation;
    private String status;

}
