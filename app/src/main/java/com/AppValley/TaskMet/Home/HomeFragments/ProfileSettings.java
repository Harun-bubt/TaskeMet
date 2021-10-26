package com.AppValley.TaskMet.Home.HomeFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.AppValley.TaskMet.Home.FragmentViewActivity;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.Registration.Activity2_Registration;
import com.AppValley.TaskMet.constant.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.rizlee.rangeseekbar.RangeSeekBar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettings extends Fragment implements RangeSeekBar.OnRangeSeekBarPostListener {

    LinearLayout walletLayout,singoutLayout,profileLayout,settingsLayout,contact_us;
    CardView image_card;
    CircleImageView profileImageView;
    TextView nameTextView,my_number;
    String name,number;

    SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_settings, container, false);

        SharedPreferences preferences = getContext().getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);

        image_card = view.findViewById(R.id.image_card);
        walletLayout = view.findViewById(R.id.walletLayout);
        contact_us = view.findViewById(R.id.contact_us);
        singoutLayout = view.findViewById(R.id.layoutSignOut);
        profileLayout = view.findViewById(R.id.profileLayout);
        settingsLayout = view.findViewById(R.id.settingsLayout);
        profileImageView = view.findViewById(R.id.profileImageView);
        nameTextView = view.findViewById(R.id.nameTextView);
        my_number = view.findViewById(R.id.my_number);

        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        String profileImageLink = sharedPreferences.getString(Constants.PROFILE_PIC,"");
        name = sharedPreferences.getString(Constants.NAME,"");
        number = sharedPreferences.getString(Constants.NUMBER,"");


        image_card.setBackgroundResource(R.drawable.profile_card);
        nameTextView.setText(name);
        my_number.setText(number);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.logo_round)
                .error(R.drawable.logo_round);

        Glide
                .with(getContext())
                .load(profileImageLink)
                .apply(options)
                .into(profileImageView);

        walletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FragmentViewActivity.class);

                SharedPreferences.Editor direction = preferences.edit();
                direction.putString(Constants.FRAGMENT_NAME, "WalletFragment");
                direction.apply();

                startActivity(intent);

            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), FragmentViewActivity.class);

                SharedPreferences.Editor direction = preferences.edit();
                direction.putString(Constants.FRAGMENT_NAME, "profileFragment");
                direction.apply();

                startActivity(intent);

            }
        });


        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), FragmentViewActivity.class);

                SharedPreferences.Editor direction = preferences.edit();
                direction.putString(Constants.FRAGMENT_NAME, "contact_us");
                direction.apply();

                startActivity(intent);

            }
        });

        settingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FragmentViewActivity.class);

                SharedPreferences.Editor direction = preferences.edit();
                direction.putString(Constants.FRAGMENT_NAME, "settingFragment");
                direction.apply();

                startActivity(intent);

            }
        });

        singoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.putString(Constants.USER_EXIST, Constants.FALSE);
                editor.apply();

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), Activity2_Registration.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        return view;

    }


    @Override
    public void onValuesChanged(float v, float v1) {

    }

    @Override
    public void onValuesChanged(int i, int i1) {

        //Set Text View Values here
        Toast.makeText(getContext(), "Min: "+String.valueOf(i)+"    Max: "+String.valueOf(i1), Toast.LENGTH_SHORT).show();

    }

}