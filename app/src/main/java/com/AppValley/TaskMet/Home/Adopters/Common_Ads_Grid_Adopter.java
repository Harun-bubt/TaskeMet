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
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.CommonAdModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class Common_Ads_Grid_Adopter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<CommonAdModel> commonPostsList;
    private final Common_Ads_Grid_Adopter.MyPOstItemClickListener mItemClickListener;
    Context context;

    public Common_Ads_Grid_Adopter(ArrayList<CommonAdModel> commonPostsList, Context context, Common_Ads_Grid_Adopter.MyPOstItemClickListener itemClickListener) {
        this.commonPostsList = commonPostsList;
        this.context = context;
        this.mItemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_post_grid_item, parent, false);

        //Create View Holder
        final Common_Ads_Grid_Adopter.MyViewHolder commonViewHolder = new Common_Ads_Grid_Adopter.MyViewHolder(view);


        //Item Clicks
        commonViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked((CommonAdModel) commonPostsList.get(commonViewHolder.getLayoutPosition()));
            }
        });

        return commonViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        MyViewHolder mHolder = (MyViewHolder) holder;
        CommonAdModel commonAdModel = commonPostsList.get(position);

        if (commonAdModel.getIsPremiumPost() == 0) {

            mHolder.premiumPostIconLayout.setVisibility(View.INVISIBLE);

        } else {

            mHolder.premiumPostIconLayout.setVisibility(View.VISIBLE);

        }


        mHolder.title.setText(commonAdModel.getTitle());

        if(commonAdModel.getPrice() != null){

            mHolder.price.setText(commonAdModel.getPrice());

        }else {

            mHolder.price.setText(commonAdModel.getCost());

        }

        RequestOptions options = new RequestOptions()
                //.centerCrop()
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
        return commonPostsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView adImage;
        TextView title, price;
        LinearLayout premiumPostIconLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            adImage = itemView.findViewById(R.id.adImage);
            title = itemView.findViewById(R.id.adTitle);
            price = itemView.findViewById(R.id.adPrice);
            premiumPostIconLayout = itemView.findViewById(R.id.premiumPostIconLayout);

        }
    }

    public interface MyPOstItemClickListener {
        void onItemClicked(CommonAdModel model);
    }


}