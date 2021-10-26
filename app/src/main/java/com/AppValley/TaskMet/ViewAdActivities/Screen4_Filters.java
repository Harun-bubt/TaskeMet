package com.AppValley.TaskMet.ViewAdActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.AppValley.TaskMet.R;
import com.rizlee.rangeseekbar.RangeSeekBar;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

public class Screen4_Filters extends Fragment implements  View.OnClickListener, RangeSeekBar.OnRangeSeekBarPostListener{

    View view;
    TextView minAge, maxAge;

    Button btnBride,btnGroom,btnWidower,btnMarried,btnClear,btnApply;
    Button btnUnmarried,btnDivorced,btnSeparated;
    int btn_count = 1;
    FragmentManager fragmentManager;
    public  static String looking_for,maritial_status, sort ,ageFrom, ageTo,distance;
    public static boolean isFilterSelected = false;


    IndicatorSeekBar distanceSeekbar;
    RangeSeekBar rangeSeekBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_marriage_ad_filter, container, false);
        initializeVeiw();

        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

        ((Screen4_Marriage_Ads_Activity)getActivity()).changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adListFragment();
            }
        });

        if(Screen4_UserMarriageAds.isFilterSelectedPrevious)
        {
            controlLookingForAndMaritalStatus(looking_for,maritial_status);
            controlSeekbar(ageFrom,ageTo,distance);

        }else
        {
            controlByDefault();
        }

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
    public void adListFragment()
    {
        Screen4_UserMarriageAds screen4UserMarriageAds = new Screen4_UserMarriageAds();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, screen4UserMarriageAds);
        transaction.addToBackStack("tag");
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnBride:
                looking_for = "Bride";

                if(btn_count == 0){

                    btn_count = 1 ;
                    btnBride.setBackgroundResource(R.drawable.button_selected);
                    btnBride.setTextColor(getResources().getColor(R.color.Theme1));
                    btnGroom.setBackgroundResource(R.drawable.button_unselected);
                    btnGroom.setTextColor(getResources().getColor(R.color.text_light_gray));
                    btnWidower.setText("Widower");
                    btnMarried.setVisibility(View.VISIBLE);
                    Animation mLoadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
                    btnMarried.startAnimation(mLoadAnimation);

                }
                break;

            case R.id.btnGroom:
                looking_for = "Groom";
                if(btn_count == 1) {

                    btn_count = 0 ;
                    btnGroom.setBackgroundResource(R.drawable.button_selected);
                    btnGroom.setTextColor(getResources().getColor(R.color.Theme1));
                    btnBride.setBackgroundResource(R.drawable.button_unselected);
                    btnBride.setTextColor(getResources().getColor(R.color.text_light_gray));
                    btnWidower.setText("Widow");
                    btnMarried.setVisibility(View.GONE);
                    Animation mLoadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_out);
                    btnMarried.startAnimation(mLoadAnimation);
                }
                break;

            case R.id.btnUnmarried:
                maritial_status = "Unmarried";
                btnUnmarried.setBackgroundResource(R.drawable.button_selected);
                btnUnmarried.setTextColor(getResources().getColor(R.color.Theme1));
                btnDivorced.setBackgroundResource(R.drawable.button_unselected);
                btnDivorced.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnWidower.setBackgroundResource(R.drawable.button_unselected);
                btnWidower.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnSeparated.setBackgroundResource(R.drawable.button_unselected);
                btnSeparated.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnMarried.setBackgroundResource(R.drawable.button_unselected);
                btnMarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                break;
            case R.id.btnDivorced:
                maritial_status = "Divorced";
                btnDivorced.setBackgroundResource(R.drawable.button_selected);
                btnDivorced.setTextColor(getResources().getColor(R.color.Theme1));
                btnUnmarried.setBackgroundResource(R.drawable.button_unselected);
                btnUnmarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnWidower.setBackgroundResource(R.drawable.button_unselected);
                btnWidower.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnSeparated.setBackgroundResource(R.drawable.button_unselected);
                btnSeparated.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnMarried.setBackgroundResource(R.drawable.button_unselected);
                btnMarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                break;
            case R.id.btnWidower:
                if(btn_count == 0)
                {
                    maritial_status = "Widow";
                }else if(btn_count == 1)
                {
                    maritial_status = "Widower";
                }
                btnWidower.setBackgroundResource(R.drawable.button_selected);
                btnWidower.setTextColor(getResources().getColor(R.color.Theme1));
                btnDivorced.setBackgroundResource(R.drawable.button_unselected);
                btnDivorced.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnUnmarried.setBackgroundResource(R.drawable.button_unselected);
                btnUnmarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnSeparated.setBackgroundResource(R.drawable.button_unselected);
                btnSeparated.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnMarried.setBackgroundResource(R.drawable.button_unselected);
                btnMarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                break;
            case R.id.btnSeparated:
                maritial_status = "Separated";
                btnSeparated.setBackgroundResource(R.drawable.button_selected);
                btnSeparated.setTextColor(getResources().getColor(R.color.Theme1));
                btnDivorced.setBackgroundResource(R.drawable.button_unselected);
                btnDivorced.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnWidower.setBackgroundResource(R.drawable.button_unselected);
                btnWidower.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnUnmarried.setBackgroundResource(R.drawable.button_unselected);
                btnUnmarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnMarried.setBackgroundResource(R.drawable.button_unselected);
                btnMarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                break;
            case R.id.btnMarried:
                maritial_status = "Married";
                btnMarried.setBackgroundResource(R.drawable.button_selected);
                btnMarried.setTextColor(getResources().getColor(R.color.Theme1));
                btnDivorced.setBackgroundResource(R.drawable.button_unselected);
                btnDivorced.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnWidower.setBackgroundResource(R.drawable.button_unselected);
                btnWidower.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnSeparated.setBackgroundResource(R.drawable.button_unselected);
                btnSeparated.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnUnmarried.setBackgroundResource(R.drawable.button_unselected);
                btnUnmarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                break;


        }
    }

    @Override
    public void onValuesChanged(float v, float v1) {

    }

    @Override
    public void onValuesChanged(int i, int i1) {
        minAge.setText(String.valueOf(i));
        maxAge.setText(String.valueOf(i1));
        ageFrom = String.valueOf(i);
        ageTo = String.valueOf(i1);
    }
    public void initializeVeiw()
    {
        minAge = view.findViewById(R.id.minTextView);
        maxAge = view.findViewById(R.id.maxTextView);
        btnBride = view.findViewById(R.id.btnBride);
        btnBride.setOnClickListener(this);
        btnGroom = view.findViewById(R.id.btnGroom);
        btnGroom.setOnClickListener(this);
        btnWidower = view.findViewById(R.id.btnWidower);
        btnWidower.setOnClickListener(this);
        btnMarried = view.findViewById(R.id.btnMarried);
        btnMarried.setOnClickListener(this);
        btnUnmarried = view.findViewById(R.id.btnUnmarried);
        btnUnmarried.setOnClickListener(this);
        btnDivorced = view.findViewById(R.id.btnDivorced);
        btnDivorced.setOnClickListener(this);
        btnSeparated = view.findViewById(R.id.btnSeparated);
        btnSeparated.setOnClickListener(this);

        distanceSeekbar = view.findViewById(R.id.seekBar);
        rangeSeekBar = view.findViewById(R.id.rangeSeekBar);
        rangeSeekBar.setListenerPost(this);
        btnClear = view.findViewById(R.id.btnClear);
        btnApply = view.findViewById(R.id.btnApply);
    }
    public void controlLookingForAndMaritalStatus(String looking_for,String maritial_status)
    {
        if(looking_for.equals("Bride"))
        {
            btnBride.performClick();
        }
        else
        {
            btnGroom.performClick();
        }

        if(maritial_status.equals("Unmarried"))
        {
            btnUnmarried.performClick();
        }else if(maritial_status.equals("Divorced"))
        {
            btnDivorced.performClick();
        } else if(maritial_status.equals("Widower") || maritial_status.equals("Widow"))
        {
            btnWidower.performClick();
        }
        else if(maritial_status.equals("Separated"))
        {
            btnSeparated.performClick();
        }
        else if(maritial_status.equals("Married"))
        {
            btnMarried.performClick();
        }
    }
    public void controlByDefault()
    {
        controlLookingForAndMaritalStatus("Bride","Unmarried");
        controlSeekbar("18","55","90");
        looking_for = "Bride";
        maritial_status="Unmarried";
        ageFrom="18";
        ageTo="55";
        distance="100";
        isFilterSelected = false;
    }
    public  void controlSeekbar(String ageF,String AgeT,String distance)
    {
        minAge.setText(ageF);
        maxAge.setText(AgeT);
        ageFrom = ageF;
        ageTo = AgeT;
        rangeSeekBar.setCurrentValues(Integer.valueOf(ageF),Integer.valueOf(AgeT));
        distanceSeekbar.setProgress(Float.valueOf(distance));
    }


}
