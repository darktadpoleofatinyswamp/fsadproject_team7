package com.backend.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeoLocationService {
    private static final Map<String, String> ZONE_MAP = new HashMap<>();

    static {
        ZONE_MAP.put("106.51.", "Gachibowli (500032)");
        ZONE_MAP.put("122.17.", "Banjara Hills (500034)");
        ZONE_MAP.put("49.205.", "Jubilee Hills (500033)");
        ZONE_MAP.put("183.82.", "Madhapur (500081)");
        ZONE_MAP.put("127.0.0.", "Development (Local)");
    }

    public String resolveZoneFromIp(String ipAddress) {
        if (ipAddress == null) return "Unknown Zone";

        return ZONE_MAP.entrySet().stream()
                .filter(entry -> ipAddress.startsWith(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("Hyderabad General (500001)");
    }
}
