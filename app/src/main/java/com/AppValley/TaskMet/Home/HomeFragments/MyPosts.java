package com.AppValley.TaskMet.Home.HomeFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AppValley.TaskMet.Home.Adopters.MyPostsAdopter;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MyPostData;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MyPostModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MyPostWithLimitModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MyWallet;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.NormalPostLimit;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostPrices;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PremiumPostLimit;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.WalletInfoModel;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;
import com.github.ybq.android.spinkit.SpinKitView;
import com.krishna.securetimer.SecureTimer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPosts extends Fragment {

    boolean flag = true;
    boolean dialog_status = false;
    String number, library_date, device_date,next_page_url = null;

    Button btn_my_post, close;
    RelativeLayout my_post_screen;

    int currentItems, totalItems, scrollOutItems;
    boolean isScrolling = true;

    MyPostsAdopter adopter;
    RecyclerView my_posts_recycler;
    MyPostWithLimitModel my_posts;
    LinearLayoutManager layoutManager;
    MyPostModel post_item;

    SpinKitView progress_bar;


    List<NormalPostLimit> normal_post_info = new ArrayList<>();
    List<PremiumPostLimit> premium_post_info = new ArrayList<>();
    List<PostPrices> post_prices_info = new ArrayList<>();
    List<MyWallet> post_wallet_info = new ArrayList<>();

    List<MyPostData> postData = new ArrayList<>();
    List<MyPostData> allPostData = new ArrayList<>();

    Call<MyPostWithLimitModel> call;


    public static SharedPreferences sharedPreferences;
    TaskMetServer taskMetServer;
    ImageView wallet_info, icon_my_post;

    SweetAlertDialog progress;

    CardView info_card;
    TextView rc_balance, gc_balance, normal_ads, premium_ads, my_post_text;

    FragmentTransaction transaction;
    FragmentManager fragmentManager;

    View MyPostView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyPostView = inflater.inflate(R.layout.fragment_my_posts, container, false);

        //Support Fragment Manager
        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

        icon_my_post = MyPostView.findViewById(R.id.icon_my_post);
        my_post_text = MyPostView.findViewById(R.id.my_post_text);
        btn_my_post = MyPostView.findViewById(R.id.btn_my_post);
        my_post_screen = MyPostView.findViewById(R.id.my_post_screen);
        my_post_screen.setVisibility(View.GONE);

        wallet_info = MyPostView.findViewById(R.id.wallet_info);
        info_card = MyPostView.findViewById(R.id.info_card);
        rc_balance = MyPostView.findViewById(R.id.rc_balance);
        gc_balance = MyPostView.findViewById(R.id.gc_balance);
        normal_ads = MyPostView.findViewById(R.id.normal_ads);
        progress_bar = MyPostView.findViewById(R.id.progress_bar);
        premium_ads = MyPostView.findViewById(R.id.premium_ads);
        close = MyPostView.findViewById(R.id.close);

        wallet_info.setEnabled(false);
        info_card.setVisibility(View.GONE);

        setMenuVisibility(true);

        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        number = sharedPreferences.getString(Constants.NUMBER, Constants.NULL);

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        layoutManager = new LinearLayoutManager(getContext());
        my_posts_recycler = MyPostView.findViewById(R.id.my_posts_recycler);
        my_posts_recycler.setLayoutManager(layoutManager);
        adopter = new MyPostsAdopter(getContext(), allPostData, normal_post_info, premium_post_info, post_prices_info, post_wallet_info);
        my_posts_recycler.setAdapter(adopter);

        my_posts_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {

                    isScrolling = false;
                    getData();

                }
            }
        });

        return MyPostView;
    }

    @Override
    public void onResume() {
        super.onResume();

        progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);

        getData();

        btn_my_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (btn_my_post.getText().toString().equals("Edit Date")) {

                    Intent intent = new Intent(android.provider.Settings.ACTION_DATE_SETTINGS);
                    getDateSettings.launch(intent);

                } else {

                    transaction = fragmentManager.beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.home_frame_layout, new PostTask());
                    transaction.commit();

                }

            }
        });

        wallet_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!dialog_status) {

                    Call<WalletInfoModel> call = taskMetServer.getAccountInfo(number, device_date);
                    call.enqueue(new Callback<WalletInfoModel>() {
                        @Override
                        public void onResponse(Call<WalletInfoModel> call, Response<WalletInfoModel> response) {

                            if (response.isSuccessful()) {

                                dialog_status = true;

                                WalletInfoModel walletInfo = response.body();

                                Animation scaleAnim = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
                                info_card.startAnimation(scaleAnim);
                                info_card.setVisibility(View.VISIBLE);

                                MyWallet wallet = walletInfo.getMyWallet();
                                NormalPostLimit normalPostLimit = walletInfo.getPostLimitModel();
                                PremiumPostLimit premiumPostLimit = walletInfo.getPremiumPostLimit();

                                gc_balance.setText("GC Balance: " + String.valueOf(wallet.getGc_balance()));
                                rc_balance.setText("RC Balance: " + String.valueOf(wallet.getRc_balance()));

                                normal_ads.setText("Normal Ads: " + String.valueOf(normalPostLimit.getTodayPostCount())
                                        + "\\" + String.valueOf(normalPostLimit.getLimit()));

                                premium_ads.setText("Premium Ads: " + String.valueOf(premiumPostLimit.getTodayPremiumPostCount())
                                        + "\\" + String.valueOf(premiumPostLimit.getLimit()));

                            }
                        }

                        @Override
                        public void onFailure(Call<WalletInfoModel> call, Throwable t) {
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_status = false;

                Animation scaleAnim = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
                info_card.startAnimation(scaleAnim);
                info_card.setVisibility(View.GONE);

            }
        });

    }

    private void getData() {

        //------------------------------------ Validating Date -------------------------------------
        SecureTimer.with(getContext()).initialize();

        Date date = SecureTimer.with(getActivity()).getCurrentDate();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        library_date = date_format.format(date);

        Calendar calender = Calendar.getInstance();
        device_date = date_format.format(calender.getTime());


        //---------------------------------------------------------------------------

        if (!library_date.equals(device_date)) {

            my_post_screen.setVisibility(View.VISIBLE);
            icon_my_post.setImageResource(R.drawable.icon_wrong_date);
            my_post_text.setTextColor(getResources().getColor(R.color.red_1));
            my_post_text.setText("Device Date is not Correct");
            btn_my_post.setText("Edit Date");

        }
        else {

            my_post_screen.setVisibility(View.GONE);

            if (next_page_url != null) {

                Log.d("call_status", "next page call");
                call = taskMetServer.getMyPosts(next_page_url);

                progress_bar.setVisibility(View.VISIBLE);

                calling_api(call);

            }
            else if(postData.isEmpty()) {

                progress.setTitleText("Loading Ads");
                progress.show();

                Log.d("call_status", "Simple call");
                call = taskMetServer.getMyPosts(number, device_date);
                calling_api(call);

            }

        }

    }


    private void calling_api(Call<MyPostWithLimitModel> call) {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {


                call.enqueue(new Callback<MyPostWithLimitModel>() {
                    @Override
                    public void onResponse(Call<MyPostWithLimitModel> call, Response<MyPostWithLimitModel> response) {

                        if (response.isSuccessful()) {


                            if(next_page_url == null){
                                progress.dismissWithAnimation();
                            }

                            progress_bar.setVisibility(View.GONE);

                            wallet_info.setEnabled(true);
                            my_posts = response.body();

                            post_item = my_posts.getPosts();

                            next_page_url = post_item.getNext_page_url();

                            postData = post_item.getPostData();
                            allPostData.addAll(postData);

                            if(flag){

                                normal_post_info.add(my_posts.getPostLimitModel());
                                premium_post_info.add(my_posts.getPremiumPostLimit());
                                post_prices_info.add(my_posts.getPostPrices());
                                post_wallet_info.add(my_posts.getMyWallet());

                                flag = false;

                            }

                            if (postData.isEmpty()) {

                                my_post_screen.setVisibility(View.VISIBLE);
                                icon_my_post.setImageResource(R.drawable.icon_empty_box);
                                my_post_text.setTextColor(getResources().getColor(R.color.Theme1));
                                my_post_text.setText("No Ad found.\nPost an Ad & win 10 RC reward.");
                                btn_my_post.setText("Post new Ad");

                            } else {

                                //  Log.d("my_posts", "Fragment Ads list size: " + postData.size());

                                adopter.notifyDataSetChanged();

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<MyPostWithLimitModel> call, Throwable t) {

                        progress.dismissWithAnimation();
                        Toast.makeText(getContext(), t.getMessage() + " Error", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
        thread.start();

    }

    ActivityResultLauncher<Intent> getDateSettings = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        // There are no request codes
                        Intent data = result.getData();

                        assert data != null;
                        if (data.getData() != null) {


                        }

                    }
                }
            });


}