package com.AppValley.TaskMet.PostTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.NothingSelectedSpinnerAdapter;
import com.AppValley.TaskMet.constant.Constants;

public class Screen2_Matrimonial extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button btnBride,btnGroom,btnWidower,btnMarried;
    Button btnUnmarried,btnDivorced,btnSeparated;
    TextView currency;
    Button btnNext;
    ImageView backArrow;

    EditText postTitleEditText,myHeghtEditText,myAgeEditText,myCastClanEditText,mySalaryEditText,writeMyselfEditText;
    EditText partnerHeghtEditText,partnerAgeEditText,partnerCastClanEditText,partnerWriteAboutPartnerEditText;

    ImageView iconPostTitle;
    ArrayAdapter<CharSequence> adapter;

    Spinner myReligionSpinner,myEducationSpinner,myJobSpinner,partnerReligionSpinner,partnerEducationSpinner,partnerJobSpinner;

    //............Variable for collect data.................
        String looking_for = "Bride";
        String maritial_status = "Unmarried";
        String postTile;
        String my_height;
        String my_age;
        String my_religion="";
        String my_cast_clan;
        String my_education="";
        String my_job="";
        String my_salary;
        String write_myself;
        String partner_height;
        String partner_age;
        String partner_religion="";
        String partner_cast_clan;
        String partner_education="";
        String partner_job="";
        String partner_write_about_partner;
    //......................................................

    int btn_count = 1;
    View view;

    //..................Sharedpreferences declare...................
    SharedPreferences sharedPreferences,profileSharedPreferences;
    SharedPreferences.Editor marriage_detail_editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_screen2__matrimonial, container, false);
        initializeView();
        onBackPressed(view);

        profileSharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        String Currency = profileSharedPreferences.getString(Constants.CURRENCY,"");
        currency.setText(Currency);
        backArrow = view.findViewById(R.id.backArrowImageView);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        setReligionCategory();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myReligionSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_default_text,
                        getContext()));
        myReligionSpinner.setOnItemSelectedListener(this);
        partnerReligionSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_default_text,
                        getContext()));
        partnerReligionSpinner.setOnItemSelectedListener(this);

        setEducationCategory();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myEducationSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_default_text,
                        getContext()));
        myEducationSpinner.setOnItemSelectedListener(this);

        partnerEducationSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_default_text,
                        getContext()));
        partnerEducationSpinner.setOnItemSelectedListener(this);

        setJobCategory();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myJobSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_default_text,
                        getContext()));
        myJobSpinner.setOnItemSelectedListener(this);

        partnerJobSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_default_text,
                        getContext()));
        partnerJobSpinner.setOnItemSelectedListener(this);



        //...................Store all info SharedPreferences...........................
        sharedPreferences = getContext().getSharedPreferences(Constants.SERVICE_MARRIAGE_NEW_POST, Context.MODE_PRIVATE);
        marriage_detail_editor = sharedPreferences.edit();

        //...................Store all info SharedPreferences...........................

       postTitleIconAppearDisapear();

        return view;
    }



    public void initializeView(){

        //..........all EditText.........................
        postTitleEditText = view.findViewById(R.id.postTitle);
        myHeghtEditText = view.findViewById(R.id.myHeight);
        myAgeEditText = view.findViewById(R.id.myAge);
        myCastClanEditText = view.findViewById(R.id.myCast);
        mySalaryEditText = view.findViewById(R.id.mySalary);
        writeMyselfEditText = view.findViewById(R.id.mySelf);
        partnerHeghtEditText = view.findViewById(R.id.partnerHeight);
        partnerAgeEditText = view.findViewById(R.id.partnerAge);
        partnerCastClanEditText = view.findViewById(R.id.partnerCast);
        partnerWriteAboutPartnerEditText = view.findViewById(R.id.partnerSelf);
        myReligionSpinner = view.findViewById(R.id.myReligionSpinner);
        myEducationSpinner = view.findViewById(R.id.educationTypeSpinner);
        myJobSpinner = view.findViewById(R.id.jobTypeSpinner);

        partnerReligionSpinner = view.findViewById(R.id.partnerReligionSpinner);
        partnerEducationSpinner = view.findViewById(R.id.partnerEducationTypeSpinner);
        partnerJobSpinner = view.findViewById(R.id.partnerJobTypeSpinner);
        currency = view.findViewById(R.id.currency);

        //.............iconPost Title..............
        iconPostTitle = view.findViewById(R.id.iconPostTitle);

        //Looking for buttons
        btnNext = view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        btnBride = view.findViewById(R.id.btnBride);
        btnBride.setOnClickListener(this);
        btnGroom = view.findViewById(R.id.btnGroom);
        btnGroom.setOnClickListener(this);

        //martial status buttons
        btnWidower = view.findViewById(R.id.btnWidower);
        btnWidower.setOnClickListener(this);
        btnMarried = view.findViewById(R.id.btnMarried);
        btnMarried.setOnClickListener(this);
        btnUnmarried = view.findViewById(R.id.btnUnmarried);
        btnUnmarried.setOnClickListener(this);
        btnDivorced = view.findViewById(R.id.btnDivorced);
        btnDivorced.setOnClickListener(this);
        btnSeparated = view.findViewById(R.id.btnSeparated);
        btnSeparated.setOnClickListener(this);

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnBride:
                looking_for = "Bride";

                if(btn_count == 0){

                    btn_count = 1 ;
                    btnBride.setBackgroundResource(R.drawable.button_selected);
                    btnBride.setTextColor(getResources().getColor(R.color.Theme1));
                    btnGroom.setBackgroundResource(R.drawable.button_unselected);
                    btnGroom.setTextColor(getResources().getColor(R.color.text_light_gray));
                    btnWidower.setText("Widower");
                    btnMarried.setVisibility(View.VISIBLE);
                    Animation mLoadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
                    btnMarried.startAnimation(mLoadAnimation);

                }
                break;

            case R.id.btnGroom:
                looking_for = "Groom";
                if(btn_count == 1) {

                    btn_count = 0 ;
                    btnGroom.setBackgroundResource(R.drawable.button_selected);
                    btnGroom.setTextColor(getResources().getColor(R.color.Theme1));
                    btnBride.setBackgroundResource(R.drawable.button_unselected);
                    btnBride.setTextColor(getResources().getColor(R.color.text_light_gray));
                    btnWidower.setText("Widow");
                    btnMarried.setVisibility(View.GONE);
                    Animation mLoadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_out);
                    btnMarried.startAnimation(mLoadAnimation);
                }
                break;

            case R.id.btnUnmarried:
                maritial_status = getString(R.string.unmarried);
                btnUnmarried.setBackgroundResource(R.drawable.button_selected);
                btnUnmarried.setTextColor(getResources().getColor(R.color.Theme1));
                btnDivorced.setBackgroundResource(R.drawable.button_unselected);
                btnDivorced.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnWidower.setBackgroundResource(R.drawable.button_unselected);
                btnWidower.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnSeparated.setBackgroundResource(R.drawable.button_unselected);
                btnSeparated.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnMarried.setBackgroundResource(R.drawable.button_unselected);
                btnMarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                break;
            case R.id.btnDivorced:
                maritial_status = getString(R.string.divorced);
                btnDivorced.setBackgroundResource(R.drawable.button_selected);
                btnDivorced.setTextColor(getResources().getColor(R.color.Theme1));
                btnUnmarried.setBackgroundResource(R.drawable.button_unselected);
                btnUnmarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnWidower.setBackgroundResource(R.drawable.button_unselected);
                btnWidower.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnSeparated.setBackgroundResource(R.drawable.button_unselected);
                btnSeparated.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnMarried.setBackgroundResource(R.drawable.button_unselected);
                btnMarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                break;
            case R.id.btnWidower:
                if(btn_count == 0)
                {
                    maritial_status = getString(R.string.widow);
                }else if(btn_count == 1)
                {
                    maritial_status = getString(R.string.widower);
                }
                btnWidower.setBackgroundResource(R.drawable.button_selected);
                btnWidower.setTextColor(getResources().getColor(R.color.Theme1));
                btnDivorced.setBackgroundResource(R.drawable.button_unselected);
                btnDivorced.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnUnmarried.setBackgroundResource(R.drawable.button_unselected);
                btnUnmarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnSeparated.setBackgroundResource(R.drawable.button_unselected);
                btnSeparated.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnMarried.setBackgroundResource(R.drawable.button_unselected);
                btnMarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                break;
            case R.id.btnSeparated:
                maritial_status = getString(R.string.separated);
                btnSeparated.setBackgroundResource(R.drawable.button_selected);
                btnSeparated.setTextColor(getResources().getColor(R.color.Theme1));
                btnDivorced.setBackgroundResource(R.drawable.button_unselected);
                btnDivorced.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnWidower.setBackgroundResource(R.drawable.button_unselected);
                btnWidower.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnUnmarried.setBackgroundResource(R.drawable.button_unselected);
                btnUnmarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnMarried.setBackgroundResource(R.drawable.button_unselected);
                btnMarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                break;
            case R.id.btnMarried:
                maritial_status = getString(R.string.married);
                btnMarried.setBackgroundResource(R.drawable.button_selected);
                btnMarried.setTextColor(getResources().getColor(R.color.Theme1));
                btnDivorced.setBackgroundResource(R.drawable.button_unselected);
                btnDivorced.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnWidower.setBackgroundResource(R.drawable.button_unselected);
                btnWidower.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnSeparated.setBackgroundResource(R.drawable.button_unselected);
                btnSeparated.setTextColor(getResources().getColor(R.color.text_light_gray));
                btnUnmarried.setBackgroundResource(R.drawable.button_unselected);
                btnUnmarried.setTextColor(getResources().getColor(R.color.text_light_gray));
                break;
            case R.id.btnNext:
                postTile = postTitleEditText.getText().toString();
                if(postTile.equals(""))
                {
                    postTitleEditText.setError("Write title");
                    postTitleEditText.requestFocus();
                    return;
                }
                my_height = myHeghtEditText.getText().toString();
                if(my_height.equals(""))
                {
                    myHeghtEditText.setError("Write height");
                    myHeghtEditText.requestFocus();
                    return;
                }
                my_age = myAgeEditText.getText().toString();
                if(my_age.equals(""))
                {
                    myAgeEditText.setError("Write age");
                    myAgeEditText.requestFocus();
                    return;
                }

                if(my_religion.length() < 1)
                {
                    TextView errorText = (TextView)myReligionSpinner.getSelectedView();
                    errorText.setError("This field is required");
                    errorText.requestFocus();
                    return;
                }
                my_cast_clan = myCastClanEditText.getText().toString();
                if(my_cast_clan.equals(""))
                {
                    myCastClanEditText.setError("Write cast/clan");
                    myCastClanEditText.requestFocus();
                    return;
                }
                if(my_education.length() < 1)
                {
                    TextView errorText = (TextView)myEducationSpinner.getSelectedView();
                    errorText.setError("This field is required");
                    errorText.requestFocus();
                    return;
                }

                if(my_job.length() < 1)
                {
                    TextView errorText = (TextView)myJobSpinner.getSelectedView();
                    errorText.setError("This field is required");
                    errorText.requestFocus();
                    return;
                }
                my_salary =  mySalaryEditText.getText().toString();
                if(my_salary.equals(""))
                {
                    my_salary = "empty";
                }
                write_myself = writeMyselfEditText.getText().toString();
                if(write_myself.equals(""))
                {
                    write_myself = "empty";
                }
                partner_height = partnerHeghtEditText.getText().toString();
                if(partner_height.equals(""))
                {
                    partner_height = "empty";
                }
                partner_age =partnerAgeEditText.getText().toString();
                if(partner_age.equals(""))
                {
                    partner_age = "empty";
                }

                if(partner_religion.equals(""))
                {
                    partner_religion = "empty";
                }
                partner_cast_clan = partnerCastClanEditText.getText().toString();
                if(partner_cast_clan.equals(""))
                {
                    partner_cast_clan = "empty";
                }

                if(partner_education.equals(""))
                {
                    partner_education = "empty";
                }

                if(partner_job.equals(""))
                {
                    partner_job = "empty";
                }
                partner_write_about_partner = partnerWriteAboutPartnerEditText.getText().toString();
                if(partner_write_about_partner.equals(""))
                {
                    partner_write_about_partner = "empty";
                }
                storeAllInfor();
                goToNextFragment();
                break;





        }

    }

    public void storeAllInfor(){


         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_LOOKING_FOR,looking_for);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_MARTIAL_STATUS,maritial_status);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_POST_TITLE,postTile);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_MY_HEIGHT,my_height);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_MY_AGE,my_age);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_MY_RELIGION,my_religion);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_MY_CAST_CLAN,my_cast_clan);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_MY_EDUCATION,my_education);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_MY_JOB,my_job);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_MY_SALARY,my_salary);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_WRITE_MYSELF,write_myself);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_PARTNER_HEIGHT,partner_height);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_PARTNER_AGE,partner_age);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_PARTNER_RELIGION,partner_religion);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_PARTNER_CAST_CLAN,partner_cast_clan);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_PARTNER_EDUCATION,partner_education);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_PARTNER_JOB,partner_job);
         marriage_detail_editor.putString(Constants.SERVICE_MARRIAGE_PARTNER_WRITE_ABOUT_PARTNER,partner_write_about_partner);
         marriage_detail_editor.apply();
    }

    public void goToNextFragment(){
        Screen3_PostPhoto screen3_postPhoto = new Screen3_PostPhoto();
        androidx.fragment.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.framelayout_post_task, screen3_postPhoto);
        transaction.addToBackStack("tag");
        transaction.commit();
    }
    private void postTitleIconAppearDisapear() {
        postTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(postTitleEditText.getText().toString().equals("")){
                    iconPostTitle.setVisibility(View.VISIBLE);
                }else
                {
                    iconPostTitle.setVisibility(View.GONE);
                }
            }
        });
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
    private void setReligionCategory() {
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.religionArrary, android.R.layout.simple_spinner_item);
    } private void setEducationCategory() {
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.educationArray, android.R.layout.simple_spinner_item);
    }
    private void setJobCategory() {
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.jobArray, android.R.layout.simple_spinner_item);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position > 0)
        {
            if(parent.equals(myReligionSpinner))
            {
                my_religion =  parent.getItemAtPosition(position).toString();
            }
            else if(parent.equals(myEducationSpinner))
            {
                my_education =  parent.getItemAtPosition(position).toString();
            }
            else if(parent.equals(myJobSpinner))
            {
                my_job =  parent.getItemAtPosition(position).toString();
            }
            else if(parent.equals(partnerReligionSpinner))
            {
                partner_religion =  parent.getItemAtPosition(position).toString();
            }
            else if(parent.equals(partnerEducationSpinner))
            {
                partner_education =  parent.getItemAtPosition(position).toString();
            }
            else if(parent.equals(partnerJobSpinner))
            {
                partner_job =  parent.getItemAtPosition(position).toString();
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}