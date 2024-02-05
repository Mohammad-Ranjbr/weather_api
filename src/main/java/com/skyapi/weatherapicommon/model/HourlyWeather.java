package com.skyapi.weatherapicommon.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "weather_hourly")
public class HourlyWeather {

    @EmbeddedId
    private HourlyWeatherId id = new HourlyWeatherId();
    private int temperature;
    private int precipitation;
    @Column(length = 50)
    private String status;

    public HourlyWeather temperature(int temperature){
        this.temperature = temperature;
        return this;
    }

    public HourlyWeather id(Location location , int hour){
        this.id.setLocation(location);
        this.id.setHourOfDay(hour);
        return this;
    }

    public HourlyWeather precipitation(int precipitation){
        this.precipitation = precipitation;
        return this;
    }

    public HourlyWeather status(String status){
        this.status = status;
        return this;
    }

    public HourlyWeather location(Location location){
        this.id.setLocation(location);
        return this;
    }

    public HourlyWeather hourOfDay(int hour){
        this.id.setHourOfDay(hour);
        return this;
    }

}
