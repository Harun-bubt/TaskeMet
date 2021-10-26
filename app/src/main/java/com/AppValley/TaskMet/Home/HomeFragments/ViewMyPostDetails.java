package com.AppValley.TaskMet.Home.HomeFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;
import com.AppValley.TaskMet.Home.Adopters.SliderAdapter;
import com.AppValley.TaskMet.PostPurchases.Screen5_PremiumPost;
import com.AppValley.TaskMet.PostTask.Edit_Post;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostDataModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostImagesModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostOwnerModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.Status_Response;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.ViewMyPostModel;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.Utils.ABTextUtil;
import com.AppValley.TaskMet.constant.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import java.util.ArrayList;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewMyPostDetails extends Fragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener, OnMapReadyCallback {

    RapidFloatingActionButton rfaBtn;
    RapidFloatingActionHelper rfabHelper;
    RapidFloatingActionLayout rfaLayout;
    public static SharedPreferences sharedPreferences;

    ViewMyPostModel myPostModel;
    PostOwnerModel postOwner;
    PostDataModel postData;

    Call<ViewMyPostModel> call;

    int height = 160;
    int width = 145;

    Bitmap b,smallMarker;
    BitmapDescriptor ad_marker;

    GoogleMap map;
    MapView mapView;

    Adapter_view_tabLayout sa;

    View view;
    SliderView sliderView;

    TabLayout tabLayout;
    ViewPager2 viewPager;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    String id, key, action, ad_type, currency;
    TaskMetServer taskMetServer;

    public ViewMyPostDetails(String id, String key, String action, String ad_type) {

        this.id = id;
        this.key = key;
        this.action = action;
        this.ad_type = ad_type;

    }

    LinearLayout shopLayout, jobLayout, marriageLayout, propertyLayout;

    //....................... Common views for all..............
    TextView priceTextView, likes, views, cityTextview,
            titleTextView, descriptionTextView, post_id, post_category, posting_date, post_expiry;

    //.....Shop view....................
    TextView item_condition, item_condition_scale;

    //...............Job View........................
    TextView salary_from,salary_to,salary_period,job_type,job_category,position_type;


    //...................PropertyView......................................
    TextView areaType, propertyType, area, areaUnit, floor, totalFloors, furnished,property_for;
    SweetAlertDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_my_post, container, false);

        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        currency = sharedPreferences.getString(Constants.CURRENCY, Constants.NULL);

        rfaLayout = view.findViewById(R.id.activity_main_rfal);
        rfaBtn = view.findViewById(R.id.activity_main_rfab);
        progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progress.setTitleText("Loading");
        progress.setCancelable(false);

        initiaLizeAllCommonView();
        initializeShopView();
        initializeJobView();
        initializeMarriageView();
        initializePropertyView();
        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);


        if (key.equals("shopping")) {

            shopLayout.setVisibility(View.VISIBLE);
            jobLayout.setVisibility(View.GONE);
            marriageLayout.setVisibility(View.GONE);
            propertyLayout.setVisibility(View.GONE);

        } else if (key.equals("common_service")) {

            shopLayout.setVisibility(View.GONE);
            jobLayout.setVisibility(View.GONE);
            marriageLayout.setVisibility(View.GONE);
            propertyLayout.setVisibility(View.GONE);

        } else if (key.equals("job_service")) {

            jobLayout.setVisibility(View.VISIBLE);
            shopLayout.setVisibility(View.GONE);
            marriageLayout.setVisibility(View.GONE);
            propertyLayout.setVisibility(View.GONE);

        } else if (key.equals("marriage_service")) {

            marriageLayout.setVisibility(View.VISIBLE);
            shopLayout.setVisibility(View.GONE);
            jobLayout.setVisibility(View.GONE);
            propertyLayout.setVisibility(View.GONE);

        } else if (key.equals(Constants.PROPERTY_KEY)) {

            propertyLayout.setVisibility(View.VISIBLE);
            marriageLayout.setVisibility(View.GONE);
            shopLayout.setVisibility(View.GONE);
            jobLayout.setVisibility(View.GONE);

        }


        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(getContext());
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();


        if (!ad_type.isEmpty() && ad_type.equals("0")) {

            items.add(new RFACLabelItem<Integer>()
                    .setLabel("Make Ad Premium")
                    .setResId(R.drawable.icon_gold_symbol)
                    .setIconNormalColor(Color.parseColor("#FFCC00"))
                    .setIconPressedColor(0xffbf360c)
                    .setLabelBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.golden_floating_option, null))
                    .setLabelTextBold(true)
                    .setLabelColor(Color.BLACK)
                    .setLabelSizeSp(16)
                    .setWrapper(0)
            );

        }
        else {

            items.add(new RFACLabelItem<Integer>()
                    .setLabel("Mark as Sold")
                    .setResId(R.drawable.icon_status_sold)
                    .setIconNormalColor(Color.parseColor("#FFCC00"))
                    .setIconPressedColor(0xffbf360c)
                    .setLabelBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.golden_floating_option, null))
                    .setLabelTextBold(true)
                    .setLabelColor(Color.BLACK)
                    .setLabelSizeSp(16)
                    .setWrapper(0)
            );

        }

        items.add(new RFACLabelItem<Integer>()
                .setLabel("Edit Ad")
                .setResId(R.drawable.icon_edit_ad)
                .setIconNormalColor(Color.parseColor("#00A86B"))
                .setIconPressedColor(0xff3e2723)
                .setLabelBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.green_floating_option, null))
                .setLabelTextBold(true)
                .setLabelColor(Color.WHITE)
                .setLabelSizeSp(16)
                //.setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(0xaa000000, ABTextUtil.dip2px(context, 4)))
                .setWrapper(1)
        );

        items.add(new RFACLabelItem<Integer>()
                .setLabel("Delete Ad")
                .setResId(R.drawable.icon_trash_bin)
                .setIconNormalColor(Color.parseColor("#F63600"))
                .setIconPressedColor(0xff0d5302)
                .setLabelBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.red_floating_option, null))
                .setLabelTextBold(true)
                .setLabelColor(Color.WHITE)
                .setLabelSizeSp(16)
                .setWrapper(2)
        );

        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(getContext(), 0))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(getContext(), 0))
        ;
        rfabHelper = new RapidFloatingActionHelper(
                getContext(),
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();


        return view;
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {

        //Make Post Premium Post
        if (position == 0) {

            if (!ad_type.isEmpty() && ad_type.equals("0")) {

                Screen5_PremiumPost edit_post = new Screen5_PremiumPost();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.framelayout_post_task, edit_post);
                transaction.addToBackStack(null);
                transaction.commit();

            }
            else if (!ad_type.isEmpty() && ad_type.equals("1")) {

                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setContentText("Are you sure, you want to mark this Ad as Sold?")
                        .setConfirmText("Confirm")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                                progress.show();

                                Thread thread3 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Call<Status_Response> call = taskMetServer.soldMyPost(id);
                                        call.enqueue(new Callback<Status_Response>() {
                                            @Override
                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                                                Status_Response message = response.body();

                                                String msg = message.getStatus();

                                                if (msg.equals("true")) {

                                                    progress.dismissWithAnimation();

                                                    getActivity().finish();

                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<Status_Response> call, Throwable t) {
                                                progress.dismissWithAnimation();
                                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }
                                });
                                thread3.start();

                            }
                        })
                        .showCancelButton(true)
                        .setCancelText("Cancel")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();

            }

        }

        //Edit Post
        if (position == 1) {
            Edit_Post edit_post = new Edit_Post(id, key);
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            transaction.replace(R.id.framelayout_post_task, edit_post);
            transaction.addToBackStack(null);
            transaction.commit();

        }

        //Delete Post
        else if (position == 2) {

            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setContentText("Are you sure, you want to delete this Ad?")
                    .setConfirmText("Confirm")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                            progress.show();

                            Thread thread2 = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    Call<Status_Response> call = taskMetServer.deleteMyPost(Integer.parseInt(id));
                                    call.enqueue(new Callback<Status_Response>() {
                                        @Override
                                        public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                                            Status_Response message = response.body();

                                            String msg = message.getStatus();

                                            if (msg.equals("true")) {

                                                progress.dismissWithAnimation();

                                                getActivity().finish();

                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<Status_Response> call, Throwable t) {
                                            progress.dismissWithAnimation();
                                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });
                            thread2.start();

                        }
                    })
                    .showCancelButton(true)
                    .setCancelText("Cancel")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .show();

        }
        rfabHelper.toggleContent();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {

        if (position == 0) {

            if (!ad_type.isEmpty() && ad_type.equals("0")) {

                Screen5_PremiumPost edit_post = new Screen5_PremiumPost();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.framelayout_post_task, edit_post);
                transaction.addToBackStack(null);
                transaction.commit();

            }
            else if (!ad_type.isEmpty() && ad_type.equals("1")) {

                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setContentText("Are you sure, you want to mark this Ad as Sold?")
                        .setConfirmText("Confirm")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                                progress.show();

                                Thread thread3 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Call<Status_Response> call = taskMetServer.soldMyPost(id);
                                        call.enqueue(new Callback<Status_Response>() {
                                            @Override
                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                                                Status_Response message = response.body();

                                                String msg = message.getStatus();

                                                if (msg.equals("true")) {

                                                    progress.dismissWithAnimation();

                                                    getActivity().finish();

                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<Status_Response> call, Throwable t) {
                                                progress.dismissWithAnimation();
                                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }
                                });
                                thread3.start();

                            }
                        })
                        .showCancelButton(true)
                        .setCancelText("Cancel")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();

            }

        }
        else if (position == 1) {

            Edit_Post edit_post = new Edit_Post(id, key);
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            transaction.replace(R.id.framelayout_post_task, edit_post);
            transaction.addToBackStack(null);
            transaction.commit();

        }

        //Delete Post
        else if (position == 2) {

            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setContentText("Are you sure, you want to delete this Ad?")
                    .setConfirmText("Confirm")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                            progress.show();

                            Thread thread2 = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    Call<Status_Response> call = taskMetServer.deleteMyPost(Integer.parseInt(id));
                                    call.enqueue(new Callback<Status_Response>() {
                                        @Override
                                        public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                                            Status_Response message = response.body();

                                            String msg = message.getStatus();

                                            if (msg.equals("true")) {

                                                progress.dismissWithAnimation();

                                                getActivity().finish();

                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<Status_Response> call, Throwable t) {
                                            progress.dismissWithAnimation();
                                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });
                            thread2.start();

                        }
                    })
                    .showCancelButton(true)
                    .setCancelText("Cancel")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .show();

        }

        rfabHelper.toggleContent();
    }

    @Override
    public void onResume() {
        super.onResume();

        switch (action) {

            case "delete":

                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setContentText("Are you sure, you want to delete this Ad?")
                        .setConfirmText("Confirm")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                                progress.show();

                                Thread thread2 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Call<Status_Response> call = taskMetServer.deleteMyPost(Integer.parseInt(id));
                                        call.enqueue(new Callback<Status_Response>() {
                                            @Override
                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                                                Status_Response message = response.body();

                                                String msg = message.getStatus();

                                                if (msg.equals("true")) {

                                                    progress.dismissWithAnimation();

                                                    getActivity().finish();

                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<Status_Response> call, Throwable t) {
                                                progress.dismissWithAnimation();
                                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });
                                thread2.start();

                            }
                        })
                        .showCancelButton(true)
                        .setCancelText("Cancel")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();

                break;

            case "sold":

                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setContentText("Are you sure, you want to mark this Ad as Sold?")
                        .setConfirmText("Confirm")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                                progress.show();

                                Thread thread3 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Call<Status_Response> call = taskMetServer.soldMyPost(id);
                                        call.enqueue(new Callback<Status_Response>() {
                                            @Override
                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                                                Status_Response message = response.body();

                                                String msg = message.getStatus();

                                                if (msg.equals("true")) {

                                                    progress.dismissWithAnimation();

                                                    getActivity().finish();

                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<Status_Response> call, Throwable t) {
                                                progress.dismissWithAnimation();
                                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }
                                });
                                thread3.start();

                            }
                        })
                        .showCancelButton(true)
                        .setCancelText("Cancel")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();

                break;

            case "edit":

                Edit_Post edit_post = new Edit_Post(id, key);
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.framelayout_post_task, edit_post);
                transaction.addToBackStack(null);
                transaction.commit();

                break;
        }

    }

    public void initiaLizeAllCommonView() {

        shopLayout = view.findViewById(R.id.shopDetails);
        jobLayout = view.findViewById(R.id.jobDetails);
        marriageLayout = view.findViewById(R.id.marriageDetails);
        propertyLayout = view.findViewById(R.id.propertyDetails);
        priceTextView = view.findViewById(R.id.postDetailsPriceTextView);
        cityTextview = view.findViewById(R.id.cityTextView);
        titleTextView = view.findViewById(R.id.postDetailsTitleTextview);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        likes = view.findViewById(R.id.like);
        views = view.findViewById(R.id.view);

        post_id = view.findViewById(R.id.post_id);
        post_category = view.findViewById(R.id.post_category);
        posting_date = view.findViewById(R.id.posting_date);
        post_expiry = view.findViewById(R.id.post_expiry);

    }

    public void initializeShopView() {

        item_condition = view.findViewById(R.id.item_condition);
        item_condition_scale = view.findViewById(R.id.item_condition_scale);

    }

    public void initializeJobView() {

        salary_from = view.findViewById(R.id.salary_from);
        salary_to = view.findViewById(R.id.salary_to);
        salary_period = view.findViewById(R.id.salary_period);
        job_type = view.findViewById(R.id.job_type);
        job_category = view.findViewById(R.id.job_category);
        position_type = view.findViewById(R.id.position_type);

       // companyLayout = view.findViewById(R.id.companyLayout);

    }

    public void initializeMarriageView() {

        tabLayout = view.findViewById(R.id.post_tabLayout);
        viewPager = view.findViewById(R.id.post_viewPager);

    }

    public void initializePropertyView() {

        property_for = view.findViewById(R.id.property_for);
        areaType = view.findViewById(R.id.propertyAreaType);
        propertyType = view.findViewById(R.id.property_type);
        area = view.findViewById(R.id.propertyArea);
        areaUnit = view.findViewById(R.id.propertyAreaUnit);
        floor = view.findViewById(R.id.propertyFloor);
        totalFloors = view.findViewById(R.id.propertyTotalFloors);
        furnished = view.findViewById(R.id.propertyFurnished);
    }

    public void setAllCommon(PostDataModel myPostModel, PostOwnerModel postOwner, ViewMyPostModel postModel, String key) {

        if (key.equals("marriage_service")) {

            priceTextView.setText("Looking for " + myPostModel.getLookingFor());
            descriptionTextView.setText(myPostModel.getWriteMyself());

        } else if (key.equals("job_service")) {


            if(myPostModel.getCompanyName().equals("empty")){

                priceTextView.setText("Looking for job");

            }
            else {

                priceTextView.setText(myPostModel.getCompanyName());

            }

            descriptionTextView.setText(myPostModel.getDescription());

        } else if (key.equals("common_service")) {

            priceTextView.setText("Cost:" + " " + currency + " " + myPostModel.getCost());
            descriptionTextView.setText(myPostModel.getDescription());

        } else if (key.equals(Constants.PROPERTY_KEY)) {

            priceTextView.setText("Price:" + " " + currency + " " + myPostModel.getCost());
            descriptionTextView.setText(myPostModel.getDescription());

        } else {

            //Shopping Common services
            descriptionTextView.setText(myPostModel.getDescription());
            priceTextView.setText("Price:" + " " + currency + " " + myPostModel.getPrice());


        }

        cityTextview.setText(postOwner.getCity_name() + " , " + postOwner.getCountry_name());
        titleTextView.setText(myPostModel.getTitle());
        likes.setText(String.valueOf(postModel.getLikes()));
        views.setText(String.valueOf(postModel.getViews()));

        post_id.setText(String.valueOf(postModel.getId()));
        post_category.setText(String.valueOf(myPostModel.getMainCategory()));
        posting_date.setText(String.valueOf(postModel.getCurrentDate()));
        post_expiry.setText(String.valueOf(postModel.getExpireDate()));

    }

    public void setShopText(PostDataModel myPostModel, ViewMyPostModel postModel) {

        item_condition.setText(myPostModel.getCondition());
        item_condition_scale.setText(myPostModel.getCondition_meter() + " / 10");

    }

    public void setCommonText(ViewMyPostModel myPostModel, PostDataModel postData) {
        descriptionTextView.setText(postData.getDescription());
    }

    public void setJobText(ViewMyPostModel postModel, PostDataModel myPostModel) {

        salary_from.setText(myPostModel.getSalaryFrom());
        salary_to.setText(myPostModel.getSalaryTo());
        job_type.setText(myPostModel.getType());
        salary_period.setText(myPostModel.getSalaryPeriod());
        job_category.setText(myPostModel.getJobCategory());
        position_type.setText(myPostModel.getPositionType());

    }

    public void setMarriageText(PostDataModel myPostModel) {

        sa = new Adapter_view_tabLayout(fragmentManager, getLifecycle(),myPostModel);
        viewPager.setAdapter(sa);

        tabLayout.addTab(tabLayout.newTab().setText("Poster Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Ideal Partner"));

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

    }

    public void setProperytText(PostDataModel postData) {

        property_for.setText(postData.getPropertyPostType());
        areaType.setText(postData.getAreaType());
        propertyType.setText(postData.getPropertyType());
        area.setText(postData.getArea());
        areaUnit.setText(postData.getAreaUnit());
        floor.setText(postData.getFloorLevel());
        totalFloors.setText(postData.getTotalFloors());
        furnished.setText(postData.getFurnished());

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.ad_map);

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

                call = taskMetServer.getMyPostData(id);
                call.enqueue(new Callback<ViewMyPostModel>() {
                    @Override
                    public void onResponse(Call<ViewMyPostModel> call, Response<ViewMyPostModel> response) {

                        if (response.isSuccessful()) {

                            myPostModel = response.body();

                            key = myPostModel.getKey();

                            postData = myPostModel.getPostDataModel();
                            postOwner = myPostModel.getPostOwnerModel();


                            if (key.equals("shopping")) {

                                setAllCommon(postData, postOwner, myPostModel, key);
                                setShopText(postData, myPostModel);

                            } else if (key.equals("common_service")) {

                                setAllCommon(postData, postOwner, myPostModel, key);
                                setCommonText(myPostModel, postData);

                            } else if (key.equals("job_service")) {

                                setAllCommon(postData, postOwner, myPostModel, key);
                                setJobText(myPostModel, postData);

                            } else if (key.equals("marriage_service")) {

                                setAllCommon(postData, postOwner, myPostModel, key);
                                setMarriageText(postData);

                            } else if (key.equals(Constants.PROPERTY_KEY)) {

                                setAllCommon(postData, postOwner, myPostModel, key);
                                setProperytText(postData);

                            }

                            //----------------------------------- Slider of images -------------------------------------


                            List<PostImagesModel> postImagesModels = myPostModel.getPostImages();

                            Log.d("postPrices", "Images: " + postImagesModels.toString());

                            sliderView = view.findViewById(R.id.imageSlider);
                            sliderView.setSliderAdapter(new SliderAdapter(getContext(), postImagesModels));

                            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                            sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
                            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                            sliderView.setIndicatorSelectedColor(getResources().getColor(R.color.white));
                            sliderView.setIndicatorUnselectedColor(Color.GRAY);
                            sliderView.setAutoCycle(false);

                            //------------------------------------------------------------------------------------------

                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(myPostModel.getLatitude(), myPostModel.getLongitude()))
                                    .title("Ad Location")
                                    .icon(ad_marker));

                            CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(myPostModel.getLatitude(), myPostModel.getLongitude()))
                                    .bearing(0)
                                    .tilt(45)
                                    .zoom(16)
                                    .build();

                            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            drawCircle(new LatLng(myPostModel.getLatitude(), myPostModel.getLongitude()));

                        }

                    }

                    @Override
                    public void onFailure(Call<ViewMyPostModel> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        thread.start();

    }

    private void drawCircle(LatLng point){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(20);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x3078d6af);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        map.addCircle(circleOptions);

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
