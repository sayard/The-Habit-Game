package pl.c0.sayard.thehabitgame.utilities;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import pl.c0.sayard.thehabitgame.R;

/**
 * Created by Karol on 13.04.2017.
 */

public class NotificationActionNotTodayReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        final int NOTIFICATION_ID = intent.getIntExtra(context.getString(R.string.EXTRA_NOTIFICATION_ID), -1);
        if(NOTIFICATION_ID == -1){
            Toast.makeText(context, "Failed to cancel notification", Toast.LENGTH_SHORT).show();
        }else{
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }
}
