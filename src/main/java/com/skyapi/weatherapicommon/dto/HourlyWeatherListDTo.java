package com.skyapi.weatherapicommon.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HourlyWeatherListDTo {

    private String location;
    private List<HourlyWeatherDTO> hourlyForecast = new ArrayList<>();

    public void addWeatherHourlyDTO(HourlyWeatherDTO hourlyWeatherDTO){
        this.hourlyForecast.add(hourlyWeatherDTO);
    }

}
