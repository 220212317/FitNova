package za.ac.cput.entity;

import za.ac.cput.entity.enums.SlotStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
/*
 * Author: Phumelela Sakie (240040546)
 * */
public class AvailabilitySlot {

    private String slotId;
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SlotStatus status;

    private AvailabilitySlot() {}

<<<<<<< master
    private AvailabilitySlot(Builder builder) {
=======
    public AvailabilitySlot(Builder builder) {
>>>>>>> master
        this.slotId = builder.slotId;
        this.date = builder.date;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.status = builder.status;
    }

    public String getSlotId() {
        return slotId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public SlotStatus getStatus() {
        return status;
    }


    public boolean isAvailable() {
        return this.status == SlotStatus.AVAILABLE;
    }

    public static class Builder {
        private String slotId;
        private LocalDate date;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private SlotStatus status;

        public Builder setSlotId(String slotId) {
            this.slotId = slotId;
            return this;
        }

        public Builder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder setStatus(SlotStatus status) {
            this.status = status;
            return this;
        }

        public Builder copy(AvailabilitySlot slot) {
            this.slotId = slot.slotId;
            this.date = slot.date;
            this.startTime = slot.startTime;
            this.endTime = slot.endTime;
            this.status = slot.status;
            return this;
        }

        public AvailabilitySlot build() {
            return new AvailabilitySlot(this);
        }
    }

    @Override
    public String toString() {
        return "AvailabilitySlot{" +
                "slotId='" + slotId + '\'' +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                '}';
    }
<<<<<<< master
}
=======
}
>>>>>>> master
