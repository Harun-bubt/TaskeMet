package com.AppValley.TaskMet.ViewAdActivities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;

public class Screen1_Common_Ads_Activity extends AppCompatActivity {

    RecyclerView recyclerView;

    public ImageView changeView, filterView, Ad_map_view;

    TaskMetServer taskMetServer;
    SharedPreferences sharedPreferences;
    TextView itemNameTextView;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_ad__view_);

        recyclerView = findViewById(R.id.recyclerview);
        changeView = findViewById(R.id.iconChagnedView);
        filterView = findViewById(R.id.iconFilter);
        Ad_map_view = findViewById(R.id.Ad_map_view);
        itemNameTextView = findViewById(R.id.itemName);

        fragmentManager = this.getSupportFragmentManager();

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        sharedPreferences = this.getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        itemNameTextView.setText(getString(R.string.services)+" > "+sharedPreferences.getString(Constants.SELECTED_MAIN_CATEGORY,"item"));

        adListFragment();

        filterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adFilterFragment();
            }
        });

    }

    public void adListFragment() {

        Screen1_UserCommonAds screen1UserCommonAds = new Screen1_UserCommonAds();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, screen1UserCommonAds);
        transaction.addToBackStack("tag");
        transaction.commit();

    }
    public void adFilterFragment() {

        Screen1_Filters filter1CommonAds = new Screen1_Filters();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, filter1CommonAds);
        transaction.addToBackStack("tag");
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}