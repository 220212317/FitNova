package za.ac.cput.factory;
/**
 *
 * 25 March 2026
 * Author: Avuyile Sitoyi
 * (240971051)
 */
import za.ac.cput.entity.UserAccount;
import za.ac.cput.entity.enums.RoleType;
import za.ac.cput.util.helper;

import java.time.LocalDate;

public class UserAccountFactory
{
    public static UserAccount CreateUserAccount(String accountId,
     String email, String password,
    RoleType role, LocalDate registrationDate
    ) {
    if(helper.isNullOrEmpty(accountId))
        throw new IllegalArgumentException("AccountId aquired");
     if(helper.isNullOrEmpty(email))
         throw new IllegalArgumentException("Invalid email");
     if(helper.isNullOrEmpty(password))
         throw new IllegalArgumentException("Password is required");

return new UserAccount.Builder().accountId(accountId).email(email).
        password(password).role(role).registrationDate(registrationDate).build();
}

}
