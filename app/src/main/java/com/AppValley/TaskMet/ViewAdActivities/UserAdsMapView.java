package com.AppValley.TaskMet.ViewAdActivities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.UserAdsMapModel;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdsMapView extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    View view;

    String id,number_str,screen_name;
    GoogleMap map;
    MapView mapView;
    Button ad_button;
    Bitmap b,smallMarker;
    BitmapDescriptor ad_marker;

    Call<List<UserAdsMapModel>> call;

    TaskMetServer taskMetServer;
    FragmentManager fragmentManager;
    List<UserAdsMapModel> userAdMarkers = new ArrayList<>();

    String number, main, sub, lat, log, date;

    public UserAdsMapView(String number, String main, String sub, String lat, String log, String date,String screen_name) {

        this.number = number;
        this.main = main;
        this.sub = sub;
        this.lat = lat;
        this.log = log;
        this.date = date;
        this.screen_name = screen_name;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.user_ads_map_view, container, false);

        if(screen_name.equals("Screen1_Common_Ads_Activity")){

            ((Screen1_Common_Ads_Activity)getActivity()).Ad_map_view.setVisibility(View.GONE);
            ((Screen1_Common_Ads_Activity)getActivity()).changeView.setVisibility(View.GONE);
            ((Screen1_Common_Ads_Activity)getActivity()).filterView.setVisibility(View.GONE);

        }
        else if(screen_name.equals("Screen2_Shopping_Ads_Activity")){

            ((Screen2_Shopping_Ads_Activity)getActivity()).Ad_map_view.setVisibility(View.GONE);
            ((Screen2_Shopping_Ads_Activity)getActivity()).changeView.setVisibility(View.GONE);
            ((Screen2_Shopping_Ads_Activity)getActivity()).filterView.setVisibility(View.GONE);

        }
        else if(screen_name.equals("Screen3_Job_Ads_Activity")){

            ((Screen3_Job_Ads_Activity)getActivity()).Ad_map_view.setVisibility(View.GONE);
            ((Screen3_Job_Ads_Activity)getActivity()).changeView.setVisibility(View.GONE);
            ((Screen3_Job_Ads_Activity)getActivity()).filterView.setVisibility(View.GONE);

        }
        else if(screen_name.equals("Screen4_Marriage_Ads_Activity")){

            ((Screen4_Marriage_Ads_Activity)getActivity()).Ad_map_view.setVisibility(View.GONE);
            ((Screen4_Marriage_Ads_Activity)getActivity()).changeView.setVisibility(View.GONE);
            ((Screen4_Marriage_Ads_Activity)getActivity()).filterView.setVisibility(View.GONE);

        }
        else if(screen_name.equals("Screen5_Property_Ads_Activity")){

            ((Screen5_Property_Ads_Activity)getActivity()).Ad_map_view.setVisibility(View.GONE);
            ((Screen5_Property_Ads_Activity)getActivity()).changeView.setVisibility(View.GONE);
            ((Screen5_Property_Ads_Activity)getActivity()).filterView.setVisibility(View.GONE);

        }

        ad_button = view.findViewById(R.id.ad_button);
        ad_button.setVisibility(View.GONE);
        ad_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int ad_id = Integer.parseInt(id);

                if(!TextUtils.isEmpty(id)){

                    ViewUsersAdDetails viewUsersAdDetails = new ViewUsersAdDetails(userAdMarkers.get(ad_id).getId());
                    androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.framelayoutAdView, viewUsersAdDetails);
                    transaction.addToBackStack("tag");
                    transaction.commit();

                }

            }
        });

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        mapView = view.findViewById(R.id.ad_map);
        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        int height = 160;
        int width = 145;

        b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_ad_map_markers);
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        ad_marker = BitmapDescriptorFactory.fromBitmap(smallMarker);


        googleMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                if (!TextUtils.isEmpty(sub)) {

                    call = taskMetServer.getAdsMapMarkers(number, main, sub, lat, log, date);
                    call.enqueue(new Callback<List<UserAdsMapModel>>() {
                        @Override
                        public void onResponse(Call<List<UserAdsMapModel>> call, Response<List<UserAdsMapModel>> response) {

                            if (response.isSuccessful()) {

                                userAdMarkers = response.body();

                                Log.d("my_posts", "array size: " + userAdMarkers.size());

                                if (!userAdMarkers.isEmpty()) {


                                    Log.d("my_posts", "array data: " + userAdMarkers);

                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    LatLng latlng;

                                    for (int i = 0; i < userAdMarkers.size(); i++) {

                                        map.addMarker(new MarkerOptions()
                                                .position(new LatLng(userAdMarkers.get(i).getLatitude(), userAdMarkers.get(i).getLongitude()))
                                                .title(userAdMarkers.get(i).getTitle())
                                                .icon(ad_marker));

                                        latlng = new LatLng(userAdMarkers.get(i).getLatitude(), userAdMarkers.get(i).getLongitude());
                                        builder.include(latlng);

                                    }

                                    LatLngBounds bounds = builder.build();

                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 150);
                                    map.animateCamera(cu);

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

                            }

                        }

                        @Override
                        public void onFailure(Call<List<UserAdsMapModel>> call, Throwable t) {

                        }
                    });

                } else {

                    Call<List<UserAdsMapModel>> call = taskMetServer.getAdsMapMarkers(number, main, lat, log, date);
                    call.enqueue(new Callback<List<UserAdsMapModel>>() {
                        @Override
                        public void onResponse(Call<List<UserAdsMapModel>> call, Response<List<UserAdsMapModel>> response) {

                            if (response.isSuccessful()) {

                                userAdMarkers = response.body();

                                Log.d("my_posts", "array size: " + userAdMarkers.size());

                                if (!userAdMarkers.isEmpty()) {


                                    Log.d("my_posts", "array data: " + userAdMarkers);

                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    LatLng latlng;

                                    for (int i = 0; i < userAdMarkers.size(); i++) {

                                        map.addMarker(new MarkerOptions()
                                                .position(new LatLng(userAdMarkers.get(i).getLatitude(), userAdMarkers.get(i).getLongitude()))
                                                .title(userAdMarkers.get(i).getTitle())
                                                .icon(ad_marker));

                                        latlng = new LatLng(userAdMarkers.get(i).getLatitude(), userAdMarkers.get(i).getLongitude());
                                        builder.include(latlng);

                                    }

                                    LatLngBounds bounds = builder.build();

                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 150);
                                    map.animateCamera(cu);

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

                            }

                        }

                        @Override
                        public void onFailure(Call<List<UserAdsMapModel>> call, Throwable t) {

                        }
                    });

                }

            }
        });

        thread.start();

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        number_str = marker.getId();
        id = number_str.replaceAll("\\D+","");

        if(!TextUtils.isEmpty(id)){

            ad_button.setVisibility(View.VISIBLE);

        }


        return false;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call!=null){
            call.cancel();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(call!=null){
            call.cancel();
        }
    }

}