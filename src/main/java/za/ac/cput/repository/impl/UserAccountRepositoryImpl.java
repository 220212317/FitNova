package za.ac.cput.repository.impl;
/*
        * Author: Avuyile Sitoyi
        * (240971051)
        26 March 2026
        */
import za.ac.cput.entity.UserAccount;
import za.ac.cput.entity.UserProfile;
import za.ac.cput.repository.UserAccountRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAccountRepositoryImpl implements UserAccountRepository

{
  private static UserAccountRepositoryImpl repository;
  private Map<String, UserAccount> userAccountMap;
     private UserAccountRepositoryImpl(){
         userAccountMap = new HashMap<>();

     }
     public static UserAccountRepositoryImpl getRepository(){
       if(repository == null)
           repository = new UserAccountRepositoryImpl();
       return repository;
}
    @Override
    public UserAccount create(UserAccount userAccount) {
         userAccountMap.put(userAccount.getAccountId(),userAccount);
        return userAccount;
    }

    @Override
    public UserAccount read(String id) {
         return userAccountMap.get(id);
    }


    @Override
    public UserAccount update(UserAccount userAccount) {
        if(userAccountMap.containsKey(userAccount.getAccountId())){
            userAccountMap.put(userAccount.getAccountId(),userAccount);
        }
        return userAccount;
    }

    @Override
    public boolean delete(String id) {
         return userAccountMap.remove(id) != null;
    }

    @Override
    public List<UserAccount> getAll() {
         return new ArrayList<>(userAccountMap.values());
    }
}
