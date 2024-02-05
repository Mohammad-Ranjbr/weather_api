package com.skyapi.hourly;

import com.skyapi.weatherapicommon.model.HourlyWeather;
import com.skyapi.weatherapicommon.model.HourlyWeatherId;
import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapiservice.repository.HourlyWeatherRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

@DataJpaTest
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HourlyWeatherRepositoryTests {

    private final HourlyWeatherRepository hourlyWeatherRepository;

    @Autowired
    public HourlyWeatherRepositoryTests(HourlyWeatherRepository hourlyWeatherRepository){
        this.hourlyWeatherRepository = hourlyWeatherRepository;
    }

    @Test
    public void testAdd(){
        String location_code = "DEHLI_IN";
        Location location = new Location().code(location_code);
        int hourOfDay = 13;
        HourlyWeather forecast = new HourlyWeather()
                .location(location)
                .hourOfDay(hourOfDay)
                .temperature(13)
                .precipitation(70)
                .status("Cloudy");

        HourlyWeather hourlyWeather = hourlyWeatherRepository.save(forecast);
        Assertions.assertThat(hourlyWeather.getId().getLocation().getCode()).isEqualTo(location_code);
        Assertions.assertThat(hourlyWeather.getId().getHourOfDay()).isEqualTo(hourOfDay);
    }

    @Test
    public void testDelete(){
        Location location = new Location().code("DEHLI_IN");
        HourlyWeatherId id = new HourlyWeatherId(13,location);
        hourlyWeatherRepository.deleteById(id);

        Optional<HourlyWeather> result = hourlyWeatherRepository.findById(id);
        Assertions.assertThat(result).isNotPresent();
    }

}
