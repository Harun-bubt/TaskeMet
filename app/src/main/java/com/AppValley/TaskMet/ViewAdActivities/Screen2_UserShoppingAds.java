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

import com.AppValley.TaskMet.Home.Adopters.Shop_Ads_Grid_Adopter;
import com.AppValley.TaskMet.Home.Adopters.Shop_Ads_List_Adopter;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.HomeAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserShoppingAds;
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

public class Screen2_UserShoppingAds extends Fragment {

    View view;
    SharedPreferences sharedPreferences;
    int currentItems, totalItems, scrollOutItems;
    boolean isListShow = true, isScrolling = true;
    String number,mainCategory,subCategory,currency,latitude,longitude,current_date,next_page_url = null;

    SweetAlertDialog pDialog;
    ProgressBar progressBar;

    TaskMetServer taskMetServer;
    Call<UserShoppingAds> call;

    ArrayList<HomeAdModel> shopPostsList = new ArrayList<>();
    ArrayList<HomeAdModel> allPostShopItems = new ArrayList<>();

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    Shop_Ads_List_Adopter list_adopter;
    Shop_Ads_Grid_Adopter grid_adopter;

    FragmentManager fragmentManager;
    public static boolean isFilterSelectedPrevious = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shopping_ad_list, container, false);

        getShopPostData();

        ((Screen2_Shopping_Ads_Activity)getActivity()).Ad_map_view.setVisibility(View.VISIBLE);
        ((Screen2_Shopping_Ads_Activity)getActivity()).changeView.setVisibility(View.VISIBLE);
        ((Screen2_Shopping_Ads_Activity)getActivity()).filterView.setVisibility(View.VISIBLE);

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


    private void getShopPostData() {

        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);
        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        number = sharedPreferences.getString(Constants.NUMBER, Constants.NULL);
        currency = sharedPreferences.getString(Constants.CURRENCY, Constants.NULL);
        latitude = sharedPreferences.getString(Constants.LATITUDE, Constants.NULL);
        longitude = sharedPreferences.getString(Constants.LONGITUDE, Constants.NULL);
        mainCategory = sharedPreferences.getString(Constants.SELECTED_MAIN_CATEGORY, Constants.NULL);
        subCategory = sharedPreferences.getString(Constants.SELECTED_SUB_CATEGORY, Constants.NULL);
        SecureTimer.with(getContext()).initialize();

        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);

        Date date = SecureTimer.with(getActivity()).getCurrentDate();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        current_date = date_format.format(date);

        ((Screen2_Shopping_Ads_Activity)getActivity()).Ad_map_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToMap(number,mainCategory,subCategory,latitude,longitude,current_date);

            }
        });

        ((Screen2_Shopping_Ads_Activity)getActivity()).filterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Screen2_Filters screen2Filters = new Screen2_Filters();
                androidx.fragment.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framelayoutAdView, screen2Filters);
                transaction.addToBackStack("tag");
                transaction.commit();

            }
        });

        isFilterSelectedPrevious = false;

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
            call = taskMetServer.getHomeAdByCategory(next_page_url);

            progressBar.setVisibility(View.VISIBLE);

            calling_api(call);

        }
        else if (Screen2_Filters.isFilterSelected && shopPostsList.isEmpty()) {

            pDialog.setTitleText("Loading");
            pDialog.show();

            call = taskMetServer.getHomeAdByCategory(number, mainCategory, subCategory, Screen2_Filters.sort, Screen2_Filters.condition,
                    Screen2_Filters.priceFrom, Screen2_Filters.priceTo, latitude, longitude, current_date, Screen2_Filters.distance);

            Screen2_Filters.isFilterSelected = false;
            isFilterSelectedPrevious = true;

            calling_api(call);

        }
        else if (shopPostsList.isEmpty()) {

            pDialog.setTitleText("Loading");
            pDialog.show();

            Log.d("call_status", "Simple call");

            call = taskMetServer.getHomeAdByCategory(number, mainCategory, subCategory, "ASC", "Used,New",
                    "1500", "250000", latitude, longitude, current_date, "100");

            calling_api(call);
        }

    }


    public void calling_api(Call<UserShoppingAds> call) {

        call.enqueue(new Callback<UserShoppingAds>() {
            @Override
            public void onResponse(Call<UserShoppingAds> call, Response<UserShoppingAds> response) {

                if (response.isSuccessful()) {

                    if(next_page_url == null){
                        pDialog.dismissWithAnimation();
                    }
                    progressBar.setVisibility(View.GONE);

                    UserShoppingAds userShoppingAds = response.body();

                    //Getting next page url if any.
                    next_page_url = userShoppingAds.getNext_page_url();

                    shopPostsList =  userShoppingAds.getCommonAds();

                    allPostShopItems.addAll(shopPostsList);


                    if (shopPostsList.size() > 0) {

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


                    ((Screen2_Shopping_Ads_Activity)getActivity()).changeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

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
            public void onFailure(Call<UserShoppingAds> call, Throwable t) {

            }
        });

    }

    private void recyclerViewSettings() {

        if (!isListShow) {

            Log.d("call_status", "Grid session execute");


            gridLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);

            //Create adapter
            grid_adopter = new Shop_Ads_Grid_Adopter(allPostShopItems, getContext(), new Shop_Ads_Grid_Adopter.MyPOstItemClickListener() {
                @Override
                public void onItemClicked(HomeAdModel model) {

                    goToAdDetails(model.getId());

                }
            });

            recyclerView.setAdapter(grid_adopter);

        }
        else {

            Log.d("call_status", "List session execute");

            //Reference of RecyclerView & Calling adopter
            linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);

            //Create adapter
            list_adopter = new Shop_Ads_List_Adopter(allPostShopItems, getContext(), new Shop_Ads_List_Adopter.MyPOstItemClickListener() {
                @Override
                public void onItemClicked(HomeAdModel model) {

                    goToAdDetails(model.getId());

                }
            });

            //isListShow = false;
            recyclerView.setAdapter(list_adopter);

        }

    }


    public void goToAdDetails(int id) {

        ViewUsersAdDetails viewUsersAdDetails = new ViewUsersAdDetails(id);
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, viewUsersAdDetails);
        transaction.addToBackStack("tag");
        transaction.commit();

    }

    private void goToMap(String number, String mainCategory, String sub, String latitude, String longitude, String current_date) {

        UserAdsMapView userAdsMapView = new UserAdsMapView(number,mainCategory,sub,latitude,longitude,current_date,"Screen2_Shopping_Ads_Activity");
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
