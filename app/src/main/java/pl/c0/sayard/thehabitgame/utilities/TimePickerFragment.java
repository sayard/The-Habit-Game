package pl.c0.sayard.thehabitgame.utilities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import pl.c0.sayard.thehabitgame.R;


/**
 * Created by Karol on 29.03.2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TextView hourTextView;

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

        switch (tag){
            case "Monday":
                hourTextView = (TextView) getActivity().findViewById(R.id.notification_monday_tv);
                hourTextView.setText(hourString + ":" + minuteString);
                break;
            case "Tuesday":
                hourTextView = (TextView) getActivity().findViewById(R.id.notification_tuesday_tv);
                hourTextView.setText(hourString + ":" + minuteString);
                break;
            case "Wednesday":
                hourTextView = (TextView) getActivity().findViewById(R.id.notification_wednesday_tv);
                hourTextView.setText(hourString + ":" + minuteString);
                break;
            case "Thursday":
                hourTextView = (TextView) getActivity().findViewById(R.id.notification_thursday_tv);
                hourTextView.setText(hourString + ":" + minuteString);
                break;
            case "Friday":
                hourTextView = (TextView) getActivity().findViewById(R.id.notification_friday_tv);
                hourTextView.setText(hourString + ":" + minuteString);
                break;
            case "Saturday":
                hourTextView = (TextView) getActivity().findViewById(R.id.notification_saturday_tv);
                hourTextView.setText(hourString + ":" + minuteString);
                break;
            case "Sunday":
                hourTextView = (TextView) getActivity().findViewById(R.id.notification_sunday_tv);
                hourTextView.setText(hourString + ":" + minuteString);
                break;
            default:
                break;
        }
    }
}
