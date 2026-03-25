package za.ac.cput.factory;
import  za.ac.cput.entity.Trainer;
import za.ac.cput.entity.ContactDetails;
import za.ac.cput.entity.UserAccount;
import za.ac.cput.entity.UserProfile;
import java.util.UUID;

/**
 *
 * 25 March 2026
 * Author: Lisakhanya Tshokolo
 * (220239215)
 */

public class TrainerFactory {
    public static Trainer createTrainer(UserAccount account, UserProfile profile, ContactDetails contact){

       if(account == null)
           throw new IllegalArgumentException("UserAccount cannot be null");
       if(profile == null)
           throw new IllegalArgumentException("UserProfile is required");
       if(contact == null)
           throw new IllegalArgumentException("ContactDetails is required");

       String trainerId = generateTrainerId();

       return new Trainer.Builder().setTrainerId(trainerId)
               .setAccount(account)
               .setProfile(profile)
               .setContactDetails(contact)
               .build();

    }
    private static String generateTrainerId(){
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
