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

public class Screen2_Filters extends Fragment implements RangeSeekBar.OnRangeSeekBarPostListener {

    View view;
    TextView minPrice,maxPrice;
    Button btnNew,btnUsed,btnAll,btnAsc,btnDsc, btnClear,btnApply;
    public  static String condition,sort,priceFrom,priceTo,distance;
    public static boolean isFilterSelected = false;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String currency;
    FragmentManager fragmentManager;
    IndicatorSeekBar distanceSeekbar;
    RangeSeekBar rangeSeekBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shopping_ad_filter, container, false);
        initilizeAllView();

        rangeSeekBar.setListenerPost(this);

        //shared preferences.........................

        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        currency = sharedPreferences.getString(Constants.CURRENCY, Constants.NULL);

        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        if(Screen2_UserShoppingAds.isFilterSelectedPrevious)
        {
            controlConditionAndSort(condition,sort);
            controlSeekbar(priceFrom,priceTo,distance);

        }else
        {
          controlByDefault();
        }

        ((Screen2_Shopping_Ads_Activity)getActivity()).changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adListFragment();
            }
        });

        ((Screen2_Shopping_Ads_Activity)getActivity()).filterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               controlNew();
            }
        });
        btnUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               controlUsed();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               controlallCondition();
            }
        });
        btnAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              controlAsc();
            }
        });
        btnDsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               controlDesc();
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

    private void controlDesc() {
        sort = "DESC";
        btnDsc.setBackgroundResource(R.drawable.button_selected);
        btnDsc.setTextColor(getResources().getColor(R.color.Theme1));
        btnAsc.setBackgroundResource(R.drawable.button_unselected);
        btnAsc.setTextColor(getResources().getColor(R.color.text_light_gray));
    }
    public  void controlAsc() {
        sort ="ASC";
        btnAsc.setBackgroundResource(R.drawable.button_selected);
        btnAsc.setTextColor(getResources().getColor(R.color.Theme1));
        btnDsc.setBackgroundResource(R.drawable.button_unselected);
        btnDsc.setTextColor(getResources().getColor(R.color.text_light_gray));
    }
    public  void controlNew()
    {
        condition = "New";
        btnNew.setBackgroundResource(R.drawable.button_selected);
        btnNew.setTextColor(getResources().getColor(R.color.Theme1));
        btnUsed.setBackgroundResource(R.drawable.button_unselected);
        btnUsed.setTextColor(getResources().getColor(R.color.text_light_gray));
        btnAll.setBackgroundResource(R.drawable.button_unselected);
        btnAll.setTextColor(getResources().getColor(R.color.text_light_gray));
    }

    private void controlUsed() {
        condition = "Used";
        btnUsed.setBackgroundResource(R.drawable.button_selected);
        btnUsed.setTextColor(getResources().getColor(R.color.Theme1));
        btnNew.setBackgroundResource(R.drawable.button_unselected);
        btnNew.setTextColor(getResources().getColor(R.color.text_light_gray));
        btnAll.setBackgroundResource(R.drawable.button_unselected);
        btnAll.setTextColor(getResources().getColor(R.color.text_light_gray));
    }
    public void controlallCondition()
    {
        condition = "Used,New";
        btnAll.setBackgroundResource(R.drawable.button_selected);
        btnAll.setTextColor(getResources().getColor(R.color.Theme1));
        btnUsed.setBackgroundResource(R.drawable.button_unselected);
        btnUsed.setTextColor(getResources().getColor(R.color.text_light_gray));
        btnNew.setBackgroundResource(R.drawable.button_unselected);
        btnNew.setTextColor(getResources().getColor(R.color.text_light_gray));
    }

    public void adListFragment()
    {
        Screen2_UserShoppingAds screen2UserShoppingAds = new Screen2_UserShoppingAds();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, screen2UserShoppingAds);
        transaction.addToBackStack("tag");
        transaction.commit();
    }

    public  void controlSeekbar(String minP,String maxP,String distance)
    {
        minPrice.setText(minP);
        maxPrice.setText(maxP);
        priceFrom = minP;
        priceTo = maxP;
        rangeSeekBar.setCurrentValues(Integer.valueOf(minP),Integer.valueOf(maxP));
        distanceSeekbar.setProgress(Float.valueOf(distance));
    }
    public void controlConditionAndSort(String condition,String sort)
    {
        if(condition.equals("Used,New"))
        {
            controlallCondition();
        }
        else if(condition.equals("Used"))
        {
            controlUsed();
        }
        else
        {
            controlNew();
        }
        if(sort.equals("ASC"))
        {
            controlAsc();
        }else
        {
            controlDesc();
        }
    }
    public void controlByDefault()
    {
        controlConditionAndSort("Used,New","ASC");
        controlSeekbar("1500","250000","100");
        condition = "Used,New";
        sort="ASC";
        priceFrom="1500";
        priceTo="250000";
        distance="100";
        isFilterSelected = false;
    }

    public void initilizeAllView()
    {
        minPrice = view.findViewById(R.id.minTextView);
        maxPrice = view.findViewById(R.id.maxTextView);
        btnNew = view.findViewById(R.id.btnNew);
        btnUsed = view.findViewById(R.id.btnUsed);
        btnAll = view.findViewById(R.id.btnAll);
        btnAsc = view.findViewById(R.id.btnAsc);
        btnDsc = view.findViewById(R.id.btnDsc);
        btnClear = view.findViewById(R.id.btnClear);
        btnApply = view.findViewById(R.id.btnApply);
        distanceSeekbar = view.findViewById(R.id.seekBar);
        rangeSeekBar = view.findViewById(R.id.rangeSeekBar);
        rangeSeekBar.setListenerPost(this);

    }

    @Override
    public void onResume() {
        super.onResume();

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
