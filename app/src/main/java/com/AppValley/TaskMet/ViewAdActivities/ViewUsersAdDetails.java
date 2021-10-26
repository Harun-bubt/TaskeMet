package com.AppValley.TaskMet.ViewAdActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AppValley.TaskMet.Chat_Module.User;
import com.AppValley.TaskMet.Chat_Module.UsersAdapter;
import com.AppValley.TaskMet.Home.Adopters.SliderAdapter;
import com.AppValley.TaskMet.Home.FragmentViewActivity;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostDataModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostImagesModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostOwnerModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.ViewMyPostModel;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewUsersAdDetails extends Fragment implements OnMapReadyCallback {

    View view;
    GoogleMap map;
    MapView mapView;

    String key;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    double latitude, longitude;
    FragmentManager fragmentManager;

    Call<ViewMyPostModel> call;

    int height = 160;
    int width = 145;

    Bitmap b,smallMarker;
    BitmapDescriptor ad_marker;

    private ViewMyPostModel myPostModel;
    public static PostDataModel postData;
    public static PostOwnerModel postOwner;
    public static boolean isFromAd = false;


    //CommonView for all
    SliderView sliderView;
    TextView priceView, titleView, profileNameView, adIdView, adCategoryKeyView, postDateView, expireDateVeiw, address;

    TextView descriptionView;
    ImageView likeImageView, profileImageView;
    LinearLayout shoppingLayout, jobLayout, propertyLayout, marriageLayout, callLayout, smsLayout, chatLayout;
    //shopping view
    TextView conditionView, conditionMeterView;

    // Job View
    TextView salaryFrom, salaryTo, jobType, jobCategory, positionType, salaryPeriod;
    // Property view
    TextView propertyFor, propertyType, areaUnit, area, areaType, furnished, floor, totalFloors;

    int id;
    TaskMetServer taskMetServer;
    FirebaseDatabase database;

    TemplateView template, template_medium;

    public ViewUsersAdDetails(int id) {
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_shop_ad_details, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        template = view.findViewById(R.id.my_template_small);
        template_medium = view.findViewById(R.id.my_template_medium);

        mapView = view.findViewById(R.id.ad_map);
        initializeCommonView();
        initializeShopView();
        initializeJobView();
        initializePropertyView();
        database = FirebaseDatabase.getInstance();

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

        b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_ad_map_markers);
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        ad_marker = BitmapDescriptorFactory.fromBitmap(smallMarker);


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                call = taskMetServer.getMyPostData(String.valueOf(id));
                call.enqueue(new Callback<ViewMyPostModel>() {
                    @Override
                    public void onResponse(Call<ViewMyPostModel> call, Response<ViewMyPostModel> response) {

                        if (response.isSuccessful()) {

                            myPostModel = response.body();

                            chatLayout.setEnabled(true);

                            if (myPostModel.isShowNumber() == 1) {

                                smsLayout.setVisibility(View.VISIBLE);
                                callLayout.setVisibility(View.VISIBLE);

                            }

                            key = myPostModel.getKey();

                            postData = myPostModel.getPostDataModel();
                            postOwner = myPostModel.getPostOwnerModel();

                            setAllCommonView(myPostModel, postData, postOwner);

                            if (key.equals("shopping")) {

                                shoppingLayout.setVisibility(View.VISIBLE);
                                adCategoryKeyView.setText("Shopping");
                                setShoppingData(postData);

                            }
                            else if (key.equals("job_service")) {

                                jobLayout.setVisibility(View.VISIBLE);
                                adCategoryKeyView.setText("Job");

                                if (postData.getCompanyName().equals("empty")) {

                                    priceView.setText("Resume for job");

                                }
                                setJobData(postData, postOwner);

                            }
                            else if (key.equals("common_service")) {

                                adCategoryKeyView.setText("Service");
                                priceView.setText(postOwner.getCurrency() + " " + postData.getCost());

                            }
                            else if (key.equals("marriage_service")) {

                                marriageLayout.setVisibility(View.VISIBLE);
                                adCategoryKeyView.setText("Marriage");

                                priceView.setText("Looking For: " + postData.getLookingFor());

                                if(!postData.getWriteMyself().equals("empty")){
                                    descriptionView.setText(postData.getWriteMyself());
                                }else {
                                    descriptionView.setText("Not Specified");
                                }

                                fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                                Adapter_view_tabLayout sa = new Adapter_view_tabLayout(fragmentManager, getLifecycle(), postData, postOwner);
                                viewPager.setAdapter(sa);

                                tabLayout.addTab(tabLayout.newTab().setText("Poster Info"));

                                if (postData.getLookingFor().equals("Bride")) {

                                    tabLayout.addTab(tabLayout.newTab().setText("Ideal Bride"));

                                } else {

                                    tabLayout.addTab(tabLayout.newTab().setText("Ideal Groom"));

                                }

                                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                    @Override
                                    public void onTabSelected(TabLayout.Tab tab) {
                                        viewPager.setCurrentItem(tab.getPosition());
                                    }

                                    @Override
                                    public void onTabUnselected(TabLayout.Tab tab) {
                                    }

                                    @Override
                                    public void onTabReselected(TabLayout.Tab tab) {
                                    }
                                });

                                viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                    @Override
                                    public void onPageSelected(int position) {
                                        tabLayout.selectTab(tabLayout.getTabAt(position));
                                    }
                                });


                            } else if (key.equals(Constants.PROPERTY_KEY)) {
                                priceView.setText(postData.getPrice());
                                propertyLayout.setVisibility(View.VISIBLE);
                                adCategoryKeyView.setText("Property");
                                setPropertyData(postData);
                            }


                            //------------------ Google Maps --------------------

                            latitude = Double.parseDouble(postOwner.getLatitude());
                            longitude = Double.parseDouble(postOwner.getLongitude());

                            setMap(latitude, longitude, myPostModel);

                            //-------------------------------------------


                            loadGoogleAds();


                        }

                    }

                    @Override
                    public void onFailure(Call<ViewMyPostModel> call, Throwable t) {

                    }
                });
            }
        });
        thread.start();


    }

    public void setMap(double lat, double log, ViewMyPostModel myPostModel) {

        Marker markers = map.addMarker(new MarkerOptions()
                .position(new LatLng(lat, log))
                .title(postData.getTitle())
                .icon(ad_marker)
                .draggable(false));


        if (myPostModel.getIsShowLocation() == 1) {

            CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(lat, log))
                    .bearing(0)
                    .tilt(45)
                    .zoom(16)
                    .build();

            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            drawCircle(new LatLng(lat, log), 50);

        } else {

            CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(lat, log))
                    .bearing(0)
                    .zoom(15)
                    .build();

            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            markers.remove();
            drawCircle(new LatLng(lat, log), 150);

            map.getUiSettings().setMapToolbarEnabled(false);
            map.getUiSettings().setZoomControlsEnabled(false);

        }


    }

    private void drawCircle(LatLng point, int radius) {

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(radius);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x3078d6af);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        map.addCircle(circleOptions);

    }

    public void initializeJobView() {
        salaryFrom = view.findViewById(R.id.viewSalaryFrom);
        salaryTo = view.findViewById(R.id.viewSalaryTo);
        jobType = view.findViewById(R.id.viewJobType);
        jobCategory = view.findViewById(R.id.viewJobCategory);
        positionType = view.findViewById(R.id.viewPositionType);
        salaryPeriod = view.findViewById(R.id.viewSalaryPeriod);
    }

    public void setJobData(PostDataModel postDataModel, PostOwnerModel postOwnerModel) {
        if (!postDataModel.getCompanyName().equals("empty")) {
            priceView.setText(postDataModel.getCompanyName());
        } else {
            priceView.setText(postOwnerModel.getName());
        }
        salaryFrom.setText(postDataModel.getSalaryFrom());
        salaryTo.setText(postDataModel.getSalaryTo());
        jobType.setText(postDataModel.getType());
        jobCategory.setText(postDataModel.getJobCategory());
        positionType.setText(postDataModel.getPositionType());
        salaryPeriod.setText(postDataModel.getSalaryPeriod());
    }

    public void initializeShopView() {
        conditionView = view.findViewById(R.id.viewCondition);
        conditionMeterView = view.findViewById(R.id.viewConditionScale);
    }

    public void setShoppingData(PostDataModel postDataModel) {
        conditionView.setText(postDataModel.getCondition());
        conditionMeterView.setText(postDataModel.getCondition_meter());
    }

    public void initializePropertyView() {

        propertyFor = view.findViewById(R.id.viewPropertyFor);
        propertyType = view.findViewById(R.id.viewPropertyType);
        areaUnit = view.findViewById(R.id.viewAreaUnit);
        area = view.findViewById(R.id.viewArea);
        areaType = view.findViewById(R.id.viewAreaType);
        furnished = view.findViewById(R.id.viewFurnished);
        floor = view.findViewById(R.id.viewFloor);
        totalFloors = view.findViewById(R.id.viewTotalFloor);

    }

    public void setPropertyData(PostDataModel postDataModel) {
        propertyFor.setText(postDataModel.getPropertyPostType());
        propertyType.setText(postDataModel.getPropertyType());
        areaUnit.setText(postDataModel.getAreaUnit());
        area.setText(postDataModel.getArea());
        areaType.setText(postDataModel.getAreaType());
       /* if(!postDataModel.getFurnished().equals("empty") && !postDataModel.getFurnished().equals(null))
        {
            furnished.setText(postDataModel.getFurnished());
        }else
        {
            furnished.setText("--");
        }
        if(!postDataModel.getFloorLevel().equals("empty") && !postDataModel.getFloorLevel().equals(null))
        {
            floor.setText(postDataModel.getFurnished());
        }else
        {
            floor.setText("--");
        }
        if(!postDataModel.getTotalFloors().equals("empty") && !postDataModel.getTotalFloors().equals(null))
        {
            totalFloors.setText(postDataModel.getFurnished());
        }else
        {
            totalFloors.setText("--");
        }*/

    }

    public void initializeCommonView() {

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);
        sliderView = view.findViewById(R.id.imageSlider);
        priceView = view.findViewById(R.id.viewPriceTextView);
        titleView = view.findViewById(R.id.viewTitleTextView);
        descriptionView = view.findViewById(R.id.viewDescription);
        profileNameView = view.findViewById(R.id.viewProfileNameTextView);
        adIdView = view.findViewById(R.id.viewIdTextView);
        adCategoryKeyView = view.findViewById(R.id.viewKeyCategory);
        postDateView = view.findViewById(R.id.viewPostDate);
        expireDateVeiw = view.findViewById(R.id.viewExpireDate);
        likeImageView = view.findViewById(R.id.viewLikeImageView);
        profileImageView = view.findViewById(R.id.viewProfileImageView);
        shoppingLayout = view.findViewById(R.id.viewshoppingLayout);
        jobLayout = view.findViewById(R.id.viewJobLayout);
        propertyLayout = view.findViewById(R.id.viewPropertyLayout);
        marriageLayout = view.findViewById(R.id.marriageLayout);
        tabLayout = view.findViewById(R.id.post_tabLayout);
        viewPager = view.findViewById(R.id.post_viewPager);
        address = view.findViewById(R.id.viewAddress);
        callLayout = view.findViewById(R.id.callLayout);
        smsLayout = view.findViewById(R.id.smsLayout);
        chatLayout = view.findViewById(R.id.chatLayout);
        chatLayout.setEnabled(false);

    }

    public void setAllCommonView(ViewMyPostModel myPostModel, PostDataModel postData, PostOwnerModel postOwnerModel) {

        priceView.setText(postOwnerModel.getCurrency() + " " + postData.getPrice());
        titleView.setText(postData.getTitle());
        descriptionView.setText(postData.getDescription());
        profileNameView.setText(postOwnerModel.getName());
        postDateView.setText(myPostModel.getCurrentDate());
        expireDateVeiw.setText(myPostModel.getExpireDate());
        adIdView.setText(String.valueOf(id));
        address.setText(postOwnerModel.getAddress());


        List<PostImagesModel> postImagesModels = myPostModel.getPostImages();
        sliderView.setSliderAdapter(new SliderAdapter(getContext(), postImagesModels));
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(getResources().getColor(R.color.white));
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setAutoCycle(false);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo);

        Glide
                .with(getContext())
                .load(postOwnerModel.getProfile_pic())
                .apply(options)
                .into(profileImageView);

        smsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(postOwner.getNumber());
            }
        });

        callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailCall(postOwner.getNumber());
            }
        });

        chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInChatList(myPostModel,postData,postOwner,postImagesModels);
            }
        });

    }

    protected void sendSMS(String number) {

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + number));
        sendIntent.putExtra("sms_body", "");
        try {
            startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                    .setContentText("SMS App loading failed. Please again")
                    .show();
        }

    }

    protected void dailCall(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    public void addInChatList(ViewMyPostModel myPostModel,PostDataModel postData,PostOwnerModel postOwnerModel,List<PostImagesModel> postImagesModels) {

        String uid = postOwnerModel.getUid();
        String phone = postOwnerModel.getNumber();
        String name = postOwnerModel.getName();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        String myUid = sharedPreferences.getString(Constants.UID, "");

        database.getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 = snapshot.getValue(User.class);
                User user = new User(uid, name, phone, user1.getProfileImage());

                database.getReference()
                        .child("chatList")
                        .child(myUid)
                        .child(user.getUid())
                        .setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                UsersAdapter.name = user.getName();
                                UsersAdapter.Image = user.getProfileImage();
                                UsersAdapter.receiverId = user.getUid();

                                SharedPreferences preferences = getContext().getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
                                SharedPreferences.Editor premium_data = preferences.edit();
                                Intent intent = new Intent(getActivity(), FragmentViewActivity.class);
                                premium_data.putString(Constants.FRAGMENT_NAME, "ChatInbox");
                                premium_data.apply();
                                isFromAd = true;
                                startActivity(intent);

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void loadGoogleAds() {

        ColorDrawable cd = new ColorDrawable(0xFFFF6666);

        AdLoader adLoader_small = new AdLoader.Builder(getContext(), "ca-app-pub-3940256099942544/2247696110")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                    }
                })
                .build();


        AdLoader adLoader_medium = new AdLoader.Builder(getContext(), "ca-app-pub-3940256099942544/2247696110")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                        template_medium.setStyles(styles);
                        template_medium.setNativeAd(nativeAd);
                    }
                })
                .build();

        adLoader_small.loadAd(new AdRequest.Builder().build());
        adLoader_medium.loadAd(new AdRequest.Builder().build());

    }

    @Override
    public void onStop() {
        super.onStop();
        if(call!=null){
            call.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call!=null){
            call.cancel();
        }
    }


}