package za.ac.cput.util;

import java.util.UUID;

public class Helper {
    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }

    public static boolean isEmptyorNull(String str){
        if(str == null || str.isEmpty()){
            return true;
        }
        return false;
    }

    /*public static boolean isValidEmail(String email){
        EmailValidator emailValidator = EmailValidator.getInstance();
        if(emailValidator.isValid(email)){
            return true;
        }
        return false;
    }*/
}
