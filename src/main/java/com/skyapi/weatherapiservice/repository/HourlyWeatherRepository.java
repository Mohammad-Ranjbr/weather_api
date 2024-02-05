package com.skyapi.weatherapiservice.repository;

import com.skyapi.weatherapicommon.model.HourlyWeather;
import com.skyapi.weatherapicommon.model.HourlyWeatherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HourlyWeatherRepository extends JpaRepository<HourlyWeather, HourlyWeatherId> {
}
