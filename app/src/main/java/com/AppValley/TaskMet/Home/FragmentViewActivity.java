package com.AppValley.TaskMet.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.AppValley.TaskMet.Chat_Module.Chat_Inbox;
import com.AppValley.TaskMet.Home.HomeFragments.HomeSearchResults;
import com.AppValley.TaskMet.Home.HomeFragments.ServiceDetails;
import com.AppValley.TaskMet.Home.HomeFragments.ShoppingDetails;
import com.AppValley.TaskMet.PostPurchases.Screen5_PremiumPost;
import com.AppValley.TaskMet.PostPurchases.Screen6_WaitingPostNow;
import com.AppValley.TaskMet.ProfileSettings.AboutUs;
import com.AppValley.TaskMet.ProfileSettings.EditProfile;
import com.AppValley.TaskMet.ProfileSettings.Settings;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.ViewAdActivities.ViewUsersAdDetails;
import com.AppValley.TaskMet.Wallet.WalletScreen1_Home;
import com.AppValley.TaskMet.constant.Constants;

public class FragmentViewActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    String Fragment_Name;
    final Context context = this;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment__frame__activty);

        frameLayout = findViewById(R.id.fragment_framLayout);

        //Support Fragment Manager
        fragmentManager = this.getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        SharedPreferences preferences = getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
        Fragment_Name = preferences.getString(Constants.FRAGMENT_NAME,"");


        switch (Fragment_Name) {

            case "WalletFragment":
                WalletScreen1_Home calling_fragment1 = new WalletScreen1_Home();
                Calling_Fragment(calling_fragment1);
                break;

            case "profileFragment":
                EditProfile calling_fragment2 = new EditProfile();
                Calling_Fragment(calling_fragment2);
                break;

            case "settingFragment":
                Settings calling_fragment3 = new Settings();
                Calling_Fragment(calling_fragment3);
                break;

            case "Screen5_PremiumPost":
                Screen5_PremiumPost screen5_PremiumPost = new Screen5_PremiumPost();
                Calling_Fragment(screen5_PremiumPost);
                break;

            case "Screen6_PostNow":
                Screen6_WaitingPostNow screen6_Waiting_PostNow = new Screen6_WaitingPostNow(context);
                Calling_Fragment(screen6_Waiting_PostNow);
                break;

            case "shopingDetails":
                ShoppingDetails shoppingDetails = new ShoppingDetails();
                Calling_Fragment(shoppingDetails);
                break;

            case "serviceDetails":
                ServiceDetails serviceDetails = new ServiceDetails();
                Calling_Fragment(serviceDetails);
                break;

            case "ChatInbox":
                Chat_Inbox chat_inbox = new Chat_Inbox();
                Calling_Fragment(chat_inbox);
                break;

            case "SearchResult":
                HomeSearchResults searchResults = new HomeSearchResults(preferences.getString(Constants.SEARCH_LINE,""));
                Calling_Fragment(searchResults);
                break;

            case "PostDetails":
                ViewUsersAdDetails adDetails = new ViewUsersAdDetails(preferences.getInt(Constants.ID,0));
                Calling_Fragment(adDetails);
                break;


                case "contact_us":
                AboutUs aboutUs = new AboutUs();
                Calling_Fragment(aboutUs);
                break;

        }

    }


    public void Calling_Fragment(Fragment calling_fragment) {

        transaction.replace(R.id.fragment_framLayout, calling_fragment);
        transaction.addToBackStack("tag");
        transaction.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

}