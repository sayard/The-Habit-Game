package pl.c0.sayard.thehabitgame.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karol on 06.03.2017.
 */

public class SampleData {

    public static void insertSampleData(SQLiteDatabase db){
        if(db == null)
            return;

        List<ContentValues> habitList = new ArrayList<>();

        ContentValues contentValues = new ContentValues();
        contentValues.put(HabitContract.HabitEntry.COLUM_NAME, "Reading");
        contentValues.put(HabitContract.HabitEntry.COLUM_DESCRIPTION, "Reade every 2 days");
        contentValues.put(HabitContract.HabitEntry.COLUMN_COLOR, 1);
        habitList.add(contentValues);

        contentValues = new ContentValues();
        contentValues.put(HabitContract.HabitEntry.COLUM_NAME, "Hit the gym");
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
            db.endTransaction();
        }

    }
}
