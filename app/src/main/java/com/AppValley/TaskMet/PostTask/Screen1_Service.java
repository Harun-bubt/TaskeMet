package com.AppValley.TaskMet.PostTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.constant.Constants;
import com.krishna.securetimer.SecureTimer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Screen1_Service extends Fragment implements View.OnClickListener {

    public  static  SharedPreferences selectingMainCategory;
    SharedPreferences.Editor selectingMainCategoryEditor;

    String library_date,device_date;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_service, container, false);


        //........................Shared preferences..........................
        selectingMainCategory = getContext().getSharedPreferences(Constants.SELECTING_CATEGORY_SERVICE_SHOP, Context.MODE_PRIVATE);
        //....................................................................


        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();


        //-------------------------- Getting Current Date ---------------------------
        SecureTimer.with(getContext()).initialize();

        Date date = SecureTimer.with(getActivity()).getCurrentDate();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        library_date = date_format.format(date);

        Calendar calender = Calendar.getInstance();
        device_date =  date_format.format(calender.getTime());

        //---------------------------------------------------------------------------

        androidx.cardview.widget.CardView job = view.findViewById(R.id.serv_jobs);
        job.setOnClickListener(this);
        androidx.cardview.widget.CardView travel = view.findViewById(R.id.serv_travel);
        travel.setOnClickListener(this);
        androidx.cardview.widget.CardView education = view.findViewById(R.id.serv_education);
        education.setOnClickListener(this);
        androidx.cardview.widget.CardView property = view.findViewById(R.id.serv_property);
        property.setOnClickListener(this);
        androidx.cardview.widget.CardView marriage = view.findViewById(R.id.serv_Marriage);
        marriage.setOnClickListener(this);
        androidx.cardview.widget.CardView development = view.findViewById(R.id.serv_development);
        development.setOnClickListener(this);
        androidx.cardview.widget.CardView electronics_repair = view.findViewById(R.id.serv_ElectronicsRepair);
        electronics_repair.setOnClickListener(this);
        androidx.cardview.widget.CardView home_delivery = view.findViewById(R.id.serv_Home_Delivery);
        home_delivery.setOnClickListener(this);
        androidx.cardview.widget.CardView car_rental = view.findViewById(R.id.serv_Carrental);
        car_rental.setOnClickListener(this);
        androidx.cardview.widget.CardView events = view.findViewById(R.id.serv_Events);
        events.setOnClickListener(this);
        androidx.cardview.widget.CardView health = view.findViewById(R.id.serv_health);
        health.setOnClickListener(this);
        androidx.cardview.widget.CardView catering = view.findViewById(R.id.serv_Catering);
        catering.setOnClickListener(this);
        androidx.cardview.widget.CardView house_Office_renovation = view.findViewById(R.id.serv_House_Office_Renovation);
        house_Office_renovation.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(!library_date.equals(device_date)){

            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Incorrect Date")
                    .setContentText("Please correct your device date before posting a new Ad.")
                    .setConfirmText("Edit Date")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            sDialog.dismissWithAnimation();
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);

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

            return;

        }

        selectingMainCategoryEditor = selectingMainCategory.edit();
        int check = 0;
        switch (v.getId()) {
            case R.id.serv_jobs:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.jobs));
                selectingMainCategoryEditor.putString(Constants.SELECTING_TYPE,Constants.JOB_TYPE);
                check = 0;
                gotoJobFragment();
                break;
            case R.id.serv_travel:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.travel));
                check = 1;
                gotoPostFragment();
                break;
            case R.id.serv_education:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.education));
                check = 1;
                gotoPostFragment();
                break;
            case R.id.serv_property:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.property));
                selectingMainCategoryEditor.putString(Constants.SELECTING_TYPE,Constants.PROPERTY_TYPE_SELECTING);
                gotoPropertyFragment();
                break;
            case R.id.serv_Marriage:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.marriage));
                selectingMainCategoryEditor.putString(Constants.SELECTING_TYPE,Constants.MARRIAGE_TYPE);
                check = 0;
                gotoMariageFragment();
                break;
            case R.id.serv_development:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.development));
                check = 1;
                gotoPostFragment();
                break;
            case R.id.serv_ElectronicsRepair:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.electronics));
                check = 1;
                gotoPostFragment();
                break;
            case R.id.serv_Home_Delivery:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.home_delivery));
                check = 1;
                gotoPostFragment();
                break;
            case R.id.serv_Carrental:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.car_rental));
                check = 1;
                gotoPostFragment();
                break;
            case R.id.serv_Events:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.events));
                check = 1;
                gotoPostFragment();
                break;
            case R.id.serv_health:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.health));
                check = 1;
                gotoPostFragment();
                break;
            case R.id.serv_Catering:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.catering));
                check = 1;
                gotoPostFragment();
                break;
            case R.id.serv_House_Office_Renovation:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.house_office_renovation));
                check = 1;
                gotoPostFragment();
                break;
        }
        if(check == 1)
        {
            selectingMainCategoryEditor.putString(Constants.SELECTING_TYPE,Constants.COMMON_TYPE);
        }
        selectingMainCategoryEditor.apply();



    }
    public void gotoPostFragment() {
        Intent intent = new Intent(getActivity(), PostTaskActivity.class);
        intent.putExtra("service",true);
        startActivity(intent);
    }
    public void gotoJobFragment() {
        Intent intent = new Intent(getActivity(), PostTaskActivity.class);
        intent.putExtra("job",true);
        startActivity(intent);
    }
    public void gotoMariageFragment() {
        Intent intent = new Intent(getActivity(), PostTaskActivity.class);
        intent.putExtra("mariage",true);
        startActivity(intent);
    }
    public void gotoPropertyFragment() {
        Intent intent = new Intent(getActivity(), PostTaskActivity.class);
        intent.putExtra("property",true);
        startActivity(intent);
    }



}