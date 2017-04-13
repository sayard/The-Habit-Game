package pl.c0.sayard.thehabitgame;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
            Toast.makeText(this, R.string.name_field_error, Toast.LENGTH_SHORT).show();
            return;
        }

        if(addHabitToDb(name, desc, color) == -1){
            Toast.makeText(this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, R.string.habit_added, Toast.LENGTH_SHORT).show();
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

        commitToSharedPreferences();

        return db.insert(HabitContract.HabitEntry.TABLE_NAME, null, values);
    }

    private void commitToSharedPreferences(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(this.getString(R.string.preference_button_first_click), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getFirstTimeClickSharedPreferenceKey(), true);
        editor.commit();
    }

    private String getFirstTimeClickSharedPreferenceKey(){
        HabitDbHelper dbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(HabitContract.HabitEntry.TABLE_NAME,
                new String[]{HabitContract.HabitEntry._ID},
                null,
                null,
                null,
                null,
                HabitContract.HabitEntry._ID + " DESC",
                "1");

        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry._ID));
        cursor.close();
        String key = "isFirstTime" + id;
        return key;
    }
}
