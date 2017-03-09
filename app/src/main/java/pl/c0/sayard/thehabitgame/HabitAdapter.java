package pl.c0.sayard.thehabitgame;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Karol on 06.03.2017.
 */

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {

    private String[] mDataSet;
    private Context mContext;
    private Toast toast;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;
        public ViewHolder(TextView textView) {
            super(textView);
            mTextView = textView;
        }
    }

    public HabitAdapter(String[] dataset, Context context) {
        mDataSet = dataset;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habit_adapter_item, parent, false);

        final ViewHolder viewHolder = new ViewHolder(textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                String toastText = "#" + position + " " + mDataSet[position];
                showToast(toastText);
            }
        });

        return viewHolder;
    }

    private void showToast(String toastText) {
        try{ toast.getView().isShown();
            toast.setText(toastText);
        } catch (Exception e) {
            toast = Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(mDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
