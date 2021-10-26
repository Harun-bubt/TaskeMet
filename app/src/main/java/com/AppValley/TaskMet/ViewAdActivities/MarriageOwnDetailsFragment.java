package com.AppValley.TaskMet.ViewAdActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostDataModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostOwnerModel;

public class MarriageOwnDetailsFragment extends Fragment {

    View view;
    PostDataModel postDataModel;
    PostOwnerModel postOwnerModel;
    TextView maritalStatus,education,age,height,religion,castClan,job,salary;



    public MarriageOwnDetailsFragment( PostDataModel postDataModel,PostOwnerModel postOwnerModel)
    {
        this.postDataModel = postDataModel;
        this.postOwnerModel = postOwnerModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_marriage_owner, container, false);
        initializeAllViews();

        maritalStatus.setText(postDataModel.getMaritialStatus());
        education.setText(postDataModel.getMyEducation());
        age.setText(postDataModel.getMyAge());
        height.setText(postDataModel.getMyHeight());
        religion.setText(postDataModel.getMyReligion());
        castClan.setText(postDataModel.getMyCastClan());
        job.setText(postDataModel.getMyJob());

        salary.setText(postOwnerModel.getCurrency()+" "+postDataModel.getMySalary());
        
        return view;
    }
    public void initializeAllViews()
    {
        maritalStatus = view.findViewById(R.id.viewMaritalStatus);
        education = view.findViewById(R.id.viewEducation);
        age = view.findViewById(R.id.viewAge);
        height = view.findViewById(R.id.viewHeight);
        religion = view.findViewById(R.id.viewReligion);
        castClan = view.findViewById(R.id.viewCastClan);
        job = view.findViewById(R.id.viewJob);
        salary = view.findViewById(R.id.viewSalary);
    }
}
