package com.AppValley.TaskMet.ViewAdDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AppValley.TaskMet.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ZoomImageAdopter extends RecyclerView.Adapter<ZoomImageAdopter.RecycleViewHolder> {

    private final Context context;
    private List<String> imageList = new ArrayList<>();

    public ZoomImageAdopter(Context context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflate view
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_full_image, parent, false);

        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {

      //  holder.setIsRecyclable(false);

        Glide.with(context)
                .load(imageList.get(position))
                .fitCenter()
                .into(holder.full_image);

        holder.full_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClick(position);

                }
        });
    }

    @Override
    public int getItemCount() {

        return imageList.size();

    }


    public static class RecycleViewHolder extends RecyclerView.ViewHolder {

        ImageView full_image;

        public RecycleViewHolder(@NonNull View itemView) {

            super(itemView);

            full_image = itemView.findViewById(R.id.full_image);


        }
    }

    //Declarative interface
    private ItemClickListener listener;
    //set method
    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }
    //Defining interface
    public interface ItemClickListener{
        //Achieve the click method, passing the subscript.
        void onItemClick(int position);
    }

}