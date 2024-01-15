package com.skyapi.weatherapiservice.repository;

import com.skyapi.weatherapicommon.model.RealtimeWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RealtimeWeatherRepository extends JpaRepository<RealtimeWeather,String> {

    @Query("Select r From RealtimeWeather r Where r.location.countryCode = ?1 And r.location.cityName = ?2")
    RealtimeWeather findByCountryCodeAndCity(String countryCode , String city);

    @Query("Select r From RealtimeWeather r Where r.locationCode = ?1 And r.location.trashed = false")
    RealtimeWeather findByLocationCode(String locationCode);

}
