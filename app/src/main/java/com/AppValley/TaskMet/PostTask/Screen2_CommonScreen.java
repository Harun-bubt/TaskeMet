package com.AppValley.TaskMet.PostTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.AppValley.TaskMet.Home.HomeScreen;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.constant.Constants;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Screen2_CommonScreen extends Fragment {

    EditText editTextServiceTitle,editTextServieCost,editTextdescribe_service;
    ImageView iconImageJobTitle,iconImageDescribeService;
    Button btnNext;
    ImageView backArrow;
    TextView currencyTextView;


    String serviceTitle;
    String serviceCost;
    String serviceDescription;

    public  static SharedPreferences getSharedPreferencesEditPost;
    public  static SharedPreferences sharedPreferences;
    SharedPreferences.Editor service_CommonCategory_editor;

    SharedPreferences profileSharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_common, container, false);
        onBackPressed(view);

        editTextServiceTitle = view.findViewById(R.id.editTextJobTitle_CommonScreen);
        currencyTextView = view.findViewById(R.id.currencyText);
        editTextServieCost = view.findViewById(R.id.serviceCost);
        editTextdescribe_service = view.findViewById(R.id.describeService_commonScreen);
        iconImageJobTitle = view.findViewById(R.id.iconImageJobTitle_CommonScreen);
        iconImageDescribeService = view.findViewById(R.id.iconImageJobDescribtion_CommonScreen);
        backArrow = view.findViewById(R.id.backArrowImageView);
        btnNext= view.findViewById(R.id.btnNext);

        iconAppearDisAppear(editTextServiceTitle,iconImageJobTitle);
        iconAppearDisAppear(editTextdescribe_service,iconImageDescribeService);

        setAlertDialogForIcon(iconImageJobTitle,"Write a suitable title for your service");
        setAlertDialogForIcon(iconImageDescribeService,"Describe your service details");



        //........................Shared preferences..........................
        sharedPreferences = getContext().getSharedPreferences(Constants.SERVICE_COMMON_NEW_POST, Context.MODE_PRIVATE);
        service_CommonCategory_editor = sharedPreferences.edit();
        //........................Shared preferences..........................

        //...............Get profile data && Set Currency in cost...............................
        profileSharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        String Currency = profileSharedPreferences.getString(Constants.CURRENCY,"");
        currencyTextView.setText(Currency);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                serviceTitle = editTextServiceTitle.getText().toString();
                if(serviceTitle.equals(""))
                {
                    editTextServiceTitle.setError("This field is required");
                    editTextServiceTitle.requestFocus();
                    return ;
                }
                serviceCost = editTextServieCost.getText().toString();
                if(serviceCost.equals(""))
                {
                    editTextServieCost.setError("This field is required");
                    editTextServieCost.requestFocus();
                    return ;
                }
                else
                {
                    if(Integer.parseInt(editTextServieCost.getText().toString()) < 1500)
                    {
                        editTextServieCost.setError("Should be greater than 1500");
                        editTextServieCost.requestFocus();
                        return ;
                    }
                    if(Integer.parseInt(editTextServieCost.getText().toString()) > 250000)
                    {
                        editTextServieCost.setError("Should be less than 250000");
                        editTextServieCost.requestFocus();
                        return ;
                    }
                }
                serviceDescription = editTextdescribe_service.getText().toString();
                if(serviceDescription.equals(""))
                {
                    editTextdescribe_service.setError("This field is required");
                    editTextdescribe_service.requestFocus();
                    return ;
                }


                storeAllInfor();
                goToNext();
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        return view;
    }
    public void iconAppearDisAppear(EditText editText,ImageView imageView){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText.getText().toString().equals("")){
                    imageView.setVisibility(View.VISIBLE);
                }else
                {
                    imageView.setVisibility(View.GONE);
                }
            }
        });
    }
    public void storeAllInfor(){


        service_CommonCategory_editor.putString(Constants.SERVICE_COMMON_POST_TITLE,serviceTitle);
        service_CommonCategory_editor.putString(Constants.SERVICE_COMMON_COST,serviceCost);
        service_CommonCategory_editor.putString(Constants.SERVICE_COMMON_DESCRIPTION,serviceDescription);
        service_CommonCategory_editor.apply();
    }
    public void onBackPressed(View view){
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {


            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        } );

    }
    public void goToNext() {
        Screen3_PostPhoto screen3_postPhoto = new Screen3_PostPhoto();
        androidx.fragment.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.framelayout_post_task, screen3_postPhoto);
        transaction.addToBackStack("tag");
        transaction.commit();
    }
    public void setAlertDialogForIcon(ImageView imageView, String contentText) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setContentText(contentText)
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });
    }


}
