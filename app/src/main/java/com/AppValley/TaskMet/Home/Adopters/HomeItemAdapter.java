package com.AppValley.TaskMet.Home.Adopters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.HomeAdModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class HomeItemAdapter  extends RecyclerView.Adapter<HomeItemAdapter.MyViewHolder> {

    private LayoutInflater mInflater;
    Context context;
    private ItemClickListener mClickListener;
    private ArrayList<HomeAdModel> homeAdModels = new ArrayList<>();

    // data is passed into the constructor
    public HomeItemAdapter(Context context, ArrayList<HomeAdModel> homeAdModels) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.homeAdModels = homeAdModels;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_home_ads, parent, false);
        return new MyViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.adPrice.setText(homeAdModels.get(position).getCost());

        String cate = homeAdModels.get(position).getMainCategory();
        if(cate.equals("Travel") || cate.equals("travel")){
            holder.adPrice.setVisibility(View.GONE);
        }
        holder.ad_description.setText(homeAdModels.get(position).getTitle());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.logo_transparent)
                .error(R.drawable.logo_transparent);

        Glide
                .with(context)
                .load(homeAdModels.get(position).getLink())
                .apply(options)
                .into(holder.adImage);

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return homeAdModels.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView ad_description,adPrice;
        ImageView adImage;

        MyViewHolder(View itemView) {
            super(itemView);

            adImage = itemView.findViewById(R.id.adImage);
            adPrice = itemView.findViewById(R.id.adPrice);
            ad_description = itemView.findViewById(R.id.priceTextView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    HomeAdModel getItem(int id) {
        return homeAdModels.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}