package com.skyapi;

import com.ip2location.IP2Location;
import com.skyapi.weatherapicommon.dto.HourlyWeatherDTO;
import com.skyapi.weatherapicommon.model.HourlyWeather;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WeatherApiProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherApiProjectApplication.class, args);
	}

	@Bean
	public IP2Location getIP2Location(){
		return new IP2Location();
	}

	@Bean
	public ModelMapper getModelMapper(){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		TypeMap<HourlyWeather,HourlyWeatherDTO> typeMap1 = modelMapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class);
		typeMap1.addMapping(src -> src.getId().getHourOfDay() , HourlyWeatherDTO::setHourOfDay);

		TypeMap<HourlyWeatherDTO,HourlyWeather> typeMap2 = modelMapper.typeMap(HourlyWeatherDTO.class,HourlyWeather.class);
		typeMap2.addMapping(HourlyWeatherDTO::getHourOfDay,(dest, value) -> dest.getId().setHourOfDay(value != null ? (int) value : 0));

		return modelMapper;
	}

}
