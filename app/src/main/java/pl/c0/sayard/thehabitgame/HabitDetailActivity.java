package pl.c0.sayard.thehabitgame;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;

public class HabitDetailActivity extends AppCompatActivity {

    private int detailId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        Intent intent = getIntent();
        detailId = intent.getIntExtra(getString(R.string.EXTRA_DETAIL_ID), -1);
        String detailName = intent.getStringExtra(getString(R.string.EXTRA_DETAIL_NAME));
        int detailColor = intent.getIntExtra(getString(R.string.EXTRA_DETAIL_COLOR), 0);
        String detailDesc = intent.getStringExtra(getString(R.string.EXTRA_DETAIL_DESCRIPTION));
        int detailStreak = intent.getIntExtra(getString(R.string.EXTRA_DETAIL_STREAK), 0);
        String detailDaysLeft = intent.getStringExtra(getString(R.string.EXTRA_DETAIL_DAYS_LEFT));

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
        daysLeftTextView.setText(detailDaysLeft);

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
            if(deleteHabitFromDatabase()){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Deleting has failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean deleteHabitFromDatabase() {
        if(detailId == -1)
            return false;

        HabitDbHelper helper = new HabitDbHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        return db.delete(HabitContract.HabitEntry.TABLE_NAME,
                HabitContract.HabitEntry._ID + " = " + detailId,
                null) > 0;
    }

    public void habitPerformed(View view){
        if(updateStreakAndDaysLeft()){
            Toast.makeText(this, "Good job!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Updating has failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean updateStreakAndDaysLeft() {
        if(detailId == -1)
            return false;

        HabitDbHelper helper = new HabitDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String columns[] = {HabitContract.HabitEntry.COLUMN_STREAK,
                    HabitContract.HabitEntry.COLUMN_DAYS_LEFT};

        Cursor cursor = db.query(HabitContract.HabitEntry.TABLE_NAME,
                columns,
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
}
