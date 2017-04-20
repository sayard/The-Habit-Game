package pl.c0.sayard.thehabitgame;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;

/**
 * Created by Karol on 20.04.2017.
 */

public class HabitListFragment extends Fragment {

    public static Fragment newInstance(int position) {
        HabitListFragment habitListFragment = new HabitListFragment();
        return habitListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habit_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.habit_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        HabitDbHelper dbHelper = new HabitDbHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = getAllHabits(db);

        RecyclerView.Adapter adapter = new HabitAdapter(getActivity(), cursor);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private static Cursor getAllHabits(SQLiteDatabase db){
        return db.query(HabitContract.HabitEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                HabitContract.HabitEntry.COLUMN_COLOR);
    }

}
