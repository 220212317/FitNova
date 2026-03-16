package za.ac.cput.domain;

public class GeoLocation {

        private double latitude;
        private double longitude;

        private GeoLocation(Builder builder) {
            this.latitude = builder.latitude;
            this.longitude = builder.longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public static class Builder {

            private double latitude;
            private double longitude;

            public Builder setLatitude(double latitude) {
                this.latitude = latitude;
                return this;
            }

            public Builder setLongitude(double longitude) {
                this.longitude = longitude;
                return this;
            }

            public GeoLocation build() {
                return new GeoLocation(this);
            }
        }
    }
