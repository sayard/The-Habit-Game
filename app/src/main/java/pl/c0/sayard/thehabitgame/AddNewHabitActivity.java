package pl.c0.sayard.thehabitgame;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;

public class AddNewHabitActivity extends AppCompatActivity {

    private String[] colors = {"Red", "Green", "Blue"};
    private Spinner colorSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_habit);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.color_spinner_item, colors);

        colorSpinner = (Spinner)findViewById(R.id.spinner1);
        colorSpinner.setAdapter(arrayAdapter);
    }

    public void addNewHabit(View view) {
        EditText nameEditText = (EditText) findViewById(R.id.add_new_habit_name);
        String name = nameEditText.getText().toString();

        EditText descEditText = (EditText) findViewById(R.id.add_new_habit_desc);
        String desc = descEditText.getText().toString();

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

        if(name.isEmpty()){
            nameEditText.setBackgroundColor(Color.RED);
            Toast.makeText(this, "Name field must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(addHabitToDb(name, desc, color) == -1){
            Toast.makeText(this, "An error has occurred. Please try again.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Habit added successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private long addHabitToDb(String name, String desc, int color){
        HabitDbHelper helper = new HabitDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_NAME, name);
        values.put(HabitContract.HabitEntry.COLUMN_DESCRIPTION, desc);
        values.put(HabitContract.HabitEntry.COLUMN_COLOR, color);

        return db.insert(HabitContract.HabitEntry.TABLE_NAME, null, values);
    }
}
