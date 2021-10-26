package com.AppValley.TaskMet.Registration;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.AppValley.TaskMet.Chat_Module.User;
import com.AppValley.TaskMet.Home.HomeFragments.MyPosts;
import com.AppValley.TaskMet.Home.HomeScreen;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;
import com.airbnb.lottie.LottieAnimationView;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.Status_Response;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment2_EnterNumber extends Fragment {

    public String number;
    EditText editText;
    Context context;
    Button button;
    String countryCode,fullNumber;
    Boolean numberIsCorrect = false;

    TaskMetServer taskMetServer;
    public static SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.enter_number_fragment, container, false);
        context = getContext();

        SweetAlertDialog progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progress.setTitleText("Checking number");
        progress.setCancelable(false);


        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        //------------------------------- Storing Data & response ----------------------------------
        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences.edit();
        //------------------------------------------------------------------------------------------

        editText = view.findViewById(R.id.phoneEditText);
        button = view.findViewById(R.id.continueButton);
        CountryCodePicker countryCodePicker = view.findViewById(R.id.ccp);

        countryCode = countryCodePicker.getSelectedCountryCode();
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = countryCodePicker.getSelectedCountryCode();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = editText.getText().toString().trim();

                if (TextUtils.isEmpty(number)) {
                    editText.setError("Please enter number");
                    editText.requestFocus();
                    return;
                }

                hideSoftKeyboard(getActivity());

                fullNumber = "+" + countryCode + number;

                String message = "We are about to send 6 digit code on this " +fullNumber+ " number. Please confirm your number is correct";

                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Confirm Phone Number")
                        .setContentText(message)
                        .setConfirmText(Constants.YES)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                numberIsCorrect = true;

                                progress.show();

                                Thread thread = new Thread(new Runnable(){
                                    @Override
                                    public void run(){

                                        Call<Status_Response> call = taskMetServer.getUserStatus(fullNumber);
                                        call.enqueue(new Callback<Status_Response>() {
                                            @Override
                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                                                if (response.isSuccessful()) {


                                                    Status_Response status_response = response.body();

                                                    String user_exist = status_response.getStatus();

                                                    if (!TextUtils.isEmpty(user_exist) && user_exist.equals("true")) {

                                                        progress.dismissWithAnimation();

                                                        Log.d("myResponse", "onResponse: "+user_exist+". Number: "+fullNumber);
                                                        editor1.putString(Constants.USER_STATUS, user_exist);
                                                        editor1.putString(Constants.NUMBER, fullNumber);
                                                        editor1.apply();

                                                        nextScreen();

                                                    } else if (!TextUtils.isEmpty(user_exist) && user_exist.equals("false")) {

                                                        progress.dismissWithAnimation();
                                                        Log.d("myResponse", "onResponse: "+user_exist+". Number: "+fullNumber);
                                                        editor1.putString(Constants.USER_STATUS, user_exist);
                                                        editor1.putString(Constants.NUMBER, fullNumber);
                                                        editor1.apply();

                                                        nextScreen();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Status_Response> call, Throwable t) {
                                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                                progress.dismissWithAnimation();
                                            }
                                        });

                                    }
                                });
                                thread.start();

                            }
                        })
                        .showCancelButton(true)
                        .setCancelText(Constants.EDIT)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            }
        });


        return view;

    }

    private void nextScreen() {

        // Segment for call next fragment
        Fragment3_OtpVerification fragment3OtpVerification = new Fragment3_OtpVerification();
        androidx.fragment.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.framelayout, fragment3OtpVerification);
        transaction.addToBackStack("tag");
        transaction.commit();

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}


