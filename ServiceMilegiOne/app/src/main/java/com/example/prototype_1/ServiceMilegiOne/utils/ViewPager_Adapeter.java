package com.example.prototype_1.ServiceMilegiOne.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.prototype_1.ServiceMilegiOne.activity.BookingsFragment;
import com.example.prototype_1.ServiceMilegiOne.activity.HomeFragment;

public class ViewPager_Adapeter extends FragmentStatePagerAdapter {


    public ViewPager_Adapeter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();

            case 1:
                return  new BookingsFragment();


        }

        return new HomeFragment();


    }

    @Override
    public int getCount() {
        return 2;
    }
}
