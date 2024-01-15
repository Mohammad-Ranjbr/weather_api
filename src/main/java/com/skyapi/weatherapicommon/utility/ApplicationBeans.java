package com.skyapi.weatherapicommon.utility;

import com.ip2location.IP2Location;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeans {

    @Bean
    public IP2Location getIP2Location(){
        return new IP2Location();
    }

}
