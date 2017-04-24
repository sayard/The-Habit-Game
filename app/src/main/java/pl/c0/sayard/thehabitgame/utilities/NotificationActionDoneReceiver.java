package pl.c0.sayard.thehabitgame.utilities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pl.c0.sayard.thehabitgame.HabitDetailActivity;
import pl.c0.sayard.thehabitgame.MainActivity;
import pl.c0.sayard.thehabitgame.R;
import pl.c0.sayard.thehabitgame.data.AchievementManager;
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
        Activity instance = HabitDetailActivity.getInstance();

        SharedPreferences isFirstTimeSharedPreferences = instance.getSharedPreferences(instance.getString(R.string.preference_button_first_click), Context.MODE_PRIVATE);
        SharedPreferences dateCheckSharedPreferences = instance.getSharedPreferences(instance.getString(R.string.preference_day_check_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor dateCheckEditor = dateCheckSharedPreferences.edit();

        HabitDbHelper helper = new HabitDbHelper(instance);
        SQLiteDatabase db = helper.getReadableDatabase();

        String columnsToUpdate[] = {HabitContract.HabitEntry.COLUMN_STREAK,
                HabitContract.HabitEntry.COLUMN_DAYS_LEFT};

        Cursor cursor = db.query(HabitContract.HabitEntry.TABLE_NAME,
                columnsToUpdate,
                HabitContract.HabitEntry._ID + " = " + HABIT_ID,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        int currentStreak = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_STREAK));
        int currentDaysLeft = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DAYS_LEFT));
        cursor.close();

        if(isFirstTimeSharedPreferences.getBoolean("isFirstTime"+HABIT_ID, true)){
            SharedPreferences.Editor isFirstTimeEditor = isFirstTimeSharedPreferences.edit();
            isFirstTimeEditor.putBoolean("isFirstTime"+HABIT_ID, false);
            isFirstTimeEditor.commit();
            if(updateStreakAndDaysLeft(instance, HABIT_ID, currentStreak, currentDaysLeft)){
                Toast.makeText(instance, R.string.good_job, Toast.LENGTH_SHORT).show();
                dateCheckEditor.putString("dateCheck"+HABIT_ID, getCurrentDateString());
                dateCheckEditor.commit();
                HabitDetailActivity.getInstance().finish();
            }else{
                Toast.makeText(instance, R.string.updating_failed, Toast.LENGTH_SHORT).show();
            }
        }else{
            if(dateCheckSharedPreferences.getString("dateCheck"+HABIT_ID, null).equals(getCurrentDateString())){
                Toast.makeText(instance, R.string.youre_done_for_today, Toast.LENGTH_LONG).show();
            }else{

                if(currentDaysLeft <= 1){
                    Intent habitDetailIntent = new Intent(context, HabitDetailActivity.class);
                    habitDetailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    habitDetailIntent.putExtra(context.getString(R.string.EXTRA_DETAIL_ID), HABIT_ID);
                    habitDetailIntent.putExtra(context.getString(R.string.EXTRA_DETAIL_SHOULD_SHOW_DELETING_DIALOG), true);
                    context.startActivity(habitDetailIntent);
                    return;
                }

                if(updateStreakAndDaysLeft(instance, HABIT_ID, currentStreak, currentDaysLeft)){
                    Toast.makeText(instance, R.string.good_job, Toast.LENGTH_SHORT).show();
                    dateCheckEditor.putString("dateCheck"+HABIT_ID, getCurrentDateString());
                    dateCheckEditor.commit();
                }else{
                    Toast.makeText(instance, R.string.updating_failed, Toast.LENGTH_SHORT).show();
                }
            }
        }

        if(NOTIFICATION_ID == -1){
            Toast.makeText(context, "Failed to cancel notification", Toast.LENGTH_SHORT).show();
        }else{
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }

    private String getCurrentDateString(){
        Calendar currTimeCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(currTimeCalendar.getTime());
    }

    public boolean updateStreakAndDaysLeft(Context context, int habitId, int currentStreak, int currentDaysLeft){
        if(habitId == -1)
            return false;

        currentStreak++;
        currentDaysLeft--;

        if(currentStreak==7){
            AchievementManager.setAchievementCompleted(context, 1);
        }else if(currentStreak==30){
            AchievementManager.setAchievementCompleted(context, 2);
        }else if(currentStreak==60){
            AchievementManager.setAchievementCompleted(context, 3);
        }

        HabitDbHelper dbHelper = new HabitDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(HabitContract.HabitEntry.COLUMN_STREAK, currentStreak);
        contentValues.put(HabitContract.HabitEntry.COLUMN_DAYS_LEFT, currentDaysLeft);

        return db.update(HabitContract.HabitEntry.TABLE_NAME,
                contentValues,
                HabitContract.HabitEntry._ID + " = " + habitId,
                null) > 0;
    }

}
