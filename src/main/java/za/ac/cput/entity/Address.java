package za.ac.cput.entity;

/**
 * Address.java
 * Address entity class
 * Author: Athi Sintiya (220212317)
 */
public class Address {

    private String addressId;
    private String streetNumber;
    private String streetName;
    private String suburbName;
    private String city;
    private String postalCode;
    private String province;
    private String country;


    public Address() {
    }

    public Address(Builder builder) {
        this.addressId = builder.addressId;
        this.streetNumber = builder.streetNumber;
        this.streetName = builder.streetName;
        this.suburbName = builder.suburbName;
        this.city = builder.city;
        this.postalCode = builder.postalCode;
        this.province = builder.province;
        this.country = builder.country;
    }

    public String getAddressId() {
        return addressId;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getSuburbName() {
        return suburbName;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getProvince() {
        return province;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId='" + addressId + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", streetName='" + streetName + '\'' +
                ", suburbName='" + suburbName + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    public static class Builder {
        private String addressId;
        private String streetNumber;
        private String streetName;
        private String suburbName;
        private String city;
        private String postalCode;
        private String province;
        private String country;

        public Builder setAddressId(String addressId) {
            this.addressId = addressId;
            return this;
        }

        public Builder setStreetNumber(String streetNumber) {
            this.streetNumber = streetNumber;
            return this;
        }

        public Builder setStreetName(String streetName) {
            this.streetName = streetName;
            return this;
        }

        public Builder setSuburbName(String suburbName) {
            this.suburbName = suburbName;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder setProvince(String province) {
            this.province = province;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder copy(Address address) {
            this.addressId = address.addressId;
            this.streetNumber = address.streetNumber;
            this.streetName = address.streetName;
            this.suburbName = address.suburbName;
            this.city = address.city;
            this.postalCode = address.postalCode;
            this.province = address.province;
            this.country = address.country;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }
}