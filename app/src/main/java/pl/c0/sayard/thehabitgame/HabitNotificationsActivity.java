package pl.c0.sayard.thehabitgame;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;
import pl.c0.sayard.thehabitgame.utilities.NotificationReceiver;
import pl.c0.sayard.thehabitgame.utilities.TimePickerFragment;

public class HabitNotificationsActivity extends AppCompatActivity {

    private CheckBox[] weekDays = new CheckBox[7];
    private TextView[] hoursAndMinutes = new TextView[7];
    private int habitId;
    private String habitName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_notifications);

        assignWeekDaysAndHoursAndMinutes();

        Intent intent = getIntent();
        habitId = intent.getIntExtra(getString(R.string.EXTRA_DETAIL_ID), -1);
        habitName = intent.getStringExtra(getString(R.string.EXTRA_DETAIL_NAME));

        setCheckBoxesStatus();
        setHoursAndMinutes();
    }

    public void assignWeekDaysAndHoursAndMinutes(){
        weekDays[0] = (CheckBox) findViewById(R.id.notification_monday_check_box);
        weekDays[1] = (CheckBox) findViewById(R.id.notification_tuesday_check_box);
        weekDays[2] = (CheckBox) findViewById(R.id.notification_wednesday_check_box);
        weekDays[3] = (CheckBox) findViewById(R.id.notification_thursday_check_box);
        weekDays[4] = (CheckBox) findViewById(R.id.notification_friday_check_box);
        weekDays[5] = (CheckBox) findViewById(R.id.notification_saturday_check_box);
        weekDays[6] = (CheckBox) findViewById(R.id.notification_sunday_check_box);

        hoursAndMinutes[0] = (TextView) findViewById(R.id.notification_monday_tv);
        hoursAndMinutes[1] = (TextView) findViewById(R.id.notification_tuesday_tv);
        hoursAndMinutes[2] = (TextView) findViewById(R.id.notification_wednesday_tv);
        hoursAndMinutes[3] = (TextView) findViewById(R.id.notification_thursday_tv);
        hoursAndMinutes[4] = (TextView) findViewById(R.id.notification_friday_tv);
        hoursAndMinutes[5] = (TextView) findViewById(R.id.notification_saturday_tv);
        hoursAndMinutes[6] = (TextView) findViewById(R.id.notification_sunday_tv);
    }

    public void setCheckBoxesStatus(){
        HabitDbHelper dbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String notificationsStatusColumns[] = {
                HabitContract.HabitEntry.COLUMN_IS_MONDAY_NOTIFICATION_ACTIVE,
                HabitContract.HabitEntry.COLUMN_IS_TUESDAY_NOTIFICATION_ACTIVE,
                HabitContract.HabitEntry.COLUMN_IS_WEDNESDAY_NOTIFICATION_ACTIVE,
                HabitContract.HabitEntry.COLUMN_IS_THURSDAY_NOTIFICATION_ACTIVE,
                HabitContract.HabitEntry.COLUMN_IS_FRIDAY_NOTIFICATION_ACTIVE,
                HabitContract.HabitEntry.COLUMN_IS_SATURDAY_NOTIFICATION_ACTIVE,
                HabitContract.HabitEntry.COLUMN_IS_SUNDAY_NOTIFICATION_ACTIVE
        };

        Cursor notificationsStatusCursor = db.query(
                HabitContract.HabitEntry.TABLE_NAME,
                notificationsStatusColumns,
                HabitContract.HabitEntry._ID + "=" + habitId,
                null,
                null,
                null,
                null
        );

        notificationsStatusCursor.moveToFirst();

        if(notificationsStatusCursor.getInt(notificationsStatusCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_IS_MONDAY_NOTIFICATION_ACTIVE)) == 1){
            weekDays[0].setChecked(true);
        }else{
            weekDays[0].setChecked(false);
        }

        if(notificationsStatusCursor.getInt(notificationsStatusCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_IS_TUESDAY_NOTIFICATION_ACTIVE)) == 1){
            weekDays[1].setChecked(true);
        }else{
            weekDays[1].setChecked(false);
        }

        if(notificationsStatusCursor.getInt(notificationsStatusCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_IS_WEDNESDAY_NOTIFICATION_ACTIVE)) == 1){
            weekDays[2].setChecked(true);
        }else{
            weekDays[2].setChecked(false);
        }

        if(notificationsStatusCursor.getInt(notificationsStatusCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_IS_THURSDAY_NOTIFICATION_ACTIVE)) == 1){
            weekDays[3].setChecked(true);
        }else{
            weekDays[3].setChecked(false);
        }

        if(notificationsStatusCursor.getInt(notificationsStatusCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_IS_FRIDAY_NOTIFICATION_ACTIVE)) == 1){
            weekDays[4].setChecked(true);
        }else{
            weekDays[4].setChecked(false);
        }

        if(notificationsStatusCursor.getInt(notificationsStatusCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_IS_SATURDAY_NOTIFICATION_ACTIVE)) == 1){
            weekDays[5].setChecked(true);
        }else{
            weekDays[5].setChecked(false);
        }

        if(notificationsStatusCursor.getInt(notificationsStatusCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_IS_SUNDAY_NOTIFICATION_ACTIVE)) == 1){
            weekDays[6].setChecked(true);
        }else{
            weekDays[6].setChecked(false);
        }

        notificationsStatusCursor.close();
    }

    public void setHoursAndMinutes(){
        HabitDbHelper dbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String notificationsHoursColumns[] = {
                HabitContract.HabitEntry.COLUMN_MONDAY_NOTIFICATION_HOUR,
                HabitContract.HabitEntry.COLUMN_TUESDAY_NOTIFICATION_HOUR,
                HabitContract.HabitEntry.COLUMN_WEDNESDAY_NOTIFICATION_HOUR,
                HabitContract.HabitEntry.COLUMN_THURSDAY_NOTIFICATION_HOUR,
                HabitContract.HabitEntry.COLUMN_FRIDAY_NOTIFICATION_HOUR,
                HabitContract.HabitEntry.COLUMN_SATURDAY_NOTIFICATION_HOUR,
                HabitContract.HabitEntry.COLUMN_SUNDAY_NOTIFICATION_HOUR
        };

        Cursor notificationsHourCursor = db.query(
                HabitContract.HabitEntry.TABLE_NAME,
                notificationsHoursColumns,
                HabitContract.HabitEntry._ID + "=" + habitId,
                null,
                null,
                null,
                null
        );

        notificationsHourCursor.moveToFirst();

        hoursAndMinutes[0].setText(notificationsHourCursor.getString(notificationsHourCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_MONDAY_NOTIFICATION_HOUR)));
        hoursAndMinutes[1].setText(notificationsHourCursor.getString(notificationsHourCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_TUESDAY_NOTIFICATION_HOUR)));
        hoursAndMinutes[2].setText(notificationsHourCursor.getString(notificationsHourCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_WEDNESDAY_NOTIFICATION_HOUR)));
        hoursAndMinutes[3].setText(notificationsHourCursor.getString(notificationsHourCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_THURSDAY_NOTIFICATION_HOUR)));
        hoursAndMinutes[4].setText(notificationsHourCursor.getString(notificationsHourCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_FRIDAY_NOTIFICATION_HOUR)));
        hoursAndMinutes[5].setText(notificationsHourCursor.getString(notificationsHourCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_SATURDAY_NOTIFICATION_HOUR)));
        hoursAndMinutes[6].setText(notificationsHourCursor.getString(notificationsHourCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_SUNDAY_NOTIFICATION_HOUR)));

        notificationsHourCursor.close();
    }

    public void createNotification(View view) {
        CheckBox checkBox;
        switch (view.getId()){
            case R.id.notification_monday_check_box:
                checkBox = (CheckBox) findViewById(view.getId());
                if(!updateDb(0, checkBox.isChecked(), hoursAndMinutes[0].getText().toString()))
                    Toast.makeText(this, "Failed to create notification", Toast.LENGTH_SHORT).show();
                if(checkBox.isChecked()) {
                    startNotification(habitId * 1000, 0);
                    Toast.makeText(this, "Created notification", Toast.LENGTH_SHORT).show();
                }
                else{
                    stopNotification(habitId * 1000);
                    Toast.makeText(this, "Deleted notification", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.notification_tuesday_check_box:
                checkBox = (CheckBox) findViewById(view.getId());
                if(!updateDb(1, checkBox.isChecked(), hoursAndMinutes[1].getText().toString()))
                    Toast.makeText(this, "Failed to create notification", Toast.LENGTH_SHORT).show();
                if(checkBox.isChecked()) {
                    startNotification(habitId * 1000 + 1, 1);
                    Toast.makeText(this, "Created notification", Toast.LENGTH_SHORT).show();
                }
                else{
                    stopNotification(habitId * 1000 + 1);
                    Toast.makeText(this, "Deleted notification", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.notification_wednesday_check_box:
                checkBox = (CheckBox) findViewById(view.getId());
                if(!updateDb(2, checkBox.isChecked(), hoursAndMinutes[2].getText().toString()))
                    Toast.makeText(this, "Failed to create notification", Toast.LENGTH_SHORT).show();
                if(checkBox.isChecked()) {
                    startNotification(habitId * 1000 + 2, 2);
                    Toast.makeText(this, "Created notification", Toast.LENGTH_SHORT).show();
                }
                else{
                    stopNotification(habitId * 1000 + 2);
                    Toast.makeText(this, "Deleted notification", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.notification_thursday_check_box:
                checkBox = (CheckBox) findViewById(view.getId());
                if(!updateDb(3, checkBox.isChecked(), hoursAndMinutes[3].getText().toString()))
                    Toast.makeText(this, "Failed to create notification", Toast.LENGTH_SHORT).show();
                if(checkBox.isChecked()) {
                    startNotification(habitId * 1000 + 3, 3);
                    Toast.makeText(this, "Created notification", Toast.LENGTH_SHORT).show();
                }
                else{
                    stopNotification(habitId * 1000 + 3);
                    Toast.makeText(this, "Deleted notification", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.notification_friday_check_box:
                checkBox = (CheckBox) findViewById(view.getId());
                if(!updateDb(4, checkBox.isChecked(), hoursAndMinutes[4].getText().toString()))
                    Toast.makeText(this, "Failed to create notification", Toast.LENGTH_SHORT).show();
                if(checkBox.isChecked()) {
                    startNotification(habitId * 1000 + 4, 4);
                    Toast.makeText(this, "Created notification", Toast.LENGTH_SHORT).show();
                }
                else{
                    stopNotification(habitId * 1000 + 4);
                    Toast.makeText(this, "Deleted notification", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.notification_saturday_check_box:
                checkBox = (CheckBox) findViewById(view.getId());
                if(!updateDb(5, checkBox.isChecked(), hoursAndMinutes[5].getText().toString()))
                    Toast.makeText(this, "Failed to create notification", Toast.LENGTH_SHORT).show();
                if(checkBox.isChecked()) {
                    startNotification(habitId * 1000 + 5, 5);
                    Toast.makeText(this, "Created notification", Toast.LENGTH_SHORT).show();
                }
                else{
                    stopNotification(habitId * 1000 + 5);
                    Toast.makeText(this, "Deleted notification", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.notification_sunday_check_box:
                checkBox = (CheckBox) findViewById(view.getId());
                if(!updateDb(6, checkBox.isChecked(), hoursAndMinutes[6].getText().toString()))
                    Toast.makeText(this, "Failed to create notification", Toast.LENGTH_SHORT).show();
                if(checkBox.isChecked()) {
                    startNotification(habitId * 1000 + 6, 6);
                    Toast.makeText(this, "Created notification", Toast.LENGTH_SHORT).show();
                }
                else{
                    stopNotification(habitId * 1000 + 6);
                    Toast.makeText(this, "Deleted notification", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public boolean updateDb(int day, boolean isChecked, String hour){
        String columnsToUpdate[] = new String[2];
        int valueShouldBeActive;
        switch (day){
            case 0:
                columnsToUpdate[0] = HabitContract.HabitEntry.COLUMN_IS_MONDAY_NOTIFICATION_ACTIVE;
                columnsToUpdate[1] = HabitContract.HabitEntry.COLUMN_MONDAY_NOTIFICATION_HOUR;
                break;
            case 1:
                columnsToUpdate[0] = HabitContract.HabitEntry.COLUMN_IS_TUESDAY_NOTIFICATION_ACTIVE;
                columnsToUpdate[1] = HabitContract.HabitEntry.COLUMN_TUESDAY_NOTIFICATION_HOUR;
                break;
            case 2:
                columnsToUpdate[0] = HabitContract.HabitEntry.COLUMN_IS_WEDNESDAY_NOTIFICATION_ACTIVE;
                columnsToUpdate[1] = HabitContract.HabitEntry.COLUMN_WEDNESDAY_NOTIFICATION_HOUR;
                break;
            case 3:
                columnsToUpdate[0] = HabitContract.HabitEntry.COLUMN_IS_THURSDAY_NOTIFICATION_ACTIVE;
                columnsToUpdate[1] = HabitContract.HabitEntry.COLUMN_THURSDAY_NOTIFICATION_HOUR;
                break;
            case 4:
                columnsToUpdate[0] = HabitContract.HabitEntry.COLUMN_IS_FRIDAY_NOTIFICATION_ACTIVE;
                columnsToUpdate[1] = HabitContract.HabitEntry.COLUMN_FRIDAY_NOTIFICATION_HOUR;
                break;
            case 5:
                columnsToUpdate[0] = HabitContract.HabitEntry.COLUMN_IS_SATURDAY_NOTIFICATION_ACTIVE;
                columnsToUpdate[1] = HabitContract.HabitEntry.COLUMN_SATURDAY_NOTIFICATION_HOUR;
                break;
            case 6:
                columnsToUpdate[0] = HabitContract.HabitEntry.COLUMN_IS_SUNDAY_NOTIFICATION_ACTIVE;
                columnsToUpdate[1] = HabitContract.HabitEntry.COLUMN_SUNDAY_NOTIFICATION_HOUR;
                break;
            default:
                return false;
        }

        if(isChecked)
            valueShouldBeActive = 1;
        else
            valueShouldBeActive = 0;

        ContentValues contentValues = new ContentValues();
        contentValues.put(columnsToUpdate[0], valueShouldBeActive);
        contentValues.put(columnsToUpdate[1], hour);

        HabitDbHelper helper = new HabitDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        return db.update(HabitContract.HabitEntry.TABLE_NAME,
                contentValues,
                HabitContract.HabitEntry._ID + " = " + habitId,
                null) > 0;
    }

    public void startNotification(int notificationId, int weekDay){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, weekDay+2);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hoursAndMinutes[weekDay].getText().toString().substring(0,2)));
        calendar.set(Calendar.MINUTE, Integer.valueOf(hoursAndMinutes[weekDay].getText().toString().substring(3,5)));

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        intent.putExtra(getString(R.string.EXTRA_NOTIFICATION_ID), notificationId);
        intent.putExtra(getString(R.string.EXTRA_DETAIL_NAME), habitName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(calendar.getTimeInMillis()<System.currentTimeMillis()){
            System.out.println("time");//TODO calendar time is behind system FIX
            SimpleDateFormat dateFormat = new SimpleDateFormat("EE HH:mm");
            String formatted = dateFormat.format(calendar.getTime());
            System.out.println(formatted);
            formatted = dateFormat.format(System.currentTimeMillis());
            System.out.println(formatted);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);
    }

    public void stopNotification(int notificationId){
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        intent.putExtra(getString(R.string.EXTRA_NOTIFICATION_ID), notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), notificationId, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public void setTime(View view) {

        int viewId = view.getId();
        DialogFragment timePickerFragment = new TimePickerFragment();

        switch (viewId){
            case R.id.notification_monday_tv:
                timePickerFragment.show(getFragmentManager(), "Monday");
                break;
            case R.id.notification_tuesday_tv:
                timePickerFragment.show(getFragmentManager(), "Tuesday");
                break;
            case R.id.notification_wednesday_tv:
                timePickerFragment.show(getFragmentManager(), "Wednesday");
                break;
            case R.id.notification_thursday_tv:
                timePickerFragment.show(getFragmentManager(), "Thursday");
                break;
            case R.id.notification_friday_tv:
                timePickerFragment.show(getFragmentManager(), "Friday");
                break;
            case R.id.notification_saturday_tv:
                timePickerFragment.show(getFragmentManager(), "Saturday");
                break;
            case R.id.notification_sunday_tv:
                timePickerFragment.show(getFragmentManager(), "Sunday");
                break;
            default:
                break;
        }
    }
}
