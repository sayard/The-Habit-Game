package pl.c0.sayard.thehabitgame;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

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

        weekDays[0] = (CheckBox) findViewById(R.id.notification_monday_check_box);
        weekDays[1] = (CheckBox) findViewById(R.id.notification_tuesday_check_box);
        weekDays[2] = (CheckBox) findViewById(R.id.notification_wednesday_check_box);
        weekDays[3] = (CheckBox) findViewById(R.id.notification_thursday_check_box);
        weekDays[4] = (CheckBox) findViewById(R.id.notification_friday_check_box);
        weekDays[5] = (CheckBox) findViewById(R.id.notification_saturday_check_box);
        weekDays[6] = (CheckBox) findViewById(R.id.notification_sunday_check_box);

        Intent intent = getIntent();
        habitId = intent.getIntExtra(getString(R.string.EXTRA_DETAIL_ID), -1);
        habitName = intent.getStringExtra(getString(R.string.EXTRA_DETAIL_NAME));

        hoursAndMinutes[0] = (TextView) findViewById(R.id.notification_monday_tv);
        hoursAndMinutes[1] = (TextView) findViewById(R.id.notification_tuesday_tv);
        hoursAndMinutes[2] = (TextView) findViewById(R.id.notification_wednesday_tv);
        hoursAndMinutes[3] = (TextView) findViewById(R.id.notification_thursday_tv);
        hoursAndMinutes[4] = (TextView) findViewById(R.id.notification_friday_tv);
        hoursAndMinutes[5] = (TextView) findViewById(R.id.notification_saturday_tv);
        hoursAndMinutes[6] = (TextView) findViewById(R.id.notification_sunday_tv);
    }

    public void applyNotifications(View view) {
        for(int i=1; i<2; i++){
            int notificationId = habitId + (1000 * (i+1));
            if(weekDays[i].isChecked()){
                String hourString = hoursAndMinutes[i].getText().toString().substring(0,2);
                int hour = Integer.valueOf(hourString);
                String minuteString = hoursAndMinutes[i].getText().toString().substring(3,5);
                int minute = Integer.valueOf(minuteString);
                startNotification(notificationId, i, hour, minute);
            }else{
                stopNotification(notificationId);
            }
        }
        Toast.makeText(this, "Notifications set", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void startNotification(int notificationId, int weekDay, int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, weekDay+1);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        intent.putExtra(getString(R.string.EXTRA_NOTIFICATION_ID), notificationId);
        intent.putExtra(getString(R.string.EXTRA_DETAIL_NAME), habitName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
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
