package com.backend.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.net.InetAddress;

@Service
public class GeoLocationService {
    private DatabaseReader dbReader;

    public GeoLocationService() {
        try (InputStream dbStream = new ClassPathResource("GeoLite2-City.mmdb").getInputStream()) {
            this.dbReader = new DatabaseReader.Builder(dbStream).build();
        } catch (Exception e) {
            // Log this so you know the DB is missing in production
            System.err.println("CRITICAL: GeoLite2 database not found. Falling back to default zones.");
        }
    }

    public String resolveZoneFromIp(String ipAddress) {
        // Handle local development cases
        if (isLocal(ipAddress)) {
            return "Hyderabad, India (Dev)";
        }

        try {
            InetAddress addr = InetAddress.getByName(ipAddress);
            CityResponse response = dbReader.city(addr);

            String city = response.getCity().getName();
            String country = response.getCountry().getName();

            if (city == null) return country;
            return city + ", " + country;

        } catch (Exception e) {
            // Fallback if IP is not in database or is malformed
            return "Global Community";
        }
    }

    private boolean isLocal(String ip) {
        return ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1") || ip.startsWith("192.168.");
    }
}
