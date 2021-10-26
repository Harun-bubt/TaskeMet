package com.AppValley.TaskMet.PostTask;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;
import com.google.gson.Gson;


import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;
import gun0912.tedimagepicker.builder.type.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Screen3_PostPhoto extends Fragment {

    Button btnAddImages;
    ImageView backArrow;
    Context context;
    SharedPreferences sharedPreferences;

    TaskMetServer taskMetServer;

    String realPath;
    ArrayList<String> imageList = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen3_post_details, container, false);
        context = getContext();

        //..............................SharedPreferences.................

        sharedPreferences = getContext().getSharedPreferences(Constants.SELECTING_CATEGORY_SERVICE_SHOP, Context.MODE_PRIVATE);

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        //..............................SharedPreferences.................


        btnAddImages = view.findViewById(R.id.btnRetainImages);
        backArrow = view.findViewById(R.id.backArrowImageView);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });


        btnAddImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TedImagePicker.with(getContext())
                        .mediaType(MediaType.IMAGE)
                        .cameraTileBackground(R.color.white)
                        .cameraTileImage(R.drawable.icon_camera_chat)
                        .title("Select Post Images")
                        .max(12,"Maximum limit is 12")
                        .savedDirectoryName("/TaskMet/images")
                        .buttonText("Next")
                        .startAnimation(R.anim.enter_from_right, R.anim.exit_to_left)
                        .finishAnimation(R.anim.enter_from_left, R.anim.exit_to_right)
                        .startMultiImage(new OnMultiSelectedListener() {
                            @Override
                            public void onSelected(@NotNull List<? extends Uri> uriList) {

                                for (int i = 0; i < uriList.size(); i++) {

                                    realPath = getRealPathFromURIPath(uriList.get(i), getActivity());
                                    imageList.add(realPath);

                                }

                                Log.d("myImages_3", ": " + imageList);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String jsonText = gson.toJson(imageList);
                                editor.putString(Constants.POST_PHOTOARRAY, jsonText);
                                editor.apply();

                                goToNextFragment();

                            }
                        });
            }
        });


        return view;

    }


    public void goToNextFragment() {
        Screen4_PostNow screen4_postNow = new Screen4_PostNow();
        androidx.fragment.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.framelayout_post_task, screen4_postNow);
        transaction.addToBackStack("tag");
        transaction.commit();
        Log.d("clicked:", "ok");

    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


}
