package pl.c0.sayard.thehabitgame.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.Calendar;

import pl.c0.sayard.thehabitgame.HabitDetailActivity;
import pl.c0.sayard.thehabitgame.R;
import pl.c0.sayard.thehabitgame.data.HabitContract;
import pl.c0.sayard.thehabitgame.data.HabitDbHelper;

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
        Calendar calendar = Calendar.getInstance();

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekNotificationColumnName;
        switch (dayOfWeek){
            case Calendar.MONDAY:
                dayOfWeekNotificationColumnName = HabitContract.HabitEntry.COLUMN_IS_MONDAY_NOTIFICATION_ACTIVE;
                break;
            case Calendar.TUESDAY:
                dayOfWeekNotificationColumnName = HabitContract.HabitEntry.COLUMN_IS_TUESDAY_NOTIFICATION_ACTIVE;
                break;
            case Calendar.WEDNESDAY:
                dayOfWeekNotificationColumnName = HabitContract.HabitEntry.COLUMN_IS_WEDNESDAY_NOTIFICATION_ACTIVE;
                break;
            case Calendar.THURSDAY:
                dayOfWeekNotificationColumnName = HabitContract.HabitEntry.COLUMN_IS_THURSDAY_NOTIFICATION_ACTIVE;
                break;
            case Calendar.FRIDAY:
                dayOfWeekNotificationColumnName = HabitContract.HabitEntry.COLUMN_IS_FRIDAY_NOTIFICATION_ACTIVE;
                break;
            case Calendar.SATURDAY:
                dayOfWeekNotificationColumnName = HabitContract.HabitEntry.COLUMN_IS_SATURDAY_NOTIFICATION_ACTIVE;
                break;
            case Calendar.SUNDAY:
                dayOfWeekNotificationColumnName = HabitContract.HabitEntry.COLUMN_IS_SUNDAY_NOTIFICATION_ACTIVE;
                break;
            default:
                return;
        }

        HabitDbHelper dbHelper = new HabitDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String columns[] = {
                HabitContract.HabitEntry._ID,
                HabitContract.HabitEntry.COLUMN_NAME,
                HabitContract.HabitEntry.COLUMN_STREAK};

        Cursor cursor = db.query(HabitContract.HabitEntry.TABLE_NAME,
                columns,
                dayOfWeekNotificationColumnName + " = 1",
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        try {
            do{
                HabitListItem listItem = new HabitListItem();
                listItem.setId(cursor.getInt(cursor.getColumnIndex(HabitContract.HabitEntry._ID)));
                listItem.setHeading(cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_NAME)));
                listItem.setContent("Current streak:" +
                        cursor.getString(cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_STREAK)));
                listItemList.add(listItem);
            }while (cursor.moveToNext());
        }catch (CursorIndexOutOfBoundsException e){
            cursor.close();
        }finally {
            cursor.close();
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
                R.layout.widget_list_row);
        HabitListItem listItem = listItemList.get(position);
        remoteViews.setTextViewText(R.id.habit_list_row_heading, listItem.getHeading());
        remoteViews.setTextViewText(R.id.habit_list_row_content, listItem.getContent());

        Bundle extras = new Bundle();
        extras.putInt(context.getString(R.string.EXTRA_DETAIL_ID), listItemList.get(position).getId());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.habit_list_row_linear_layout, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
