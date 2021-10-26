package com.AppValley.TaskMet.ViewAdDetails;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.AppValley.TaskMet.Home.Adopters.MyPostsAdopter;
import com.AppValley.TaskMet.Home.Adopters.SliderAdapter;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MyPostModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MyPostWithLimitModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostImagesModel;
import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class View_Full_Image extends Fragment {

    View view;

    String image_url;
    private List<PostImagesModel> mSliderItems = new ArrayList<>();
    private final List<String> imageList = new ArrayList<>();

    RecyclerView zoom_image_recycler;
    ZoomImageAdopter zoomImageAdopter;
    LinearLayoutManager linearLayoutManager;

    TextView image_counter;


    public View_Full_Image(String image_url,List<PostImagesModel> mSliderItems) {
        this.image_url = image_url;
        this.mSliderItems = mSliderItems;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.view_full_image, container, false);


        image_counter = view.findViewById(R.id.image_counter);
        ZoomageView zoomageView = view.findViewById(R.id.myZoomageView);

        Glide.with(getContext())
                .load(image_url)
                .fitCenter()
                .into(zoomageView);

        //----------------------------------- Slider of images -------------------------------------

        for(int i=0 ; i<mSliderItems.size() ; i++ ){

            PostImagesModel imageItem = mSliderItems.get(i);

            imageList.add(imageItem.getLink());

        }

        Log.d("seekBarData", "Image List Url: "+imageList);

        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        zoom_image_recycler = view.findViewById(R.id.zoom_image_recycler);
        zoom_image_recycler.setLayoutManager(linearLayoutManager);
        zoomImageAdopter = new ZoomImageAdopter(getContext(), imageList);
        zoom_image_recycler.setAdapter(zoomImageAdopter);

        image_counter.setText("1/"+String.valueOf(imageList.size()));


        //Call click method
        zoomImageAdopter.setListener(new ZoomImageAdopter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {

                image_counter.setText(String.valueOf(position+1)+"/"+String.valueOf(imageList.size()));

                Glide.with(getContext())
                        .load(imageList.get(position))
                        .fitCenter()
                        .into(zoomageView);

            }
        });

        //zoom_image_recycler.setHasFixedSize(true);


        //------------------------------------------------------------------------------------------


        return view;
    }
}