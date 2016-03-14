package il.ac.shenkar.david.todolistex2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by David on 02-Mar-16.
 */
public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
       switch (position) {
            case 0:
                fragment = new AllTasksTabFragment();
            case 1:
                fragment = new WaitingTasksTabFragment();
            default:
                fragment = new AllTasksTabFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
