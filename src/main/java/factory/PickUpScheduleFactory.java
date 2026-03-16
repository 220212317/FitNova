package factory;

import za.ac.cput.domain.PickUpSchedule;

public class PickUpScheduleFactory {

        public static PickUpSchedule createPickupSchedule(
                String scheduleId,
                String collectionDay,
                String collectionTime,
                String area,
                boolean reminderEnabled) {

            return new PickUpSchedule.Builder()
                    .setScheduleId(scheduleId)
                    .setCollectionDay(collectionDay)
                    .setCollectionTime(collectionTime)
                    .setArea(area)
                    .setReminderEnabled(reminderEnabled)
                    .build();
        }
    }

