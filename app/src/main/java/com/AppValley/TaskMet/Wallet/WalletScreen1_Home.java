package com.AppValley.TaskMet.Wallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.AppValley.TaskMet.PostTask.Screen4_PostNow;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.Registration.Activity2_Registration;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PackagesModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.ProfileDataModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserShoppingAds;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.WalletModel;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WalletScreen1_Home extends Fragment {

    ImageView history;
    String number,currency;
    LinearLayout layoutBuyGc,layoutBuyRc,layoutPremium,allViews;

    WalletModel walletModel;
    PackagesModel gc_packages,rc_packages;

    View line_color;
    SweetAlertDialog progress;
    FragmentTransaction transaction;

    ImageView icon_account,icon_gc,icon_rc;
    TextView accountTypeTextView,gcBalanceTextView,rcBalanceTextView,
            premiumAdLimitsTextview,NormalAdLimitsTextview;

    View bottomSheet;
    FragmentManager fragmentManager;


    TaskMetServer taskMetServer;
    public static SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.walletscreen_home_1, container, false);

        //Support Fragment Manager
        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

        progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progress.setTitleText("Please wait");
        progress.show();

        //-------------------------- Getting Profile Data ---------------------

        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        number = sharedPreferences.getString(Constants.NUMBER, Constants.NULL);
        currency = sharedPreferences.getString(Constants.CURRENCY, Constants.NULL);
        //.....................................................................

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        initializeAllViews(view);

        layoutBuyGc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToGcFragment(gc_packages,currency);

            }
        });

        layoutBuyRc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRcFragment(rc_packages,currency);
            }
        });

        layoutPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buyPremiumAccountFragment();

            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WalletHistory();

            }
        });

        icon_gc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGcFragment(gc_packages,currency);
            }
        });

        icon_rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRcFragment(rc_packages,currency);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                Call<WalletModel> call = taskMetServer.getWalletData(number);
                call.enqueue(new Callback<WalletModel>() {
                    @Override
                    public void onResponse(Call<WalletModel> call, Response<WalletModel> response) {

                        if(response.isSuccessful()){

                            allViews.setVisibility(View.VISIBLE);

                            progress.dismissWithAnimation();

                            walletModel = response.body();

                            setAllAccountInfo(walletModel);

                            //for gc packages
                            gc_packages = walletModel.getGcPackages();

                            //for rc packages
                            rc_packages = walletModel.getGcPackages();

                        }

                    }

                    @Override
                    public void onFailure(Call<WalletModel> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        thread.start();

    }

    public void buyPremiumAccountFragment() {

        Get_Premium_Account wallet_gc_package = new Get_Premium_Account();
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_framLayout, wallet_gc_package);
        transaction.addToBackStack("tag");
        transaction.commit();

    }

    public void goToGcFragment(PackagesModel gc_packages,String currency) {

        Wallet_GC_Package wallet_gc_package = new Wallet_GC_Package(gc_packages,currency);
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_framLayout, wallet_gc_package);
        transaction.addToBackStack("tag");
        transaction.commit();

    }

    public void goToRcFragment(PackagesModel rc_packages,String currency) {

        Wallet_RC_Package wallet_rc_package = new Wallet_RC_Package(rc_packages,currency);
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_framLayout, wallet_rc_package);
        transaction.addToBackStack("tag");
        transaction.commit();

    }

    public void WalletHistory() {

        Wallet_History wallet_history = new Wallet_History();
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_framLayout, wallet_history);
        transaction.addToBackStack("tag");
        transaction.commit();

    }

    public void initializeAllViews(View view) {

        allViews = view.findViewById(R.id.allViews);
        history = view.findViewById(R.id.history);

        icon_gc = view.findViewById(R.id.icon_gc);
        icon_rc = view.findViewById(R.id.icon_rc);
        line_color = view.findViewById(R.id.line_color);
        bottomSheet = view.findViewById(R.id.bottom_sheet);
        icon_account = view.findViewById(R.id.icon_account);

        layoutPremium = view.findViewById(R.id.layoutPremium);
        layoutBuyGc = view.findViewById(R.id.layoutBuyGc);
        layoutBuyRc = view.findViewById(R.id.layoutBuyRc);
        accountTypeTextView = view.findViewById(R.id.accountTypeTextView);
        gcBalanceTextView = view.findViewById(R.id.gcBalanceTextView);
        rcBalanceTextView = view.findViewById(R.id.rcBalanceTextView);
        premiumAdLimitsTextview = view.findViewById(R.id.premiumAdLimitsTextview);
        NormalAdLimitsTextview = view.findViewById(R.id.NormalAdLimitsTextview);

    }

    public void setAllAccountInfo( WalletModel walletModel)
    {
        if (walletModel.getAccount_type().equals("PREMIUM")) {

            icon_account.setImageResource(R.drawable.icon_account_premium);
            layoutPremium.setVisibility(View.GONE);
            line_color.setBackgroundColor(getResources().getColor(R.color.yellow));
        }

        else {

            icon_account.setImageResource(R.drawable.icon_account_normal);
            line_color.setBackgroundColor(getResources().getColor(R.color.Theme1));
            layoutPremium.setVisibility(View.VISIBLE);
        }

        accountTypeTextView.setText(walletModel.getAccount_type()+ " ACCOUNT");

        gcBalanceTextView.setText("GC : "+walletModel.getGC());
        rcBalanceTextView.setText("RC : "+walletModel.getRC());
        premiumAdLimitsTextview.setText("Premium : "+walletModel.getFreePremiumAds());
        NormalAdLimitsTextview.setText("Normal : "+walletModel.getFreeAds());

    }


}