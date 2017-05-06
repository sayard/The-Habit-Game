package pl.c0.sayard.thehabitgame.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import pl.c0.sayard.thehabitgame.HabitDetailActivity;
import pl.c0.sayard.thehabitgame.R;

/**
 * Created by Karol on 04.05.2017.
 */

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int widgetCount = appWidgetIds.length;
        for(int i=0; i<widgetCount; i++){
            RemoteViews remoteViews = updateWidgetListView(context,
                    appWidgetIds[i]);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.widget_list);

            appWidgetManager.updateAppWidget(appWidgetIds[i],
                    remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget);

        Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(
            serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.widget_list, serviceIntent);
        remoteViews.setEmptyView(R.id.widget_list, R.id.empty_view);

        Intent clickIntent = new Intent(context, HabitDetailActivity.class);
        PendingIntent clickPendingIntent = PendingIntent.getActivity(context,
                0,
                clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setPendingIntentTemplate(R.id.widget_list, clickPendingIntent);

        return remoteViews;
    }
}
