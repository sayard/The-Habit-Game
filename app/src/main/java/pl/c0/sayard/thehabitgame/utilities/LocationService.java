package pl.c0.sayard.thehabitgame.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import pl.c0.sayard.thehabitgame.HabitDetailActivity;
import pl.c0.sayard.thehabitgame.R;
import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;

/**
 * Created by Karol on 16.05.2017.
 */

public class LocationService extends Service {

    private LocationListener locationListener;
    private LocationManager locationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Cursor cursor = getGpsLocationData();
                cursor.moveToFirst();
                while (cursor.moveToNext()){
                    double latitude = cursor.getDouble(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_GEO_NOTIFICATION_LATITUDE));
                    double longitude = cursor.getDouble(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_GEO_NOTIFICATION_LONGITUDE));
                    Location dbLocation = new Location("");
                    dbLocation.setLatitude(latitude);
                    dbLocation.setLongitude(longitude);
                    if(dbLocation.distanceTo(location)<=200){
                        int id = cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry._ID));
                        createNotification(id);
                    }
                }
                cursor.close();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent settingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                settingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(settingIntent);
            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 20, locationListener);
    }

    private Cursor getGpsLocationData(){
        HabitDbHelper dbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {
                HabitContract.HabitEntry._ID,
                HabitContract.HabitEntry.COLUMN_GEO_NOTIFICATION_LATITUDE,
                HabitContract.HabitEntry.COLUMN_GEO_NOTIFICATION_LONGITUDE
        };
        return db.query(HabitContract.HabitEntry.TABLE_NAME,
                columns,
                HabitContract.HabitEntry.COLUMN_IS_GEO_NOTIFICATION_ACTIVE + " = 1",
                null,
                null,
                null,
                null);
    }

    private void createNotification(int id){

        final int NOTIFICATION_ID = (1000+id)*-1;

        Cursor cursor = getHabitData(id);

        cursor.moveToFirst();

        String title = cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_NAME));

        Intent repeatingIntent = new Intent(this, HabitDetailActivity.class);
        repeatingIntent.putExtra(getString(R.string.EXTRA_DETAIL_ID), id);
        repeatingIntent.putExtra(getString(R.string.EXTRA_DETAIL_NAME), title);
        repeatingIntent.putExtra(getString(R.string.EXTRA_DETAIL_COLOR),
                cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_COLOR)));
        repeatingIntent.putExtra(getString(R.string.EXTRA_DETAIL_DESCRIPTION),
                cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DESCRIPTION)));
        repeatingIntent.putExtra(getString(R.string.EXTRA_DETAIL_STREAK),
                cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_STREAK)));
        repeatingIntent.putExtra(getString(R.string.EXTRA_DETAIL_DAYS_LEFT),
                cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DAYS_LEFT)));
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        cursor.close();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent doneActionIntent = new Intent(this, NotificationActionDoneReceiver.class);
        doneActionIntent.putExtra(getString(R.string.EXTRA_DETAIL_ID), id);
        doneActionIntent.putExtra(getString(R.string.EXTRA_NOTIFICATION_ID), NOTIFICATION_ID);
        PendingIntent doneActionPendingIntent = PendingIntent.getBroadcast(this, -1, doneActionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent notTodayActionIntent = new Intent(this, NotificationActionNotTodayReceiver.class);
        notTodayActionIntent.putExtra(getString(R.string.EXTRA_NOTIFICATION_ID), NOTIFICATION_ID);
        PendingIntent notTodayActionPendingIntent = PendingIntent.getBroadcast(this, -2, notTodayActionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.notification_icon))
                .setContentTitle(title)
                .setContentText("It's time for " + title)
                .setVibrate(new long[] { 250, 250, 250, 250, 250 })
                .setSound(alarmSound)
                .addAction(R.drawable.ic_done_black_24dp, "Done", doneActionPendingIntent)
                .addAction(R.drawable.ic_not_today_black_24dp, "Not Today", notTodayActionPendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private Cursor getHabitData(int habitId){
        HabitDbHelper dbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                HabitContract.HabitEntry.COLUMN_NAME,
                HabitContract.HabitEntry.COLUMN_COLOR,
                HabitContract.HabitEntry.COLUMN_DESCRIPTION,
                HabitContract.HabitEntry.COLUMN_STREAK,
                HabitContract.HabitEntry.COLUMN_DAYS_LEFT
        };

        return db.query(HabitContract.HabitEntry.TABLE_NAME,
                columns,
                HabitContract.HabitEntry._ID + " = " + habitId,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null)
            locationManager.removeUpdates(locationListener);
    }
}
