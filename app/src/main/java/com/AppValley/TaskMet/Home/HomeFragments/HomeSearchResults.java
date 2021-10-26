package com.AppValley.TaskMet.Home.HomeFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.AppValley.TaskMet.Home.Adopters.Shop_Ads_Grid_Adopter;
import com.AppValley.TaskMet.Home.Adopters.Shop_Ads_List_Adopter;
import com.AppValley.TaskMet.Home.FragmentViewActivity;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.HomeAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserShoppingAds;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.ViewAdActivities.Screen2_Filters;
import com.AppValley.TaskMet.ViewAdActivities.Screen2_Shopping_Ads_Activity;
import com.AppValley.TaskMet.constant.Constants;
import com.krishna.securetimer.SecureTimer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeSearchResults extends Fragment {

    View view;
    String search_line;
    ImageView changeView;
    TextView toolBarTitle;

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

    SharedPreferences preferences;
    SharedPreferences.Editor fragmentNameEditor;

    public HomeSearchResults(String search_line) {
        this.search_line = search_line;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.home_search_results, container, false);

        Toast.makeText(getContext(), search_line, Toast.LENGTH_SHORT).show();

        getSearchAdsData();

        return view;

    }

    private void getSearchAdsData() {

        preferences = getContext().getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
        fragmentNameEditor = preferences.edit();

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

        //-------------------------------- Settings RecyclerView -----------------------------------
        recyclerView = view.findViewById(R.id.recyclerview);
        changeView = view.findViewById(R.id.changeView);
        toolBarTitle = view.findViewById(R.id.toolBarTitle);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        toolBarTitle.setText("Search Result for: "+"\""+search_line+"\"");

        gridLayoutManager = new GridLayoutManager(getContext(), 2);

        //Reference of RecyclerView & Calling adopter
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        //Create adapter
        list_adopter = new Shop_Ads_List_Adopter(allPostShopItems, getContext(), new Shop_Ads_List_Adopter.MyPOstItemClickListener() {
            @Override
            public void onItemClicked(HomeAdModel model) {

                goToAdDetails(model.getId());

            }
        });

        //Create adapter
        grid_adopter = new Shop_Ads_Grid_Adopter(allPostShopItems, getContext(), new Shop_Ads_Grid_Adopter.MyPOstItemClickListener() {
            @Override
            public void onItemClicked(HomeAdModel model) {

                goToAdDetails(model.getId());

            }
        });


        if (!isListShow) {

            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(grid_adopter);

        } else {

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(list_adopter);

        }

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
            call = taskMetServer.getSearchAds(next_page_url);

            progressBar.setVisibility(View.VISIBLE);

            calling_api(call);

        }
        else if (shopPostsList.isEmpty()) {

            pDialog.setTitleText("Loading");
            pDialog.show();

            Log.d("call_status", "Simple call");

            call = taskMetServer.getSearchAds(search_line, number, latitude, longitude, current_date);

            calling_api(call);
        }

    }

    private void calling_api(Call<UserShoppingAds> call) {

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

                        grid_adopter.notifyDataSetChanged();
                        list_adopter.notifyDataSetChanged();

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

                    changeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Log.d("call_status", "First value: "+isListShow);

                            if (isListShow) {

                                recyclerView.setLayoutManager(gridLayoutManager);
                                recyclerView.setAdapter(grid_adopter);
                                isListShow = false;

                            } else {

                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(list_adopter);
                                isListShow = true;

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

    private void goToAdDetails(int id) {

        Intent intent = new Intent(getContext(), FragmentViewActivity.class);
        fragmentNameEditor.putString(Constants.FRAGMENT_NAME, "PostDetails");
        fragmentNameEditor.putInt(Constants.ID, id);
        fragmentNameEditor.apply();
        startActivity(intent);

    }


}