package com.example.controlgastoslight.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.controlgastoslight.EstadisticasDataFragment;

public class EstadisticasPageAdapter extends FragmentPagerAdapter {
    private final int numTabs;

    public EstadisticasPageAdapter(FragmentManager fm, int numTabs){
        super(fm);
        this.numTabs = numTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return EstadisticasDataFragment.newInstance(0);
            case 1: return EstadisticasDataFragment.newInstance(1);
            case 2: return EstadisticasDataFragment.newInstance(2);
            case 3: return EstadisticasDataFragment.newInstance(3);
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return this.numTabs;
    }
}
