package com.AppValley.TaskMet.ViewAdActivities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.NothingSelectedSpinnerAdapter;
import com.rizlee.rangeseekbar.RangeSeekBar;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.time.Year;


public class Screen3_Filters extends Fragment implements RangeSeekBar.OnRangeSeekBarPostListener {

    View view;
    TextView minSalary, maxSalary;
    Button btnAsc, btnDsc;

    Button jobOffer, cvResume, btnClear, btnApply,
            btnAllSalary,btnHourly, btnWeekly, btnMonthly, btnYearly,
            btnAllPosition,btnContract, btnFullTime, btnPartTime;

    public static String jobType = "offering Job", positionType, salaryPeriod, salaryFrom, salaryTo, sort = "ASC", distance;
    public static int salary_id,position_id;

    boolean isOfferingJob = true;

    FragmentManager fragmentManager;
    IndicatorSeekBar distanceSeekbar;
    RangeSeekBar rangeSeekBar;
    public static boolean isFilterSelected = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_job_ad_filter, container, false);

        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

        initialize_view();

        if (Screen3_UserJobAds.isFilterSelectedPrevious) {

            controlSalaryPeriod(salary_id);
            controlPositionType(position_id);
            controlConditionAndSort(jobType, sort);
            controlSeekbar(salaryFrom, salaryTo, distance);

        }
        else {

            controlByDefault();

        }

        ((Screen3_Job_Ads_Activity)getActivity()).changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adListFragment();
            }
        });


        jobOffer.setOnClickListener(myClick);
        cvResume.setOnClickListener(myClick);

        btnAllSalary.setOnClickListener(myClick);
        btnHourly.setOnClickListener(myClick);
        btnWeekly.setOnClickListener(myClick);
        btnMonthly.setOnClickListener(myClick);
        btnYearly.setOnClickListener(myClick);

        btnAllPosition.setOnClickListener(myClick);
        btnContract.setOnClickListener(myClick);
        btnFullTime.setOnClickListener(myClick);
        btnPartTime.setOnClickListener(myClick);

        btnAsc.setOnClickListener(myClick);
        btnDsc.setOnClickListener(myClick);

        btnApply.setOnClickListener(myClick);
        btnClear.setOnClickListener(myClick);

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

        return view;
    }

    private final View.OnClickListener myClick = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.btnJobOffer:
                    controlJobOffer();
                    break;

                case R.id.btnCvResume:
                    controlCvResume();
                    break;

                case R.id.btnAsc:
                    controlAsc();
                    break;

                case R.id.btnDsc:
                    controlDesc();
                    break;

                case R.id.btnClear:
                    controlByDefault();
                    break;

                case R.id.btnApply:
                    adListFragment();
                    isFilterSelected = true;
                    break;

                case R.id.btnAllSalary:
                    salary_id = R.id.btnAllSalary;
                    controlSalaryPeriod(R.id.btnAllSalary);
                    break;

                case R.id.btnHourly:
                    salary_id = R.id.btnHourly;
                    controlSalaryPeriod(R.id.btnHourly);
                    break;

                case R.id.btnWeekly:
                    salary_id = R.id.btnWeekly;
                    controlSalaryPeriod(R.id.btnWeekly);
                    break;

                case R.id.btnMonthly:
                    salary_id = R.id.btnMonthly;
                    controlSalaryPeriod(R.id.btnMonthly);
                    break;

                case R.id.btnYearly:
                    salary_id = R.id.btnYearly;
                    controlSalaryPeriod(R.id.btnYearly);
                    break;

                case R.id.btnAllPosition:
                    position_id = R.id.btnAllPosition;
                    controlPositionType(R.id.btnAllPosition);
                    break;

                case R.id.btnContract:
                    position_id = R.id.btnContract;
                    controlPositionType(R.id.btnContract);
                    break;

                case R.id.btnFullTime:
                    position_id = R.id.btnFullTime;
                    controlPositionType(R.id.btnFullTime);
                    break;

                case R.id.btnPartTime:
                    position_id = R.id.btnPartTime;
                    controlPositionType(R.id.btnPartTime);
                    break;

                default:
                    break;

            }
        }
    };

    private void controlSalaryPeriod(int id) {

        Log.d("button_status", "\nSalary Period Button Pressed\n");

        if (btnAllSalary.getId() == id) {

            salaryPeriod = getString(R.string.all_salary);
            Log.d("button_status", "Salary Button : "+salaryPeriod);

            btnAllSalary.setBackgroundResource(R.drawable.button_selected);
            btnAllSalary.setTextColor(getResources().getColor(R.color.Theme1));

            btnHourly.setBackgroundResource(R.drawable.button_unselected);
            btnHourly.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnWeekly.setBackgroundResource(R.drawable.button_unselected);
            btnWeekly.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnMonthly.setBackgroundResource(R.drawable.button_unselected);
            btnMonthly.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnYearly.setBackgroundResource(R.drawable.button_unselected);
            btnYearly.setTextColor(getResources().getColor(R.color.text_light_gray));

        }
        else if (btnHourly.getId() == id) {

            salaryPeriod = getString(R.string.hourly);
            Log.d("button_status", "Salary Button : "+salaryPeriod);

            btnHourly.setBackgroundResource(R.drawable.button_selected);
            btnHourly.setTextColor(getResources().getColor(R.color.Theme1));

            btnAllSalary.setBackgroundResource(R.drawable.button_unselected);
            btnAllSalary.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnWeekly.setBackgroundResource(R.drawable.button_unselected);
            btnWeekly.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnMonthly.setBackgroundResource(R.drawable.button_unselected);
            btnMonthly.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnYearly.setBackgroundResource(R.drawable.button_unselected);
            btnYearly.setTextColor(getResources().getColor(R.color.text_light_gray));

        }
        else  if (btnWeekly.getId() == id) {

            salaryPeriod = getString(R.string.weekly);
            Log.d("button_status", "Salary Button : "+salaryPeriod);

            btnWeekly.setBackgroundResource(R.drawable.button_selected);
            btnWeekly.setTextColor(getResources().getColor(R.color.Theme1));

            btnAllSalary.setBackgroundResource(R.drawable.button_unselected);
            btnAllSalary.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnHourly.setBackgroundResource(R.drawable.button_unselected);
            btnHourly.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnMonthly.setBackgroundResource(R.drawable.button_unselected);
            btnMonthly.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnYearly.setBackgroundResource(R.drawable.button_unselected);
            btnYearly.setTextColor(getResources().getColor(R.color.text_light_gray));

        }
        else  if (btnMonthly.getId() == id) {

            salaryPeriod = getString(R.string.monthly);
            Log.d("button_status", "Salary Button : "+salaryPeriod);

            btnMonthly.setBackgroundResource(R.drawable.button_selected);
            btnMonthly.setTextColor(getResources().getColor(R.color.Theme1));

            btnAllSalary.setBackgroundResource(R.drawable.button_unselected);
            btnAllSalary.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnHourly.setBackgroundResource(R.drawable.button_unselected);
            btnHourly.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnWeekly.setBackgroundResource(R.drawable.button_unselected);
            btnWeekly.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnYearly.setBackgroundResource(R.drawable.button_unselected);
            btnYearly.setTextColor(getResources().getColor(R.color.text_light_gray));

        }
        else  if (btnYearly.getId() == id) {

            salaryPeriod = getString(R.string.yearly);
            Log.d("button_status", "Salary Button : "+salaryPeriod);

            btnYearly.setBackgroundResource(R.drawable.button_selected);
            btnYearly.setTextColor(getResources().getColor(R.color.Theme1));

            btnAllSalary.setBackgroundResource(R.drawable.button_unselected);
            btnAllSalary.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnHourly.setBackgroundResource(R.drawable.button_unselected);
            btnHourly.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnWeekly.setBackgroundResource(R.drawable.button_unselected);
            btnWeekly.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnMonthly.setBackgroundResource(R.drawable.button_unselected);
            btnMonthly.setTextColor(getResources().getColor(R.color.text_light_gray));

        }

    }

    private void controlPositionType(int id) {

        Log.d("button_status", "\nPosition Type Button Pressed\n");

        if (btnAllPosition.getId() == id) {

            positionType = getString(R.string.all_position);
            Log.d("button_status", "Position Type Button : "+positionType);

            btnAllPosition.setBackgroundResource(R.drawable.button_selected);
            btnAllPosition.setTextColor(getResources().getColor(R.color.Theme1));

            btnContract.setBackgroundResource(R.drawable.button_unselected);
            btnContract.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnFullTime.setBackgroundResource(R.drawable.button_unselected);
            btnFullTime.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnPartTime.setBackgroundResource(R.drawable.button_unselected);
            btnPartTime.setTextColor(getResources().getColor(R.color.text_light_gray));

        }
        else if (btnContract.getId() == id) {

            positionType = getString(R.string.contract_base);
            Log.d("button_status", "Position Type Button : "+positionType);

            btnContract.setBackgroundResource(R.drawable.button_selected);
            btnContract.setTextColor(getResources().getColor(R.color.Theme1));

            btnAllPosition.setBackgroundResource(R.drawable.button_unselected);
            btnAllPosition.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnFullTime.setBackgroundResource(R.drawable.button_unselected);
            btnFullTime.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnPartTime.setBackgroundResource(R.drawable.button_unselected);
            btnPartTime.setTextColor(getResources().getColor(R.color.text_light_gray));

        }
        else  if (btnFullTime.getId() == id) {

            positionType = getString(R.string.full_time_job);
            Log.d("button_status", "Position Type Button : "+positionType);

            btnFullTime.setBackgroundResource(R.drawable.button_selected);
            btnFullTime.setTextColor(getResources().getColor(R.color.Theme1));

            btnAllPosition.setBackgroundResource(R.drawable.button_unselected);
            btnAllPosition.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnContract.setBackgroundResource(R.drawable.button_unselected);
            btnContract.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnPartTime.setBackgroundResource(R.drawable.button_unselected);
            btnPartTime.setTextColor(getResources().getColor(R.color.text_light_gray));

        }
        else  if (btnPartTime.getId() == id) {

            positionType = getString(R.string.part_time_job);
            Log.d("button_status", "Position Type Button : "+positionType);

            btnPartTime.setBackgroundResource(R.drawable.button_selected);
            btnPartTime.setTextColor(getResources().getColor(R.color.Theme1));

            btnAllPosition.setBackgroundResource(R.drawable.button_unselected);
            btnAllPosition.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnContract.setBackgroundResource(R.drawable.button_unselected);
            btnContract.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnFullTime.setBackgroundResource(R.drawable.button_unselected);
            btnFullTime.setTextColor(getResources().getColor(R.color.text_light_gray));

        }

    }

    public void adListFragment() {
        Screen3_UserJobAds jobAdList = new Screen3_UserJobAds();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, jobAdList);
        transaction.addToBackStack("tag");
        transaction.commit();
    }

    public void initialize_view() {

        jobOffer = view.findViewById(R.id.btnJobOffer);
        cvResume = view.findViewById(R.id.btnCvResume);

        btnAllSalary = view.findViewById(R.id.btnAllSalary);
        btnHourly = view.findViewById(R.id.btnHourly);
        btnWeekly = view.findViewById(R.id.btnWeekly);
        btnMonthly = view.findViewById(R.id.btnMonthly);
        btnYearly = view.findViewById(R.id.btnYearly);

        btnAllPosition = view.findViewById(R.id.btnAllPosition);
        btnContract = view.findViewById(R.id.btnContract);
        btnFullTime = view.findViewById(R.id.btnFullTime);
        btnPartTime = view.findViewById(R.id.btnPartTime);

        minSalary = view.findViewById(R.id.minTextView);
        maxSalary = view.findViewById(R.id.maxTextView);
        btnAsc = view.findViewById(R.id.btnAsc);
        btnDsc = view.findViewById(R.id.btnDsc);
        btnClear = view.findViewById(R.id.btnClear);
        btnApply = view.findViewById(R.id.btnApply);
        distanceSeekbar = view.findViewById(R.id.seekBar);
        rangeSeekBar = view.findViewById(R.id.rangeSeekBar);
        rangeSeekBar.setListenerPost(this);

    }

    @Override
    public void onValuesChanged(float v, float v1) {

    }

    @Override
    public void onValuesChanged(int i, int i1) {
        minSalary.setText(String.valueOf(i));
        maxSalary.setText(String.valueOf(i1));
        salaryFrom = String.valueOf(i);
        salaryTo = String.valueOf(i1);
    }

    public void controlByDefault() {

        controlConditionAndSort("offering Job", "ASC");
        controlSeekbar("50", "250000", "100");
        jobType = getString(R.string.all_job_types);
        positionType = getResources().getString(R.string.contract_base);
        salaryPeriod = getResources().getString(R.string.hourly);
        sort = "ASC";
        salaryFrom = "50";
        salaryTo = "250000";
        distance = "100";
        isFilterSelected = false;
        controlSalaryPeriod(R.id.btnAllSalary);
        controlPositionType(R.id.btnAllPosition);

    }

    public void controlSeekbar(String minP, String maxP, String distance) {
        minSalary.setText(minP);
        maxSalary.setText(maxP);
        salaryFrom = minP;
        salaryTo = maxP;
        rangeSeekBar.setCurrentValues(Integer.valueOf(minP), Integer.valueOf(maxP));
        distanceSeekbar.setProgress(Float.valueOf(distance));
    }

    public void controlConditionAndSort(String jobType, String sort) {
        if (jobType.equals(getString(R.string.all_job_types))) {
            controlJobOffer();
        } else {
            controlCvResume();
        }
        if (sort.equals("ASC")) {
            controlAsc();
        } else {
            controlDesc();
        }
    }

    private void controlDesc() {
        sort = "DESC";
        btnDsc.setBackgroundResource(R.drawable.button_selected);
        btnDsc.setTextColor(getResources().getColor(R.color.Theme1));
        btnAsc.setBackgroundResource(R.drawable.button_unselected);
        btnAsc.setTextColor(getResources().getColor(R.color.text_light_gray));
    }

    public void controlAsc() {
        sort = "ASC";
        btnAsc.setBackgroundResource(R.drawable.button_selected);
        btnAsc.setTextColor(getResources().getColor(R.color.Theme1));
        btnDsc.setBackgroundResource(R.drawable.button_unselected);
        btnDsc.setTextColor(getResources().getColor(R.color.text_light_gray));
    }

    public void controlJobOffer() {
        jobType = "offering Job";
        isOfferingJob = true;
        jobOffer.setBackgroundResource(R.drawable.button_selected);
        jobOffer.setTextColor(getResources().getColor(R.color.Theme1));
        cvResume.setBackgroundResource(R.drawable.button_unselected);
        cvResume.setTextColor(getResources().getColor(R.color.text_light_gray));

    }

    public void controlCvResume() {
        jobType = "cv Resume";
        isOfferingJob = false;
        cvResume.setBackgroundResource(R.drawable.button_selected);
        cvResume.setTextColor(getResources().getColor(R.color.Theme1));
        jobOffer.setBackgroundResource(R.drawable.button_unselected);
        jobOffer.setTextColor(getResources().getColor(R.color.text_light_gray));
    }

}