package com.AppValley.TaskMet.Home.HomeFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AppValley.TaskMet.GoogleMaps.MapModels.AddressData;
import com.AppValley.TaskMet.Home.Adopters.HomeItemAdapter;
import com.AppValley.TaskMet.Home.Adopters.Shop_Ads_Grid_Adopter;
import com.AppValley.TaskMet.Home.Adopters.Shop_Ads_List_Adopter;
import com.AppValley.TaskMet.Home.FragmentViewActivity;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.HomeAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MyPostWithLimitModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.ProfileDataModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.Status_Response;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserShoppingAds;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.WalletModel;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.ViewAdActivities.Screen2_Filters;
import com.AppValley.TaskMet.ViewAdActivities.Screen2_Shopping_Ads_Activity;
import com.AppValley.TaskMet.ViewAdActivities.ViewUsersAdDetails;
import com.AppValley.TaskMet.constant.Constants;
import com.AppValley.TaskMet.constant.PlacePicker;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.krishna.securetimer.SecureTimer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment implements HomeItemAdapter.ItemClickListener {

    ImageView search_icon;
    SpinKitView progressBar;
    LinearLayout no_recent_ad;
    boolean show_ads = true, isScrolling = true;

    int currentItems, totalItems, scrollOutItems;
    String search_data, call_api = Constants.TRUE, next_page_url = null, current_date,show_home_ads = Constants.TRUE;

    FragmentManager fragmentManager;

    Call<UserShoppingAds> call;
    ArrayList<HomeAdModel> shopPostsList = new ArrayList<>();
    ArrayList<HomeAdModel> allPostShopItems = new ArrayList<>();

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    Shop_Ads_Grid_Adopter grid_adopter;

    View view;

    //----------------------------------- profile data var -----------------------------------------
    TaskMetServer taskMetServer;
    public static SharedPreferences sharedPreferences;

    double Latitude, Longitude;
    String id, number, uid, name, email, email_verified,
            profile_pic, country_name, city_name, country_code,
            address, currency, latitude, longitude, token, CountryName,
            Country_code, Currency, CityName, fullAddress, shortAddress,
            Address, mCountryCode, TAG = "Home_new_location";

    TextView location, ad_status;

    LinearLayout searchBarLayout, shopping, service;
    SharedPreferences preferences;
    SharedPreferences.Editor fragmentNameEditor,userData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        preferences = getContext().getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
        fragmentNameEditor = preferences.edit();

        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        userData = sharedPreferences.edit();

        number = sharedPreferences.getString(Constants.NUMBER, Constants.NULL);
        show_home_ads = sharedPreferences.getString(Constants.SHOW_ADS, Constants.TRUE);
        call_api = sharedPreferences.getString(Constants.CALL_API, Constants.FALSE);

        initializeAllViews();

        if(show_home_ads.equals(Constants.TRUE)){

            Log.d(TAG, "show_home_ads: "+show_home_ads);

            show_ads = false;
            ad_status.setText(R.string.hide);

        }
        else {

            Log.d(TAG, "show_home_ads: "+show_home_ads);

            show_ads = true;
            ad_status.setText(R.string.show);

        }

        if (call_api.equals(Constants.TRUE)) {

            callProfileAPI();

        } else {

            loadData();

            if(show_home_ads.equals(Constants.TRUE)){
                callAdApi();
            }
        }

        location.setText(address);
        location.setEnabled(true);

        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToshopingDetails();
            }
        });

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToServiceDetails();
            }
        });

        ad_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Ad status: "+show_ads);

                if (show_ads) {

                    userData.putString(Constants.SHOW_ADS, Constants.TRUE);
                    userData.apply();

                  /*  if (shopPostsList.size() <= 0) {
                        no_recent_ad.setVisibility(View.VISIBLE);
                    }*/

                    ad_status.setText(R.string.hide);

                    recyclerView.setVisibility(View.VISIBLE);
                    show_ads = false;

                    callAdApi();

                }

                else {

                    userData.putString(Constants.SHOW_ADS, Constants.FALSE);
                    userData.apply();

                    ad_status.setText(R.string.show);
                    no_recent_ad.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    show_ads = true;

                }

            }
        });

        return view;

    }

    public void initializeAllViews() {

        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        progressBar = view.findViewById(R.id.progress_bar);
        ad_status = view.findViewById(R.id.ad_status);
        location = view.findViewById(R.id.location);
        shopping = view.findViewById(R.id.shopping);
        service = view.findViewById(R.id.service);
        recyclerView = view.findViewById(R.id.homeRecyclerView);
        search_icon = view.findViewById(R.id.search_icon);
        no_recent_ad = view.findViewById(R.id.no_recent_ad);

        no_recent_ad.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

    }

    public void callProfileAPI() {

        SweetAlertDialog my_progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        my_progress.setTitleText("Loading Profile Data");
        my_progress.show();

        Call<ProfileDataModel> call = taskMetServer.getProfileData(number);
        call.enqueue(new Callback<ProfileDataModel>() {
            @Override
            public void onResponse(Call<ProfileDataModel> call, Response<ProfileDataModel> response) {

                if (response.isSuccessful()) {
                    ProfileDataModel user_data = response.body();
                    if (user_data != null) {

                        RetrieveStoreUserInfo(user_data);

                        my_progress.dismissWithAnimation();

                        SharedPreferences.Editor user_detail_editor;
                        user_detail_editor = sharedPreferences.edit();
                        user_detail_editor.putString(Constants.CALL_API, Constants.FALSE);
                        user_detail_editor.apply();

                        if(show_home_ads.equals(Constants.TRUE)){

                            Log.d(TAG, "Ad api called");
                            callAdApi();

                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ProfileDataModel> call, Throwable t) {

                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setContentText("Dear user your account is created successfully, but we are facing difficulty while loading your profile data. Please restart TaskMet app.")
                        .setConfirmText("OK")
                        .show();

                SharedPreferences.Editor user_detail_editor;
                user_detail_editor = sharedPreferences.edit();
                user_detail_editor.putString(Constants.USER_EXIST, Constants.TRUE);
                user_detail_editor.putString(Constants.NUMBER, number);
                user_detail_editor.apply();
            }
        });

    }

    private void loadData() {

        number = sharedPreferences.getString(Constants.NUMBER, Constants.NULL);
        uid = sharedPreferences.getString(Constants.UID, Constants.NULL);
        name = sharedPreferences.getString(Constants.NAME, Constants.NULL);
        email = sharedPreferences.getString(Constants.EMAIL, Constants.NULL);
        email_verified = sharedPreferences.getString(Constants.EMAIL_VERIFIED, Constants.NULL);
        profile_pic = sharedPreferences.getString(Constants.PROFILE_PIC, Constants.NULL);
        country_name = sharedPreferences.getString(Constants.COUNTRY_NAME, Constants.NULL);

        city_name = sharedPreferences.getString(Constants.CITY_NAME, Constants.NULL);
        country_code = sharedPreferences.getString(Constants.COUNTRY_CODE, Constants.NULL);
        address = sharedPreferences.getString(Constants.ADDRESS, Constants.NULL);
        currency = sharedPreferences.getString(Constants.CURRENCY, Constants.NULL);
        country_name = sharedPreferences.getString(Constants.COUNTRY_NAME, Constants.NULL);
        latitude = sharedPreferences.getString(Constants.LATITUDE, Constants.NULL);
        longitude = sharedPreferences.getString(Constants.LONGITUDE, Constants.NULL);
        token = sharedPreferences.getString(Constants.TOKEN, Constants.NULL);

    }

    @Override
    public void onResume() {
        super.onResume();

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createLocationRequest();

            }
        });

        //--------- Toolbar............................................

        EditText search = view.findViewById(R.id.search);
        TextView location = view.findViewById(R.id.location);
        ImageView clearSearch = view.findViewById(R.id.clearSearch);
        searchBarLayout = view.findViewById(R.id.topSearchBarLayout);

        //.......shop view all category control up section.........

        clearSearch.setVisibility(View.INVISIBLE);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                search_data = search.getText().toString().trim();

                if (TextUtils.isEmpty(search_data)) {
                    clearSearch.setVisibility(View.INVISIBLE);
                } else {
                    clearSearch.setVisibility(View.VISIBLE);

                }

            }
        });

        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /* Write your logic here that will be executed when user taps next button */

                    String search_line = search.getText().toString().trim();

                    if (!TextUtils.isEmpty(search_line)) {

                        getSearchResult(search_line);

                    } else {

                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Please Type something in the search box first.")
                                .show();


                    }

                    handled = true;
                }
                return handled;
            }
        });

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String search_line = search.getText().toString().trim();

                if (!TextUtils.isEmpty(search_line)) {

                    getSearchResult(search_line);

                } else {

                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setContentText("Please Type something in the search box first.")
                            .show();


                }

            }
        });

        //-----------------

    }

    private void getSearchResult(String search_line) {

        Intent intent = new Intent(getContext(), FragmentViewActivity.class);
        fragmentNameEditor.putString(Constants.FRAGMENT_NAME, "SearchResult");
        fragmentNameEditor.putString(Constants.SEARCH_LINE, search_line);
        fragmentNameEditor.apply();
        startActivity(intent);

    }

    private void callAdApi() {

        //--------------------------------- Getting Current Date -----------------------------------
        SecureTimer.with(getContext()).initialize();

        Date date = SecureTimer.with(getActivity()).getCurrentDate();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        current_date = curFormater.format(date);

        //-------------------------------- Settings RecyclerView -----------------------------------
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        //Reference of RecyclerView & Calling adopter
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        //Create adapter
        grid_adopter = new Shop_Ads_Grid_Adopter(allPostShopItems, getContext(), new Shop_Ads_Grid_Adopter.MyPOstItemClickListener() {
            @Override
            public void onItemClicked(HomeAdModel model) {

                goToAdDetails(model.getId());

            }
        });

        //Set adapter to RecyclerView
        recyclerView.setAdapter(grid_adopter);

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

                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();

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

            Log.d(TAG, "next page call");
            call = taskMetServer.getHomeRecentAd(next_page_url);

            calling_api(call);

        } else if (shopPostsList.isEmpty()) {

            Log.d(TAG, "Simple call");

            call = taskMetServer.getHomeRecentAd(number, latitude, longitude, current_date);
            progressBar.setVisibility(View.VISIBLE);

            calling_api(call);
        }

    }

    private void calling_api(Call<UserShoppingAds> call) {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                call.enqueue(new Callback<UserShoppingAds>() {
                    @Override
                    public void onResponse(Call<UserShoppingAds> call, Response<UserShoppingAds> response) {

                        if (response.isSuccessful()) {

                            UserShoppingAds userShoppingAds = response.body();

                            //Getting next page url if any.
                            next_page_url = userShoppingAds.getNext_page_url();

                            shopPostsList = userShoppingAds.getCommonAds();

                            allPostShopItems.addAll(shopPostsList);


                            if (shopPostsList.size() > 0) {

                                grid_adopter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);

                            } else {

                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.INVISIBLE);
                                no_recent_ad.setVisibility(View.VISIBLE);


                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<UserShoppingAds> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        thread.start();

    }

    private void goToAdDetails(int id) {

        Intent intent = new Intent(getContext(), FragmentViewActivity.class);
        fragmentNameEditor.putString(Constants.FRAGMENT_NAME, "PostDetails");
        fragmentNameEditor.putInt(Constants.ID, id);
        fragmentNameEditor.apply();
        startActivity(intent);

    }

    //To enable & disable gps
    protected void createLocationRequest() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(getContext());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                openGoogleMaps();

            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {

                    try {

                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(getActivity(),
                                Constants.REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {

                    }
                }
            }
        });
    }

    public void openGoogleMaps() {

        Intent intent = new PlacePicker.IntentBuilder()
                .setGoogleMapApiKey(getResources().getString(R.string.map_api_key))
                .setLatLong(33.6844, 73.0479)
                .setMapZoom(14.0f)
                .setAddressRequired(true)
                .setPrimaryTextColor(R.color.black)
                .build(getActivity());
        openMapsForLocation.launch(intent);
    }

    public void goToshopingDetails() {
        Intent intent = new Intent(getContext(), FragmentViewActivity.class);
        fragmentNameEditor.putString(Constants.FRAGMENT_NAME, "shopingDetails");
        fragmentNameEditor.apply();
        startActivity(intent);
    }

    public void goToServiceDetails() {
        Intent intent = new Intent(getContext(), FragmentViewActivity.class);
        fragmentNameEditor.putString(Constants.FRAGMENT_NAME, "serviceDetails");
        fragmentNameEditor.apply();
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    ActivityResultLauncher<Intent> openMapsForLocation = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    //Getting location from Google Map
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();

                        List<Address> address = new ArrayList<>();

                        if (data != null) {
                            AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);

                            //Decimal format to round value up to 4 decimal points
                            DecimalFormat dFormat = new DecimalFormat("#.#####");

                            //Rounding decimal values of lat & long
                            Latitude = addressData.getLatitude();
                            Latitude = Double.parseDouble(dFormat.format(Latitude));

                            Longitude = addressData.getLongitude();
                            Longitude = Double.parseDouble(dFormat.format(Longitude));


                            Geocoder geoCoder = new Geocoder(getContext(), Locale.getDefault());
                            try {
                                List<Address> addresses = geoCoder.getFromLocation(Latitude, Longitude, 1);

                                address = addresses;

                                CountryName = address.get(0).getCountryName();

                                if (CountryName.equals("Pakistan") || CountryName.equals("India") || CountryName.equals("Bangladesh")) {


                                    if (addresses != null && addresses.size() != 0) {

                                        fullAddress = addresses.get(0).getAddressLine(0);
                                        shortAddress = generateFinalAddress(fullAddress).trim();
                                        mCountryCode = addresses.get(0).getCountryCode();
                                        location.setError(null);
                                        location.setText(fullAddress);

                                        CityName = address.get(0).getLocality();
                                        Address = address.get(0).getAddressLine(0);
                                        Country_code = address.get(0).getCountryCode();

                                        if (CountryName.equals("Pakistan")) {
                                            Currency = "Rs";
                                        } else if (CountryName.equals("Bangladesh")) {
                                            Currency = "Tk";
                                        } else if (CountryName.equals("India")) {
                                            Currency = "₹";
                                        }

                                        Log.d(TAG, number + "\n" + CountryName + "\n" + CityName + "\n" + Country_code + "\n" + Address + "\n" + String.valueOf(Latitude) + "\n" + String.valueOf(Longitude));

                                        Thread thread = new Thread(new Runnable() {

                                            @Override
                                            public void run() {

                                                Call<Status_Response> call = taskMetServer.updateProfileLocation(number, CountryName, CityName, Country_code, Address, String.valueOf(Latitude), String.valueOf(Longitude), Currency);
                                                call.enqueue(new Callback<Status_Response>() {
                                                    @Override
                                                    public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                                                        if (response.isSuccessful()) {

                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString(Constants.COUNTRY_NAME, CountryName);
                                                            editor.putString(Constants.CITY_NAME, CityName);
                                                            editor.putString(Constants.COUNTRY_CODE, Country_code);
                                                            editor.putString(Constants.ADDRESS, Address);
                                                            editor.putString(Constants.CURRENCY, currency);
                                                            editor.putString(Constants.LATITUDE, String.valueOf(Latitude));
                                                            editor.putString(Constants.LONGITUDE, String.valueOf(Longitude));

                                                            editor.apply();

                                                            callAdApi();

                                                            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                                    .setContentText("Your location is updated successfully")
                                                                    .setConfirmText("Ok")
                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                        @Override
                                                                        public void onClick(SweetAlertDialog sDialog) {
                                                                            sDialog.dismissWithAnimation();
                                                                        }
                                                                    })
                                                                    .show();

                                                        }

                                                    }

                                                    @Override
                                                    public void onFailure(Call<Status_Response> call, Throwable t) {

                                                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                                .setContentText("Something went wrong while updating your location. Please try again.")
                                                                .setConfirmText("Ok")
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        sDialog.dismissWithAnimation();
                                                                    }
                                                                })
                                                                .show();

                                                    }
                                                });
                                            }
                                        });
                                        thread.start();


                                    } else {

                                        shortAddress = "";
                                        fullAddress = "";
                                    }

                                } else {

                                    location.setText("");
                                    location.setHint(getResources().getString(R.string.select_location));

                                    Latitude = 0.0;
                                    Longitude = 0.0;

                                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                            .setContentText("TaskMet Service is only available in Bangladesh, Pakistan and India countries.")
                                            .setConfirmText("Ok")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                }
                                            })
                                            .show();
                                }

                            } catch (Exception e) {
                                shortAddress = "";
                                fullAddress = "";
                                address = null;
                                e.printStackTrace();
                            }

                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(Latitude, Longitude, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                }
            });

    private String generateFinalAddress(String address) {
        String strRtr;
        String[] s = address.split(",");
        if (s.length >= 3)
            strRtr = s[1] + "," + s[2];
        else if (s.length == 2)
            strRtr = s[1];
        else
            strRtr = s[0];
        return strRtr;

    }

    public void RetrieveStoreUserInfo(ProfileDataModel user_data) {

        SharedPreferences.Editor user_detail_editor;

        WalletModel walletModel = user_data.getWallet();

        user_detail_editor = sharedPreferences.edit();

        user_detail_editor.putString(Constants.USER_EXIST, Constants.TRUE);

        user_detail_editor.putString(Constants.ID, user_data.getId());
        user_detail_editor.putString(Constants.NUMBER, number);
        user_detail_editor.putString(Constants.UID, user_data.getUid());
        user_detail_editor.putString(Constants.NAME, user_data.getName());
        user_detail_editor.putString(Constants.EMAIL, user_data.getEmail());
        user_detail_editor.putString(Constants.EMAIL_VERIFIED, user_data.getEmail_verified());
        user_detail_editor.putString(Constants.PROFILE_PIC, user_data.getProfile_pic());
        user_detail_editor.putString(Constants.COUNTRY_NAME, user_data.getCountry_name());
        user_detail_editor.putString(Constants.CITY_NAME, user_data.getCity_name());
        user_detail_editor.putString(Constants.COUNTRY_CODE, user_data.getCountry_code());
        user_detail_editor.putString(Constants.ADDRESS, user_data.getAddress());
        user_detail_editor.putString(Constants.CURRENCY, user_data.getCurrency());
        user_detail_editor.putString(Constants.LATITUDE, user_data.getLatitude());
        user_detail_editor.putString(Constants.LONGITUDE, user_data.getLongitude());
        user_detail_editor.putString(Constants.TOKEN, user_data.getToken());

        user_detail_editor.putString(Constants.GC_BALANCE, walletModel.getGC());
        user_detail_editor.putString(Constants.RC_BALANCE, walletModel.getRC());
        user_detail_editor.putString(Constants.ACCOUNT_TYPE, walletModel.getAccount_type());
        user_detail_editor.apply();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call!=null){
            call.cancel();
        }
    }
}