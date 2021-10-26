package com.AppValley.TaskMet.Home.Adopters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.AppValley.TaskMet.Home.HomeFragments.Chat;
import com.AppValley.TaskMet.Home.HomeFragments.Home;
import com.AppValley.TaskMet.Home.HomeFragments.MyPosts;
import com.AppValley.TaskMet.Home.HomeFragments.PostTask;
import com.AppValley.TaskMet.Home.HomeFragments.ProfileSettings;
import com.AppValley.TaskMet.PostPurchases.Screen6_PostNow;
import com.AppValley.TaskMet.PostPurchases.Screen5_PremiumPost;
import com.AppValley.TaskMet.Wallet.WalletScreen1_Home;
import com.AppValley.TaskMet.Wallet.Wallet_GC_Package;
import com.AppValley.TaskMet.Wallet.Wallet_RC_Package;

public class HomeAdopter extends FragmentPagerAdapter {


    public HomeAdopter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new Home();
                break;

            case 1:
                fragment = new Chat();
                break;

            case 2:
                fragment = new PostTask();
                break;

            case 3:
                fragment = new MyPosts();
                break;

            case 4:
                fragment = new ProfileSettings();
                break;


        }
        assert fragment != null;
        return fragment;

    }

    @Override
    public int getCount() {
        return 5;
    }
}