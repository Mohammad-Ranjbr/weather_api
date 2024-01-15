package com.skyapi.weatherapicommon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "realtime_weather")
public class RealtimeWeather {

    @Id
    @JsonIgnore
    @Column(name = "location_code")
    private String locationCode;

    @Range(min = -50 , max = 50 , message = "Temperature must be in the range of -50 to 50 Celsius degree")
    private int temperature;

    @Range(min = 0 , max = 100 , message = "Humidity must be in the range of 0 to 100 percentage")
    private int humidity;

    @Range(min = 0 , max = 100 , message = "Precipitation must be in the range of 0 to 100 percentage")
    private int precipitation;

    @JsonProperty("wind_speed")
    @Range(min = 0 , max = 200 , message = "Wind Speed must be in the range of 0 to 200 km/h")
    private int windSpeed;

    @Column(length = 50)
    @NotBlank(message = "Status must not be blank")
    @Length(min = 3 , max = 50 , message = "Status must be in between 3-50 characters")
    private String status;

    @JsonIgnore
    @JsonProperty("last_updated")
    private Date lastUpdate;

    @OneToOne
    @MapsId
    @JsonIgnore
    @JoinColumn(name = "location_code")
    private Location location;

    public void setLocation(Location location){
        this.locationCode = location.getCode();
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealtimeWeather that = (RealtimeWeather) o;
        return Objects.equals(locationCode, that.locationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationCode);
    }

}
