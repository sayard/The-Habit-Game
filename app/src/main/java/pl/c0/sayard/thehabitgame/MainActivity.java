package pl.c0.sayard.thehabitgame;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import pl.c0.sayard.thehabitgame.data.HabitContract;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MainActivity extends AppCompatActivity {

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

        ViewPager viewPager = (ViewPager) findViewById(R.id.main_activity_view_pager);
        FragmentPagerAdapter fragmentPagerAdapter = new pl.c0.sayard.thehabitgame.FragmentPagerAdapter(
                getSupportFragmentManager()
        );
        viewPager.setAdapter(fragmentPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        startTutorial();
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

        sequence.addSequenceItem(new View(this),
                getString(R.string.showcase_sequence_1_3),
                getString(R.string.next));

        sequence.addSequenceItem(findViewById(R.id.fab),
                getString(R.string.showcase_sequence_1_4),
                getString(R.string.next));

        sequence.addSequenceItem(new View(this),
                getString(R.string.showcase_sequence_1_5),
                getString(R.string.got_it));

        sequence.start();
    }
}
