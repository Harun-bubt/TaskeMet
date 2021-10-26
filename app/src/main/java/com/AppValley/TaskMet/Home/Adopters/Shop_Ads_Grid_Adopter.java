package com.AppValley.TaskMet.Home.Adopters;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.HomeAdModel;
import com.AppValley.TaskMet.constant.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class Shop_Ads_Grid_Adopter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<HomeAdModel> shopPostsList;
    String Currency;
    Context context;

    SharedPreferences sharedPreferences;
            
    private final Shop_Ads_Grid_Adopter.MyPOstItemClickListener mItemClickListener;

    public Shop_Ads_Grid_Adopter(ArrayList<HomeAdModel> shopPostsList, Context context, Shop_Ads_Grid_Adopter.MyPOstItemClickListener itemClickListener) {

        this.shopPostsList = shopPostsList;
        this.context = context;
        this.mItemClickListener = itemClickListener;

        sharedPreferences = context.getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_post_grid_item, parent, false);

        //Create View Holder
        final Shop_Ads_Grid_Adopter.MyViewHolder commonViewHolder = new Shop_Ads_Grid_Adopter.MyViewHolder(view);


        //Item Clicks
        commonViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked((HomeAdModel) shopPostsList.get(commonViewHolder.getLayoutPosition()));
            }
        });

        return commonViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        MyViewHolder mHolder = (MyViewHolder) holder;
        HomeAdModel commonAdModel = shopPostsList.get(position);

        if (commonAdModel.getIsPremiumPost() == 0) {

            mHolder.premiumPostIconLayout.setVisibility(View.INVISIBLE);

        } else {

            mHolder.premiumPostIconLayout.setVisibility(View.VISIBLE);

        }

        mHolder.title.setText(commonAdModel.getTitle());

        Currency = sharedPreferences.getString(Constants.CURRENCY, Constants.NULL);


        if(commonAdModel.getCost() != null){

            mHolder.price.setText(Currency+" "+commonAdModel.getCost());

        }
        else if(commonAdModel.getPrice() != null) {

            mHolder.price.setText(Currency+" "+commonAdModel.getPrice());

        }else if(commonAdModel.getSalaryFrom() != null) {

            mHolder.price.setText("Salary: "+commonAdModel.getSalaryFrom()+"+");

        }
        else {
            mHolder.price.setText("Looking for: "+commonAdModel.getLookingFor());
            mHolder.icon_currency.setVisibility(View.GONE);
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
        return shopPostsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView adImage,icon_currency;
        TextView title, price;
        LinearLayout premiumPostIconLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            adImage = itemView.findViewById(R.id.adImage);
            title = itemView.findViewById(R.id.adTitle);
            price = itemView.findViewById(R.id.adPrice);
            icon_currency = itemView.findViewById(R.id.icon_currency);
            premiumPostIconLayout = itemView.findViewById(R.id.premiumPostIconLayout);
        }
    }

    public interface MyPOstItemClickListener {
        void onItemClicked(HomeAdModel model);
    }


}