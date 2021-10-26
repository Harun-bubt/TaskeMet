package com.AppValley.TaskMet.ViewAdDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostDataModel;

public class MarriageOwnDetailsFragment extends Fragment {

    View view;
    PostDataModel myPostModel;
    TextView posterStatus,posterEducation,posterAge,posterHeight,posterReligion,posterCast,posterJob,posterSalary;



    public MarriageOwnDetailsFragment( PostDataModel postDataModel )
    {
        this.myPostModel = postDataModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_marriage_own_details, container, false);

        posterStatus = view.findViewById(R.id.posterStatus);
        posterEducation = view.findViewById(R.id.posterEducation);
        posterAge = view.findViewById(R.id.posterAge);
        posterHeight = view.findViewById(R.id.posterHeight);
        posterReligion = view.findViewById(R.id.posterReligion);
        posterCast = view.findViewById(R.id.posterCast);
        posterJob = view.findViewById(R.id.posterJob);
        posterSalary = view.findViewById(R.id.posterSalary);


        posterStatus.setText(myPostModel.getMaritialStatus());
        posterEducation.setText(myPostModel.getMyEducation());
        posterAge.setText(myPostModel.getMyAge());
        posterHeight.setText(myPostModel.getMyHeight());
        posterReligion.setText(myPostModel.getMyReligion());
        posterCast.setText(myPostModel.getMyCastClan());
        posterJob.setText(myPostModel.getMyJob());
        posterSalary.setText(myPostModel.getMySalary());


        return view;
    }
  }
