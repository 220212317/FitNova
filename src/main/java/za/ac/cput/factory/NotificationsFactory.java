package za.ac.cput.domain.factory;
import za.ac.cput.domain.Notifications;
import java.time.LocalDateTime;

public class NotificationsFactory {

    public static Notifications createNotifications(int notificationsID,
                                                  String message,
                                                  String notificationsType,
                                                  int userID) {

        if (message == null || message.isEmpty()) {
            return null;
        }

        return new Notifications.Builder()
                .setNotificationsID(notificationsID)
                .setMessage(message)
                .setNotificationsType(notificationsType)
                .setDateSent(LocalDateTime.now())
                .setStatus("Sent")
                .setUserID(userID)
                .build();
    }
}
