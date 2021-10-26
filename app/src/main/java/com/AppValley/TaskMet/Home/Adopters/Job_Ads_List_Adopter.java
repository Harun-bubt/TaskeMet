package com.AppValley.TaskMet.Home.Adopters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.JobAdModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.JobAdModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class Job_Ads_List_Adopter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<JobAdModel> allPostShopItems;

    private final MyPOstItemClickListener mItemClickListener;
    Context context;


    public Job_Ads_List_Adopter(ArrayList<JobAdModel> allPostShopItems, Context context, Job_Ads_List_Adopter.MyPOstItemClickListener itemClickListener) {
        this.allPostShopItems = allPostShopItems;
        this.context = context;
        this.mItemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_post_item, parent, false);

        //Create View Holder
        final MyViewHolder commonViewHolder = new MyViewHolder(view);


        //Item Clicks
        commonViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked((JobAdModel) allPostShopItems.get(commonViewHolder.getLayoutPosition()));
            }
        });

        return commonViewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        MyViewHolder mHolder = (MyViewHolder) holder;

        JobAdModel commonAdModel = allPostShopItems.get(position);

        if (commonAdModel.getIsPremiumPost() == 0) {

            mHolder.premiumPostIconLayout.setVisibility(View.INVISIBLE);

        } else {

            mHolder.premiumPostIconLayout.setVisibility(View.VISIBLE);

        }


        mHolder.title.setText(commonAdModel.getTitle());

        mHolder.price.setText("Salary: "+commonAdModel.getSalaryFrom()+" to "+commonAdModel.getSalaryTo());

        mHolder.mainCategory.setText(commonAdModel.getMainCategory());
        mHolder.expireDate.setText(commonAdModel.getExpireDate());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo);

        Glide
                .with(context)
                .load(commonAdModel.getLink())
                .apply(options)
                .into(mHolder.adImage);

    }


    @Override
    public int getItemCount() {
        return allPostShopItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView adImage;
        TextView title, price, mainCategory, expireDate;
        LinearLayout premiumPostIconLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            adImage = itemView.findViewById(R.id.adImage);
            title = itemView.findViewById(R.id.adTitle);
            price = itemView.findViewById(R.id.adPrice);
            mainCategory = itemView.findViewById(R.id.mainCategory);
            expireDate = itemView.findViewById(R.id.expireDate);
            premiumPostIconLayout = itemView.findViewById(R.id.premiumPostIconLayout);

        }
    }

    public interface MyPOstItemClickListener {
        void onItemClicked(JobAdModel model);
    }


}