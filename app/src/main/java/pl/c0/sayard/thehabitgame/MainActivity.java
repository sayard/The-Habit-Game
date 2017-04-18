package pl.c0.sayard.thehabitgame;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;
import pl.c0.sayard.thehabitgame.data.SampleData;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddNewHabitActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.habit_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        HabitDbHelper dbHelper = new HabitDbHelper(this);
        mDb = dbHelper.getReadableDatabase();
        Cursor cursor = getAllHabits();

        mAdapter = new HabitAdapter(this, cursor);
        mRecyclerView.setAdapter(mAdapter);

        startTutorial();
    }

    private Cursor getAllHabits(){
        return mDb.query(HabitContract.HabitEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                HabitContract.HabitEntry.COLUMN_COLOR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startTutorial(){
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(250);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getString(R.string.showcase_sequence_1_id));
        sequence.setConfig(config);

        sequence.addSequenceItem(new View(this),
                getString(R.string.showcase_sequence_1_1),
                getString(R.string.next));

        sequence.addSequenceItem(new View(this),
                getString(R.string.showcase_sequence_1_2),
                getString(R.string.next));

        sequence.addSequenceItem(findViewById(R.id.habit_recycler_view),
                getString(R.string.showcase_sequence_1_3),
                getString(R.string.next));

        sequence.addSequenceItem(findViewById(R.id.fab),
                getString(R.string.showcase_sequence_1_4),
                getString(R.string.next));

        sequence.addSequenceItem(findViewById(R.id.habit_recycler_view),
                getString(R.string.showcase_sequence_1_5),
                getString(R.string.got_it));

        sequence.start();
    }
}
