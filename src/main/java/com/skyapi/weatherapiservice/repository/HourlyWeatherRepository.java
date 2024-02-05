package com.skyapi.weatherapiservice.repository;

import com.skyapi.weatherapicommon.model.HourlyWeather;
import com.skyapi.weatherapicommon.model.HourlyWeatherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HourlyWeatherRepository extends JpaRepository<HourlyWeather, HourlyWeatherId> {

    @Query("""
            Select h From HourlyWeather h where 
            h.id.location.code = ?1 and 
            h.id.hourOfDay > ?2 and
            h.id.location.trashed = false
            """)
    List<HourlyWeather> findByLocationCode(String locationCode , int currentHour);

}
