package pl.c0.sayard.thehabitgame.data;

import android.provider.BaseColumns;

/**
 * Created by karol on 3/13/17.
 */

public final class HabitContract {
    private HabitContract(){}

    public static class HabitEntry implements BaseColumns{
        public static final String TABLE_NAME = "habits";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_STREAK = "streak";
        public static final String COLUMN_DAYS_LEFT = "days_left";
        public static final String COLUMN_IS_MONDAY_NOTIFICATION_ACTIVE = "is_monday_notification_active";
        public static final String COLUMN_IS_TUESDAY_NOTIFICATION_ACTIVE = "is_tuesday_notification_active";
        public static final String COLUMN_IS_WEDNESDAY_NOTIFICATION_ACTIVE = "is_wednesday_notification_active";
        public static final String COLUMN_IS_THURSDAY_NOTIFICATION_ACTIVE = "is_thursday_notification_active";
        public static final String COLUMN_IS_FRIDAY_NOTIFICATION_ACTIVE = "is_friday_notification_active";
        public static final String COLUMN_IS_SATURDAY_NOTIFICATION_ACTIVE = "is_saturday_notification_active";
        public static final String COLUMN_IS_SUNDAY_NOTIFICATION_ACTIVE = "is_sunday_notification_active";
        public static final String COLUMN_MONDAY_NOTIFICATION_HOUR = "monday_notification_hour";
        public static final String COLUMN_TUESDAY_NOTIFICATION_HOUR = "tuesday_notification_hour";
        public static final String COLUMN_WEDNESDAY_NOTIFICATION_HOUR = "wednesday_notification_hour";
        public static final String COLUMN_THURSDAY_NOTIFICATION_HOUR = "thursday_notification_hour";
        public static final String COLUMN_FRIDAY_NOTIFICATION_HOUR = "friday_notification_hour";
        public static final String COLUMN_SATURDAY_NOTIFICATION_HOUR = "saturday_notification_hour";
        public static final String COLUMN_SUNDAY_NOTIFICATION_HOUR = "sunday_notification_hour";
    }

    public static class AchievementEntry implements BaseColumns{
        public static final String TABLE_NAME = "achievements";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IS_COMPLETED = "is_completed";
        public static final String IMAGE_COMPLETED = "image_completed";
        public static final String IMAGE_NOT_COMPLETED = "image_not_completed";
    }
}
