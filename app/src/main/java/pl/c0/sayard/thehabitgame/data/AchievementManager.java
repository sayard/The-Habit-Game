package pl.c0.sayard.thehabitgame.data;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import pl.c0.sayard.thehabitgame.R;

/**
 * Created by Karol on 22.04.2017.
 */

public class AchievementManager {
    public static void setAchievementCompleted(Context context, int achievementNumber){
        HabitDbHelper dbHelper = new HabitDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(HabitContract.AchievementEntry.COLUMN_IS_COMPLETED, 1);

        db.update(HabitContract.AchievementEntry.TABLE_NAME,
                contentValues,
                HabitContract.AchievementEntry._ID + " = " + achievementNumber,
                null);

        displayNotification(context, achievementNumber);
    }

    public static void displayNotification(Context context, int achievementNumber){

        HabitDbHelper dbHelper = new HabitDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {
                HabitContract.AchievementEntry.COLUMN_DESCRIPTION,
                HabitContract.AchievementEntry.IMAGE_COMPLETED
        };

        Cursor cursor = db.query(HabitContract.AchievementEntry.TABLE_NAME,
                columns,
                HabitContract.AchievementEntry._ID + " = " + achievementNumber,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        final String DESCRIPTION = cursor.getString(cursor.getColumnIndex(HabitContract.AchievementEntry.COLUMN_DESCRIPTION));
        final String IMAGE_COMPLETED = cursor.getString(cursor.getColumnIndex(HabitContract.AchievementEntry.IMAGE_COMPLETED));
        cursor.close();

        int imageId = context.getResources().getIdentifier(IMAGE_COMPLETED, "drawable", context.getPackageName());

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(imageId)
                .setContentTitle(context.getString(R.string.got_achievement))
                .setContentText(DESCRIPTION)
                .setVibrate(new long[] { 250, 250, 250, 250, 250 })
                .setSound(alarmSound)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(achievementNumber, builder.build());
    }
}
