package za.ac.cput.factory;

import za.ac.cput.domain.Admin;

public class AdminFactory {
    public static Admin createAdmin(String adminId, String fullName, String email, String adminLevel){
        if(adminId.isEmpty() || adminId == null)
            return null;

        if(fullName.isEmpty() || fullName == null)
            return null;

        if(email.isEmpty() || email == null)
            return null;

        if(adminLevel.isEmpty() || adminLevel == null)
            return null;

        return new Admin.Builder()
                .setAdminId(adminId)
                .setFullName(fullName)
                .setEmail(email)
                .setAdminLevel(adminLevel)
                .build();
    }

}
