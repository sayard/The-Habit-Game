package pl.c0.sayard.thehabitgame;

import android.app.Application;
import android.content.Context;

/**
 * Created by Karol on 13.04.2017.
 */

public class TheHabitGame extends Application{
    private static TheHabitGame instance;
    public TheHabitGame(){
        instance = this;
    }

    public static Context getContext(){
        return instance;
    }
}
