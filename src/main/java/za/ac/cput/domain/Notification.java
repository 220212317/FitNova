package za.ac.cput.domain;
import java.time.LocalDateTime;

public class Notification {

    private int notificationID;
    private String message;
    private String notificationType;
    private LocalDateTime dateSent;
    private String status;
    private int userID;

    private Notification(Builder builder) {
        this.notificationID = builder.notificationID;
        this.message = builder.message;
        this.notificationType = builder.notificationType;
        this.dateSent = builder.dateSent;
        this.status = builder.status;
        this.userID = builder.userID;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public String getMessage() {
        return message;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public LocalDateTime getDateSent() {
        return dateSent;
    }

    public String getStatus() {
        return status;
    }

    public static class Builder {

        private int notificationID;
        private String message;
        private String notificationType;
        private LocalDateTime dateSent;
        private String status;
        private int userID;

        public Builder setNotificationID(int notificationID) {
            this.notificationID = notificationID;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setNotificationType(String notificationType) {
            this.notificationType = notificationType;
            return this;
        }

        public Builder setDateSent(LocalDateTime dateSent) {
            this.dateSent = dateSent;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setUserID(int userID) {
            this.userID = userID;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }
}

