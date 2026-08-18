/* Demographic.java
   Author: Inga Plati (230126634)
   Date: 18 August 2026 */
package za.ac.cput.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Demographic {

    @Id
    @Column(name = "demography_id")
    private String demographyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_id")
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "race_id")
    private Race race;

    public Demographic() {
    }

    private Demographic(Builder builder) {
        this.demographyId = builder.demographyId;
        this.gender = builder.gender;
        this.race = builder.race;
    }

    public String getDemographyId() {
        return demographyId;
    }

    public Gender getGender() {
        return gender;
    }

    public Race getRace() {
        return race;
    }

    @Override
    public String toString() {
        return "Demographic{" +
                "demographyId='" + demographyId + '\'' +
                ", gender=" + gender +
                ", race=" + race +
                '}';
    }

    public static class Builder {
        private String demographyId;
        private Gender gender;
        private Race race;

        public Builder setDemographyId(String demographyId) {
            this.demographyId = demographyId;
            return this;
        }

        public Builder setGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder setRace(Race race) {
            this.race = race;
            return this;
        }

        public Builder copy(Demographic demographic) {
            this.demographyId = demographic.demographyId;
            this.gender = demographic.gender;
            this.race = demographic.race;
            return this;
        }

        public Demographic build() {
            return new Demographic(this);
        }
    }
}