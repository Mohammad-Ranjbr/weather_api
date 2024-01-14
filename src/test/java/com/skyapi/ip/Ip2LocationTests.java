package com.skyapi.ip;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Ip2LocationTests {

    private final String DBPath = "Ip2LocationDB/IP2LOCATION-LITE-DB3.BIN/IP2LOCATION-LITE-DB3.BIN";

    @Test
    public void testInvalidIp() throws IOException {
        IP2Location ipLocator = new IP2Location();
        ipLocator.Open(DBPath);

        String ipAddress = "abc";
        IPResult ipResult = ipLocator.IPQuery(ipAddress);

        System.out.println(ipResult);
        Assertions.assertThat(ipResult.getStatus()).isEqualTo("INVALID_IP_ADDRESS");
    }

    @Test
    public void testValidIp1() throws IOException{
        IP2Location ipLocator = new IP2Location();
        ipLocator.Open(DBPath);

        String ipAddress = "108.30.178.78";  // New York
        IPResult ipResult = ipLocator.IPQuery(ipAddress);

        System.out.println(ipResult);
        Assertions.assertThat(ipResult.getStatus()).isEqualTo("OK");
        Assertions.assertThat(ipResult.getCity()).isEqualTo("New York City");
    }

    @Test
    public void testValidIp2() throws IOException{
        IP2Location ipLocator = new IP2Location();
        ipLocator.Open(DBPath);

        String ipAddress = "103.48.198.141";  // Dehli
        IPResult ipResult = ipLocator.IPQuery(ipAddress);

        System.out.println(ipResult);
        Assertions.assertThat(ipResult.getStatus()).isEqualTo("OK");
        Assertions.assertThat(ipResult.getCity()).isEqualTo("Delhi");
    }

    @Test
    public void testValidIp3() throws IOException{
        IP2Location ipLocator = new IP2Location();
        ipLocator.Open(DBPath);

        String ipAddress = "185.218.139.84";  // Tehran
        IPResult ipResult = ipLocator.IPQuery(ipAddress);

        System.out.println(ipResult);
        Assertions.assertThat(ipResult.getStatus()).isEqualTo("OK");
        Assertions.assertThat(ipResult.getCity()).isEqualTo("Tehran");
        Assertions.assertThat(ipResult.getCountryShort()).isEqualTo("IR");
        Assertions.assertThat(ipResult.getCountryLong()).isEqualTo("Iran (Islamic Republic of)");
    }

}
