package com.skyapi.weatherapicommon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "locations")
public class Location {

    @Id
    @Column(length = 12 , nullable = false , unique = true)
    private String code;

    @Column(length = 128 , nullable = false)
    private String cityName;

    @Column(length = 128 , nullable = false)
    private String regionName;

    @Column(length = 64 , nullable = false)
    private String countryName;

    @Column(length = 2 , nullable = false)
    private String countryCode;

    private boolean enabled;

    @JsonIgnore
    private boolean trashed;

    @JsonIgnore
    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "location" , cascade = CascadeType.ALL) // casecade inja yani har balayi sare location biyarim sare realtime weather ham miad
    private RealtimeWeather realtimeWeather;

    @OneToMany(mappedBy = "id.location" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<HourlyWeather> listHourlyWeather = new ArrayList<>();

    public Location(String cityName, String regionName, String countryName, String countryCode) {
        this.cityName = cityName;
        this.regionName = regionName;
        this.countryName = countryName;
        this.countryCode = countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(code, location.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return
                cityName + ", " +
                (regionName != null ? regionName+", " : "" ) + // chon mitone khali bashe "," ro mibarim too
                countryName;
    }

    public Location code(String code){
        this.code = code;
        return this;
    }

}
