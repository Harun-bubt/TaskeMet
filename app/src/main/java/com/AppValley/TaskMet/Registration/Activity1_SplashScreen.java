package com.AppValley.TaskMet.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.AppValley.TaskMet.Home.HomeScreen;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.ProfileDataModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.WalletModel;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;
import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity1_SplashScreen extends AppCompatActivity implements DroidListener {

    final Context context = this;
    int recall_count = 0;

    public static SharedPreferences sharedPreferences;
    String userExist, TAG = "user_exist",number;
    TaskMetServer taskMetServer;

    private DroidNet netState;

    ImageView logo;
    SpinKitView progressBar;
    Button btn_check_network;
    RelativeLayout onLineLayout;
    LinearLayout circle, offLineLayout;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash_screen);

        DroidNet.init(this);

        netState = DroidNet.getInstance();
        netState.addInternetConnectivityListener(this);

        progressBar = findViewById(R.id.progress_bar);
        onLineLayout = findViewById(R.id.onLineLayout);
        offLineLayout = findViewById(R.id.offLineLayout);
        logo = findViewById(R.id.logo);
        circle = findViewById(R.id.circle);
        btn_check_network = findViewById(R.id.btn_check_network);

        progressBar.setVisibility(View.GONE);
        onLineLayout.setVisibility(View.GONE);
        offLineLayout.setVisibility(View.GONE);


        //-----------------Animation work
        Animation animFadein, scale_up;
        scale_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_down_logo);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_logo);
        circle.startAnimation(scale_up);
        logo.startAnimation(animFadein);
        //-------------------------------

        sharedPreferences = getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        number = sharedPreferences.getString(Constants.NUMBER, Constants.NULL);
        userExist = sharedPreferences.getString(Constants.USER_EXIST, "false");

    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {

        if (isConnected) {
            netIsOn();
        } else {
            netIsOff();
        }

    }

    private void netIsOn() {

        onLineLayout.setVisibility(View.VISIBLE);
        offLineLayout.setVisibility(View.GONE);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "User status: " + userExist);

                if (userExist.equals(Constants.TRUE)) {

                    progressBar.setVisibility(View.VISIBLE);

                    taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

                    recalling_api();

                } else {

                    Intent mainIntent = new Intent(Activity1_SplashScreen.this, Activity2_Registration.class);
                    Activity1_SplashScreen.this.startActivity(mainIntent);
                    finish();

                }

            }

        }, 1500);

    }

    private void recalling_api() {

        if(recall_count < 3){

            Call<ProfileDataModel> call = taskMetServer.getProfileData(number);
            call.enqueue(new Callback<ProfileDataModel>() {
                @Override
                public void onResponse(Call<ProfileDataModel> call, Response<ProfileDataModel> response) {

                    if (response.isSuccessful()) {
                        ProfileDataModel user_data = response.body();
                        if (user_data != null) {

                            RetrieveStoreUserInfo(user_data);

                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(context, HomeScreen.class));
                            finish();

                        }

                    }
                }

                @Override
                public void onFailure(Call<ProfileDataModel> call, Throwable t) {
                    recall_count = recall_count + 1;
                    recalling_api();
                }
            });

        }
        else{

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setContentText("We're facing some problem while loading your profile data. please check your internet connection & Load TaskMet app again.")
                    .showCancelButton(true)
                    .setCancelText("Exit")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .show();

        }


    }

    private void netIsOff() {

        offLineLayout.setVisibility(View.VISIBLE);
        onLineLayout.setVisibility(View.GONE);

        btn_check_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        netState.removeInternetConnectivityChangeListener(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners();
    }

    public void RetrieveStoreUserInfo(ProfileDataModel user_data) {

        SharedPreferences.Editor user_detail_editor;

        WalletModel walletModel = user_data.getWallet();

        user_detail_editor = sharedPreferences.edit();

        user_detail_editor.putString(Constants.USER_EXIST, Constants.TRUE);

        user_detail_editor.putString(Constants.ID, user_data.getId());
        user_detail_editor.putString(Constants.NUMBER, number);
        user_detail_editor.putString(Constants.UID, user_data.getUid());
        user_detail_editor.putString(Constants.NAME, user_data.getName());
        user_detail_editor.putString(Constants.EMAIL, user_data.getEmail());
        user_detail_editor.putString(Constants.EMAIL_VERIFIED, user_data.getEmail_verified());
        user_detail_editor.putString(Constants.PROFILE_PIC, user_data.getProfile_pic());
        user_detail_editor.putString(Constants.COUNTRY_NAME, user_data.getCountry_name());
        user_detail_editor.putString(Constants.CITY_NAME, user_data.getCity_name());
        user_detail_editor.putString(Constants.COUNTRY_CODE, user_data.getCountry_code());
        user_detail_editor.putString(Constants.ADDRESS, user_data.getAddress());
        user_detail_editor.putString(Constants.CURRENCY, user_data.getCurrency());
        user_detail_editor.putString(Constants.LATITUDE, user_data.getLatitude());
        user_detail_editor.putString(Constants.LONGITUDE, user_data.getLongitude());
        user_detail_editor.putString(Constants.TOKEN, user_data.getToken());

        user_detail_editor.putString(Constants.GC_BALANCE, walletModel.getGC());
        user_detail_editor.putString(Constants.RC_BALANCE, walletModel.getRC());
        user_detail_editor.putString(Constants.ACCOUNT_TYPE, walletModel.getAccount_type());
        user_detail_editor.apply();

    }

}