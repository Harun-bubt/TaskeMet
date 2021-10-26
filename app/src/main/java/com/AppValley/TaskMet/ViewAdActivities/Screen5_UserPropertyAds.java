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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AppValley.TaskMet.Home.Adopters.Marriage_Ads_Grid_Adopter;
import com.AppValley.TaskMet.Home.Adopters.Marriage_Ads_List_Adopter;
import com.AppValley.TaskMet.Home.Adopters.Property_Ads_Grid_Adopter;
import com.AppValley.TaskMet.Home.Adopters.Property_Ads_List_Adopter;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MarriageAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PropertyAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserMarriageAds;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserPropertyAds;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;
import com.krishna.securetimer.SecureTimer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Screen5_UserPropertyAds extends Fragment {

    View view;

    SharedPreferences sharedPreferences;
    String number, next_page_url = null, mainCategory, currency, latitude, longitude, current_date, propertyType, levels;

    SweetAlertDialog pDialog;
    ProgressBar progressBar;

    TaskMetServer taskMetServer;
    Call<UserPropertyAds> call;

    ArrayList<PropertyAdModel> propPostsList = new ArrayList<>();
    ArrayList<PropertyAdModel> allPropMarriageItems = new ArrayList<>();

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    Property_Ads_List_Adopter list_adopter;
    Property_Ads_Grid_Adopter grid_adopter;

    int currentItems, totalItems, scrollOutItems;
    boolean isListShow = true, isScrolling = true;
    public static boolean isFilterSelectedPrevious = false;

    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shopping_ad_list, container, false);

        getPropertyPostData();

        ((Screen5_Property_Ads_Activity)getActivity()).Ad_map_view.setVisibility(View.VISIBLE);
        ((Screen5_Property_Ads_Activity)getActivity()).changeView.setVisibility(View.VISIBLE);
        ((Screen5_Property_Ads_Activity)getActivity()).filterView.setVisibility(View.VISIBLE);

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

    private void getPropertyPostData() {

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);
        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        number = sharedPreferences.getString(Constants.NUMBER, Constants.NULL);
        currency = sharedPreferences.getString(Constants.CURRENCY, Constants.NULL);
        latitude = sharedPreferences.getString(Constants.LATITUDE, Constants.NULL);
        longitude = sharedPreferences.getString(Constants.LONGITUDE, Constants.NULL);
        mainCategory = sharedPreferences.getString(Constants.SELECTED_MAIN_CATEGORY, Constants.NULL);
        propertyType = sharedPreferences.getString(Constants.SELECTED_PROPERTY_TYPE, Constants.NULL);

        SecureTimer.with(getContext()).initialize();

        Date date = SecureTimer.with(getActivity()).getCurrentDate();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        current_date = date_format.format(date);

        ((Screen5_Property_Ads_Activity)getActivity()).Ad_map_view.setOnClickListener(new View.OnClickListener() {
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

            if (propertyType.equals(getResources().getString(R.string.plots_land))) {

                call = taskMetServer.getLandPropertyAds(next_page_url);

            } else {

                call = taskMetServer.getHouseShopPropertyAds(next_page_url);

            }

            progressBar.setVisibility(View.VISIBLE);

            calling_api(call);

        }
        else if (Screen5_Filters1.isFilterSelected && propPostsList.isEmpty()) {

            Log.d("call_status", "Filter call");

            pDialog.setTitleText("Loading");
            pDialog.show();

            if (propertyType.equals(getResources().getString(R.string.plots_land))) {

                call = taskMetServer.getLandPropertyAds(number, mainCategory, Screen5_Filters1.propertyPostType, propertyType
                        , Screen5_Filters1.areaType, Screen5_Filters1.costFrom, Screen5_Filters1.costTo,
                        Screen5_Filters1.areaFrom, Screen5_Filters1.areaTo, latitude, longitude, current_date, Screen5_Filters1.distance);

                Log.d("call_status", "Filter if condition"+
                        "\npropertyPostType "+Screen5_Filters1.propertyPostType+
                        "\nareaType "+Screen5_Filters1.areaType+
                        "\ncostFrom "+Screen5_Filters1.costFrom+
                        "\ncostTo "+Screen5_Filters1.costTo+
                        "\nareaFrom "+Screen5_Filters1.areaFrom+
                        "\nareaTo "+Screen5_Filters1.areaTo+
                        "\ndistance "+Screen5_Filters1.distance);

            }
            else {

                call = taskMetServer.getHouseShopPropertyAds(number, mainCategory, Screen5_Filters1.propertyPostType, propertyType
                        , Screen5_Filters1.areaType, Screen5_Filters1.costFrom, Screen5_Filters1.costTo,
                        Screen5_Filters1.areaFrom, Screen5_Filters1.areaTo, Screen5_Filters1.furnished,
                        "1","500", "1","500", latitude, longitude, current_date, Screen5_Filters1.distance);


                Log.d("call_status", "Filter else condition"+
                        "\npropertyPostType "+Screen5_Filters1.propertyPostType+
                        "\npropertyType "+propertyType+
                        "\nareaType "+Screen5_Filters1.areaType+
                        "\ncostFrom "+Screen5_Filters1.costFrom+
                        "\ncostTo "+Screen5_Filters1.costTo+
                        "\nareaFrom "+Screen5_Filters1.areaFrom+
                        "\nareaTo "+Screen5_Filters1.areaTo+
                        "\nfurnished "+Screen5_Filters1.furnished+
                        "\ndistance "+Screen5_Filters1.distance);

            }

            Screen5_Filters1.isFilterSelected = false;

            isFilterSelectedPrevious = true;

/*            Toast.makeText(getContext(), "Filter api is calling" + Screen5_Filters1.propertyPostType, Toast.LENGTH_SHORT).show();

            Log.d("checkPFilter", Screen5_Filters1.propertyPostType + propertyType
                    + Screen5_Filters1.areaType + Screen5_Filters1.costFrom + Screen5_Filters1.costTo +
                    Screen5_Filters1.areaFrom + Screen5_Filters1.areaTo + Screen5_Filters1.distance + propertyType);*/


            calling_api(call);

        }
        else if (propPostsList.isEmpty()) {

            Log.d("call_status", "First time call");

            pDialog.setTitleText("Loading");
            pDialog.show();

            Log.d("call_status", "Simple call");

            if (propertyType.equals(getResources().getString(R.string.plots_land))) {

                call = taskMetServer.getLandPropertyAds(number, mainCategory, "Rent,Sell",
                        propertyType, getString(R.string.all_area_type), "1500", "100000000", "1", "5000",
                        latitude, longitude, current_date, "120");

            } else {

                call = taskMetServer.getHouseShopPropertyAds(number, mainCategory, "Rent,Sell",
                        propertyType, getString(R.string.all_area_house), "1500", "100000000", "1", "5000", "yes,no",
                        "1","500", "1","500", latitude, longitude, current_date, "120");


            }

            calling_api(call);

        }


    }

    private void calling_api(Call<UserPropertyAds> call) {

        call.enqueue(new Callback<UserPropertyAds>() {
            @Override
            public void onResponse(Call<UserPropertyAds> call, Response<UserPropertyAds> response) {

                if (response.isSuccessful()) {

                    pDialog.dismissWithAnimation();

                    progressBar.setVisibility(View.GONE);

                    UserPropertyAds userPropertyAds = response.body();

                    //Getting next page url if any.
                    next_page_url = userPropertyAds.getNext_page_url();

                    propPostsList = userPropertyAds.getPropertyAds();

                    allPropMarriageItems.addAll(propPostsList);


                    if (propPostsList.size() > 0) {

                        if (!isListShow) {

                            grid_adopter.notifyDataSetChanged();

                        }else {

                            list_adopter.notifyDataSetChanged();

                        }

                    } else {

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

                    ((Screen5_Property_Ads_Activity)getActivity()).changeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Log.d("call_status", "First value: " + isListShow);

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
            public void onFailure(Call<UserPropertyAds> call, Throwable t) {
                pDialog.dismissWithAnimation();
            }
        });

    }

    private void recyclerViewSettings() {

        if (!isListShow) {

            gridLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);

            //Create adapter
            grid_adopter = new Property_Ads_Grid_Adopter(allPropMarriageItems, getContext(), new Property_Ads_Grid_Adopter.MyPOstItemClickListener() {
                @Override
                public void onItemClicked(PropertyAdModel model) {

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
            list_adopter = new Property_Ads_List_Adopter(allPropMarriageItems, getContext(), new Property_Ads_List_Adopter.MyPOstItemClickListener() {
                @Override
                public void onItemClicked(PropertyAdModel model) {

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

        UserAdsMapView userAdsMapView = new UserAdsMapView(number,mainCategory,sub,latitude,longitude,current_date,"Screen5_Property_Ads_Activity");
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
