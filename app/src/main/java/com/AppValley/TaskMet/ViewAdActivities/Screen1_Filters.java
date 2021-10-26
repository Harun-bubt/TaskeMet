package com.AppValley.TaskMet.ViewAdActivities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class Screen1_Filters extends Fragment implements RangeSeekBar.OnRangeSeekBarPostListener{

    View view;
    TextView minPrice,maxPrice;
    Button btnAsc,btnDsc, btnClear,btnApply;

    public  static String sort,priceFrom,priceTo,distance;
    public static boolean isFilterSelected = false;

    SharedPreferences sharedPreferences;
    String currency;
    FragmentManager fragmentManager;
    IndicatorSeekBar distanceSeekbar;
    RangeSeekBar rangeSeekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_common_ad_filter, container, false);
        initilizeAllView();
        rangeSeekBar.setListenerPost(this);


        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        currency = sharedPreferences.getString(Constants.CURRENCY, Constants.NULL);

        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        if(Screen1_UserCommonAds.isFilterSelectedPrevious)
        {
            controlSeekbarAndSort(priceFrom,priceTo,distance,sort);

        }else
        {
          controlByDefault();
        }

        ((Screen1_Common_Ads_Activity)getActivity()).filterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ((Screen1_Common_Ads_Activity)getActivity()).changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adListFragment();
            }
        });

        btnAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              controlASC();
            }
        });
        btnDsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlDESC();
            }
        });
        distanceSeekbar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

                distance = Integer.toString(seekParams.progress);
                Log.d("distancT",distance);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });



        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adListFragment();
                isFilterSelected = true;
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlByDefault();
            }
        });

        return view;

    }


    public void controlASC() {

        sort ="ASC";
        btnAsc.setBackgroundResource(R.drawable.button_selected);
        btnAsc.setTextColor(getResources().getColor(R.color.Theme1));
        btnDsc.setBackgroundResource(R.drawable.button_unselected);
        btnDsc.setTextColor(getResources().getColor(R.color.text_light_gray));

    }

    public void controlDESC() {

        sort = "DESC";
        btnDsc.setBackgroundResource(R.drawable.button_selected);
        btnDsc.setTextColor(getResources().getColor(R.color.Theme1));
        btnAsc.setBackgroundResource(R.drawable.button_unselected);
        btnAsc.setTextColor(getResources().getColor(R.color.text_light_gray));

    }

    public  void controlSeekbarAndSort(String minP,String maxP,String distance,String sort) {

        minPrice.setText(currency+" "+ minP);
        maxPrice.setText(currency+" "+ maxP);
        priceFrom = minP;
        priceTo = maxP;
        rangeSeekBar.setCurrentValues(Integer.valueOf(minP),Integer.valueOf(maxP));
        distanceSeekbar.setProgress(Float.valueOf(distance));
        if(sort.equals("ASC"))
        {
            controlASC();
        }else
        {
            controlDESC();
        }

    }

    public void controlByDefault() {

        controlSeekbarAndSort("1500","250000","100","ASC");
        sort="ASC";
        priceFrom="1500";
        priceTo="250000";
        distance="100";
        isFilterSelected = false;

    }

    public void adListFragment() {

        Screen1_UserCommonAds screen1UserCommonAds = new Screen1_UserCommonAds();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, screen1UserCommonAds);
        transaction.addToBackStack("tag");
        transaction.commit();

    }

    public void initilizeAllView() {

        minPrice = view.findViewById(R.id.minTextView);
        maxPrice = view.findViewById(R.id.maxTextView);
        btnAsc = view.findViewById(R.id.btnAsc);
        btnDsc = view.findViewById(R.id.btnDsc);
        btnClear = view.findViewById(R.id.btnClear);
        btnApply = view.findViewById(R.id.btnApply);
        distanceSeekbar = view.findViewById(R.id.seekBar);
        rangeSeekBar = view.findViewById(R.id.rangeSeekBar);

    }

    @Override
    public void onValuesChanged(float v, float v1) {

    }

    @Override
    public void onValuesChanged(int i, int i1) {
        minPrice.setText(currency+" "+String.valueOf(i));
        maxPrice.setText(currency+" "+String.valueOf(i1));
        priceFrom = String.valueOf(i);
        priceTo = String.valueOf(i1);
    }

}
