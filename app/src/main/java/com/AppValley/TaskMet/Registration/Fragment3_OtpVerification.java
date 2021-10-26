package com.AppValley.TaskMet.Registration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.AppValley.TaskMet.Home.HomeScreen;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.ProfileDataModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.WalletModel;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.samanjafari.easycountdowntimer.CountDownInterface;
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ticker.views.com.ticker.widgets.circular.timer.callbacks.CircularViewCallback;
import ticker.views.com.ticker.widgets.circular.timer.view.CircularView;

import static android.content.ContentValues.TAG;

public class Fragment3_OtpVerification extends Fragment {

    Context context;
    String number, user_status;
    private FirebaseAuth mAuth;
    com.chaos.view.PinView pinView;
    private String mVerificationId;
    EasyCountDownTextview countDown;
    TextView textViewShowPhoneNumber,resend_code;
    Button button;

    TaskMetServer taskMetServer;
    public static SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.otp_fragment, container, false);

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        //------------------------------- Storing Data & response ----------------------------------
        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        user_status = sharedPreferences.getString(Constants.USER_STATUS, Constants.NULL);
        number = sharedPreferences.getString(Constants.NUMBER, Constants.NULL);
        Log.d("user_status", "user status: " + user_status + ". Number: " + number);
        //------------------------------------------------------------------------------------------

        context = getContext();
        pinView = view.findViewById(R.id.pinViewForVerificationCode);
        resend_code = view.findViewById(R.id.resend_code);
        button = view.findViewById(R.id.verifyButton);
        textViewShowPhoneNumber = view.findViewById(R.id.phoneNumberShowForOtpTextView);
        textViewShowPhoneNumber.setText(number);

        //------------------------------------- Timer ----------------------------------------------

        countDown = view.findViewById(R.id.easyCountDownTextview);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.proxima_nova_regular);
        countDown.setTypeFace(typeface);

        countDown.setOnTick(new CountDownInterface() {
            @Override
            public void onTick(long time) {

                Log.d("user_status", "Time: " + time);

                if(time!=0 && (time/1000)==1){
                    sendVerificationCode(number);
                    countDown.setVisibility(View.GONE);
                    resend_code.setText("Code is sent again");
                }

            }

            @Override
            public void onFinish() {

            }

        });

        //------------------------------------------------------------------------------------------

        mAuth = FirebaseAuth.getInstance();
        sendVerificationCode(number);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button.setEnabled(false);

                String code = pinView.getText().toString();
                if (code.isEmpty() || code.length() < 6) {
                    pinView.setError("Enter valid code");
                    pinView.requestFocus();
                    button.setEnabled(true);
                    return;
                }

                verifyVerificationCode(code);

            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // TextView textView = getView().findViewById(R.id.register_textView);

    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mobile)       // Phone number to verify
                        .setTimeout(55L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                pinView.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            Toast.makeText(context, "sms sent", Toast.LENGTH_SHORT).show();

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            UpdateToken();

                            countDown.pause();

                            if (user_status.equals(Constants.TRUE)) {

                                SweetAlertDialog my_progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                                my_progress.setTitleText("Loading Profile Data");
                                my_progress.show();

                                Call<ProfileDataModel> call = taskMetServer.getProfileData(number);
                                call.enqueue(new Callback<ProfileDataModel>() {
                                    @Override
                                    public void onResponse(Call<ProfileDataModel> call, Response<ProfileDataModel> response) {

                                        if (response.isSuccessful()) {

                                            ProfileDataModel user_data = response.body();

                                            if (user_data != null) {

                                                RetrieveStoreUserInfo(user_data);

                                                my_progress.dismissWithAnimation();
                                                Intent intent = new Intent(getActivity(), HomeScreen.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                getActivity().finish();

                                            }

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ProfileDataModel> call, Throwable t) {

                                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                .setContentText("You are logged in successfully, but we are facing difficulty while loading your profile data. Please restart TaskMet app.")
                                                .setConfirmText("OK")
                                                .show();

                                        SharedPreferences.Editor user_detail_editor;
                                        user_detail_editor = sharedPreferences.edit();
                                        user_detail_editor.putString(Constants.USER_EXIST, Constants.TRUE);
                                        user_detail_editor.putString(Constants.NUMBER, number);
                                        user_detail_editor.apply();

                                    }

                                });



                            }
                            else if (user_status.equals(Constants.FALSE)) {

                                Intent intent = new Intent(getActivity(), Activity3_SignUp.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }

                        }
                        else {
                            button.setEnabled(true);
                            Toast.makeText(context, "Please enter correct OTP code", Toast.LENGTH_LONG).show();
                        }

                    }

                });
    }

    private void UpdateToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = token.toString();
                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(msg);

                    }
                });

    }

    public void RetrieveStoreUserInfo(ProfileDataModel user_data) {

        SharedPreferences.Editor user_detail_editor;

        WalletModel walletModel = user_data.getWallet();

        user_detail_editor = sharedPreferences.edit();

        user_detail_editor.putString(Constants.USER_EXIST, Constants.TRUE);
        user_detail_editor.putString(Constants.NUMBER, number);

        user_detail_editor.putString(Constants.ID, user_data.getId());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDown.pause();
    }

}


