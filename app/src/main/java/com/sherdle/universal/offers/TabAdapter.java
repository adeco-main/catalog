package com.sherdle.universal.offers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sherdle.universal.AbstractTabFragment;

import java.util.Map;

public class TabAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;

    public TabAdapter(FragmentManager fm, Map<Integer, AbstractTabFragment> tabs) {
        super(fm);
        this.tabs = tabs;

    }

    @Override
    public Fragment getItem(int position) {
            return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }


}