package com.skyapi.weatherapicommon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "locations")
public class Location {

    @Id
    @NotNull(message = "Location code cannot be null")
    @Column(length = 12 , nullable = false , unique = true)
    @Length(min = 3 , max = 12 , message = "Location code must have 3-12 characters")
    private String code;

    //@NotBlank(message = "City name cannot be left blank") // ina nemitonan khali bashan
    @JsonProperty("city_name")
    @Column(length = 128 , nullable = false)
    @NotNull(message = "City name cannot be null")
    @Length(min = 3 , max = 128 , message = "City name must have 3-128 characters")
    private String cityName;

    //@NotNull(message = "Region name cannot be left null") // in mitone khali bashe
    @JsonProperty("region_name")
    @Column(length = 128 , nullable = false)
    @Length(min = 3 , max = 128 , message = "Region name must have 3-128 characters")
    private String regionName;

    @JsonProperty("country_name")
    @Column(length = 64 , nullable = false)
    @NotNull(message = "Country name cannot be null")
    @Length(min = 2 , max = 64 , message = "Country name must have 3-64 characters")
    private String countryName;

    @JsonProperty("country_code")
    @Column(length = 2 , nullable = false)
    @NotNull(message = "Country code cannot be null")
    @Length(min = 2 , max = 128 , message = "Country code must have 2-128 characters")
    private String countryCode;

    private boolean enabled;

    @JsonIgnore
    private boolean trashed;

    @JsonIgnore
    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "location" , cascade = CascadeType.ALL) // casecade inja yani har balayi sare location biyarim sare realtime weather ham miad
    private RealtimeWeather realtimeWeather;

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

}
