/* UserRole.java
     UserRole POJO class (Entity)
     Author: Collins Shibambo (230093183)
     Date: 18 August 2026 */
package za.ac.cput.domain;

import za.ac.cput.domain.enums.RoleType;

import jakarta.persistence.*;

@Entity
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userRoleId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_id")
    private RoleType roleId;

    private String description;

    public UserRole() {

    }

    private UserRole(Builder builder) {
        this.userRoleId = builder.userRoleId;
        this.user = builder.user;
        this.roleId = builder.roleId;
        this.description = builder.description;
    }

    public String getUserRoleId() {
        return userRoleId;
    }

    public User getUser() {
        return user;
    }

    public RoleType getRoleId() {
        return roleId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "userRoleId='" + userRoleId + '\'' +
                ", roleId=" + roleId +
                ", description='" + description + '\'' +
                '}';
    }

    public static class Builder {
        private String userRoleId;
        private User user;
        private RoleType roleId;
        private String description;

        public Builder setUserRoleId(String userRoleId) {
            this.userRoleId = userRoleId;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setRoleId(RoleType roleId) {
            this.roleId = roleId;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder copy(UserRole userRole) {
            this.userRoleId = userRole.userRoleId;
            this.user = userRole.user;
            this.roleId = userRole.roleId;
            this.description = userRole.description;
            return this;
        }

        public UserRole build() {
            return new UserRole(this);
        }
    }
}