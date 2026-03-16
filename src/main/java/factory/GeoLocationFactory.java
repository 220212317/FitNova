package factory;

import za.ac.cput.domain.GeoLocation;

public class GeoLocationFactory {

        public static GeoLocation createGeoLocation(double latitude, double longitude) {

            return new GeoLocation.Builder()
                    .setLatitude(latitude)
                    .setLongitude(longitude)
                    .build();
        }
    }

