package pl.c0.sayard.thehabitgame.achievements;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pl.c0.sayard.thehabitgame.R;
import pl.c0.sayard.thehabitgame.data.HabitContract;

/**
 * Created by Karol on 20.04.2017.
 */

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder>{

    private Context context;
    private Cursor cursor;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView achievementImage;
        private TextView achievementName;
        private TextView achievementDesc;

        public ViewHolder(View itemView) {
            super(itemView);

            achievementImage = (ImageView) itemView.findViewById(R.id.achievement_img);
            achievementName = (TextView) itemView.findViewById(R.id.achievement_name_text_view);
            achievementDesc = (TextView) itemView.findViewById(R.id.achievement_desc_text_view);
        }
    }

    public AchievementAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public AchievementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.achievement_item, parent, false);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AchievementAdapter.ViewHolder holder, int position) {
        if(!cursor.moveToPosition(position))
            return;

        final String NAME = cursor.getString(cursor.getColumnIndex(HabitContract.AchievementEntry.COLUMN_NAME));
        final String DESCRIPTION = cursor.getString(cursor.getColumnIndex(HabitContract.AchievementEntry.COLUMN_DESCRIPTION));
        final int IS_COMPLETED = cursor.getInt(cursor.getColumnIndex(HabitContract.AchievementEntry.COLUMN_IS_COMPLETED));
        final String IMAGE_COMPLETED = cursor.getString(cursor.getColumnIndex(HabitContract.AchievementEntry.IMAGE_COMPLETED));
        final String IMAGE_NOT_COMPLETED = cursor.getString(cursor.getColumnIndex(HabitContract.AchievementEntry.IMAGE_NOT_COMPLETED));

        holder.achievementName.setText(NAME);
        holder.achievementDesc.setText(DESCRIPTION);
        if(IS_COMPLETED == 1){
            int imageId = context.getResources().getIdentifier(IMAGE_COMPLETED, "drawable", context.getPackageName());
            holder.achievementImage.setImageResource(imageId);
        }else{
            int imageId = context.getResources().getIdentifier(IMAGE_NOT_COMPLETED, "drawable", context.getPackageName());
            holder.achievementImage.setImageResource(imageId);
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
