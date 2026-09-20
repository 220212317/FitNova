/*
 * Gender.java
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
public class Gender {

    @Id
    @Column(name = "gender_id")
    private String genderId;

    @Column(nullable = false)
    private String description;

    public Gender() {
    }

    private Gender(Builder builder) {
        this.genderId = builder.genderId;
        this.description = builder.description;
    }

    public String getGenderId() {
        return genderId;
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
        if (!(o instanceof Gender)) return false;
        Gender gender = (Gender) o;
        return Objects.equals(genderId, gender.genderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genderId);
    }

    @Override
    public String toString() {
        return "Gender{" +
                "genderId='" + genderId + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static class Builder {
        private String genderId;
        private String description;

        public Builder setGenderId(String genderId) {
            this.genderId = genderId;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder copy(Gender gender) {
            this.genderId = gender.genderId;
            this.description = gender.description;
            return this;
        }

        public Gender build() {
            return new Gender(this);
        }
    }
}