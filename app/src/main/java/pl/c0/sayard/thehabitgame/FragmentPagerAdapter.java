package pl.c0.sayard.thehabitgame;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import pl.c0.sayard.thehabitgame.achievements.AchievementListFragment;

/**
 * Created by Karol on 20.04.2017.
 */

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Habits", "Achievements" };

    public FragmentPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return HabitListFragment.newInstance();
        else
            return AchievementListFragment.newInstance();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
