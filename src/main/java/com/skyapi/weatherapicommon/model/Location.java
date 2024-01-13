package com.skyapi.weatherapicommon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "locations")
public class Location {

    @Id
    @NotBlank
    @Column(length = 12 , nullable = false , unique = true)
    private String code;

    @NotBlank // ina nemitonan khali bashan
    @JsonProperty("city_name")
    @Column(length = 128 , nullable = false)
    private String cityName;

    @NotNull // in mitone khali bashe
    @JsonProperty("region_name")
    @Column(length = 128 , nullable = false)
    private String regionName;

    @NotBlank
    @JsonProperty("country_name")
    @Column(length = 64 , nullable = false)
    private String countryName;

    @NotBlank
    @JsonProperty("country_code")
    @Column(length = 128 , nullable = false)
    private String countryCode;

    private boolean enabled;

    @JsonIgnore
    private boolean trashed;

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

}
