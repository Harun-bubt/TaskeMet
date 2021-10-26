package com.AppValley.TaskMet.ViewAdActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.AppValley.TaskMet.Home.DetailsClass.Ad_details;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.HomeAdModel;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;

import java.util.ArrayList;

public class Screen2_Shopping_Ads_Activity extends AppCompatActivity {


    public ImageView changeView, filterView, Ad_map_view;

    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    TextView itemNameTextView;
    TaskMetServer taskMetServer;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_ad__view_);

        recyclerView = findViewById(R.id.recyclerview);
        Ad_map_view = findViewById(R.id.Ad_map_view);
        changeView = findViewById(R.id.iconChagnedView);
        filterView = findViewById(R.id.iconFilter);
        itemNameTextView = findViewById(R.id.itemName);

        fragmentManager = this.getSupportFragmentManager();

        sharedPreferences = this.getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        itemNameTextView.setText(getString(R.string.shopping)+" > "+sharedPreferences.getString(Constants.SELECTED_SUB_CATEGORY,"item"));

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        adListFragment();
        filterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adFilterFragment();
            }
        });

    }
    public void adListFragment() {

        Screen2_UserShoppingAds screen2UserShoppingAds = new Screen2_UserShoppingAds();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, screen2UserShoppingAds);
        transaction.addToBackStack("tag");
        transaction.commit();

    }

    public void adFilterFragment() {

        Screen2_Filters screen2Filters = new Screen2_Filters();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, screen2Filters);
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