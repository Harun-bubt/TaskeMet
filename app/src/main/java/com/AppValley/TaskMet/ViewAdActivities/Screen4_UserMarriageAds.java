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
import com.AppValley.TaskMet.Home.Adopters.Marriage_Ads_Grid_Adopter;
import com.AppValley.TaskMet.Home.Adopters.Marriage_Ads_List_Adopter;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.JobAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MarriageAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserJobAds;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserMarriageAds;
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

public class Screen4_UserMarriageAds extends Fragment {

    View view;
    String martial_status;
    SharedPreferences sharedPreferences;

    String number, next_page_url = null, mainCategory, currency, latitude, longitude, current_date;

    SweetAlertDialog pDialog;
    ProgressBar progressBar;

    TaskMetServer taskMetServer;
    Call<UserMarriageAds> call;

    ArrayList<MarriageAdModel> marriagePostsList = new ArrayList<>();
    ArrayList<MarriageAdModel> allPostMarriageItems = new ArrayList<>();

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    Marriage_Ads_List_Adopter list_adopter;
    Marriage_Ads_Grid_Adopter grid_adopter;

    int currentItems, totalItems, scrollOutItems;
    boolean isListShow = true, isScrolling = true;

    String lookingFor;
    public static boolean isFilterSelectedPrevious = false;
    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shopping_ad_list, container, false);

        getMarriagePostData();

        ((Screen4_Marriage_Ads_Activity)getActivity()).Ad_map_view.setVisibility(View.VISIBLE);
        ((Screen4_Marriage_Ads_Activity)getActivity()).changeView.setVisibility(View.VISIBLE);
        ((Screen4_Marriage_Ads_Activity)getActivity()).filterView.setVisibility(View.VISIBLE);

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

    private void getMarriagePostData() {

        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);
        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        number = sharedPreferences.getString(Constants.NUMBER, Constants.NULL);
        currency = sharedPreferences.getString(Constants.CURRENCY, Constants.NULL);
        latitude = sharedPreferences.getString(Constants.LATITUDE, Constants.NULL);
        longitude = sharedPreferences.getString(Constants.LONGITUDE, Constants.NULL);
        mainCategory = sharedPreferences.getString(Constants.SELECTED_MAIN_CATEGORY, Constants.NULL);
        lookingFor = sharedPreferences.getString(Constants.SELECTED_LOOKING_FOR, Constants.NULL);

        SecureTimer.with(getContext()).initialize();

        Date date = SecureTimer.with(getActivity()).getCurrentDate();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        current_date = date_format.format(date);

        ((Screen4_Marriage_Ads_Activity)getActivity()).Ad_map_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sub = null;

                goToMap(number,mainCategory,sub,latitude,longitude,current_date);

            }
        });

        isFilterSelectedPrevious = false;

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
            call = taskMetServer.getMarriageAdByCategory(next_page_url);

            progressBar.setVisibility(View.VISIBLE);

            calling_api(call);

        }

        else if (Screen4_Filters.isFilterSelected && marriagePostsList.isEmpty()) {

            pDialog.setTitleText("Loading");
            pDialog.show();

            call = taskMetServer.getMarriageAdByCategory(number, mainCategory, Screen4_Filters.looking_for, Screen4_Filters.maritial_status,
                    Screen4_Filters.ageFrom, Screen4_Filters.ageTo, latitude, longitude, current_date, Screen4_Filters.distance);

            Screen4_Filters.isFilterSelected = false;
            isFilterSelectedPrevious = true;

            calling_api(call);

        }
        else if (marriagePostsList.isEmpty()) {

            pDialog.setTitleText("Loading");
            pDialog.show();

            Log.d("call_status", "Simple call");

            martial_status = getString(R.string.all_martial_status);

            call = taskMetServer.getMarriageAdByCategory(number, mainCategory, lookingFor,
                    martial_status, "18", "60", latitude, longitude, current_date, "80");

            calling_api(call);

        }

    }

    private void calling_api(Call<UserMarriageAds> call) {

        call.enqueue(new Callback<UserMarriageAds>() {
            @Override
            public void onResponse(Call<UserMarriageAds> call, Response<UserMarriageAds> response) {

                if (response.isSuccessful()) {

                    if(next_page_url == null){

                        pDialog.dismissWithAnimation();

                    }
                    progressBar.setVisibility(View.GONE);

                    UserMarriageAds userMarriageAds = response.body();

                    //Getting next page url if any.
                    next_page_url = userMarriageAds.getNext_page_url();

                    marriagePostsList =  userMarriageAds.getMarriageAds();

                    allPostMarriageItems.addAll(marriagePostsList);


                    if (marriagePostsList.size() > 0) {

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

                    ((Screen4_Marriage_Ads_Activity)getActivity()).changeView.setOnClickListener(new View.OnClickListener() {
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
            public void onFailure(Call<UserMarriageAds> call, Throwable t) {
                pDialog.dismissWithAnimation();
            }
        });

    }


    private void recyclerViewSettings() {

        if (!isListShow) {

            gridLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);

            //Create adapter
            grid_adopter = new Marriage_Ads_Grid_Adopter(allPostMarriageItems, getContext(), new Marriage_Ads_Grid_Adopter.MyPOstItemClickListener() {
                @Override
                public void onItemClicked(MarriageAdModel model) {

                    goToDetailsFragment(model.getId());

                }
            });


            recyclerView.setAdapter(grid_adopter);

        }
        else {

            //Reference of RecyclerView & Calling adopter
            linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);

            //Create adapter
            list_adopter = new Marriage_Ads_List_Adopter(allPostMarriageItems, getContext(), new Marriage_Ads_List_Adopter.MyPOstItemClickListener() {
                @Override
                public void onItemClicked(MarriageAdModel model) {

                    goToDetailsFragment(model.getId());

                }
            });


            //isListShow = false;
            recyclerView.setAdapter(list_adopter);

        }

    }


    public void goToDetailsFragment(int id) {

        ViewUsersAdDetails viewUsersAdDetails = new ViewUsersAdDetails(id);
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayoutAdView, viewUsersAdDetails);
        transaction.addToBackStack("tag");
        transaction.commit();

    }

    private void goToMap(String number, String mainCategory, String sub, String latitude, String longitude, String current_date) {

        UserAdsMapView userAdsMapView = new UserAdsMapView(number,mainCategory,sub,latitude,longitude,current_date,"Screen4_Marriage_Ads_Activity");
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
