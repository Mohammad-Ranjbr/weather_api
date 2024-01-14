package com.skyapi.weatherapicommon.utility;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtility {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtility.class);

    public static String getIPAddress(HttpServletRequest request){
        String ip = request.getHeader("X-FORWARED-FOR"); // if real client ip hides with load balancer
        if(ip == null || ip.isEmpty()){ // null == null | empty == ""
            ip = request.getRemoteAddr();
        }
        LOGGER.info("Clients IP Address : " + ip);
        return ip;
    }

}
