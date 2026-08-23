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

import java.util.Objects;

@Entity
public class Demographic {

    @Id
    @Column(name = "demography_id")
    private String demographyId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gender_id")
    private Gender gender;

    @ManyToOne(fetch = FetchType.EAGER)
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Demographic)) return false;
        Demographic that = (Demographic) o;
        return Objects.equals(demographyId, that.demographyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(demographyId);
    }

    @Override
    public String toString() {
        return "Demographic{" +
                "demographyId='" + demographyId + '\'' +
                ", genderId=" + (gender != null ? gender.getGenderId() : null) +
                ", raceId=" + (race != null ? race.getRaceId() : null) +
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