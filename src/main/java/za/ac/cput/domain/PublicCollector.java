package za.ac.cput.domain;

public class PublicCollector {

    private String publicCollectorID;
    private String municipalityName;

    private PublicCollector() {}

    private PublicCollector(Builder builder) {
        this.publicCollectorID = builder.publicCollectorID;
        this.municipalityName = builder.municipalityName;
    }

    public String getPublicCollectorID() {
        return publicCollectorID;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void collectWaste(Report report) {
        System.out.println("Municipality collecting waste for report: " + report);
    }

    public double calculateFee() {
        return 0.0; // public collection is free
    }

    @Override
    public String toString() {
        return "PublicCollector{" +
                "publicCollectorID='" + publicCollectorID + '\'' +
                ", municipalityName='" + municipalityName + '\'' +
                '}';
    }

    public static class Builder {

        private String publicCollectorID;
        private String municipalityName;

        public Builder setPublicCollectorID(String publicCollectorID) {
            this.publicCollectorID = publicCollectorID;
            return this;
        }

        public Builder setMunicipalityName(String municipalityName) {
            this.municipalityName = municipalityName;
            return this;
        }

        public PublicCollector build() {
            return new PublicCollector(this);
        }
    }
}