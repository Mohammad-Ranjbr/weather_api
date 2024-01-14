package com.skyapi.location;

import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapicommon.model.RealtimeWeather;
import com.skyapi.weatherapiservice.repository.LocationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

@DataJpaTest
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LocationRepositoryTests {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void testAddSuccess(){
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryName("US");
        location.setCountryCode("United States of America");
        location.setEnabled(true);

        Location savedLocation = locationRepository.save(location);
        Assertions.assertThat(savedLocation).isNotNull();
        Assertions.assertThat(savedLocation.getCode()).isEqualTo("NYC_USA");
    }

    @Test
    public void testListSuccess(){
        List<Location> locations = locationRepository.findUntrashed();
        Assertions.assertThat(locations).isNotEmpty();
        locations.forEach(System.out::println);
    }

    @Test
    public void testGetNotFound(){
        String code = "ABCD";
        Location location = locationRepository.findByCode(code);
        Assertions.assertThat(location).isNull();
    }

    @Test
    public void testGetFound(){
        String code = "DEHLI_IN";
        Location location = locationRepository.findByCode(code);
        Assertions.assertThat(location).isNotNull();
        Assertions.assertThat(location.getCode()).isEqualTo(code);
    }

    @Test
    public void testTrashSuccess(){
        String code = "LACA_USA";
        locationRepository.trashByCode(code);
        Location location = locationRepository.findByCode(code);
        Assertions.assertThat(location).isNull();
    }

    @Test
    public void testAddRealtimeWeatherData(){
        String code = "NYC_USA";

        Location location = locationRepository.findByCode(code);
        RealtimeWeather realtimeWeather = location.getRealtimeWeather();

        if(realtimeWeather == null){
            realtimeWeather = new RealtimeWeather();
            realtimeWeather.setLocation(location);
            location.setRealtimeWeather(realtimeWeather);
        }

        realtimeWeather.setTemperature(-1);
        realtimeWeather.setHumidity(30);
        realtimeWeather.setPrecipitation(40);
        realtimeWeather.setStatus("Snowy");
        realtimeWeather.setWindSpeed(30);
        realtimeWeather.setLastUpdate(new Date());

        Location updatedLocation = locationRepository.save(location);
        Assertions.assertThat(updatedLocation.getRealtimeWeather().getLocationCode()).isEqualTo(code);

    }

}
