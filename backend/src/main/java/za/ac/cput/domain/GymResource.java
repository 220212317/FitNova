/*
 * GymResource.java
 * Manual, nutrition guide or training tip shown on the Resources page.
 * Managed (create / update / delete) by admins; readable by any signed-in user.
 */
package za.ac.cput.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import za.ac.cput.domain.enums.ResourceCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "gym_resource")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GymResource {

    @Id
    @Column(name = "resource_id")
    private String resourceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceCategory category;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String summary;

    @Column(length = 4000)
    private String body;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "gym_resource_tag", joinColumns = @JoinColumn(name = "resource_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    public GymResource() {
    }

    private GymResource(Builder builder) {
        this.resourceId = builder.resourceId;
        this.category = builder.category;
        this.title = builder.title;
        this.summary = builder.summary;
        this.body = builder.body;
        this.tags = builder.tags == null ? new ArrayList<>() : new ArrayList<>(builder.tags);
    }

    public String getResourceId() {
        return resourceId;
    }

    public ResourceCategory getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getBody() {
        return body;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GymResource)) return false;
        GymResource that = (GymResource) o;
        return Objects.equals(resourceId, that.resourceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceId);
    }

    @Override
    public String toString() {
        return "GymResource{" +
                "resourceId='" + resourceId + '\'' +
                ", category=" + category +
                ", title='" + title + '\'' +
                '}';
    }

    public static class Builder {
        private String resourceId;
        private ResourceCategory category;
        private String title;
        private String summary;
        private String body;
        private List<String> tags = new ArrayList<>();

        public Builder setResourceId(String resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public Builder setCategory(ResourceCategory category) {
            this.category = category;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Builder setTags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder copy(GymResource resource) {
            this.resourceId = resource.resourceId;
            this.category = resource.category;
            this.title = resource.title;
            this.summary = resource.summary;
            this.body = resource.body;
            this.tags = resource.tags == null ? new ArrayList<>() : new ArrayList<>(resource.tags);
            return this;
        }

        public GymResource build() {
            return new GymResource(this);
        }
    }
}
