package pl.c0.sayard.thehabitgame.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import pl.c0.sayard.thehabitgame.MainActivity;
import pl.c0.sayard.thehabitgame.R;

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

        Intent repeatingIntent = new Intent(context, MainActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

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

}
