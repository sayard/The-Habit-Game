package pl.c0.sayard.thehabitgame.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Karol on 22.04.2017.
 */

public class AchievementManager {//TODO notification/toast
    public static void setAchievementCompleted(Context context, int achievementNumber){
        HabitDbHelper dbHelper = new HabitDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(HabitContract.AchievementEntry.COLUMN_IS_COMPLETED, 1);

        db.update(HabitContract.AchievementEntry.TABLE_NAME,
                contentValues,
                HabitContract.AchievementEntry._ID + " = " + achievementNumber,
                null);
    }
}
