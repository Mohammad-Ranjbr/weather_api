package com.skyapi.hourly;

import com.skyapi.weatherapiservice.repository.HourlyWeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HourlyWeatherRepositoryTests {

    private final HourlyWeatherRepository hourlyWeatherRepository;

    @Autowired
    public HourlyWeatherRepositoryTests(HourlyWeatherRepository hourlyWeatherRepository){
        this.hourlyWeatherRepository = hourlyWeatherRepository;
    }
}
