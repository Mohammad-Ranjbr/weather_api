package com.skyapi.weatherapicommon.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "realtime_weather")
public class RealtimeWeather {

    @Id
    @Column(name = "location_code")
    private String locationCode;
    private int temperature;
    private int humidity;
    private int precipitation;
    @JsonProperty("wind_speed")
    private int windSpeed;

    @Column(length = 50)
    private String status;
    @JsonProperty("last_updated")
    private Date lastUpdate;

    @OneToOne
    @MapsId
    @JoinColumn(name = "location_code")
    private Location location;

    public void setLocation(Location location){
        this.locationCode = location.getCode();
        this.location = location;
    }

}
