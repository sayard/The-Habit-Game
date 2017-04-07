package pl.c0.sayard.thehabitgame.utilities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Karol on 07.04.2017.
 */

public class RestartNotificationsReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            Intent bootServiceIntent = new Intent(context, BootService.class);
            ComponentName service = context.startService(bootServiceIntent);

            if(service == null){
                Log.e("RestartService", "Could not start service ");
            }else{
                Log.e("RestartService", "Successfully started service ");
            }
        }else {
            Log.e("RestartService", "Received unexpected intent " + intent.toString());
        }
    }
}
