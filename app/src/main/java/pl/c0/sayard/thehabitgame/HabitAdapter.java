package pl.c0.sayard.thehabitgame;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Karol on 06.03.2017.
 */

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {

    private String[] mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;
        public ViewHolder(TextView textView) {
            super(textView);
            mTextView = textView;
        }
    }

    public HabitAdapter(String[] dataset) {
        mDataSet = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habit_adapter_item, parent, false);

        ViewHolder vh = new ViewHolder(textView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
