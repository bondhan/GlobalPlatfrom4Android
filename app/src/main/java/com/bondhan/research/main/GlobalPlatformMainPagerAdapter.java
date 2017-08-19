package com.bondhan.research.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by bondhan on 29/07/17.
 */

public class GlobalPlatformMainPagerAdapter extends FragmentPagerAdapter{

    int mNumOfTabs;

    private static CardInfoSubFragment tab0;
    private static CardContentsSubFragment tab1;
    private static SetupGpSubFragment tab2;

    public GlobalPlatformMainPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (tab0 == null) {
                    tab0 = new CardInfoSubFragment();
                }
                return tab0;
            case 1:
                if (tab1 == null) {
                    tab1 = new CardContentsSubFragment();
                }
                return tab1;
            case 2:
                if (tab2 == null) {
                    tab2 = new SetupGpSubFragment();
                }
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
