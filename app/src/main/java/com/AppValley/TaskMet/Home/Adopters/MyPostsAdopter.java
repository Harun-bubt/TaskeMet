package com.AppValley.TaskMet.Home.Adopters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.AppValley.TaskMet.Home.FragmentViewActivity;
import com.AppValley.TaskMet.Home.HomeScreen;
import com.AppValley.TaskMet.PostTask.PostTaskActivity;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MyPostData;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MyWallet;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.NormalPostLimit;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MyPostModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MyPostWithLimitModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostNowPrice;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostPrices;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PremiumPostLimit;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PremiumPostPrice;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.Status_Response;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPostsAdopter extends RecyclerView.Adapter<MyPostsAdopter.RecycleViewHolder> {

    private final Context context;


    int post_id;
    String key,action_type;

    List<MyPostData> myPostData = new ArrayList<>();

    List<NormalPostLimit> normal_post_info = new ArrayList<>();
    List<PremiumPostLimit> premium_post_info = new ArrayList<>();
    List<PostPrices> post_prices_info = new ArrayList<>();
    List<MyWallet> post_wallet_info = new ArrayList<>();

    int normalTodayPosts, normalDayLimit, premiumTodayPosts, premiumDayLimit;
    int postNowPrice_GC, postNowPrice_RC, premiumPostPrice_GC, premiumPostPrice_RC, my_gc_balance, my_rc_balance;

    TaskMetServer taskMetServer;

    public MyPostsAdopter(Context context,List<MyPostData> myPostData,List<NormalPostLimit> normal_post_info,List<PremiumPostLimit> premium_post_info
    ,List<PostPrices> post_prices_info,List<MyWallet> post_wallet_info) {

        this.context = context;
        this.myPostData = myPostData;

        this.normal_post_info = normal_post_info;
        this.premium_post_info = premium_post_info;
        this.post_prices_info = post_prices_info;
        this.post_wallet_info = post_wallet_info;

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflate view
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.my_post_item, parent, false);

        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        SweetAlertDialog progress = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        progress.setTitleText("Loading");
        progress.setCancelable(false);

        Log.d("my_posts", "Adapter testing, Get Limit: " + normal_post_info.get(0).getLimit());


        //-------------------------- Setting post data in recycler view ------------------------------

        //Today & total Limits of Normal Posts
        normalTodayPosts = normal_post_info.get(0).getTodayPostCount();
        normalDayLimit = normal_post_info.get(0).getLimit();

        //Today & total Limit of Premium Posts
        premiumTodayPosts = premium_post_info.get(0).getTodayPremiumPostCount();
        premiumDayLimit = premium_post_info.get(0).getLimit();

        // price
        //post now price
        PostNowPrice postNowPrice = post_prices_info.get(0).getPostNowPrice();
        postNowPrice_GC = postNowPrice.getGC();
        postNowPrice_RC = postNowPrice.getRC();
        //make post premium price
        PremiumPostPrice premiumPostPrice = post_prices_info.get(0).getPremiumPostPrice();
        premiumPostPrice_GC = premiumPostPrice.getGC();
        premiumPostPrice_RC = premiumPostPrice.getRC();

        //My Wallet Balance & Data
        my_gc_balance = post_wallet_info.get(0).getGc_balance();
        my_rc_balance = post_wallet_info.get(0).getRc_balance();


        //post_item = post_data.getPosts().get(position);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo);

        Glide
                .with(context)
                .load(myPostData.get(position).getLink())
                .apply(options)
                .into(holder.post_image);


        holder.post_type.setBackgroundResource(R.drawable.icon_status_normal);
        holder.post_title.setText(myPostData.get(position).getTitle());
        holder.post_date.setText(myPostData.get(position).getCurrentDate() + " - " + myPostData.get(position).getExpireDate());
        holder.post_category.setText(myPostData.get(position).getMainCategory());
        holder.post_views.setText(String.valueOf(myPostData.get(position).getViews()));
        holder.post_likes.setText(String.valueOf(myPostData.get(position).getLikes()));

        Log.d("recyclerData", "Title: "+myPostData.get(position).getTitle());

        post_id = myPostData.get(position).getPost_id();
        key = myPostData.get(position).getKey();


       // myPostData.get(position) = post_data.getPosts().get(position);

        if ((myPostData.get(position).getIsApproved() == 0) && (myPostData.get(position).getIsRejected() == 0) && (myPostData.get(position).getIsWaiting() == 0) && (myPostData.get(position).getIsComplete() == 0) && (myPostData.get(position).getIsClosed() == 0) && (myPostData.get(position).getIsPremiumPost() == 0)) {

            holder.post_type.setBackgroundResource(R.drawable.icon_status_normal);

            holder.main_layout.setBackgroundResource(R.drawable.my_post_pending);
            holder.icon_status.setBackgroundResource(R.drawable.icon_status_wait);
            holder.post_status.setText("Pending");

            holder.post_btn.setText("Make Ad Premium");
        }
        else if ((myPostData.get(position).getIsApproved() == 1) && (myPostData.get(position).getIsRejected() == 0) && (myPostData.get(position).getIsWaiting() == 0) && (myPostData.get(position).getIsComplete() == 0) && (myPostData.get(position).getIsClosed() == 0) && (myPostData.get(position).getIsPremiumPost() == 0)) {

            holder.post_type.setBackgroundResource(R.drawable.icon_status_normal);

            holder.main_layout.setBackgroundResource(R.drawable.my_post_live);
            holder.icon_status.setBackgroundResource(R.drawable.icon_status_posted);
            holder.post_status.setText("Ad is Live");

            holder.post_btn.setText("Make Ad Premium");


        }
        else if ((myPostData.get(position).getIsApproved() == 0) && (myPostData.get(position).getIsRejected() == 1) && (myPostData.get(position).getIsWaiting() == 0) && (myPostData.get(position).getIsComplete() == 0) && (myPostData.get(position).getIsClosed() == 0) && (myPostData.get(position).getIsPremiumPost() == 0)) {

            holder.post_type.setBackgroundResource(R.drawable.icon_status_normal);

            holder.main_layout.setBackgroundResource(R.drawable.my_post_close);
            holder.icon_status.setBackgroundResource(R.drawable.icon_status_closed);
            holder.post_status.setText("Ad is Rejected");
            holder.post_status.setTextColor(context.getResources().getColor(R.color.red_1));

            holder.post_btn.setBackgroundResource(R.drawable.btn_sign_in);
            holder.post_btn.setText("Edit Post");

     /*       action_type = "edit";
            condition_no = "3";*/

        }
        else if ((myPostData.get(position).getIsApproved() == 0) && (myPostData.get(position).getIsRejected() == 0) && (myPostData.get(position).getIsWaiting() == 0) && (myPostData.get(position).getIsComplete() == 0) && (myPostData.get(position).getIsClosed() == 1) && (myPostData.get(position).getIsPremiumPost() == 0)) {

            holder.post_type.setBackgroundResource(R.drawable.icon_status_normal);

            holder.main_layout.setBackgroundResource(R.drawable.my_post_close);
            holder.icon_status.setBackgroundResource(R.drawable.icon_status_closed);
            holder.post_status.setText("Ad is closed");

            holder.post_btn.setBackgroundResource(R.drawable.btn_sign_in);
            holder.post_btn.setText("Delete Ad");


        }
        else if ((myPostData.get(position).getIsApproved() == 1) && (myPostData.get(position).getIsRejected() == 0) && (myPostData.get(position).getIsWaiting() == 0) && (myPostData.get(position).getIsComplete() == 0) && (myPostData.get(position).getIsClosed() == 0) && (myPostData.get(position).getIsPremiumPost() == 1)) {

            holder.main_layout.setBackgroundResource(R.drawable.my_post_premium);
            holder.post_type.setBackgroundResource(R.drawable.icon_status_gold);
            holder.post_btn.setBackgroundResource(R.drawable.btn_sign_in);
            holder.icon_status.setBackgroundResource(R.drawable.icon_status_posted);
            holder.post_status.setText("Ad is Live");

            holder.post_btn.setText("Mark as sold");


        }
        else if ((myPostData.get(position).getIsApproved() == 0) && (myPostData.get(position).getIsRejected() == 0) && (myPostData.get(position).getIsWaiting() == 0) && (myPostData.get(position).getIsComplete() == 0) && (myPostData.get(position).getIsClosed() == 0) && (myPostData.get(position).getIsPremiumPost() == 1)) {

            holder.post_type.setBackgroundResource(R.drawable.icon_status_gold);
            holder.post_btn.setBackgroundResource(R.drawable.btn_sign_in);
            holder.icon_status.setBackgroundResource(R.drawable.icon_status_wait);
            holder.post_status.setText("Pending");

            holder.post_btn.setText("Edit Ad");

        }
        else if ((myPostData.get(position).getIsApproved() == 1) && (myPostData.get(position).getIsRejected() == 0) && (myPostData.get(position).getIsWaiting() == 0) && (myPostData.get(position).getIsComplete() == 1) && (myPostData.get(position).getIsClosed() == 0) && (myPostData.get(position).getIsPremiumPost() == 0)) {

            holder.post_type.setBackgroundResource(R.drawable.icon_status_normal);

            holder.main_layout.setBackgroundResource(R.drawable.my_post_pending);
            holder.icon_status.setBackgroundResource(R.drawable.icon_status_sold);
            holder.post_status.setText("Sold");

            holder.post_btn.setBackgroundResource(R.drawable.btn_sign_in);
            holder.post_btn.setText("Delete Ad");


        }
        else if ((myPostData.get(position).getIsApproved() == 1) && (myPostData.get(position).getIsRejected() == 0) && (myPostData.get(position).getIsWaiting() == 0) && (myPostData.get(position).getIsComplete() == 1) && (myPostData.get(position).getIsClosed() == 0) && (myPostData.get(position).getIsPremiumPost() == 1)) {

            holder.post_type.setBackgroundResource(R.drawable.icon_status_gold);

            holder.main_layout.setBackgroundResource(R.drawable.my_post_pending);
            holder.icon_status.setBackgroundResource(R.drawable.icon_status_sold);
            holder.post_status.setText("Sold");
            holder.post_status.setTextColor(context.getResources().getColor(R.color.text_color));

            holder.post_btn.setBackgroundResource(R.drawable.btn_sign_in);
            holder.post_btn.setText("Delete Ad");


        }
        else if ((myPostData.get(position).getIsApproved() == 0) && (myPostData.get(position).getIsRejected() == 0) && (myPostData.get(position).getIsWaiting() == 1) && (myPostData.get(position).getIsComplete() == 0) && (myPostData.get(position).getIsClosed() == 0) && (myPostData.get(position).getIsPremiumPost() == 0)) {
            if(normalTodayPosts <= normalDayLimit){

                holder.post_type.setBackgroundResource(R.drawable.icon_status_normal);

                holder.main_layout.setBackgroundResource(R.drawable.my_post_expire);
                holder.icon_status.setBackgroundResource(R.drawable.icon_status_wait);
                holder.post_status.setText("Free Post this Ad");

                holder.post_btn.setBackgroundResource(R.drawable.btn_sign_in);
                holder.post_btn.setText("Post Now");

            }
            else{

                holder.post_type.setBackgroundResource(R.drawable.icon_status_normal);

                holder.main_layout.setBackgroundResource(R.drawable.my_post_expire);
                holder.icon_status.setBackgroundResource(R.drawable.icon_status_wait);
                holder.post_status.setText("Ad is on Waiting");

                holder.post_btn.setBackgroundResource(R.drawable.btn_sign_in);
                holder.post_btn.setText("Post Now");

            }
        }


        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //myPostData.get(position) = post_data.getPosts().get(position);
                key = myPostData.get(position).getKey();

                SharedPreferences preferences = context.getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
                SharedPreferences.Editor premium_data = preferences.edit();

                premium_data.putString(Constants.FRAGMENT_NAME, "Screen5_PremiumPost");
                premium_data.putInt(Constants.ID, myPostData.get(position).getPost_id());
                premium_data.putInt(Constants.GC_BALANCE, my_gc_balance);
                premium_data.putInt(Constants.RC_BALANCE, my_rc_balance);
                premium_data.putInt(Constants.PREMIUM_COST_GC, premiumPostPrice_GC);
                premium_data.putInt(Constants.PREMIUM_COST_RC, premiumPostPrice_RC);
                premium_data.apply();

                Intent intent = new Intent(context, PostTaskActivity.class);
                intent.putExtra(Constants.POST_ACTION, "none");
                intent.putExtra(Constants.IS_PREMIUM, String.valueOf(myPostData.get(position).getIsPremiumPost()));
                intent.putExtra(Constants.VIEW_AD, true);
                intent.putExtra(Constants.ID, String.valueOf(myPostData.get(position).getPost_id()));
                intent.putExtra(Constants.KEY, myPostData.get(position).getKey());

                context.startActivity(intent);

            }
        });


        //Post button
        holder.post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                action_type = holder.post_btn.getText().toString().trim();

                SharedPreferences preferences = context.getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
                SharedPreferences.Editor premium_data = preferences.edit();

                switch (action_type){

                    case "Make Ad Premium":
                        Intent intent = new Intent(context, FragmentViewActivity.class);

                        //myPostData.get(position) = post_data.getPosts().get(position);

                        premium_data.putString(Constants.FRAGMENT_NAME, "Screen5_PremiumPost");

                        premium_data.putInt(Constants.ID, myPostData.get(position).getPost_id());
                        premium_data.putInt(Constants.GC_BALANCE, my_gc_balance);
                        premium_data.putInt(Constants.RC_BALANCE, my_rc_balance);
                        premium_data.putInt(Constants.PREMIUM_COST_GC, premiumPostPrice_GC);
                        premium_data.putInt(Constants.PREMIUM_COST_RC, premiumPostPrice_RC);
                        premium_data.apply();

                        context.startActivity(intent);
                        break;


                    case "Edit Ad":

                        //myPostData.get(position) = post_data.getPosts().get(position);
                        key = myPostData.get(position).getKey();
                        Intent intent1 = new Intent(context, PostTaskActivity.class);
                        intent1.putExtra(Constants.EDIT_POST, true);
                        intent1.putExtra(Constants.POST_ACTION, "edit");
                        intent1.putExtra(Constants.IS_PREMIUM, String.valueOf(myPostData.get(position).getIsPremiumPost()));
                        intent1.putExtra(Constants.ID, String.valueOf(myPostData.get(position).getPost_id()));
                        intent1.putExtra(Constants.KEY, myPostData.get(position).getKey());
                        context.startActivity(intent1);

                        break;

                    case "Delete Ad":
                       // myPostData.get(position) = post_data.getPosts().get(position);
                        key = myPostData.get(position).getKey();
                        Intent intent2 = new Intent(context, PostTaskActivity.class);
                        intent2.putExtra(Constants.VIEW_AD, true);
                        intent2.putExtra(Constants.POST_ACTION, "delete");
                        intent2.putExtra(Constants.IS_PREMIUM, String.valueOf(myPostData.get(position).getIsPremiumPost()));
                        intent2.putExtra(Constants.ID, String.valueOf(myPostData.get(position).getPost_id()));
                        intent2.putExtra(Constants.KEY, myPostData.get(position).getKey());
                        context.startActivity(intent2);

                        break;

                    case "Post Now":

                        if(normalTodayPosts <= normalDayLimit){

                            progress.show();

                            //API to make post premium
                            Thread thread = new Thread(new Runnable(){

                                @Override
                                public void run() {

                                    //myPostData.get(position) = post_data.getPosts().get(position);

                                    Call<Status_Response> call = taskMetServer.postNowFree(myPostData.get(position).getPost_id());
                                    call.enqueue(new Callback<Status_Response>() {
                                        @Override
                                        public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {
                                            if(response.isSuccessful()){

                                                Status_Response status_response = response.body();

                                                String msg = status_response.getStatus();

                                                if(msg.equals("true")){

                                                    progress.dismissWithAnimation();

                                                    SharedPreferences preferences = context.getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor premium_data = preferences.edit();

                                                    premium_data.putInt(Constants.SCREEN_PAGER, 3);
                                                    premium_data.apply();

                                                    context.startActivity(new Intent(context, HomeScreen.class));

                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Status_Response> call, Throwable t) {
                                            progress.dismissWithAnimation();
                                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });
                            thread.start();

                        }
                        else{

                           // myPostData.get(position) = post_data.getPosts().get(position);
                            //Waiting Ad post now with payment
                            Intent intent3 = new Intent(context, FragmentViewActivity.class);

                            SharedPreferences preferences1 = context.getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
                            SharedPreferences.Editor premium_data1 = preferences1.edit();
                            premium_data1.putString(Constants.FRAGMENT_NAME, "Screen6_PostNow");
                            premium_data1.putInt(Constants.ID, myPostData.get(position).getPost_id());
                            premium_data1.putInt(Constants.GC_BALANCE, my_gc_balance);
                            premium_data1.putInt(Constants.RC_BALANCE, my_rc_balance);
                            premium_data1.putInt(Constants.POST_NOW_COST_GC, postNowPrice_GC);
                            premium_data1.putInt(Constants.POST_NOW_COST_RC, postNowPrice_RC);
                            premium_data1.apply();

                            context.startActivity(intent3);

                        }

                        break;

                    case "Mark as sold":

                        //myPostData.get(position) = post_data.getPosts().get(position);

                        key = myPostData.get(position).getKey();
                        Intent intent4 = new Intent(context, PostTaskActivity.class);
                        intent4.putExtra(Constants.VIEW_AD, true);
                        intent4.putExtra(Constants.POST_ACTION, "sold");
                        intent4.putExtra(Constants.IS_PREMIUM, String.valueOf(myPostData.get(position).getIsPremiumPost()));
                        intent4.putExtra(Constants.ID, String.valueOf(myPostData.get(position).getPost_id()));
                        intent4.putExtra(Constants.KEY, myPostData.get(position).getKey());
                        context.startActivity(intent4);

                        break;

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        //Log.d("my_posts", "Adopter Ads list size: " + myPostData.size());
        return myPostData.size();
    }

    public static class RecycleViewHolder extends RecyclerView.ViewHolder {

        LinearLayout main_layout;
        ImageView post_image, icon_status, post_type;
        TextView post_title, post_date, post_category, post_status, post_views, post_likes;
        Button post_btn;

        public RecycleViewHolder(@NonNull View itemView) {

            super(itemView);

            main_layout = itemView.findViewById(R.id.main_layout);
            post_image = itemView.findViewById(R.id.post_image);
            post_title = itemView.findViewById(R.id.post_title);
            post_date = itemView.findViewById(R.id.post_date);
            post_category = itemView.findViewById(R.id.post_category);
            post_status = itemView.findViewById(R.id.post_status);
            post_views = itemView.findViewById(R.id.post_views);
            post_likes = itemView.findViewById(R.id.post_likes);
            post_btn = itemView.findViewById(R.id.post_btn);
            icon_status = itemView.findViewById(R.id.icon_status);
            post_type = itemView.findViewById(R.id.post_type);

        }
    }
}
