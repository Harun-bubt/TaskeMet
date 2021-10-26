package com.AppValley.TaskMet.PostTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.NothingSelectedSpinnerAdapter;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.constant.Constants;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Screen2_Jobdetails extends Fragment implements AdapterView.OnItemSelectedListener {

    Context context;
    ArrayAdapter<CharSequence> adapter;
    Spinner jobCategorySpinner,positionTypeSpinner,salaryPeriodSpinner;
    Button jobOffer,cvResume;
    LinearLayout salaryFromSalaryToLayout,companyNameLayout;
    TextView describtionAboutJob;
    ImageView backArrow;


    EditText jobTitleEditText,jobDescribtionEditText,salaryFromEditText,salaryToEditText;
    EditText companyNameEditText;
    ImageView iconJobTitle, iconJobDescribtion;

    Button btnNext;
    //.............All job info................
    String jobType = "offering Job";
    String jobCategory;
    String positionType;
    String salaryPeriod;
    String job_title;
    String salary_From;
    String salary_to;
    String job_description;
    String company_name;

    public  static SharedPreferences sharedPreferences;
    SharedPreferences.Editor service_jobCategory_editor;

    boolean isOfferingJob = true;

    boolean isJobCategorySelected = false;
    boolean isPositiontypeSelected = false;
    boolean isSalaryPeriodSelected = false;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_job, container, false);
        context = getContext();

        onBackPressed(view);
        initializeAllViews();



        setAlertDialogForIcon(iconJobTitle,"Write a suitable title for job");
        setAlertDialogForIcon(iconJobDescribtion,"Describe your job details");




        //........................Shared preferences..........................
        sharedPreferences = getContext().getSharedPreferences(Constants.SERVICE_JOB_NEW_POST, Context.MODE_PRIVATE);
        service_jobCategory_editor = sharedPreferences.edit();
        //........................Shared preferences..........................




        //...................AnimationView for Visible and gone.................

        Animation scaleDown = AnimationUtils.loadAnimation(getContext(),R.anim.scale_down);
        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        //...................AnimationView for Visible and gone.................




        //.................iconAppearDisapear.....................
        iconAppearDisAppear(jobTitleEditText,iconJobTitle);
        iconAppearDisAppear(jobDescribtionEditText,iconJobDescribtion);
        //.................iconAppearDisapear......................


        //............setSpinner Text............................................
        

        setJobCategory();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobCategorySpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_default_text,
                        getContext()));
        setPositionType();
        jobCategorySpinner.setOnItemSelectedListener(this);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionTypeSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_default_text,
                        getContext()));
        positionTypeSpinner.setOnItemSelectedListener(this);
        setSalaryPeriodType();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        salaryPeriodSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_default_text,
                        getContext()));
        salaryPeriodSpinner.setOnItemSelectedListener(this);

        //............setSpinner Text...........................................


       //....................ButtonOnclickListener.............................
          backArrow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().finish();
         }
        });

        jobOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jobType = "offering Job";
                isOfferingJob = true;


                companyNameLayout.setVisibility(View.VISIBLE);
                companyNameLayout.startAnimation(scaleUp);
                describtionAboutJob.setText("Describe job you're offering*");
                jobOffer.setBackgroundResource(R.drawable.button_selected);
                jobOffer.setTextColor(getResources().getColor(R.color.Theme1));
                cvResume.setBackgroundResource(R.drawable.button_unselected);
                cvResume.setTextColor(getResources().getColor(R.color.text_light_gray));
            }
        });
        cvResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jobType = "cv Resume";
                isOfferingJob = false;

                companyNameLayout.startAnimation(scaleDown);
                companyNameLayout.setVisibility(View.GONE);
                describtionAboutJob.setText("Describe yourself*");
                cvResume.setBackgroundResource(R.drawable.button_selected);
                cvResume.setTextColor(getResources().getColor(R.color.Theme1));
                jobOffer.setBackgroundResource(R.drawable.button_unselected);
                jobOffer.setTextColor(getResources().getColor(R.color.text_light_gray));
            }
        });

        //....................ButtonOnclickListener.............................

        //...................Go to Next Fragment.................................

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isJobCategorySelected)
                {
                    TextView errorText = (TextView)jobCategorySpinner.getSelectedView();
                    errorText.setError("This field is required");
                    errorText.requestFocus();
                    return;
                }
                if(!isPositiontypeSelected)
                {
                    TextView errorText = (TextView)positionTypeSpinner.getSelectedView();
                    errorText.setError("This field is required");
                    errorText.requestFocus();
                    return;
                }
                if(!isSalaryPeriodSelected)
                {
                    TextView errorText = (TextView)salaryPeriodSpinner.getSelectedView();
                    errorText.setError("This field is required");
                    errorText.requestFocus();
                    return;
                }


                if(jobTitleEditText.getText().toString().equals(""))
                {
                    jobTitleEditText.setError("This field is required");
                    jobTitleEditText.requestFocus();
                    return ;
                }else
                {
                    job_title = jobTitleEditText.getText().toString();
                }
                if(salaryFromEditText.getText().toString().equals(""))
                {
                    salaryFromEditText.setError("This field is required");
                    salaryFromEditText.requestFocus();
                    return ;
                }else
                {
                    if(Integer.parseInt(salaryFromEditText.getText().toString()) <= 0)
                    {
                        salaryFromEditText.setError("Should be greater than 0");
                        salaryFromEditText.requestFocus();
                        return ;
                    }else if(Integer.parseInt(salaryFromEditText.getText().toString()) < 50)
                    {
                        salaryFromEditText.setError("Salary not less than 50");
                        salaryFromEditText.requestFocus();
                        return ;
                    }
                    else
                    {
                        salary_From = salaryFromEditText.getText().toString();
                    }

                }
                if(salaryToEditText.getText().toString().equals(""))
                {
                    salaryToEditText.setError("This field is required");
                    salaryToEditText.requestFocus();
                    return ;
                }else
                {
                    if(Integer.parseInt(salaryToEditText.getText().toString()) <= 0)
                    {
                        salaryToEditText.setError("Should be greater than 0");
                        salaryToEditText.requestFocus();
                        return ;
                    }else
                    {
                        salary_to = salaryToEditText.getText().toString();
                    }

                }
                int salaryFrom = Integer.parseInt(salaryFromEditText.getText().toString());
                int salaryTo =  Integer.parseInt(salaryToEditText.getText().toString());
                if(salaryFrom >= salaryTo)
                {
                    salaryToEditText.setError("Salary From must be less than Salary to");
                    salaryToEditText.requestFocus();
                    return ;
                }


                if(jobDescribtionEditText.getText().toString().equals(""))
                {
                    jobDescribtionEditText.setError("This field is required");
                    jobDescribtionEditText.requestFocus();
                    return ;
                }else
                {
                    job_description = jobDescribtionEditText.getText().toString();
                }
                if(!isOfferingJob)
                {
                    company_name = "empty";
                }else if(companyNameEditText.getText().toString().equals(""))
                {
                    companyNameEditText.setError("This field is required");
                    companyNameEditText.requestFocus();
                    return ;
                }else{
                    company_name = companyNameEditText.getText().toString();
                }
                storeAllInfo();


                Screen3_PostPhoto screen3_postPhoto = new Screen3_PostPhoto();
                androidx.fragment.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.framelayout_post_task, screen3_postPhoto);
                transaction.addToBackStack("tag");
                transaction.commit();
                Log.d("clicked:","ok");
            }
        });
        //...................Go to Next Fragment.................................

        return view;

    }

    private void setJobCategory() {
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.jobcategory, android.R.layout.simple_spinner_item);
    }
    private void setPositionType() {
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.position_type, android.R.layout.simple_spinner_item);
    }
    private void setSalaryPeriodType() {
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.salaryPeriodcategory, android.R.layout.simple_spinner_item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position > 0) {

            if(parent.getItemAtPosition(1).equals(getResources().getString(R.string.online_remote)))
            {
                String text = parent.getItemAtPosition(position).toString();
                jobCategory = text;
                isJobCategorySelected = true;
            }
            else if(parent.getItemAtPosition(1).equals(getResources().getString(R.string.contract_base)))
            {
                String text = parent.getItemAtPosition(position).toString();
                positionType = text;
                isPositiontypeSelected = true;

            }
            else
            {
                String text = parent.getItemAtPosition(position).toString();
                salaryPeriod = text;
                isSalaryPeriodSelected = true;
            }


        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void iconAppearDisAppear(EditText editText,ImageView imageView){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText.getText().toString().equals("")){
                    imageView.setVisibility(View.VISIBLE);
                }else
                {
                    imageView.setVisibility(View.GONE);
                }
            }
        });
    }
    public void storeAllInfo(){
        service_jobCategory_editor.putString(Constants.SERVICE_JOB_TYPE,jobType);
        service_jobCategory_editor.putString(Constants.SERVICE_JOB_CATEGORY,jobCategory);
        service_jobCategory_editor.putString(Constants.SERVICE_POSITION_TYPE,positionType);
        service_jobCategory_editor.putString(Constants.SERVICE_JOB_TITLE,job_title);
        service_jobCategory_editor.putString(Constants.SERVICE_SALARY_FROM,salary_From);
        service_jobCategory_editor.putString(Constants.SERVICE_SALARY_TO,salary_to);
        service_jobCategory_editor.putString(Constants.SERVICE_SALARY_PERIOD,salaryPeriod);
        service_jobCategory_editor.putString(Constants.SERVICE_JOB_DESCRIPTION,job_description);
        service_jobCategory_editor.putString(Constants.SERVICE_COMPANY_NAME,company_name);
        service_jobCategory_editor.apply();
    }
    public void onBackPressed(View view){
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {

            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        } );

    }
    public void setAlertDialogForIcon(ImageView imageView, String contentText) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setContentText(contentText)
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });
    }
    public void initializeAllViews()
    {
        salaryFromEditText = view.findViewById(R.id.salaryFromEditText);
        salaryToEditText = view.findViewById(R.id.salaryToEditText);
        companyNameEditText = view.findViewById(R.id.companyNameEditText);
        jobCategorySpinner = view.findViewById(R.id.jobCategorySpinner);
        positionTypeSpinner = view.findViewById(R.id.positionTypeSpinner);
        salaryPeriodSpinner = view.findViewById(R.id.salaryCategorySpinner);
        jobOffer = view.findViewById(R.id.btnJobOffer);
        cvResume = view.findViewById(R.id.btnCvResume);
        salaryFromSalaryToLayout = view.findViewById(R.id.layoutSalaryFromSalaryTo);
        companyNameLayout = view.findViewById(R.id.layoutCompanyName);
        describtionAboutJob = view.findViewById(R.id.textViewDescribtionAboutJob);
        jobTitleEditText = view.findViewById(R.id.editTextJobTitle);
        jobDescribtionEditText = view.findViewById(R.id.jobDescriptionEditText);
        iconJobTitle = view.findViewById(R.id.iconImageJobTitle);
        iconJobDescribtion = view.findViewById(R.id.iconImageJobDescribtion);
        btnNext = view.findViewById(R.id.btnNext);
        backArrow = view.findViewById(R.id.backArrowImageView);
    }

}
