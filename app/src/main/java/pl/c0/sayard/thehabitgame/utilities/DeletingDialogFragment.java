package pl.c0.sayard.thehabitgame.utilities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import pl.c0.sayard.thehabitgame.MainActivity;
import pl.c0.sayard.thehabitgame.R;
import pl.c0.sayard.thehabitgame.data.AchievementManager;
import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;

/**
 * Created by Karol on 24.04.2017.
 */

public class DeletingDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String dialogMessage = getArguments().getString(getString(R.string.EXTRA_DELETING_DIALOG_MESSAGE));
        String dialogPositiveButton = getArguments().getString(getString(R.string.EXTRA_DELETING_DIALOG_POSITIVE_BUTTON));
        String dialogNegativeButton = getArguments().getString(getString(R.string.EXTRA_DELETING_DIALOG_NEGATIVE_BUTTON));
        boolean actionDelete = getArguments().getBoolean(getString(R.string.EXTRA_DELETING_DIALOG_ACTION_DELETE));
        final int habitId = getArguments().getInt(getString(R.string.EXTRA_DELETING_DIALOG_HABIT_ID));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        DialogInterface.OnClickListener actionDeleteDialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        if(deleteHabitFromDatabase(habitId)){
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getActivity(), R.string.deleting_failed, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        return;
                    default:
                        break;
                }
            }
        };

        DialogInterface.OnClickListener actionHabitDevelopedClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteHabitFromDatabase(habitId);
                        AchievementManager.setAchievementCompleted(getActivity(), 4);
                        Toast.makeText(getActivity(), "Congratulations!", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        addOneOneMoreWeek(habitId);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        };

        DialogInterface.OnClickListener onClickListener;
        if(actionDelete)
            onClickListener = actionDeleteDialogClickListener;
        else
            onClickListener = actionHabitDevelopedClickListener;

        builder.setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton(dialogPositiveButton, onClickListener)
                .setNegativeButton(dialogNegativeButton, onClickListener)
                .show();

        return builder.show();
    }

    private boolean deleteHabitFromDatabase(int detailId) {
        if(detailId == -1)
            return false;

        HabitDbHelper dbHelper = new HabitDbHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.delete(HabitContract.HabitEntry.TABLE_NAME,
                HabitContract.HabitEntry._ID + " = " + detailId,
                null) > 0;
    }

    private boolean addOneOneMoreWeek(int detailId){
        if(detailId == -1)
            return false;

        HabitDbHelper dbHelper = new HabitDbHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(HabitContract.HabitEntry.COLUMN_DAYS_LEFT, 7);

        return db.update(HabitContract.HabitEntry.TABLE_NAME,
                contentValues,
                HabitContract.HabitEntry._ID + " = " + detailId,
                null) > 0;
    }
}
