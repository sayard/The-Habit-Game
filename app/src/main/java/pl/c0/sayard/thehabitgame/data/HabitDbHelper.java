package pl.c0.sayard.thehabitgame.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by karol on 3/13/17.
 */

public class HabitDbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 10;
    public static final String DATABASE_NAME = "HabitDB.db";

    public HabitDbHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_HABITS = "CREATE TABLE " +
                HabitContract.HabitEntry.TABLE_NAME + " (" +
                HabitContract.HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                HabitContract.HabitEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                HabitContract.HabitEntry.COLUMN_DESCRIPTION + " TEXT, " +
                HabitContract.HabitEntry.COLUMN_COLOR + " INTEGER NOT NULL, " +
                HabitContract.HabitEntry.COLUMN_STREAK + " INTEGER NOT NULL DEFAULT 0, " +
                HabitContract.HabitEntry.COLUMN_IS_MONDAY_NOTIFICATION_ACTIVE + " BOOLEAN NOT NULL DEFAULT 0, " +
                HabitContract.HabitEntry.COLUMN_IS_TUESDAY_NOTIFICATION_ACTIVE + " BOOLEAN NOT NULL DEFAULT 0, " +
                HabitContract.HabitEntry.COLUMN_IS_WEDNESDAY_NOTIFICATION_ACTIVE + " BOOLEAN NOT NULL DEFAULT 0, " +
                HabitContract.HabitEntry.COLUMN_IS_THURSDAY_NOTIFICATION_ACTIVE + " BOOLEAN NOT NULL DEFAULT 0, " +
                HabitContract.HabitEntry.COLUMN_IS_FRIDAY_NOTIFICATION_ACTIVE + " BOOLEAN NOT NULL DEFAULT 0, " +
                HabitContract.HabitEntry.COLUMN_IS_SATURDAY_NOTIFICATION_ACTIVE + " BOOLEAN NOT NULL DEFAULT 0, " +
                HabitContract.HabitEntry.COLUMN_IS_SUNDAY_NOTIFICATION_ACTIVE + " BOOLEAN NOT NULL DEFAULT 0, " +
                HabitContract.HabitEntry.COLUMN_MONDAY_NOTIFICATION_HOUR + " TEXT NOT NULL DEFAULT '15:00', " +
                HabitContract.HabitEntry.COLUMN_TUESDAY_NOTIFICATION_HOUR + " TEXT NOT NULL DEFAULT '15:00', " +
                HabitContract.HabitEntry.COLUMN_WEDNESDAY_NOTIFICATION_HOUR + " TEXT NOT NULL DEFAULT '15:00', " +
                HabitContract.HabitEntry.COLUMN_THURSDAY_NOTIFICATION_HOUR + " TEXT NOT NULL DEFAULT '15:00', " +
                HabitContract.HabitEntry.COLUMN_FRIDAY_NOTIFICATION_HOUR + " TEXT NOT NULL DEFAULT '15:00', " +
                HabitContract.HabitEntry.COLUMN_SATURDAY_NOTIFICATION_HOUR + " TEXT NOT NULL DEFAULT '15:00', " +
                HabitContract.HabitEntry.COLUMN_SUNDAY_NOTIFICATION_HOUR + " TEXT NOT NULL DEFAULT '15:00', " +
                HabitContract.HabitEntry.COLUMN_IS_GEO_NOTIFICATION_ACTIVE + " BOOLEAN NOT NULL DEFAULT 0, " +
                HabitContract.HabitEntry.COLUMN_GEO_NOTIFICATION_LATITUDE + " REAL NOT NULL DEFAULT 100.0, " +
                HabitContract.HabitEntry.COLUMN_GEO_NOTIFICATION_LONGITUDE + " REAL NOT NULL DEFAULT 200.0, " +
                HabitContract.HabitEntry.COLUMN_DAYS_LEFT + " INTEGER DEFAULT 66);";

        final String SQL_CREATE_ACHIEVEMENTS = "CREATE TABLE " +
                HabitContract.AchievementEntry.TABLE_NAME + " (" +
                HabitContract.AchievementEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HabitContract.AchievementEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                HabitContract.AchievementEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                HabitContract.AchievementEntry.COLUMN_IS_COMPLETED + " BOOLEAN NOT NULL DEFAULT 0, " +
                HabitContract.AchievementEntry.IMAGE_COMPLETED + " TEXT NOT NULL, " +
                HabitContract.AchievementEntry.IMAGE_NOT_COMPLETED + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_HABITS);
        db.execSQL(SQL_CREATE_ACHIEVEMENTS);
        SampleData sd = new SampleData();
        sd.insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String SQL_DELETE_HABITS =
                "DROP TABLE IF EXISTS " + HabitContract.HabitEntry.TABLE_NAME;

        final String SQL_DELETE_ACHIEVEMENTS =
                "DROP TABLE IF EXISTS " + HabitContract.AchievementEntry.TABLE_NAME;

        db.execSQL(SQL_DELETE_HABITS);
        db.execSQL(SQL_DELETE_ACHIEVEMENTS);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
