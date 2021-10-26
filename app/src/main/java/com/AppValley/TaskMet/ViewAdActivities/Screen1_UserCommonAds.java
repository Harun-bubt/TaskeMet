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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.AppValley.TaskMet.Home.Adopters.Common_Ads_Grid_Adopter;
import com.AppValley.TaskMet.Home.Adopters.Common_Ads_List_Adopter;
import com.AppValley.TaskMet.Home.Adopters.Shop_Ads_Grid_Adopter;
import com.AppValley.TaskMet.Home.Adopters.Shop_Ads_List_Adopter;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.CommonAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.HomeAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserCommonAds;
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

public class Screen1_UserCommonAds extends Fragment {

    View view;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    String number;
    String mainCategory,subCategory = null;
    String currency;
    String latitude;
    String longitude;
    String current_date;
    String next_page_url = null;

    boolean isScrolling = false, isListShow = true;
    public static boolean isFilterSelectedPrevious=false;

    int currentItems, totalItems, scrollOutItems;

    TaskMetServer taskMetServer;
    Call<UserCommonAds> call;

    FragmentManager fragmentManager;
    SweetAlertDialog pDialog;

    ArrayList<CommonAdModel> commonPostsList = new ArrayList<>();
    ArrayList<CommonAdModel> allPostItems = new ArrayList<>();

    LinearLayoutManager linearLayoutManager;
    Common_Ads_List_Adopter list_adopter;
    Common_Ads_Grid_Adopter grid_adopter;

    ProgressBar progressBar;
    GridLayoutManager gridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_common_ad_list, container, false);

        getCommonPostData();

        ((Screen1_Common_Ads_Activity)getActivity()).Ad_map_view.setVisibility(View.VISIBLE);
        ((Screen1_Common_Ads_Activity)getActivity()).changeView.setVisibility(View.VISIBLE);
        ((Screen1_Common_Ads_Activity)getActivity()).filterView.setVisibility(View.VISIBLE);

        ((Screen1_Common_Ads_Activity)getActivity()).Ad_map_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToMap(number,mainCategory,subCategory,latitude,longitude,current_date);

            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    isFilterSelectedPrevious = false;
                    getActivity().finish();

                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private void getCommonPostData() {

        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        number = sharedPreferences.getString(Constants.NUMBER, Constants.NULL);
        currency = sharedPreferences.getString(Constants.CURRENCY, Constants.NULL);
        latitude = sharedPreferences.getString(Constants.LATITUDE, Constants.NULL);
        longitude = sharedPreferences.getString(Constants.LONGITUDE, Constants.NULL);
        mainCategory = sharedPreferences.getString(Constants.SELECTED_MAIN_CATEGORY, Constants.NULL);
        SecureTimer.with(getContext()).initialize();

        Date date = SecureTimer.with(getActivity()).getCurrentDate();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        current_date = date_format.format(date);

        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);


        //-------------------------------- Settings RecyclerView -----------------------------------
        recyclerView = view.findViewById(R.id.recyclerview);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);


        recyclerViewSettings();

        //Reference of RecyclerView & Calling adopter
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);


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
        //------------------------------------------------------------------------------------------

    }



    private void getData() {


        if (next_page_url != null) {

            Log.d("call_status", "next page call");
            call = taskMetServer.getCommonAdByCategory(next_page_url);

            progressBar.setVisibility(View.VISIBLE);

            calling_api(call);

        }
        else if (Screen1_Filters.isFilterSelected && commonPostsList.isEmpty()) {

            pDialog.setTitleText("Loading");
            pDialog.show();

            call = taskMetServer.getCommonAdByCategory(number, mainCategory, Screen1_Filters.sort,
                    Screen1_Filters.priceFrom, Screen1_Filters.priceTo, latitude, longitude, current_date, Screen1_Filters.distance);

            Log.d("call_status", "\nsort" + Screen1_Filters.sort
            +"\nsort" + Screen1_Filters.sort
            +"\nprice from" + Screen1_Filters.priceFrom
            +"\nprice to" + Screen1_Filters.priceTo
            +"\nDistance" + Screen1_Filters.distance);

            Screen1_Filters.isFilterSelected = false;
            isFilterSelectedPrevious = true;

            calling_api(call);

        }
        else if (commonPostsList.isEmpty()) {

            pDialog.setTitleText("Loading");
            pDialog.show();

            Log.d("call_status", "Simple call");

            call = taskMetServer.getCommonAdByCategory(number, mainCategory, "ASC",
                    "1500", "100000", latitude, longitude, current_date, "100");

            calling_api(call);
        }

    }


    private void calling_api(Call<UserCommonAds> call) {

        call.enqueue(new Callback<UserCommonAds>() {
            @Override
            public void onResponse(Call<UserCommonAds> call, Response<UserCommonAds> response) {

                if (response.isSuccessful()) {

                    if(next_page_url == null){
                        pDialog.dismissWithAnimation();
                    }
                    progressBar.setVisibility(View.GONE);

                    UserCommonAds userCommonAds = response.body();

                    //Getting next page url if any.
                    next_page_url = userCommonAds.getNext_page_url();

                    commonPostsList =  userCommonAds.getCommonAds();

                    allPostItems.addAll(commonPostsList);


                    if (commonPostsList.size() > 0) {

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


                    ((Screen1_Common_Ads_Activity)getActivity()).changeView.setOnClickListener(new View.OnClickListener() {
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
            public void onFailure(Call<UserCommonAds> call, Throwable t) {
                pDialog.dismissWithAnimation();
            }
        });


    }

    public void goToAdDetails(int id) {

        ViewUsersAdDetails viewUsersAdDetails = new ViewUsersAdDetails(id);
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, viewUsersAdDetails);
        transaction.addToBackStack("tag");
        transaction.commit();

    }

    private void recyclerViewSettings() {

        if (!isListShow) {

            gridLayoutManager = new GridLayoutManager(getContext(), 2);
        /*        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % CommonAdList.ITEM_PER_AD == 0 && position != 0) {//your condition for showing ad
                    return 2; //2 number of user Ads in each row
                }
                return 1; //1 Google Ad after 5 rows
            }
        });*/
            recyclerView.setLayoutManager(gridLayoutManager);

            grid_adopter = new Common_Ads_Grid_Adopter(allPostItems, getContext(), new Common_Ads_Grid_Adopter.MyPOstItemClickListener() {
                @Override
                public void onItemClicked(CommonAdModel model) {

                    goToAdDetails(model.getId());

                }
            });

            recyclerView.setAdapter(grid_adopter);

        }
        else {

            linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

            recyclerView.setLayoutManager(linearLayoutManager);

            list_adopter = new Common_Ads_List_Adopter(allPostItems, getContext(), new Common_Ads_List_Adopter.MyPOstItemClickListener() {
                @Override
                public void onItemClicked(CommonAdModel model) {

                    goToAdDetails(model.getId());

                }
            });

            recyclerView.setAdapter(list_adopter);

        }

    }

    private void goToMap(String number, String mainCategory, String sub, String latitude, String longitude, String current_date) {

        UserAdsMapView userAdsMapView = new UserAdsMapView(number,mainCategory,sub,latitude,longitude,current_date,"Screen1_Common_Ads_Activity");
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