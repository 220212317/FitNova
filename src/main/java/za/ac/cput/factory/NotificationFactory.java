package za.ac.cput.factory;
import za.ac.cput.domain.Notification;
import java.time.LocalDateTime;

public class NotificationFactory {

    public static Notification createNotification(int notificationID,
                                                  String message,
                                                  String notificationType,
                                                  int userID) {

        if (message == null || message.isEmpty()) {
            return null;
        }

        return new Notification.Builder()
                .setNotificationID(notificationID)
                .setMessage(message)
                .setNotificationType(notificationType)
                .setDateSent(LocalDateTime.now())
                .setStatus("Sent")
                .setUserID(userID)
                .build();
    }
}

