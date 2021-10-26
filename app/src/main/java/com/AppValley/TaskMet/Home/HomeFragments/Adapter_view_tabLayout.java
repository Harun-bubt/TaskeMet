package com.AppValley.TaskMet.Home.HomeFragments;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostDataModel;
import com.AppValley.TaskMet.ViewAdDetails.MarriageOwnDetailsFragment;
import com.AppValley.TaskMet.ViewAdDetails.MarriagePartnerDetailsFragment;

public class Adapter_view_tabLayout extends FragmentStateAdapter {

    PostDataModel postDataModel;

    public Adapter_view_tabLayout(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, PostDataModel postDataModel) {
        super(fragmentManager, lifecycle);
        this.postDataModel = postDataModel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 0) {

            return new MarriageOwnDetailsFragment(postDataModel);

        }
        return new MarriagePartnerDetailsFragment(postDataModel);

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
