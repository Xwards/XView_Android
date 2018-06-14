package com.xwards.xview.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;


/**
 * Created by SuperMutt developer on 10-05-2016.
 * Pager Adapter for Classic Remote UpDown View Pager
 */
public class FragPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "FragPagerAdapter";
    private final List<PlayerFragment> mFragmentList;

    /**
     * @param fm           - Fragment manager
     * @param fragmentList - List Of Fragments
     */

    public FragPagerAdapter(FragmentManager fm, List<PlayerFragment> fragmentList) {
        super(fm);
        this.mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        try {
            return mFragmentList.get(i);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

}

