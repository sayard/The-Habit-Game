package pl.c0.sayard.thehabitgame;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;

public class HabitDetailActivity extends AppCompatActivity {

    private int detailId;
    private String detailName;
    static HabitDetailActivity detailActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        detailActivity = this;

        Intent intent = getIntent();
        detailId = intent.getIntExtra(getString(R.string.EXTRA_DETAIL_ID), -1);

        Cursor cursor = getHabitDetails(detailId);
        cursor.moveToFirst();
        detailName = cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_NAME));
        String detailDesc = cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DESCRIPTION));
        int detailColor = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_COLOR));
        int detailStreak = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_STREAK));
        int detailDaysLeft = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DAYS_LEFT));

        TextView nameTextView = (TextView) findViewById(R.id.habit_detail_name);
        nameTextView.setText(detailName);
        switch (detailColor)
        {
            case 1:
                nameTextView.setTextColor(Color.RED);
                break;

            case 2:
                nameTextView.setTextColor(Color.GREEN);
                break;

            case 3:
                nameTextView.setTextColor(Color.BLUE);
                break;

            default:
                break;
        }

        TextView descriptionTextView = (TextView) findViewById(R.id.habit_detail_desc);
        descriptionTextView.setText(detailDesc);

        TextView streakTextView = (TextView) findViewById(R.id.habit_detail_streak);
        TextView streakDescriptionTextView = (TextView) findViewById(R.id.habit_detail_streak_desc);

        streakTextView.setText(String.valueOf(detailStreak));
        if(detailStreak<7){
            streakTextView.setTextColor(Color.parseColor("#E81B19"));
            streakDescriptionTextView.setText("You need a little bit more work! Keep going!");
        }else{
            streakTextView.setTextColor(Color.parseColor("#5DE84A"));
            streakDescriptionTextView.setText("Great work! Keep it up!");
        }

        TextView daysLeftTextView = (TextView) findViewById(R.id.habit_detail_days_left);
        daysLeftTextView.setText(String.valueOf(detailDaysLeft));
    }

    private Cursor getHabitDetails(int habitId){
        HabitDbHelper dbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                HabitContract.HabitEntry.COLUMN_NAME,
                HabitContract.HabitEntry.COLUMN_COLOR,
                HabitContract.HabitEntry.COLUMN_DESCRIPTION,
                HabitContract.HabitEntry.COLUMN_STREAK,
                HabitContract.HabitEntry.COLUMN_DAYS_LEFT
        };

        return db.query(HabitContract.HabitEntry.TABLE_NAME,
                columns,
                HabitContract.HabitEntry._ID + " = " + habitId,
                null,
                null,
                null,
                null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_habit_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_delete_habit){

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which)
                    {
                        case DialogInterface.BUTTON_POSITIVE:
                            if(deleteHabitFromDatabase()){
                                Intent intent = new Intent(HabitDetailActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(HabitDetailActivity.this, "Deleting has failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;
                        default:
                            return;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete this habit?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener)
                    .show();

            return true;
        }else if(id == R.id.action_update_habit){
            Intent intent = new Intent(this, UpdateHabitActivity.class);
            intent.putExtra(this.getString(R.string.EXTRA_UPDATE_ID), detailId);
            startActivity(intent);
        }else if(id == R.id.action_notification_settings){
            Intent intent = new Intent(this, HabitNotificationsActivity.class);
            intent.putExtra(this.getString(R.string.EXTRA_DETAIL_ID), detailId);
            intent.putExtra(this.getString(R.string.EXTRA_DETAIL_NAME), detailName);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean deleteHabitFromDatabase() {
        if(detailId == -1)
            return false;

        HabitDbHelper helper = new HabitDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        return db.delete(HabitContract.HabitEntry.TABLE_NAME,
                HabitContract.HabitEntry._ID + " = " + detailId,
                null) > 0;
    }

    public void habitPerformed(View view){
        SharedPreferences isFirstTimeSharedPreferences = this.getSharedPreferences(getString(R.string.preference_button_first_click), Context.MODE_PRIVATE);
        SharedPreferences dateCheckSharedPreferences = this.getSharedPreferences(this.getString(R.string.preference_day_check_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor dateCheckEditor = dateCheckSharedPreferences.edit();
        if(isFirstTimeSharedPreferences.getBoolean("isFirstTime"+detailId, true)){
            SharedPreferences.Editor isFirstTimeEditor = isFirstTimeSharedPreferences.edit();
            isFirstTimeEditor.putBoolean("isFirstTime"+detailId, false);
            isFirstTimeEditor.commit();
            if(updateStreakAndDaysLeft()){
                Toast.makeText(this, "Good job!", Toast.LENGTH_SHORT).show();
                dateCheckEditor.putString("dateCheck"+detailId, getCurrentDateString());
                dateCheckEditor.commit();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Updating has failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(dateCheckSharedPreferences.getString("dateCheck"+detailId, null).equals(getCurrentDateString())){
                Toast.makeText(this, "You're done for today, come back tommorow.", Toast.LENGTH_LONG).show();
            }else{
                if(updateStreakAndDaysLeft()){
                    Toast.makeText(this, "Good job!", Toast.LENGTH_SHORT).show();
                    dateCheckEditor.putString("dateCheck"+detailId, getCurrentDateString());
                    dateCheckEditor.commit();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "Updating has failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean updateStreakAndDaysLeft() {
        if(detailId == -1)
            return false;

        HabitDbHelper helper = new HabitDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String columnsToUpdate[] = {HabitContract.HabitEntry.COLUMN_STREAK,
                    HabitContract.HabitEntry.COLUMN_DAYS_LEFT};

        Cursor cursor = db.query(HabitContract.HabitEntry.TABLE_NAME,
                columnsToUpdate,
                HabitContract.HabitEntry._ID + " = " +detailId,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        int currentStreak = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_STREAK));
        int currentDaysLeft = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DAYS_LEFT));
        currentStreak++;
        currentDaysLeft--;

        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_STREAK, currentStreak);
        values.put(HabitContract.HabitEntry.COLUMN_DAYS_LEFT, currentDaysLeft);

        return db.update(HabitContract.HabitEntry.TABLE_NAME,
                values,
                HabitContract.HabitEntry._ID + " = " + detailId,
                null) > 0;
    }

    private String getCurrentDateString(){
        Calendar currTimeCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(currTimeCalendar.getTime());
        return formattedDate;
    }

    public static Activity getInstance(){
        return detailActivity;
    }
}
