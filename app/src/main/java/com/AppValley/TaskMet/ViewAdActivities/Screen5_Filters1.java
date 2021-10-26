package com.AppValley.TaskMet.ViewAdActivities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.constant.Constants;
import com.rizlee.rangeseekbar.RangeSeekBar;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

public class Screen5_Filters1 extends Fragment implements RangeSeekBar.OnRangeSeekBarPostListener {

    View view;
    TextView minCost, maxCost,minArea,maxArea;
    Button btnRentSell,rentButton,sellButton,btnClear,btnApply,
            btnAllAreas, btnAgricultural, btnCom, btnResidential,
            btnYes,btnNo;

    public static String costFrom, costTo,areaFrom,areaTo,distance,furnished;
    public static String propertyPostType ;
    public static String areaType;
    public static int button_id ;

    FragmentManager fragmentManager;
    IndicatorSeekBar distanceSeekbar;
    RangeSeekBar costSeekbar, areaSeekbar;
    public static boolean isFilterSelected = false;
    LinearLayout furnishedLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_property_ad_filter1, container, false);

        initializeView();

        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

        if(Screen5_UserPropertyAds.isFilterSelectedPrevious) {

            if(propertyPostType.equals(getString(R.string.rent))) {

                controlRent();

            }
            else if(propertyPostType.equals(getString(R.string.sell))) {

                controlSell();

            }
            else {

                controlAll();

            }

            controlSeekbar(costFrom,costTo,areaFrom,areaTo,distance);
            controlAreaType(button_id);

        }
        else {

            setByDefault();

        }

        ((Screen5_Property_Ads_Activity)getActivity()).changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adListFragment();
            }
        });

        costSeekbar.setListenerPost(new RangeSeekBar.OnRangeSeekBarPostListener() {
            @Override
            public void onValuesChanged(float v, float v1) {

            }

            @Override
            public void onValuesChanged(int i, int i1) {
                minCost.setText(String.valueOf(i));
                maxCost.setText(String.valueOf(i1));
                costFrom = String.valueOf(i);
                costTo = String.valueOf(i1);
            }
        });
        areaSeekbar.setListenerPost(new RangeSeekBar.OnRangeSeekBarPostListener() {
            @Override
            public void onValuesChanged(float v, float v1) {

            }

            @Override
            public void onValuesChanged(int i, int i1) {
                minArea.setText(String.valueOf(i));
                maxArea.setText(String.valueOf(i1));
                areaFrom = String.valueOf(i);
                areaTo = String.valueOf(i1);
            }
        });
        distanceSeekbar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                distance = Integer.toString(seekParams.progress);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });


        btnRentSell.setOnClickListener(myClick);
        btnApply.setOnClickListener(myClick);
        btnClear.setOnClickListener(myClick);
        sellButton.setOnClickListener(myClick);
        rentButton.setOnClickListener(myClick);
        btnAllAreas.setOnClickListener(myClick);
        btnAgricultural.setOnClickListener(myClick);
        btnCom.setOnClickListener(myClick);
        btnResidential.setOnClickListener(myClick);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        String propertyType = sharedPreferences.getString(Constants.SELECTED_PROPERTY_TYPE, Constants.NULL);
        Log.d("button_status", "Property Type : "+propertyType);

        if(propertyType.equals(getString(R.string.houses)) || propertyType.equals(getString(R.string.apartment_flats))){

            btnAgricultural.setVisibility(View.GONE);

        }
        else if(propertyType.equals(getString(R.string.shop_offices))){

            furnished = "yes,no";

            btnAgricultural.setVisibility(View.GONE);
            furnishedLayout.setVisibility(View.GONE);

        }

        return view;

    }

    private final View.OnClickListener myClick = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.btnApply:
                    adListFragment();
                    isFilterSelected = true;
                    break;

                case R.id.btnClear:
                    setByDefault();
                    break;

                case R.id.btnRentSell:
                    controlAll();
                    break;

                    case R.id.btnSell:
                    controlSell();
                    break;

                case R.id.btnRent:
                    controlRent();
                    break;


                case R.id.btnAllAreas:
                    button_id = R.id.btnAllAreas;
                    controlAreaType(R.id.btnAllAreas);
                    break;

                case R.id.btnAgricultural:
                    button_id = R.id.btnAgricultural;
                    controlAreaType(R.id.btnAgricultural);
                    break;

                case R.id.btnCom:
                    button_id = R.id.btnCom;
                    controlAreaType(R.id.btnCom);
                    break;

                case R.id.btnResidential:
                    button_id = R.id.btnResidential;
                    controlAreaType(R.id.btnResidential);
                    break;

                default:
                    break;

            }
        }
    };


    private void controlAreaType(int id) {

        if (btnAllAreas.getId() == id) {

            areaType = getString(R.string.all_area_type);
            Log.d("button_status", "Area Type : "+areaType);

            btnAllAreas.setBackgroundResource(R.drawable.button_selected);
            btnAllAreas.setTextColor(getResources().getColor(R.color.Theme1));

            btnAgricultural.setBackgroundResource(R.drawable.button_unselected);
            btnAgricultural.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnCom.setBackgroundResource(R.drawable.button_unselected);
            btnCom.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnResidential.setBackgroundResource(R.drawable.button_unselected);
            btnResidential.setTextColor(getResources().getColor(R.color.text_light_gray));

        }
        else if (btnAgricultural.getId() == id) {

            areaType = getString(R.string.agricultural);
            Log.d("button_status", "Area Type : "+areaType);

            btnAgricultural.setBackgroundResource(R.drawable.button_selected);
            btnAgricultural.setTextColor(getResources().getColor(R.color.Theme1));

            btnAllAreas.setBackgroundResource(R.drawable.button_unselected);
            btnAllAreas.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnCom.setBackgroundResource(R.drawable.button_unselected);
            btnCom.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnResidential.setBackgroundResource(R.drawable.button_unselected);
            btnResidential.setTextColor(getResources().getColor(R.color.text_light_gray));


        }
        else  if (btnCom.getId() == id) {

            areaType = getString(R.string.commercial_industrial);
            Log.d("button_status", "Area Type : "+areaType);

            btnCom.setBackgroundResource(R.drawable.button_selected);
            btnCom.setTextColor(getResources().getColor(R.color.Theme1));

            btnAllAreas.setBackgroundResource(R.drawable.button_unselected);
            btnAllAreas.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnAgricultural.setBackgroundResource(R.drawable.button_unselected);
            btnAgricultural.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnResidential.setBackgroundResource(R.drawable.button_unselected);
            btnResidential.setTextColor(getResources().getColor(R.color.text_light_gray));


        }
        else  if (btnResidential.getId() == id) {

            areaType = getString(R.string.residential);
            Log.d("button_status", "Area Type : "+areaType);

            btnResidential.setBackgroundResource(R.drawable.button_selected);
            btnResidential.setTextColor(getResources().getColor(R.color.Theme1));

            btnAllAreas.setBackgroundResource(R.drawable.button_unselected);
            btnAllAreas.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnAgricultural.setBackgroundResource(R.drawable.button_unselected);
            btnAgricultural.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnCom.setBackgroundResource(R.drawable.button_unselected);
            btnCom.setTextColor(getResources().getColor(R.color.text_light_gray));

        }

    }


    public void adListFragment() {

        Screen5_UserPropertyAds screen5UserPropertyAds = new Screen5_UserPropertyAds();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, screen5UserPropertyAds);
        transaction.addToBackStack("tag");
        transaction.commit();

    }

    @Override
    public void onValuesChanged(float v, float v1) {

    }

    @Override
    public void onValuesChanged(int i, int i1) {
    }

    public void initializeView() {

        btnAllAreas = view.findViewById(R.id.btnAllAreas);
        btnAgricultural = view.findViewById(R.id.btnAgricultural);
        btnCom = view.findViewById(R.id.btnCom);
        btnResidential = view.findViewById(R.id.btnResidential);

        furnishedLayout = view.findViewById(R.id.furnishedLayout);

        btnYes = view.findViewById(R.id.btnYes);
        btnNo = view.findViewById(R.id.btnNo);
        btnRentSell = view.findViewById(R.id.btnRentSell);
        rentButton = view.findViewById(R.id.btnRent);
        sellButton = view.findViewById(R.id.btnSell);
        minCost = view.findViewById(R.id.minTextView);
        maxCost = view.findViewById(R.id.maxTextView);
        minArea = view.findViewById(R.id.minTextView2);
        maxArea = view.findViewById(R.id.maxTextView2);
        costSeekbar = view.findViewById(R.id.rangeSeekBar);
        costSeekbar.setListenerPost(this);
        areaSeekbar = view.findViewById(R.id.rangeSeekBar2);
        areaSeekbar.setListenerPost(this);
        distanceSeekbar = view.findViewById(R.id.seekBar);
        btnClear = view.findViewById(R.id.btnClear);
        btnApply = view.findViewById(R.id.btnApply);
    }

    public void  controlSeekbar(String costFrom,String costTo,String areaFrom,String areaTo,String distance) {

        costSeekbar.setCurrentValues(Integer.valueOf(costFrom),Integer.valueOf(costTo));
        areaSeekbar.setCurrentValues(Integer.valueOf(areaFrom),Integer.valueOf(areaTo));
        distanceSeekbar.setProgress(Float.valueOf(distance));

    }

    public void setByDefault() {

        controlSeekbar("1000","10000000","1","50000","120");
        costFrom = "1000";
        costTo = "10000000";
        areaFrom = "1";
        areaTo = "50000";
        distance = "120";
        areaType = getResources().getString(R.string.all_area_type);
        controlAreaType(R.id.btnAllAreas);
        isFilterSelected = false;
        controlAll();

    }


    public void controlRent() {

        propertyPostType = "Rent";
        rentButton.setBackgroundResource(R.drawable.button_selected);
        rentButton.setTextColor(getResources().getColor(R.color.Theme1));

        sellButton.setBackgroundResource(R.drawable.button_unselected);
        sellButton.setTextColor(getResources().getColor(R.color.text_light_gray));
        btnRentSell.setBackgroundResource(R.drawable.button_unselected);
        btnRentSell.setTextColor(getResources().getColor(R.color.text_light_gray));

    }


    public void controlSell() {

        propertyPostType = "Sell";
        sellButton.setBackgroundResource(R.drawable.button_selected);
        sellButton.setTextColor(getResources().getColor(R.color.Theme1));

        rentButton.setBackgroundResource(R.drawable.button_unselected);
        rentButton.setTextColor(getResources().getColor(R.color.text_light_gray));
        btnRentSell.setBackgroundResource(R.drawable.button_unselected);
        btnRentSell.setTextColor(getResources().getColor(R.color.text_light_gray));

    }

    public void controlAll() {

        propertyPostType = getString(R.string.all_sell_rent);
        btnRentSell.setBackgroundResource(R.drawable.button_selected);
        btnRentSell.setTextColor(getResources().getColor(R.color.Theme1));

        sellButton.setBackgroundResource(R.drawable.button_unselected);
        sellButton.setTextColor(getResources().getColor(R.color.text_light_gray));
        rentButton.setBackgroundResource(R.drawable.button_unselected);
        rentButton.setTextColor(getResources().getColor(R.color.text_light_gray));

    }

}
