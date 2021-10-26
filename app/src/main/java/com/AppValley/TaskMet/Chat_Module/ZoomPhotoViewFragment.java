package com.AppValley.TaskMet.Chat_Module;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.AppValley.TaskMet.R;

public class ZoomPhotoViewFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View zoomPhotoview = inflater.inflate(R.layout.fragment_zoom_photo_view, container, false);


        zoomPhotoview.setFocusableInTouchMode(true);
        zoomPhotoview.requestFocus();
        zoomPhotoview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getFragmentManager().popBackStack("tag", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    return true;
                }
                return false;
            }
        });

        com.github.chrisbanes.photoview.PhotoView photoView = zoomPhotoview.findViewById(R.id.zoomPhotoView);
        Glide.with(getContext())
                .load(MessageAdapter.photoZoomUrl)
                .placeholder(R.drawable.avatar)
                .into(photoView);

        return zoomPhotoview;

    }
}
