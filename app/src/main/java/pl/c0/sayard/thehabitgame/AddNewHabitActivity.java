package pl.c0.sayard.thehabitgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
}
