package pl.c0.sayard.thehabitgame.utilities;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import pl.c0.sayard.thehabitgame.HabitDetailActivity;
import pl.c0.sayard.thehabitgame.MainActivity;
import pl.c0.sayard.thehabitgame.R;
import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;

/**
 * Created by Karol on 11.04.2017.
 */

public class NotificationActionDoneReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        final int HABIT_ID = intent.getIntExtra(context.getString(R.string.EXTRA_DETAIL_ID), -1);
        final int NOTIFICATION_ID = intent.getIntExtra(context.getString(R.string.EXTRA_NOTIFICATION_ID), -1);
        if(updateStreakAndDaysLeft(context, HABIT_ID)){
            Toast.makeText(context, "Good job!", Toast.LENGTH_SHORT).show();
            HabitDetailActivity.getInstance().finish();
        }else{
            Toast.makeText(context, "Updating has failed. Please try again.", Toast.LENGTH_SHORT).show();
        }

        if(NOTIFICATION_ID == -1){
            Toast.makeText(context, "Failed to cancel notification", Toast.LENGTH_SHORT).show();
        }else{
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }

    public boolean updateStreakAndDaysLeft(Context context, int habitId){
        if(habitId == -1)
            return false;

        HabitDbHelper dbHelper = new HabitDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String columnsToUpdate[] = {HabitContract.HabitEntry.COLUMN_STREAK,
                HabitContract.HabitEntry.COLUMN_DAYS_LEFT};

        Cursor cursor = db.query(HabitContract.HabitEntry.TABLE_NAME,
                columnsToUpdate,
                HabitContract.HabitEntry._ID  + " = " + habitId,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        int currentStreak = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_STREAK));
        int currentDaysLeft = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DAYS_LEFT));
        currentStreak++;
        currentDaysLeft--;

        ContentValues contentValues = new ContentValues();
        contentValues.put(HabitContract.HabitEntry.COLUMN_STREAK, currentStreak);
        contentValues.put(HabitContract.HabitEntry.COLUMN_DAYS_LEFT, currentDaysLeft);

        return db.update(HabitContract.HabitEntry.TABLE_NAME,
                contentValues,
                HabitContract.HabitEntry._ID + " = " + habitId,
                null) > 0;
    }
}
