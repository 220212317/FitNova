package za.ac.cput.repository.impl;


import za.ac.cput.entity.UserAccount;
import za.ac.cput.entity.UserProfile;
import za.ac.cput.repository.UserProfileRepository;
/*
        * Author: Avuyile Sitoyi
        * (240971051)
        26 March 2026
        */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileRepositoryImpl implements UserProfileRepository {

    private static UserProfileRepositoryImpl repo;
    private Map<String, UserProfile> userProfileMap;
    private UserProfileRepositoryImpl(){
        userProfileMap= new HashMap<>();

    }
    public static UserProfileRepositoryImpl getRepository() {
        if (repo == null)
            repo = new UserProfileRepositoryImpl();
        return repo;
    }

    @Override
    public UserProfile create(UserProfile userProfile) {
        userProfileMap.put(userProfile.getProfileId(),userProfile);
         return userProfile;
    }

    @Override
    public UserProfile read(String id) {
        return userProfileMap.get(id);
    }

    @Override
    public List<UserProfile> findAll() {
        return new ArrayList<>(userProfileMap.values());
    }

    @Override
    public UserProfile update(UserProfile userProfile) {
        if (userProfileMap.containsKey(userProfile.getProfileId())) {
            userProfileMap.put(userProfile.getProfileId(), userProfile);
        }
        return userProfile;
    }

    @Override
    public boolean delete(String id) {
        return userProfileMap.remove(id) != null;
    }


}

