package com.skyapi.location;

import com.skyapi.weatherapicommon.model.HourlyWeather;
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
        location.setCode("MBHM_IN");
        location.setCityName("Mumbai");
        location.setRegionName("Maharashtra");
        location.setCountryName("IN");
        location.setCountryCode("India");
        location.setEnabled(true);

        Location savedLocation = locationRepository.save(location);
        Assertions.assertThat(savedLocation).isNotNull();
        Assertions.assertThat(savedLocation.getCode()).isEqualTo("MBHM_IN");
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

    @Test
    public void testAddHourlyWeatherData(){
        if(locationRepository.findById("DEHLI_IN").isPresent()){
//            Location location = locationRepository.findById("MBHM_IN").get();
//            List<HourlyWeather> hourlyWeathers = location.getListHourlyWeather();
//            HourlyWeather forecast1 = new HourlyWeather()
//                    .id(location,8)
//                    .temperature(20)
//                    .precipitation(60)
//                    .status("Cloudy");
//
//            HourlyWeather forecast2 = new HourlyWeather()
//                    .location(location)
//                    .hourOfDay(9)
//                    .temperature(21)
//                    .precipitation(58)
//                    .status("Cloudy");
//
//            hourlyWeathers.add(forecast1);
//            hourlyWeathers.add(forecast2);
//
//            Location updatedLocation = locationRepository.save(location);
//            Assertions.assertThat(updatedLocation.getListHourlyWeather()).isNotEmpty();
            Location location = locationRepository.findById("DEHLI_IN").get();
            List<HourlyWeather> hourlyWeathers = location.getListHourlyWeather();
            HourlyWeather forecast1 = new HourlyWeather()
                    .id(location,10)
                    .temperature(10)
                    .precipitation(70)
                    .status("Snowy");

            HourlyWeather forecast2 = new HourlyWeather()
                    .location(location)
                    .hourOfDay(11)
                    .temperature(9)
                    .precipitation(72)
                    .status("Snowy");

            hourlyWeathers.add(forecast1);
            hourlyWeathers.add(forecast2);

            Location updatedLocation = locationRepository.save(location);
            Assertions.assertThat(updatedLocation.getListHourlyWeather()).isNotEmpty();
        }
    }

}
