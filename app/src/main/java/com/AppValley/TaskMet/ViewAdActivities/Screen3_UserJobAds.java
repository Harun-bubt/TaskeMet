package com.AppValley.TaskMet.ViewAdActivities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AppValley.TaskMet.Home.Adopters.Job_Ads_Grid_Adopter;
import com.AppValley.TaskMet.Home.Adopters.Job_Ads_List_Adopter;
import com.AppValley.TaskMet.Home.Adopters.Shop_Ads_Grid_Adopter;
import com.AppValley.TaskMet.Home.Adopters.Shop_Ads_List_Adopter;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.HomeAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.JobAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserJobAds;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;
import com.krishna.securetimer.SecureTimer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Screen3_UserJobAds extends Fragment {

    View view;
    SharedPreferences sharedPreferences;
    
    String number, next_page_url = null,mainCategory,currency,latitude,longitude,current_date,jobCategory;

    SweetAlertDialog pDialog;
    ProgressBar progressBar;

    TaskMetServer taskMetServer;
    Call<UserJobAds> call;
    ArrayList<JobAdModel> jobPostsList = new ArrayList<>();
    ArrayList<JobAdModel> allPostShopItems = new ArrayList<>();

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    Job_Ads_List_Adopter list_adopter;
    Job_Ads_Grid_Adopter grid_adopter;

    int currentItems, totalItems, scrollOutItems;
    boolean isListShow = true, isScrolling = true;
    public static boolean isFilterSelectedPrevious = false;

    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shopping_ad_list, container, false);

        getJobPostData();

        ((Screen3_Job_Ads_Activity)getActivity()).Ad_map_view.setVisibility(View.VISIBLE);
        ((Screen3_Job_Ads_Activity)getActivity()).changeView.setVisibility(View.VISIBLE);
        ((Screen3_Job_Ads_Activity)getActivity()).filterView.setVisibility(View.VISIBLE);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    isFilterSelectedPrevious = false;
                    getActivity().finish();

                    return true;
                }
                return false;
            }
        });

        return view;

    }

    private void getJobPostData() {

        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        number = sharedPreferences.getString(Constants.NUMBER, Constants.NULL);
        currency = sharedPreferences.getString(Constants.CURRENCY, Constants.NULL);
        latitude = sharedPreferences.getString(Constants.LATITUDE, Constants.NULL);
        longitude = sharedPreferences.getString(Constants.LONGITUDE, Constants.NULL);
        mainCategory = sharedPreferences.getString(Constants.SELECTED_MAIN_CATEGORY, Constants.NULL);
        jobCategory = sharedPreferences.getString(Constants.SELECTED_JOB_CATEGORY, Constants.NULL);
        SecureTimer.with(getContext()).initialize();

        Date date = SecureTimer.with(getActivity()).getCurrentDate();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        current_date = date_format.format(date);

        ((Screen3_Job_Ads_Activity)getActivity()).Ad_map_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sub = null;

                goToMap(number,mainCategory,sub,latitude,longitude,current_date);

            }
        });

        isFilterSelectedPrevious=false;

        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);


        //-------------------------------- Settings RecyclerView -----------------------------------
        recyclerView = view.findViewById(R.id.recyclerview);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        recyclerViewSettings();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!isListShow) {

                    currentItems = gridLayoutManager.getChildCount();
                    totalItems = gridLayoutManager.getItemCount();
                    scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                } else {


                    currentItems = linearLayoutManager.getChildCount();
                    totalItems = linearLayoutManager.getItemCount();
                    scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                }

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {

                    isScrolling = false;
                    getData();

                }
            }
        });

        getData();

    }

    private void getData() {


        if (next_page_url != null) {

            Log.d("call_status", "next page call");
            call = taskMetServer.getJobAdByCategory(next_page_url);

            progressBar.setVisibility(View.VISIBLE);

            calling_api(call);

        }

        else if (Screen3_Filters.isFilterSelected && jobPostsList.isEmpty()) {

            pDialog.setTitleText("Loading");
            pDialog.show();

            call = taskMetServer.getJobAdByCategory(number,mainCategory, Screen3_Filters.jobType, jobCategory,
                    Screen3_Filters.positionType, Screen3_Filters.sort, Screen3_Filters.salaryPeriod, Screen3_Filters.salaryFrom,
                    Screen3_Filters.salaryTo,latitude,longitude,current_date, Screen3_Filters.distance);

            Screen3_Filters.isFilterSelected = false;
            isFilterSelectedPrevious = true;

            calling_api(call);

        }

        else if (jobPostsList.isEmpty()) {

            pDialog.setTitleText("Loading");
            pDialog.show();

            Log.d("call_status", "Simple call");

            call = taskMetServer.getJobAdByCategory(number,mainCategory,getString(R.string.all_job_types),jobCategory, getString(R.string.all_position),"ASC",
                    getString(R.string.all_salary),"1500","250000",latitude,longitude,current_date,"100");

            calling_api(call);

        }

    }

    private void calling_api(Call<UserJobAds> call) {

        call.enqueue(new Callback<UserJobAds>() {
            @Override
            public void onResponse(Call<UserJobAds> call, Response<UserJobAds> response) {

                if(response.isSuccessful()) {

                    if(next_page_url == null){

                        pDialog.dismissWithAnimation();

                    }
                    progressBar.setVisibility(View.GONE);

                    UserJobAds userJobAds = response.body();

                    //Getting next page url if any.
                    next_page_url = userJobAds.getNext_page_url();

                    jobPostsList =  userJobAds.getJobAds();

                    allPostShopItems.addAll(jobPostsList);


                    if (jobPostsList.size() > 0) {

                        if (!isListShow) {

                            grid_adopter.notifyDataSetChanged();

                        }else {

                            list_adopter.notifyDataSetChanged();

                        }

                    }
                    else {

                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Sorry, We don't found any Ad in your area")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        getActivity().finish();
                                    }
                                })
                                .show();

                    }

                    ((Screen3_Job_Ads_Activity)getActivity()).changeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Log.d("call_status", "First value: "+isListShow);

                            if (isListShow) {

                                isListShow = false;
                                recyclerViewSettings();

                                recyclerView.setAdapter(grid_adopter);

                            }
                            else {

                                isListShow = true;
                                recyclerViewSettings();

                                recyclerView.setAdapter(list_adopter);

                            }

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<UserJobAds> call, Throwable t) {
                pDialog.dismissWithAnimation();
            }
        });
        
    }

    private void recyclerViewSettings() {

        if (!isListShow) {

            gridLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);

            //Create adapter
            grid_adopter = new Job_Ads_Grid_Adopter(allPostShopItems, getContext(), new Job_Ads_Grid_Adopter.MyPOstItemClickListener() {
                @Override
                public void onItemClicked(JobAdModel model) {

                    goToJobAdDetails(model.getId());

                }
            });


            recyclerView.setAdapter(grid_adopter);

        }
        else {

            //Reference of RecyclerView & Calling adopter
            linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);

            //Create adapter
            list_adopter = new Job_Ads_List_Adopter(allPostShopItems, getContext(), new Job_Ads_List_Adopter.MyPOstItemClickListener() {
                @Override
                public void onItemClicked(JobAdModel model) {

                    goToJobAdDetails(model.getId());

                }
            });

            //isListShow = false;
            recyclerView.setAdapter(list_adopter);

        }

    }

    public void goToJobAdDetails(int id) {
        
        ViewUsersAdDetails viewUsersAdDetails = new ViewUsersAdDetails(id);
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, viewUsersAdDetails);
        transaction.addToBackStack("tag");
        transaction.commit();
        
    }

    private void goToMap(String number, String mainCategory, String sub, String latitude, String longitude, String current_date) {

        UserAdsMapView userAdsMapView = new UserAdsMapView(number,mainCategory,sub,latitude,longitude,current_date,"Screen3_Job_Ads_Activity");
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, userAdsMapView);
        transaction.addToBackStack("tag");
        transaction.commit();

    }


    @Override
    public void onStop() {
        super.onStop();

        if(call != null){
            call.cancel();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(call != null){
            call.cancel();
        }

    }

}
