package com.AppValley.TaskMet.Home.Adopters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AppValley.TaskMet.Home.DetailsClass.ShoppingServiceInfo;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.CommonAdModel;

import java.util.ArrayList;

public class ShopAdopter extends RecyclerView.Adapter<ShopAdopter.MyViewHolder> {

    Context context;
    ArrayList<ShoppingServiceInfo> shoppingDetails;
    int row_index = -1;

    private final OnCategorySelection mItemClickListener;

    public ShopAdopter(Context context, ArrayList<ShoppingServiceInfo> shoppingDetails, OnCategorySelection mItemClickListener) {
        this.context = context;
        this.shoppingDetails = shoppingDetails;
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_service_item_show, parent, false);

        final MyViewHolder myViewHolder = new MyViewHolder(itemView);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mItemClickListener.onItemClicked((ShoppingServiceInfo) shoppingDetails.get(myViewHolder.getLayoutPosition()));

                row_index= myViewHolder.getLayoutPosition();
                notifyDataSetChanged();

            }
        });

        return myViewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

       MyViewHolder mHolder = (MyViewHolder) holder;

        ShoppingServiceInfo currentItem = shoppingDetails.get(position);


        holder.icon.setImageResource(currentItem.getImageIcon());
        holder.textView.setText(currentItem.getName());

        if(row_index==position){

            holder.textView.setTextColor(Color.parseColor("#1BA857"));
            holder.textView.setTypeface(null , Typeface.BOLD);

        }
        else {

            holder.textView.setTextColor(Color.parseColor("#002F34"));
            holder.textView.setTypeface(null, Typeface.NORMAL);

        }

    }

    @Override
    public int getItemCount() {
        return shoppingDetails.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView textView;
        //LinearLayout selected_layout;

        public MyViewHolder(View view) {

            super(view);

            icon = itemView.findViewById(R.id.imageIcon);
            textView = itemView.findViewById(R.id.showText);
            //selected_layout = itemView.findViewById(R.id.selected_layout);

        }
    }

    public interface OnCategorySelection {
        void onItemClicked(ShoppingServiceInfo model);
    }

}
