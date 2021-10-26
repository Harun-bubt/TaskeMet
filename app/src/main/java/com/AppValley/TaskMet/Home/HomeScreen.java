package com.AppValley.TaskMet.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.AppValley.TaskMet.Home.HomeFragments.Chat;
import com.AppValley.TaskMet.Home.HomeFragments.Home;
import com.AppValley.TaskMet.Home.HomeFragments.MyPosts;
import com.AppValley.TaskMet.Home.HomeFragments.PostTask;
import com.AppValley.TaskMet.Home.HomeFragments.ProfileSettings;
import com.AppValley.TaskMet.R;
import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayDeque;
import java.util.Deque;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeScreen extends AppCompatActivity {

    BottomNavigationView bottom_navigation;
    Deque<Integer> integerDeque = new ArrayDeque<>(5);
    boolean flag = true;

    FragmentTransaction transaction;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Support Fragment Manager
        fragmentManager = this.getSupportFragmentManager();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setItemIconTintList(null);
        bottom_navigation.setSelectedItemId(R.id.home);

        integerDeque.push(R.id.home);
        loadFragment(new Home());


        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if(integerDeque.contains(id)){

                    if(id == R.id.home){

                        if(integerDeque.size() != 1){

                            if(flag){

                                integerDeque.addFirst(R.id.home);
                                flag = false;

                            }

                        }

                    }

                    integerDeque.remove(id);
                }

                integerDeque.push(id);
                loadFragment(getFragment(item.getItemId()));

                return true;
            }
        });

    }

    private Fragment getFragment(int itemId) {

        switch (itemId){

            case R.id.home:
                bottom_navigation.getMenu().getItem(0).setChecked(true);
                return new Home();

            case R.id.chat:
                bottom_navigation.getMenu().getItem(1).setChecked(true);
                return new Chat();

            case R.id.post_task:
                bottom_navigation.getMenu().getItem(2).setChecked(true);
                return new PostTask();

            case R.id.my_task:
                bottom_navigation.getMenu().getItem(3).setChecked(true);
                return new MyPosts();

            case R.id.profile:
                bottom_navigation.getMenu().getItem(4).setChecked(true);
                return new ProfileSettings();

        }

        bottom_navigation.getMenu().getItem(0).setChecked(true);
        return new Home();

    }

    private void loadFragment(Fragment home) {

        transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.home_frame_layout, home);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {

        integerDeque.pop();

        if(!integerDeque.isEmpty()){

            loadFragment(getFragment(integerDeque.peek()));

        }
        else {

            finish();
            
        }

    }


}