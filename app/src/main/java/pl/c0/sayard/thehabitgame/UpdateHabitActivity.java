package pl.c0.sayard.thehabitgame;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;

public class UpdateHabitActivity extends AppCompatActivity {

    private String[] colors = {"Red", "Green", "Blue"};
    private Spinner colorSpinner;
    private int habitId;

    private EditText habitNameEditText, habitDescEditText;
    private String columnsToUpdate[] = {HabitContract.HabitEntry.COLUMN_NAME,
            HabitContract.HabitEntry.COLUMN_DESCRIPTION,
            HabitContract.HabitEntry.COLUMN_COLOR};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_habit);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.color_spinner_item, colors);

        colorSpinner = (Spinner)findViewById(R.id.update_spinner);
        colorSpinner.setAdapter(arrayAdapter);

        habitNameEditText = (EditText) findViewById(R.id.update_habit_name);
        habitDescEditText = (EditText) findViewById(R.id.update_habit_desc);
        habitId = getIntent().getIntExtra(getString(R.string.EXTRA_UPDATE_ID), -1);

        HabitDbHelper helper = new HabitDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(HabitContract.HabitEntry.TABLE_NAME,
                columnsToUpdate,
                HabitContract.HabitEntry._ID + " = " +habitId,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        habitNameEditText.setText(cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_NAME)));
        habitDescEditText.setText(cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DESCRIPTION)));
        int colorSpinnerPosition = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_COLOR));
        colorSpinner.setSelection(colorSpinnerPosition-1);
    }


    public void updateHabit(View view) {
        if(updateHabitInDb()){
            Toast.makeText(this, "Habit successfully updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Updating has failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean updateHabitInDb(){
        if(habitId == -1)
            return false;

        String habitName = habitNameEditText.getText().toString();
        String habitDesc = habitDescEditText.getText().toString();

        String colorSpinnerValue = colorSpinner.getSelectedItem().toString();
        int color;
        switch (colorSpinnerValue)
        {
            case "Red":
                color = 1;
                break;

            case "Green":
                color = 2;
                break;

            case "Blue":
                color = 3;
                break;

            default:
                color = 1;
                break;
        }

        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_NAME, habitName);
        values.put(HabitContract.HabitEntry.COLUMN_DESCRIPTION, habitDesc);
        values.put(HabitContract.HabitEntry.COLUMN_COLOR, color);

        HabitDbHelper helper = new HabitDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        return db.update(HabitContract.HabitEntry.TABLE_NAME,
                values,
                HabitContract.HabitEntry._ID + " = " + habitId,
                null) > 0;
    }
}
