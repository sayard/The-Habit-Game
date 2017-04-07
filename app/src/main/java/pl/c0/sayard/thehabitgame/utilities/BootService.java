package pl.c0.sayard.thehabitgame.utilities;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.util.Calendar;

import pl.c0.sayard.thehabitgame.R;
import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;

/**
 * Created by Karol on 07.04.2017.
 */

public class BootService extends IntentService {
    public BootService() {
        super("BootService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        HabitDbHelper dbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                HabitContract.HabitEntry._ID,
                HabitContract.HabitEntry.COLUMN_NAME,
                HabitContract.HabitEntry.COLUMN_IS_MONDAY_NOTIFICATION_ACTIVE,
                HabitContract.HabitEntry.COLUMN_IS_TUESDAY_NOTIFICATION_ACTIVE,
                HabitContract.HabitEntry.COLUMN_IS_WEDNESDAY_NOTIFICATION_ACTIVE,
                HabitContract.HabitEntry.COLUMN_IS_THURSDAY_NOTIFICATION_ACTIVE,
                HabitContract.HabitEntry.COLUMN_IS_FRIDAY_NOTIFICATION_ACTIVE,
                HabitContract.HabitEntry.COLUMN_IS_SATURDAY_NOTIFICATION_ACTIVE,
                HabitContract.HabitEntry.COLUMN_IS_SUNDAY_NOTIFICATION_ACTIVE,
                HabitContract.HabitEntry.COLUMN_MONDAY_NOTIFICATION_HOUR,
                HabitContract.HabitEntry.COLUMN_TUESDAY_NOTIFICATION_HOUR,
                HabitContract.HabitEntry.COLUMN_WEDNESDAY_NOTIFICATION_HOUR,
                HabitContract.HabitEntry.COLUMN_THURSDAY_NOTIFICATION_HOUR,
                HabitContract.HabitEntry.COLUMN_FRIDAY_NOTIFICATION_HOUR,
                HabitContract.HabitEntry.COLUMN_SATURDAY_NOTIFICATION_HOUR,
                HabitContract.HabitEntry.COLUMN_SUNDAY_NOTIFICATION_HOUR
        };

        Cursor cursor = db.query(HabitContract.HabitEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();

        int notificationId;
        String hour, habitName;

        while(cursor.moveToNext()){
            habitName = cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_NAME));
            if(cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_IS_MONDAY_NOTIFICATION_ACTIVE)) == 1){
                notificationId = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry._ID)) * 1000;
                hour = cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_MONDAY_NOTIFICATION_HOUR));
                restartNotification(notificationId, habitName, 0, hour);
            }
            if(cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_IS_TUESDAY_NOTIFICATION_ACTIVE)) == 1){
                notificationId = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry._ID)) * 1000 + 1;
                hour = cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_TUESDAY_NOTIFICATION_HOUR));
                restartNotification(notificationId, habitName, 1, hour);
            }
            if(cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_IS_WEDNESDAY_NOTIFICATION_ACTIVE)) == 1){
                notificationId = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry._ID)) * 1000 + 2;
                hour = cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_WEDNESDAY_NOTIFICATION_HOUR));
                restartNotification(notificationId, habitName, 2, hour);
            }
            if(cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_IS_THURSDAY_NOTIFICATION_ACTIVE)) == 1){
                notificationId = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry._ID)) * 1000 + 3;
                hour = cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_THURSDAY_NOTIFICATION_HOUR));
                restartNotification(notificationId, habitName, 3, hour);
            }
            if(cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_IS_FRIDAY_NOTIFICATION_ACTIVE)) == 1){
                notificationId = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry._ID)) * 1000 + 4;
                hour = cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_FRIDAY_NOTIFICATION_HOUR));
                restartNotification(notificationId, habitName, 4, hour);
            }
            if(cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_IS_SATURDAY_NOTIFICATION_ACTIVE)) == 1){
                notificationId = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry._ID)) * 1000 + 5;
                hour = cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_SATURDAY_NOTIFICATION_HOUR));
                restartNotification(notificationId, habitName, 5, hour);
            }
            if(cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_IS_SUNDAY_NOTIFICATION_ACTIVE)) == 1){
                notificationId = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry._ID)) * 1000 + 6;
                hour = cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_SUNDAY_NOTIFICATION_HOUR));
                restartNotification(notificationId, habitName, 6, hour);
            }
        }
        cursor.close();
    }

    private void restartNotification(int notificationId, String habitName, int weekDay, String hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, weekDay+2);
        int hourNumber = Integer.valueOf(hour.substring(0,2));
        calendar.set(Calendar.HOUR_OF_DAY, hourNumber);
        int minuteNumber = Integer.valueOf(hour.substring(3,5));
        calendar.set(Calendar.MINUTE, minuteNumber);

        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra(getString(R.string.EXTRA_NOTIFICATION_ID), notificationId);
        intent.putExtra(getString(R.string.EXTRA_DETAIL_NAME), habitName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY *7, pendingIntent);
    }
}
