package za.ac.cput.domain;

/* Admin.java
   Admin entity
   Author: Athi (220212317)
   Date: 13 March 2026
*/


public class Admin {
    private String adminId;
    private String fullName;
    private String email;
    private String adminLevel;


    private Admin() {}

    private Admin(Builder builder) {
        this.adminId = builder.adminId;
        this.fullName = builder.fullName;
        this.email = builder.email;
        this.adminLevel = builder.adminLevel;
    }

    public String getAdminId() {
        return adminId;
    }
    public String getFullName() {
        return fullName;
    }
    public String getEmail() {
        return email;
    }
    public String getAdminLevel() {
        return adminLevel;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId='" + adminId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", adminLevel='" + adminLevel + '\'' +
                '}';
    }

    public static class Builder {
        private String adminId;
        private String fullName;
        private String email;
        private String adminLevel;

        public Builder setAdminId(String adminId) {
            this.adminId = adminId;
            return this;
        }

        public Builder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setAdminLevel(String adminLevel) {
            this.adminLevel = adminLevel;
            return this;
        }

        public Admin build() {
            return new Admin(this);
        }
    }
}
