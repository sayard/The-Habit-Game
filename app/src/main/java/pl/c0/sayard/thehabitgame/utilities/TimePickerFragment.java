package pl.c0.sayard.thehabitgame.utilities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import pl.c0.sayard.thehabitgame.HabitNotificationsActivity;
import pl.c0.sayard.thehabitgame.R;
import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;


/**
 * Created by Karol on 29.03.2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String tag = getTag();

        String hourString = String.valueOf(hourOfDay);
        String minuteString = String.valueOf(minute);
        if(hourOfDay<10){
            hourString = "0" + hourString;
        }
        if(minute<10){
            minuteString = "0" + minuteString;
        }

        String shortTag = getShortTag(tag);
        String hour;
        int notificationId;

        switch (shortTag){
            case "Monday":
                TextView hourTextView = (TextView) getActivity().findViewById(R.id.notification_monday_tv);
                hour = hourString + ":" + minuteString;
                hourTextView.setText(hour);
                notificationId = Integer.valueOf(tag.substring(shortTag.length(), tag.length()));
                startNotification(notificationId, 0, hour);
                break;
            case "Tuesday":
                hourTextView = (TextView) getActivity().findViewById(R.id.notification_tuesday_tv);
                hour = hourString + ":" + minuteString;
                hourTextView.setText(hour);
                notificationId = Integer.valueOf(tag.substring(shortTag.length(), tag.length()));
                startNotification(notificationId, 1, hour);
                break;
            case "Wednesday":
                hourTextView = (TextView) getActivity().findViewById(R.id.notification_wednesday_tv);
                hour = hourString + ":" + minuteString;
                hourTextView.setText(hour);
                notificationId = Integer.valueOf(tag.substring(shortTag.length(), tag.length()));
                startNotification(notificationId, 2, hour);
                break;
            case "Thursday":
                hourTextView = (TextView) getActivity().findViewById(R.id.notification_thursday_tv);
                hour = hourString + ":" + minuteString;
                hourTextView.setText(hour);
                notificationId = Integer.valueOf(tag.substring(shortTag.length(), tag.length()));
                startNotification(notificationId, 3, hour);
                break;
            case "Friday":
                hourTextView = (TextView) getActivity().findViewById(R.id.notification_friday_tv);
                hour = hourString + ":" + minuteString;
                hourTextView.setText(hour);
                notificationId = Integer.valueOf(tag.substring(shortTag.length(), tag.length()));
                startNotification(notificationId, 4, hour);
                break;
            case "Saturday":
                hourTextView = (TextView) getActivity().findViewById(R.id.notification_saturday_tv);
                hour = hourString + ":" + minuteString;
                hourTextView.setText(hour);
                notificationId = Integer.valueOf(tag.substring(shortTag.length(), tag.length()));
                startNotification(notificationId, 5, hour);
                break;
            case "Sunday":
                hourTextView = (TextView) getActivity().findViewById(R.id.notification_sunday_tv);
                hour = hourString + ":" + minuteString;
                hourTextView.setText(hour);
                notificationId = Integer.valueOf(tag.substring(shortTag.length(), tag.length()));
                startNotification(notificationId, 6, hour);
                break;
            default:
                break;
        }
    }

    private String getShortTag(String tag) {
        if(tag.contains("Monday"))
            return "Monday";
        else if(tag.contains("Tuesday"))
            return "Tuesday";
        else if(tag.contains("Wednesday"))
            return "Wednesday";
        else if(tag.contains("Thursday"))
            return "Thursday";
        else if(tag.contains("Friday"))
            return "Friday";
        else if(tag.contains("Saturday"))
            return "Saturday";
        else if(tag.contains("Sunday"))
            return "Sunday";
        else
            return "error";
    }

    private void startNotification(int notificationId, int weekDay, String hour){
        HabitNotificationsActivity instance = HabitNotificationsActivity.getInstance();
        CheckBox weekDayCheckBox = null;
        switch (weekDay){
            case 0:
                weekDayCheckBox = (CheckBox) instance.findViewById(R.id.notification_monday_check_box);
                break;
            case 1:
                weekDayCheckBox = (CheckBox) instance.findViewById(R.id.notification_tuesday_check_box);
                break;
            case 2:
                weekDayCheckBox = (CheckBox) instance.findViewById(R.id.notification_wednesday_check_box);
                break;
            case 3:
                weekDayCheckBox = (CheckBox) instance.findViewById(R.id.notification_thursday_check_box);
                break;
            case 4:
                weekDayCheckBox = (CheckBox) instance.findViewById(R.id.notification_friday_check_box);
                break;
            case 5:
                weekDayCheckBox = (CheckBox) instance.findViewById(R.id.notification_saturday_check_box);
                break;
            case 6:
                weekDayCheckBox = (CheckBox) instance.findViewById(R.id.notification_sunday_check_box);
                break;
        }

        if(weekDayCheckBox!=null &&
                weekDayCheckBox.isChecked() &&
                updateDb(weekDay, weekDayCheckBox.isChecked(), hour, notificationId/1000))
            instance.startNotification(notificationId, weekDay, true);
    }

    private boolean updateDb(int day, boolean isChecked, String hour, int habitId){
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

        HabitDbHelper helper = new HabitDbHelper(HabitNotificationsActivity.getInstance());
        SQLiteDatabase db = helper.getReadableDatabase();

        return db.update(HabitContract.HabitEntry.TABLE_NAME,
                contentValues,
                HabitContract.HabitEntry._ID + " = " + habitId,
                null) > 0;
    }
}
