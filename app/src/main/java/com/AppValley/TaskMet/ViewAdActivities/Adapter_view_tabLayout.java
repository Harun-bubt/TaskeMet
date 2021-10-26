package com.AppValley.TaskMet.ViewAdActivities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostDataModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostOwnerModel;

class Adapter_view_tabLayout extends FragmentStateAdapter {

    PostDataModel postDataModel;
    PostOwnerModel postOwnerModel;

    public Adapter_view_tabLayout(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, PostDataModel postDataModel, PostOwnerModel postOwnerModel) {
        super(fragmentManager, lifecycle);
        this.postDataModel = postDataModel;
        this.postOwnerModel = postOwnerModel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 0) {
            return new MarriageOwnDetailsFragment(postDataModel,postOwnerModel);
        }
        return new MarriagePartnerDetailsFragment(postDataModel,postOwnerModel);

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}