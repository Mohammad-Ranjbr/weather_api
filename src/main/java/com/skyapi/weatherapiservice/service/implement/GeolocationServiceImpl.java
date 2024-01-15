package com.skyapi.weatherapiservice.service.implement;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import com.skyapi.weatherapicommon.model.Location;
import com.skyapi.weatherapiservice.exception.GeolocationException;
import com.skyapi.weatherapiservice.service.GeolocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeolocationServiceImpl implements GeolocationService {

    private final IP2Location ipLocator;
    private static final Logger LOGGER = LoggerFactory.getLogger(GeolocationServiceImpl.class);
    private static final String DBPath = "Ip2LocationDB/IP2LOCATION-LITE-DB3.BIN/IP2LOCATION-LITE-DB3.BIN";

    //private final IP2Location ipLocator = new IP2Location();

    @Autowired
    public GeolocationServiceImpl(IP2Location ipLocator){
        this.ipLocator = ipLocator;
        try{
            ipLocator.Open(DBPath);
        } catch (IOException ioException){
            LOGGER.error(ioException.getMessage(),ioException);
        }
    }

    public Location getLocation(String ipAddress){
        try{
            IPResult ipResult = ipLocator.IPQuery(ipAddress);
            if(!ipResult.getStatus().equals("OK")){
                throw new GeolocationException("Geolocation failed with status: " + ipResult.getStatus());
            }
            LOGGER.info(ipResult.toString());
            return  new Location(ipResult.getCity() , ipResult.getRegion() , ipResult.getCountryLong() , ipResult.getCountryShort());
        } catch (IOException ioException){
            throw new GeolocationException("Error querying IP database" , ioException);
        }
    }


}
