package com.AppValley.TaskMet.ViewAdDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostDataModel;


public class MarriagePartnerDetailsFragment extends Fragment {

    View view;
    PostDataModel myPostModel;
    TextView partnerEducation,partnerAge,partnerHeight,partnerReligion,partnerCast,partnerJob;

    public MarriagePartnerDetailsFragment( PostDataModel postDataModel)
    {
        this.myPostModel = postDataModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_marriage_partner_details, container, false);

        partnerEducation = view.findViewById(R.id.partnerEducation);
        partnerAge = view.findViewById(R.id.partnerAge);
        partnerHeight = view.findViewById(R.id.partnerHeight);
        partnerReligion = view.findViewById(R.id.partnerReligion);
        partnerCast = view.findViewById(R.id.partnerCast);
        partnerJob = view.findViewById(R.id.partnerJob);

        partnerEducation.setText(myPostModel.getPartnerEducation());
        partnerAge.setText(myPostModel.getPartnerAge());
        partnerHeight.setText(myPostModel.getPartnerHeight());
        partnerReligion.setText(myPostModel.getPartnerReligion());
        partnerCast.setText(myPostModel.getPartnerCastClan());
        partnerJob.setText(myPostModel.getPartnerJob());

        return view;
    }

}
