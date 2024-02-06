package com.skyapi.weatherapicommon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@ToString
@JsonPropertyOrder({"hour_of_day","temperature","precipitation","status"})
public class HourlyWeatherDTO {

    @JsonProperty("hour_of_day")
    private int hourOfDay;
    @Range(min = -50 , max = 50 , message = "Temperature must be in the range of -50 to 50 Celsius degree")
    private int temperature;
    @Range(min = 0 , max = 100 , message = "Precipitation must be in the range of 0 to 100 percentage")
    private int precipitation;
    @NotBlank(message = "Status must not be blank")
    @Length(min = 3 , max = 50 , message = "Status must be in between 3-50 characters")
    private String status;

    public HourlyWeatherDTO hourOfDay(int hourOfDay){
        setHourOfDay(hourOfDay);
        return this;
    }

    public HourlyWeatherDTO temperature(int temperature){
        setTemperature(temperature);
        return this;
    }

    public HourlyWeatherDTO precipitation(int precipitation){
        setPrecipitation(precipitation);
        return this;
    }

    public HourlyWeatherDTO status(String status){
        setStatus(status);
        return this;
    }

}
