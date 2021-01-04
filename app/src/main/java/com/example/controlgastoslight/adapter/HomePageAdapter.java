package com.example.controlgastoslight.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomePageAdapter extends FragmentPagerAdapter {
    private int nTabs;

    public HomePageAdapter(@NonNull FragmentManager fm, int behavior, int nTabs) {
        super(fm, behavior);
        this.nTabs = nTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 0;
    }
}
