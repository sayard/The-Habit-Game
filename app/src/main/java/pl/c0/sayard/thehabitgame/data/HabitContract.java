package pl.c0.sayard.thehabitgame.data;

import android.provider.BaseColumns;

/**
 * Created by karol on 3/13/17.
 */

public final class HabitContract {
    private HabitContract(){}

    public static class HabitEntry implements BaseColumns{
        public static final String TABLE_NAME = "habits";
        public static final String COLUM_NAME = "name";
        public static final String COLUM_DESCRIPTION = "description";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_DAYS_LEFT = "days_left";
    }
}
