package com.AppValley.TaskMet.PostPurchases;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.AppValley.TaskMet.Home.HomeFragments.ViewMyPostDetails;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostDataModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostImagesModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostOwnerModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.ViewMyPostModel;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostAdLastScreen extends Fragment {

    View view;

    String key;
    boolean is_waiting;
    int my_gc_balance, my_rc_balance,post_id;

    ImageView my_post_image;
    LinearLayout premium_info,post_now_info;
    TextView my_post_title, my_post_date, my_post_category;

    Button btn_premium, btn_view_ad;
    TaskMetServer taskMetServer;

    SweetAlertDialog progress;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Support Fragment Manager
        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        view = inflater.inflate(R.layout.post_ad_last_screen, container, false);


        progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progress.setTitleText("Making Ad Premium");

        SharedPreferences preferences = getContext().getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
        post_id = preferences.getInt(Constants.ID, 0);
        my_gc_balance = preferences.getInt(Constants.GC_BALANCE, 0);
        my_rc_balance = preferences.getInt(Constants.RC_BALANCE, 0);
        is_waiting = preferences.getBoolean(Constants.IS_WAITING, false);
        key = preferences.getString(Constants.KEY, "");



        //Back functionality
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getActivity().finish();

                    return true;
                }
                return false;
            }
        });
        //-----------------

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        premium_info = view.findViewById(R.id.premium_info);
        post_now_info = view.findViewById(R.id.post_now_info);

        my_post_image = view.findViewById(R.id.my_post_image);
        my_post_title = view.findViewById(R.id.my_post_title);
        my_post_date = view.findViewById(R.id.my_post_date);
        my_post_category = view.findViewById(R.id.my_post_category);

        btn_view_ad = view.findViewById(R.id.btn_view_ad);
        btn_premium = view.findViewById(R.id.btn_premium);


    }

    @Override
    public void onResume() {
        super.onResume();

        premium_info.setVisibility(View.GONE);
        post_now_info.setVisibility(View.GONE);

        //Calling api to get post data
        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                Call<ViewMyPostModel> call = taskMetServer.getMyPostData(String.valueOf(post_id));
                call.enqueue(new Callback<ViewMyPostModel>() {
                    @Override
                    public void onResponse(Call<ViewMyPostModel> call, Response<ViewMyPostModel> response) {

                        if (response.isSuccessful()) {
                            ViewMyPostModel myPostModel = response.body();

                            PostDataModel postData = myPostModel.getPostDataModel();
                            PostOwnerModel postOwner = myPostModel.getPostOwnerModel();

                            my_post_title.setText(postData.getTitle());
                            my_post_date.setText(myPostModel.getCurrentDate());
                            my_post_category.setText(postData.getMainCategory());

                            //----------------------------------- Slider of images -------------------------------------


                            List<PostImagesModel> postImagesModels = myPostModel.getPostImages();

                            RequestOptions options = new RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.drawable.logo)
                                    .error(R.drawable.logo);

                            Glide
                                    .with(getContext())
                                    .load(postImagesModels.get(0).getLink())
                                    .apply(options)
                                    .into(my_post_image);

                            //------------------------------------------------------------------------------------------

                        }

                    }

                    @Override
                    public void onFailure(Call<ViewMyPostModel> call, Throwable t) {

                    }
                });
            }
        });
        thread.start();



        if(is_waiting){

            premium_info.setVisibility(View.GONE);
            post_now_info.setVisibility(View.VISIBLE);

            btn_premium.setText("Post Now");
            btn_premium.setBackgroundResource(R.drawable.btn_sign_in);

        }
        else
        {

            premium_info.setVisibility(View.VISIBLE);
            post_now_info.setVisibility(View.GONE);

            btn_premium.setText("Make this Ad premium");
            btn_premium.setBackgroundResource(R.drawable.premium_button);

        }

        btn_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(is_waiting){

                    Screen6_WaitingPostNow lastScreen = new Screen6_WaitingPostNow(getContext());
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    transaction.replace(R.id.framelayout_post_task, lastScreen);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
                else
                {

                    Screen5_PremiumPost lastScreen = new Screen5_PremiumPost();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    transaction.replace(R.id.framelayout_post_task, lastScreen);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }


            }
        });

        btn_view_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewMyPostDetails lastScreen = new ViewMyPostDetails(String.valueOf(post_id),key,"none","0");
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.framelayout_post_task, lastScreen);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

    }
}