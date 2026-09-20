/*
 * Race.java
 * Author: Inga Plati
 * 230126634
 */
package za.ac.cput.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Race {

    @Id
    @Column(name = "race_id")
    private String raceId;

    @Column(nullable = false)
    private String description;

    public Race() {
    }

    private Race(Builder builder) {
        this.raceId = builder.raceId;
        this.description = builder.description;
    }

    public String getRaceId() {
        return raceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Race)) return false;
        Race race = (Race) o;
        return Objects.equals(raceId, race.raceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(raceId);
    }

    @Override
    public String toString() {
        return "Race{" +
                "raceId='" + raceId + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static class Builder {
        private String raceId;
        private String description;

        public Builder setRaceId(String raceId) {
            this.raceId = raceId;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder copy(Race race) {
            this.raceId = race.raceId;
            this.description = race.description;
            return this;
        }

        public Race build() {
            return new Race(this);
        }
    }
}