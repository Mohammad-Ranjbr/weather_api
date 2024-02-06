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
		TypeMap<HourlyWeather,HourlyWeatherDTO> typeMap = modelMapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class);
		typeMap.addMapping(src -> src.getId().getHourOfDay() , HourlyWeatherDTO::setHourOfDay);
		return modelMapper;
	}

}
