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

import com.AppValley.TaskMet.Home.DetailsClass.Ad_details;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.HomeAdModel;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;

import java.util.ArrayList;

public class Screen4_Marriage_Ads_Activity extends AppCompatActivity {

    RecyclerView recyclerView;

    public ImageView changeView, filterView, Ad_map_view;
    SharedPreferences sharedPreferences;

    TaskMetServer taskMetServer;
    TextView itemNameTextView;

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


        sharedPreferences = this.getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        itemNameTextView.setText(getString(R.string.marriage)+" > "+sharedPreferences.getString(Constants.SELECTED_MAIN_CATEGORY,"item"));

        fragmentManager = this.getSupportFragmentManager();

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        adListFragment();
        filterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adFilterFragment();
            }
        });



    }
    public void adListFragment()
    {
        Screen4_UserMarriageAds screen4UserMarriageAds = new Screen4_UserMarriageAds();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, screen4UserMarriageAds);
        transaction.addToBackStack("tag");
        transaction.commit();
    }
    public void adFilterFragment()
    {
        Screen4_Filters screen4Filters = new Screen4_Filters();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, screen4Filters);
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