package com.AppValley.TaskMet.Home.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.AppValley.TaskMet.R;
import com.google.android.material.tabs.TabLayout;

public class PostTask extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    View postTask;

    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        postTask = inflater.inflate(R.layout.fragment_post_task, container, false);

        tabLayout = postTask.findViewById(R.id.post_tabLayout);
        viewPager = postTask.findViewById(R.id.post_viewPager);

        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        Adapter_post_tabLayout sa = new Adapter_post_tabLayout(fragmentManager, getLifecycle());
        viewPager.setAdapter(sa);

        tabLayout.addTab(tabLayout.newTab().setText("Shopping"));
        tabLayout.addTab(tabLayout.newTab().setText("Services"));
        tabLayout.addTab(tabLayout.newTab().setText("Brand"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


        return  postTask;
    }

}