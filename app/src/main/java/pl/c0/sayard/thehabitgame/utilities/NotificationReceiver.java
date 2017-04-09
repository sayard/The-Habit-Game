package pl.c0.sayard.thehabitgame.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import pl.c0.sayard.thehabitgame.HabitDetailActivity;
import pl.c0.sayard.thehabitgame.MainActivity;
import pl.c0.sayard.thehabitgame.R;
import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;

/**
 * Created by Karol on 29.03.2017.
 */

public class NotificationReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        final int NOTIFICATION_ID = intent.getIntExtra(context.getString(R.string.EXTRA_NOTIFICATION_ID), -1);
        String title;
        if(NOTIFICATION_ID == -1){
            title = "Fail" ;
        }else{
            title = intent.getStringExtra(context.getString(R.string.EXTRA_DETAIL_NAME));
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Cursor cursor = getHabitData(context, intent.getIntExtra(context.getString(R.string.EXTRA_DETAIL_ID), -1));

        cursor.moveToFirst();

        Intent repeatingIntent = new Intent(context, HabitDetailActivity.class);
        repeatingIntent.putExtra(context.getString(R.string.EXTRA_DETAIL_ID),
                cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry._ID)));
        repeatingIntent.putExtra(context.getString(R.string.EXTRA_DETAIL_NAME),
                title);
        repeatingIntent.putExtra(context.getString(R.string.EXTRA_DETAIL_COLOR),
                cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_COLOR)));
        repeatingIntent.putExtra(context.getString(R.string.EXTRA_DETAIL_DESCRIPTION),
                cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DESCRIPTION)));
        repeatingIntent.putExtra(context.getString(R.string.EXTRA_DETAIL_STREAK),
                cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_STREAK)));
        repeatingIntent.putExtra(context.getString(R.string.EXTRA_DETAIL_DAYS_LEFT),
                cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DAYS_LEFT)));
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        cursor.close();

        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle(title)
                .setContentText("It's time for " + title)
                .setVibrate(new long[] { 250, 250, 250, 250, 250 })
                .setSound(alarmSound)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private static Cursor getHabitData(Context context, int habitId){
        HabitDbHelper dbHelper = new HabitDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
            HabitContract.HabitEntry._ID,
            HabitContract.HabitEntry.COLUMN_NAME,
            HabitContract.HabitEntry.COLUMN_COLOR,
            HabitContract.HabitEntry.COLUMN_DESCRIPTION,
            HabitContract.HabitEntry.COLUMN_STREAK,
            HabitContract.HabitEntry.COLUMN_DAYS_LEFT
        };

        Cursor cursor = db.query(HabitContract.HabitEntry.TABLE_NAME,
                columns,
                HabitContract.HabitEntry._ID + " = " + habitId,
                null,
                null,
                null,
                null);

        return cursor;
    }

}
