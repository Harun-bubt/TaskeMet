package com.AppValley.TaskMet.Home.HomeFragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.AppValley.TaskMet.PostTask.Screen1_Service;
import com.AppValley.TaskMet.PostTask.Screen1_Shop;

class Adapter_post_tabLayout extends FragmentStateAdapter {

    public Adapter_post_tabLayout(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 0) {
            return new Screen1_Shop();
        }
        if (position == 1) {
            return new Screen1_Service();
        }
        return new Screen1_Brand();

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}