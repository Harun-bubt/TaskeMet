package com.AppValley.TaskMet.PostPurchases;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AppValley.TaskMet.Home.FragmentViewActivity;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostDataModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostImagesModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostOwnerModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.Status_Response;
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

public class Screen5_PremiumPost extends Fragment {

    int post_id;
    View view;
    int my_gc_balance, my_rc_balance, premium_cost_gc, premium_cost_rc;

    ImageView my_post_image;
    TextView my_post_title, my_post_date, my_post_category, tv_gc_balance,
            tv_rc_balance, gc_cost, rc_cost;

    RelativeLayout btn_gc_premium, btn_rc_premium;

    TaskMetServer taskMetServer;

    SweetAlertDialog progress;

    SharedPreferences preferences;
    SharedPreferences.Editor fragmentNameEditor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_screen5__premium_post, container, false);

        preferences = getContext().getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
        fragmentNameEditor = preferences.edit();


        progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progress.setTitleText("Making Ad Premium");

        SharedPreferences preferences = getContext().getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
        post_id = preferences.getInt(Constants.ID, 0);
        my_gc_balance = preferences.getInt(Constants.GC_BALANCE, 0);
        my_rc_balance = preferences.getInt(Constants.RC_BALANCE, 0);
        premium_cost_gc = preferences.getInt(Constants.PREMIUM_COST_GC, 0);
        premium_cost_rc = preferences.getInt(Constants.PREMIUM_COST_RC, 0);


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
    public void onResume() {
        super.onResume();

        my_post_image = view.findViewById(R.id.my_post_image);
        my_post_title = view.findViewById(R.id.my_post_title);
        my_post_date = view.findViewById(R.id.my_post_date);
        my_post_category = view.findViewById(R.id.my_post_category);
        gc_cost = view.findViewById(R.id.gc_cost);
        rc_cost = view.findViewById(R.id.rc_cost);

        tv_gc_balance = view.findViewById(R.id.tv_gc_balance);
        tv_rc_balance = view.findViewById(R.id.tv_rc_balance);

        btn_gc_premium = view.findViewById(R.id.btn_gc_premium);
        btn_rc_premium = view.findViewById(R.id.btn_rc_premium);

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

        gc_cost.setText(String.valueOf(premium_cost_gc) + " " + getString(R.string.gc));
        rc_cost.setText(String.valueOf(premium_cost_rc) + " " + getString(R.string.rc));

        tv_gc_balance.setText(String.valueOf(my_gc_balance));
        tv_rc_balance.setText(String.valueOf(my_rc_balance));

        //Button GC to make add premium
        btn_gc_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (my_gc_balance > premium_cost_gc) {

                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setContentText("On confirmation "+String.valueOf(premium_cost_gc)+" GC will be deducted from your wallet.")
                            .setConfirmText("Confirm")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    sDialog.dismissWithAnimation();

                                    progress.show();

                                    //API to make post premium
                                    Thread thread = new Thread(new Runnable(){

                                        @Override
                                        public void run() {

                                            Call<Status_Response> call = taskMetServer.makeAdPremium(post_id,"GC");
                                            call.enqueue(new Callback<Status_Response>() {
                                                @Override
                                                public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {
                                                    if(response.isSuccessful()){

                                                        Status_Response status_response = response.body();

                                                        String msg = status_response.getStatus();

                                                        if(msg.equals("true")){

                                                            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
                                                            SharedPreferences.Editor again_call_api = sharedPreferences.edit();
                                                            again_call_api.putString(Constants.CALL_API, Constants.TRUE);
                                                            again_call_api.apply();

                                                            progress.dismissWithAnimation();
                                                            getActivity().finish();


                                                        }
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
                                    thread.start();
                                }
                            })
                            .showCancelButton(true)
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                }
                else{

                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Sorry, you don't have enough GC balance to make this Ad Premium.")
                            .setConfirmText("Buy GC")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                    Intent intent = new Intent(getActivity(), FragmentViewActivity.class);
                                    fragmentNameEditor.putString(Constants.FRAGMENT_NAME, "WalletFragment");
                                    fragmentNameEditor.apply();
                                    startActivity(intent);

                                }
                            })
                            .showCancelButton(true)
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                }


            }
        });

        //Button RC to make add premium
        btn_rc_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (my_rc_balance > premium_cost_rc) {

                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setContentText("On confirmation "+String.valueOf(premium_cost_rc)+" RC will be deducted from your wallet.")
                            .setConfirmText("Confirm")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    sDialog.dismissWithAnimation();
                                    progress.show();

                                    //API to make post premium
                                    Thread thread = new Thread(new Runnable(){

                                        @Override
                                        public void run() {

                                            Call<Status_Response> call = taskMetServer.makeAdPremium(post_id,"RC");
                                            call.enqueue(new Callback<Status_Response>() {
                                                @Override
                                                public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {
                                                    if(response.isSuccessful()){

                                                        Status_Response status_response = response.body();

                                                        String msg = status_response.getStatus();

                                                        if(msg.equals("true")){

                                                            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
                                                            SharedPreferences.Editor again_call_api = sharedPreferences.edit();
                                                            again_call_api.putString(Constants.CALL_API, Constants.TRUE);
                                                            again_call_api.apply();

                                                            progress.dismissWithAnimation();
                                                            getActivity().finish();

                                                        }
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
                                    thread.start();
                                }
                            })
                            .showCancelButton(true)
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                }
                else{

                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Sorry, you don't have enough RC balance to make this Ad Premium.")
                            .setConfirmText("Buy RC")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                    Intent intent = new Intent(getActivity(), FragmentViewActivity.class);
                                    fragmentNameEditor.putString(Constants.FRAGMENT_NAME, "WalletFragment");
                                    fragmentNameEditor.apply();
                                    startActivity(intent);

                                }
                            })
                            .showCancelButton(true)
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                }


            }
        });

    }

}