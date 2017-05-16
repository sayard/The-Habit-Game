package pl.c0.sayard.thehabitgame;

import android.app.Activity;
import android.app.DialogFragment;
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

import pl.c0.sayard.thehabitgame.data.AchievementManager;
import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;
import pl.c0.sayard.thehabitgame.utilities.DeletingDialogFragment;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

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
        boolean shouldCreateDeletingDialog = intent.getBooleanExtra(getString(R.string.EXTRA_DETAIL_SHOULD_SHOW_DELETING_DIALOG), false);

        Cursor cursor = getHabitDetails(detailId);
        cursor.moveToFirst();
        detailName = cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_NAME));
        String detailDesc = cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DESCRIPTION));
        int detailColor = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_COLOR));
        int detailStreak = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_STREAK));
        int detailDaysLeft = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DAYS_LEFT));

        cursor.close();

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
        if(!detailDesc.equals("")){
            descriptionTextView.setText(detailDesc);
        }else{
            TextView descriptionHeaderTextView = (TextView) findViewById(R.id.habit_detail_desc_header);
            descriptionHeaderTextView.setVisibility(View.INVISIBLE);
            descriptionHeaderTextView.setHeight(0);
            descriptionTextView.setVisibility(View.INVISIBLE);
            descriptionTextView.setHeight(0);
        }

        TextView streakTextView = (TextView) findViewById(R.id.habit_detail_streak);
        TextView streakDescriptionTextView = (TextView) findViewById(R.id.habit_detail_streak_desc);

        streakTextView.setText(String.valueOf(detailStreak));
        if(detailStreak<7){
            streakTextView.setTextColor(Color.parseColor("#E81B19"));
            streakDescriptionTextView.setText(R.string.need_more_work);
        }else{
            streakTextView.setTextColor(Color.parseColor("#5DE84A"));
            streakDescriptionTextView.setText(R.string.great_work);
        }

        TextView daysLeftTextView = (TextView) findViewById(R.id.habit_detail_days_left);
        daysLeftTextView.setText(String.valueOf(detailDaysLeft));

        if(shouldCreateDeletingDialog)
            showDeletingDialog(getString(R.string.habit_developed_dialog_text),
                    getString(R.string.im_done),
                    getString(R.string.give_me_one_more_week),
                    false);

        startTutorial();
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
            showDeletingDialog(getString(R.string.do_you_want_to_delete),
                    getString(R.string.yes),
                    getString(R.string.no),
                    true);
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
        }else if(id == R.id.action_map){
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra(this.getString(R.string.EXTRA_DETAIL_ID), detailId);
            intent.putExtra(this.getString(R.string.EXTRA_DETAIL_NAME), detailName);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeletingDialog(String dialogMessage, String dialogPositiveButton, String dialogNegativeButton, boolean actionDelete){
        DialogFragment deletingDialogFragment = new DeletingDialogFragment();
        Bundle args = new Bundle();
        args.putString(getString(R.string.EXTRA_DELETING_DIALOG_MESSAGE), dialogMessage);
        args.putString(getString(R.string.EXTRA_DELETING_DIALOG_POSITIVE_BUTTON), dialogPositiveButton);
        args.putString(getString(R.string.EXTRA_DELETING_DIALOG_NEGATIVE_BUTTON), dialogNegativeButton);
        args.putBoolean(getString(R.string.EXTRA_DELETING_DIALOG_ACTION_DELETE), actionDelete);
        args.putInt(getString(R.string.EXTRA_DELETING_DIALOG_HABIT_ID), detailId);
        deletingDialogFragment.setArguments(args);
        deletingDialogFragment.show(getFragmentManager(), "deleting_dialog");
    }

    public void performHabit(View view){
        SharedPreferences isFirstTimeSharedPreferences = this.getSharedPreferences(getString(R.string.preference_button_first_click), Context.MODE_PRIVATE);
        SharedPreferences dateCheckSharedPreferences = this.getSharedPreferences(this.getString(R.string.preference_day_check_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor dateCheckEditor = dateCheckSharedPreferences.edit();

        if(detailId == -1)
            return;

        HabitDbHelper helper = new HabitDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String columnsToUpdate[] = {HabitContract.HabitEntry.COLUMN_STREAK,
                HabitContract.HabitEntry.COLUMN_DAYS_LEFT};

        Cursor cursor = db.query(HabitContract.HabitEntry.TABLE_NAME,
                columnsToUpdate,
                HabitContract.HabitEntry._ID + " = " + detailId,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        int currentStreak = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_STREAK));
        int currentDaysLeft = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DAYS_LEFT));
        cursor.close();

        if(isFirstTimeSharedPreferences.getBoolean("isFirstTime"+detailId, true)){
            SharedPreferences.Editor isFirstTimeEditor = isFirstTimeSharedPreferences.edit();
            isFirstTimeEditor.putBoolean("isFirstTime"+detailId, false);
            isFirstTimeEditor.commit();
            if(updateStreakAndDaysLeft(currentStreak, currentDaysLeft)){
                Toast.makeText(this, R.string.good_job, Toast.LENGTH_SHORT).show();
                dateCheckEditor.putString("dateCheck"+detailId, getCurrentDateString());
                dateCheckEditor.commit();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, R.string.updating_failed, Toast.LENGTH_SHORT).show();
            }
        }else{
            if(dateCheckSharedPreferences.getString("dateCheck"+detailId, null).equals(getCurrentDateString())){
                Toast.makeText(this, R.string.youre_done_for_today, Toast.LENGTH_LONG).show();
            }else{

                if(currentDaysLeft <= 1){
                    showDeletingDialog(getString(R.string.habit_developed_dialog_text),
                            getString(R.string.im_done),
                            getString(R.string.give_me_one_more_week),
                            false);
                    return;
                }

                if(updateStreakAndDaysLeft(currentStreak, currentDaysLeft)){
                    Toast.makeText(this, R.string.good_job, Toast.LENGTH_SHORT).show();
                    dateCheckEditor.putString("dateCheck"+detailId, getCurrentDateString());
                    dateCheckEditor.commit();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, R.string.updating_failed, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean updateStreakAndDaysLeft(int currentStreak, int currentDaysLeft) {
        currentStreak++;
        currentDaysLeft--;

        if(currentStreak==7){
            AchievementManager.setAchievementCompleted(this, 1);
        }else if(currentStreak==30){
            AchievementManager.setAchievementCompleted(this, 2);
        }else if(currentStreak==60){
            AchievementManager.setAchievementCompleted(this, 3);
        }

        HabitDbHelper helper = new HabitDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

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

    private void startTutorial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(250);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getString(R.string.showcase_sequence_2_id));
        sequence.setConfig(config);

        sequence.addSequenceItem(new View(this),
                getString(R.string.showcase_sequence_2_1),
                getString(R.string.next));

        sequence.addSequenceItem(findViewById(R.id.habit_detail_name),
                getString(R.string.showcase_sequence_2_2),
                getString(R.string.next));

        sequence.addSequenceItem(new View(this),
                getString(R.string.showcase_sequence_2_3),
                getString(R.string.next));

        sequence.addSequenceItem(findViewById(R.id.habit_detail_streak),
                getString(R.string.showcase_sequence_2_4),
                getString(R.string.next));

        sequence.addSequenceItem(findViewById(R.id.habit_detail_days_left),
                getString(R.string.showcase_sequence_2_5),
                getString(R.string.next));

        sequence.addSequenceItem(new View(this),
                getString(R.string.showcase_sequence_2_6),
                getString(R.string.next));

        sequence.addSequenceItem(findViewById(R.id.doneForTodayButton),
                getString(R.string.showcase_sequence_2_7),
                getString(R.string.got_it));

        sequence.start();
    }
}
