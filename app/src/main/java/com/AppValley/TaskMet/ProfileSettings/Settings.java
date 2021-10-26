package com.AppValley.TaskMet.ProfileSettings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.Registration.Activity2_Registration;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.Status_Response;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Settings extends Fragment {

    View view;

    LinearLayout privacyPolicyLayout, termsConditionLayout, deleteLayout;
    TaskMetServer taskMetServer;
    String number;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        privacyPolicyLayout = view.findViewById(R.id.privacyPolicyLayout);
        termsConditionLayout = view.findViewById(R.id.termsConditionsLayout);
        deleteLayout = view.findViewById(R.id.deleteMyAccountLayout);
        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);
        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        number = sharedPreferences.getString(Constants.NUMBER, Constants.NULL);

        privacyPolicyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://thetaskmet.com/privacy-policy")));
            }
        });
        termsConditionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://thetaskmet.com/terms-condition")));
            }
        });
        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setContentText("Are you sure you want to delete this account? On press confirm you also loose the RC and GC of this account")
                        .setConfirmText(Constants.YES)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                Thread thread3 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Call<Status_Response> call = taskMetServer.deleteAccount(number);
                                        call.enqueue(new Callback<Status_Response>() {
                                            @Override
                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {
                                                if (response.isSuccessful()) {
                                                    Status_Response message = response.body();
                                                    String msg = message.getStatus();
                                                    if (msg.equals(Constants.TRUE)) {
                                                        Toast.makeText(getContext(), "Api is called", Toast.LENGTH_SHORT).show();
                                                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                                            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                                            FirebaseAuth.getInstance().getCurrentUser().delete();

                                                            editor.clear().apply();
                                                            startActivity(new Intent(getActivity(),Activity2_Registration.class));
                                                            getActivity().finish();
                                                        }

                                                    }

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Status_Response> call, Throwable t) {

                                            }
                                        });


                                    }
                                });
                                thread3.start();

                            }
                        })
                        .showCancelButton(true)
                        .setCancelText("Cancel")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();


            }
        });

        onBackPressed(view);


        return view;
    }

    public void onBackPressed(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });

    }
}