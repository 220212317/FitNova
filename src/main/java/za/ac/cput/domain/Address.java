package za.ac.cput.domain;

public class Address
{
   private String addressId;
   private String streetName;
   private String suburb;
   private String city;
   private String postalCode;

     private Address(Builder builder){
        this.addressId = builder.addressId;
        this.streetName = builder.streetName;
        this.suburb = builder.suburb;
        this.city = builder.city;
        this.postalCode = builder.postalCode;


     }

   public String getAddressId() {
      return addressId;
   }

   public void setAddressId(String addressId) {
      this.addressId = addressId;
   }

   public String getStreetName() {
      return streetName;
   }

   public void setStreetName(String streetName) {
      this.streetName = streetName;
   }

   public String getSuburb() {
      return suburb;
   }

   public void setSuburb(String suburb) {
      this.suburb = suburb;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public String getPostalCode() {
      return postalCode;
   }

   public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
   }
   public static class Builder{
      private String addressId;
      private String streetName;
      private String suburb;
      private String city;
      private String postalCode;

      public Builder getAddressId(String addressId){
         this.addressId = addressId;
         return this;
      }

      public Builder getStreetName(String streetName){
         this.streetName= streetName;
         return this;
      }

      public Builder getSuburb(String suburb){
         this.suburb = suburb;
         return this;
      }
      public Builder getCity(String city){
         this.city = city;
         return this;

      }
      public Builder getpostalCode(String postalCode){
        this.postalCode= postalCode;
        return this;

      }
      public Address build(){
         return new Address(this);
      }
   }


}
