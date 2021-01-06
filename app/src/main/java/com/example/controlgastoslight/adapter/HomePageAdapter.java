package com.example.controlgastoslight.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.controlgastoslight.RegistroListFragment;

public class HomePageAdapter extends FragmentPagerAdapter {
    private final int nTabs;

    public HomePageAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.nTabs = 4;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return RegistroListFragment.newInstance(0);
            case 1: return RegistroListFragment.newInstance(1);
            case 2: return RegistroListFragment.newInstance(2);
            case 3: return RegistroListFragment.newInstance(3);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
