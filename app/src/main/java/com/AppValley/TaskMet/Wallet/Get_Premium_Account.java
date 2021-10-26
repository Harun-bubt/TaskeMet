package com.AppValley.TaskMet.Wallet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PremiumAcModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.ProfileDataModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserShoppingAds;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Get_Premium_Account extends Fragment {

    View view;
    Button btn_get_account;
    TextView tv_gc_balance, tv_rc_balance;
    String number, gc_balance, rc_balance, status, error_msg;

    TaskMetServer taskMetServer;
    SharedPreferences sharedPreferences;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.get_premium_account, container, false);

        getData();
        backButtonControl();

        tv_gc_balance.setText(gc_balance);
        tv_rc_balance.setText(rc_balance);

        btn_get_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        Call<PremiumAcModel> call = taskMetServer.getPremiumAccount(number);
                        call.enqueue(new Callback<PremiumAcModel>() {
                            @Override
                            public void onResponse(Call<PremiumAcModel> call, Response<PremiumAcModel> response) {

                                if (response.isSuccessful()) {

                                    PremiumAcModel acModel = response.body();

                                    status = acModel.getStatus();
                                    error_msg = acModel.getError();

                                    if (!TextUtils.isEmpty(error_msg) && error_msg.equals("Your Account is already premium")) {

                                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                .setContentText("Sorry! you already have a premium account.")
                                                .setConfirmText("OK")
                                                .show();

                                    } else if (!TextUtils.isEmpty(error_msg) && error_msg.equals("Low GC")) {

                                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                .setContentText("Sorry! you don't have enough GC balance to purchase Premium Account.")
                                                .setConfirmText("OK")
                                                .show();

                                    } else {

                                        if (status.equals("true")) {

                                            new SweetAlertDialog(getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                                    .setCustomImage(R.drawable.icon_account_premium)
                                                    .setTitleText("Congratulation")
                                                    .setContentText("Your TaskMet account is successfully updated to Premium Account.")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                                                            fragmentManager.popBackStack();
                                                            sweetAlertDialog.dismissWithAnimation();

                                                        }
                                                    })
                                                    .show();


                                        }

                                    }

                                }

                            }

                            @Override
                            public void onFailure(Call<PremiumAcModel> call, Throwable t) {

                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("Sorry! we are facing some technical problem while updating your account status. Please try again.")
                                        .setConfirmText("OK")
                                        .show();


                            }
                        });


                    }
                });
                thread.start();

            }
        });

        return view;
    }

    private void getData() {

        //Support Fragment Manager
        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

        tv_gc_balance = view.findViewById(R.id.tv_gc_balance);
        tv_rc_balance = view.findViewById(R.id.tv_rc_balance);
        btn_get_account = view.findViewById(R.id.btn_get_account);

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        number = sharedPreferences.getString(Constants.NUMBER, "");
        gc_balance = sharedPreferences.getString(Constants.GC_BALANCE, "");
        rc_balance = sharedPreferences.getString(Constants.RC_BALANCE, "");

    }

    public void backButtonControl() {

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    fragmentManager.popBackStackImmediate();
                    return true;
                }
                return false;
            }
        });

    }
}