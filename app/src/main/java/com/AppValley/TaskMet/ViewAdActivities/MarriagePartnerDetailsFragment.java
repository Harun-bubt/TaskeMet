package com.AppValley.TaskMet.ViewAdActivities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostDataModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostOwnerModel;


public class MarriagePartnerDetailsFragment extends Fragment {

    View view;
    String ns = "Not Specified";
    PostDataModel postDataModel;
    PostOwnerModel postOwnerModel;
    TextView maritalStatus,education,age,height,religion,castClan,job;



    public MarriagePartnerDetailsFragment( PostDataModel postDataModel,PostOwnerModel postOwnerModel)
    {
        this.postDataModel = postDataModel;
        this.postOwnerModel = postOwnerModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_marriage_partner, container, false);
        initializeAllViews();

        if(!postDataModel.getPartnerEducation().equals("empty")){
            education.setText(postDataModel.getPartnerEducation());
        }else {
            education.setText(ns);
        }

        if(!postDataModel.getPartnerAge().equals("empty")){
            age.setText(postDataModel.getPartnerAge());
        }else {
            age.setText(ns);
        }

        if(!postDataModel.getPartnerHeight().equals("empty")){
            height.setText(postDataModel.getPartnerHeight());
        }else {
            height.setText(ns);
        }

        if(!postDataModel.getPartnerCastClan().equals("empty")){
            castClan.setText(postDataModel.getPartnerCastClan());
        }else {
            castClan.setText(ns);
        }

        if(!postDataModel.getPartnerReligion().equals("empty")){
            religion.setText(postDataModel.getPartnerReligion());
        }else {
            religion.setText(ns);
        }

        if(!postDataModel.getPartnerJob().equals("empty")){
            job.setText(postDataModel.getPartnerJob());
        }else {
            job.setText(ns);
        }

        return view;
    }
    public void initializeAllViews()
    {
        education = view.findViewById(R.id.viewEducation);
        age = view.findViewById(R.id.viewAge);
        height = view.findViewById(R.id.viewHeight);
        religion = view.findViewById(R.id.viewReligion);
        castClan = view.findViewById(R.id.viewCastClan);
        job = view.findViewById(R.id.viewJob);
    }
}
