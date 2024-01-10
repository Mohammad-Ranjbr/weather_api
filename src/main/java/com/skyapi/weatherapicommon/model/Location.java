package com.skyapi.weatherapicommon.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

    @Column(length = 128 , nullable = false)
    private String countryCode;

    private boolean enabled;
    private boolean trashed;

}
