package com.AppValley.TaskMet.PostTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.constant.Constants;
import com.krishna.securetimer.SecureTimer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Screen1_Shop extends Fragment implements View.OnClickListener {

    public SharedPreferences selectingMainCategory;
    SharedPreferences.Editor selectingMainCategoryEditor;

    String library_date,device_date;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shop, container, false);
        
        selectingMainCategory = getContext().getSharedPreferences(Constants.SELECTING_CATEGORY_SERVICE_SHOP, Context.MODE_PRIVATE);
        selectingMainCategoryEditor = selectingMainCategory.edit();

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

        CardView mobile  = view.findViewById(R.id.shop_mobile);
        mobile.setOnClickListener(this);
        CardView computer = view.findViewById(R.id.shop_computer);
        computer.setOnClickListener(this);
        CardView vehicle = view.findViewById(R.id.shop_vehicle);
        vehicle.setOnClickListener(this);
        CardView furniture = view.findViewById(R.id.shop_furniture);
        furniture.setOnClickListener(this);
        CardView appliances = view.findViewById(R.id.shop_appliances);
        appliances.setOnClickListener(this);
        CardView bikes = view.findViewById(R.id.shop_Bikes);
        bikes.setOnClickListener(this);
        CardView sports = view.findViewById(R.id.shop_sports);
        sports.setOnClickListener(this);
        CardView pets_animals = view.findViewById(R.id.shop_Pets_and_animals);
        pets_animals.setOnClickListener(this);
        CardView fashion = view.findViewById(R.id.shop_Fashion);
        fashion.setOnClickListener(this);
        CardView kids = view.findViewById(R.id.shop_Kids);
        kids.setOnClickListener(this);
        CardView books = view.findViewById(R.id.shop_Books);
        books.setOnClickListener(this);

        onClick(view);

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

        switch (v.getId()) {
            case R.id.shop_mobile:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.mobile));
                Toast.makeText(getContext(), "Click Mobile", Toast.LENGTH_SHORT).show();
                gotoPostActivity();
                break;
            case R.id.shop_computer:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.computer));
                gotoPostActivity();
                break;
            case R.id.shop_vehicle:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.vehicle));
                gotoPostActivity();
                break;
            case R.id.shop_furniture:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.furniture));
                gotoPostActivity();
                break;
            case R.id.shop_appliances:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.appliances));
                gotoPostActivity();
                break;
            case R.id.shop_Bikes:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.bike));
                gotoPostActivity();
                break;
            case R.id.shop_sports:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.sports));
                gotoPostActivity();
                break;
            case R.id.shop_Pets_and_animals:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.pets_animals));
                gotoPostActivity();
                break;
            case R.id.shop_Fashion:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.fashion));
                gotoPostActivity();
                break;
            case R.id.shop_Kids:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.kids));
                gotoPostActivity();
                break;
            case R.id.shop_Books:
                selectingMainCategoryEditor.putString(Constants.MAIN_CATEGORY,getResources().getString(R.string.books));
                gotoPostActivity();
                break;

        }
        selectingMainCategoryEditor.putString(Constants.SELECTING_TYPE,Constants.SHOP_TYPE);
        selectingMainCategoryEditor.apply();

    }
    public void gotoPostActivity() {
        Intent intent = new Intent(getActivity(), PostTaskActivity.class);
        intent.putExtra("shop",true);
        startActivity(intent);
    }
}
