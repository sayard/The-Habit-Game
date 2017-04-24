package pl.c0.sayard.thehabitgame.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import pl.c0.sayard.thehabitgame.R;
import pl.c0.sayard.thehabitgame.TheHabitGame;

/**
 * Created by Karol on 06.03.2017.
 */

public class SampleData {

    public void insertSampleData(SQLiteDatabase db){
        if(db == null)
            return;

        List<ContentValues> habitList = new ArrayList<>();

        ContentValues contentValues = new ContentValues();
        contentValues.put(HabitContract.HabitEntry.COLUMN_NAME, "Reading");
        contentValues.put(HabitContract.HabitEntry.COLUMN_DESCRIPTION, "Read every 2 days");
        contentValues.put(HabitContract.HabitEntry.COLUMN_COLOR, 1);
        habitList.add(contentValues);

        contentValues = new ContentValues();
        contentValues.put(HabitContract.HabitEntry.COLUMN_NAME, "Hit the gym");
        contentValues.put(HabitContract.HabitEntry.COLUMN_DESCRIPTION, "Go to the gym every day");
        contentValues.put(HabitContract.HabitEntry.COLUMN_COLOR,2);
        habitList.add(contentValues);

        try{
            db.beginTransaction();
            db.delete(HabitContract.HabitEntry.TABLE_NAME, null, null);

            for(ContentValues contentValue : habitList){
                db.insert(HabitContract.HabitEntry.TABLE_NAME, null, contentValue);
            }
            db.setTransactionSuccessful();
        }catch (SQLiteException e){
            e.printStackTrace();
        }finally {
            commitToSharedPreferences(TheHabitGame.getContext());
            db.endTransaction();
        }

        List<ContentValues> achievementList = new ArrayList<>();

        contentValues = new ContentValues();
        contentValues.put(HabitContract.AchievementEntry.COLUMN_NAME, "My First Week");
        contentValues.put(HabitContract.AchievementEntry.COLUMN_DESCRIPTION, "Strike a one week streak");
        contentValues.put(HabitContract.AchievementEntry.IMAGE_COMPLETED, "achievement_week_streak_completed");
        contentValues.put(HabitContract.AchievementEntry.IMAGE_NOT_COMPLETED, "achievement_week_streak_not_completed");
        achievementList.add(contentValues);

        contentValues = new ContentValues();
        contentValues.put(HabitContract.AchievementEntry.COLUMN_NAME, "Thirty Days Has September...");
        contentValues.put(HabitContract.AchievementEntry.COLUMN_DESCRIPTION, "Strike a one month streak");
        contentValues.put(HabitContract.AchievementEntry.IMAGE_COMPLETED, "achievement_month_streak_completed");
        contentValues.put(HabitContract.AchievementEntry.IMAGE_NOT_COMPLETED, "achievement_month_streak_not_completed");
        achievementList.add(contentValues);

        contentValues = new ContentValues();
        contentValues.put(HabitContract.AchievementEntry.COLUMN_NAME, "...April, June and November");
        contentValues.put(HabitContract.AchievementEntry.COLUMN_DESCRIPTION, "Strike a two month streak");
        contentValues.put(HabitContract.AchievementEntry.IMAGE_COMPLETED, "achievement_two_month_streak_completed");
        contentValues.put(HabitContract.AchievementEntry.IMAGE_NOT_COMPLETED, "achievement_two_month_streak_not_completed");
        achievementList.add(contentValues);

        contentValues = new ContentValues();
        contentValues.put(HabitContract.AchievementEntry.COLUMN_NAME, "Finally got it!");
        contentValues.put(HabitContract.AchievementEntry.COLUMN_DESCRIPTION, "Develop a habit");
        contentValues.put(HabitContract.AchievementEntry.IMAGE_COMPLETED, "achievement_habit_developed_completed");
        contentValues.put(HabitContract.AchievementEntry.IMAGE_NOT_COMPLETED, "achievement_habit_developed_not_completed");
        achievementList.add(contentValues);

        try{
            db.beginTransaction();
            db.delete(HabitContract.AchievementEntry.TABLE_NAME, null, null);

            for(ContentValues contentValue : achievementList){
                db.insert(HabitContract.AchievementEntry.TABLE_NAME, null, contentValue);
            }
            db.setTransactionSuccessful();
        }catch (SQLiteException e){
            e.printStackTrace();
        }finally {
            commitToSharedPreferences(TheHabitGame.getContext());
            db.endTransaction();
        }
    }

    private void commitToSharedPreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_button_first_click), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstTime1", true);
        editor.putBoolean("isFirstTime2", true);
        editor.commit();
    }
}
