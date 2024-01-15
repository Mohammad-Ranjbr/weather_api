package com.skyapi.realtime;

import com.skyapi.weatherapicommon.model.RealtimeWeather;
import com.skyapi.weatherapiservice.repository.RealtimeWeatherRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

@DataJpaTest
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RealtimeWeatherRepositoryTests {

    @Autowired
    private RealtimeWeatherRepository realtimeWeatherRepository;

    @Test
    public void testUpdate(){
        String location_code = "NYC_USA";

        if(realtimeWeatherRepository.findById(location_code).isPresent()) {
            RealtimeWeather realtimeWeather = realtimeWeatherRepository.findById(location_code).get();
            realtimeWeather.setTemperature(2);
            realtimeWeather.setHumidity(32);
            realtimeWeather.setPrecipitation(43);
            realtimeWeather.setStatus("Snowy");
            realtimeWeather.setWindSpeed(24);
            realtimeWeather.setLastUpdate(new Date());

            RealtimeWeather updatedRealtimeWeather = realtimeWeatherRepository.save(realtimeWeather);
            Assertions.assertThat(updatedRealtimeWeather.getHumidity()).isEqualTo(32);
        }

    }

    @Test
    public void testFindByCountryCodeAndCityNotFound(){
        String countryCode = "JP";
        String cityName = "Tokyo";
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode,cityName);
        Assertions.assertThat(realtimeWeather).isNull();
    }

    @Test
    public void testFindByCountryCodeAndCityFound(){
        String countryCode = "US";
        String cityName = "New York City";
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode,cityName);
        Assertions.assertThat(realtimeWeather).isNotNull();
        Assertions.assertThat(realtimeWeather.getLocation().getCityName()).isEqualTo(cityName);
        Assertions.assertThat(realtimeWeather.getLocation().getCountryCode()).isEqualTo(countryCode);
    }

    @Test
    public void testFindByLocationNotFound(){
        String locationCode = "ABCD";
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);
        Assertions.assertThat(realtimeWeather).isNull();
    }

    @Test
    public void testFindByTrashedLocationNotFound(){
        String locationCode = "LACA_USA";
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);
        Assertions.assertThat(realtimeWeather).isNull();
    }

    @Test
    public void testFindByLocationFound(){
        String locationCode = "DEHLI_IN";
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);
        Assertions.assertThat(realtimeWeather).isNotNull();
        Assertions.assertThat(realtimeWeather.getLocationCode()).isEqualTo("DEHLI_IN");

    }

}
