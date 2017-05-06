package pl.c0.sayard.thehabitgame;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

/**
 * Created by Karol on 06.05.2017.
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<HabitListItem> listItemList = new ArrayList();
    private Context context;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent){
        this.context = context;
        appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();
    }

    private void populateListItem(){
        for(int i=0; i<10; i++){
            HabitListItem listItem = new HabitListItem();
            listItem.setHeading("Heading " + i);
            listItem.setContent("Content " + i);
            listItemList.add(listItem);
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(),
                R.layout.habit_list_row);
        HabitListItem listItem = listItemList.get(position);
        remoteViews.setTextViewText(R.id.habit_list_row_heading, listItem.getHeading());
        remoteViews.setTextViewText(R.id.habit_list_row_content, listItem.getContent());

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
