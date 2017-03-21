package pl.c0.sayard.thehabitgame;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.c0.sayard.thehabitgame.data.HabitContract;

/**
 * Created by Karol on 06.03.2017.
 */

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {

    private Context mContext;
    private Cursor mCursor;


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        TextView colorTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            colorTextView = (TextView) itemView.findViewById(R.id.color_text_view);
        }
    }

    public HabitAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        mCursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.habit_adapter_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position))
            return;

        final int _ID = mCursor.getInt(mCursor.getColumnIndex(HabitContract.HabitEntry._ID));
        final String NAME = mCursor.getString(mCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_NAME));
        final int COLOR = mCursor.getInt(mCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_COLOR));
        final String DESCRIPTION = mCursor.getString(mCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DESCRIPTION));
        final int STREAK = mCursor.getInt(mCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_STREAK));
        final String DAYS_LEFT = mCursor.getString(mCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DAYS_LEFT));

        int color = mCursor.getInt(mCursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_COLOR));

        holder.nameTextView.setText(NAME);

        holder.nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HabitDetailActivity.class);
                intent.putExtra(mContext.getString(R.string.EXTRA_DETAIL_ID), _ID);
                intent.putExtra(mContext.getString(R.string.EXTRA_DETAIL_NAME), NAME);
                intent.putExtra(mContext.getString(R.string.EXTRA_DETAIL_COLOR), COLOR);
                intent.putExtra(mContext.getString(R.string.EXTRA_DETAIL_DESCRIPTION),DESCRIPTION);
                intent.putExtra(mContext.getString(R.string.EXTRA_DETAIL_STREAK), STREAK);
                intent.putExtra(mContext.getString(R.string.EXTRA_DETAIL_DAYS_LEFT), DAYS_LEFT);
                mContext.startActivity(intent);
            }
        });

        switch (color)
        {
            case 1:
                holder.colorTextView.setBackgroundColor(Color.parseColor("#FF0000"));
                break;
            case 2:
                holder.colorTextView.setBackgroundColor(Color.parseColor("#00FF00"));
                break;
            case 3:
                holder.colorTextView.setBackgroundColor(Color.parseColor("#0000FF"));
                break;
            default:
                holder.colorTextView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}
