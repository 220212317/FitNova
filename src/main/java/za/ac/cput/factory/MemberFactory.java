package za.ac.cput.factory;
import za.ac.cput.entity.ContactDetails;
import za.ac.cput.entity.Member;
import za.ac.cput.entity.UserAccount;
import za.ac.cput.entity.UserProfile;

/**
 *
 * 25 March 2026
 * Author: Lisakhanya Tshokolo
 * (220239215)
 */

import java.util.UUID;

public class MemberFactory {
    public static Member createMember(UserAccount account, UserProfile profile, ContactDetails contact){

        if(account == null)
            throw new NullPointerException("UserAccount is required");
        if (profile == null)
            throw new NullPointerException("UserProfile is required");
        if (contact == null)
            throw new NullPointerException("ContactDetails cannot be empty");

        String memberId = generateMemberId();
        return new Member.Builder().memberId(memberId)
                .account(account)
                .profile(profile)
                .contact(contact)
                .build();
    }
    public static String generateMemberId(){
        return UUID.randomUUID().toString();
    }
}
