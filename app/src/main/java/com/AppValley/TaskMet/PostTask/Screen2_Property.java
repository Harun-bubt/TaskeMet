package com.AppValley.TaskMet.PostTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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


public class Screen2_Property extends Fragment implements AdapterView.OnItemSelectedListener {

    Context context;
    ArrayAdapter<CharSequence> adapter;
    Spinner propertyCategory,areaCategory;
    Button rentButton,sellButton,yesButton,noButton;
    LinearLayout furnishedFloorLayout,furnishedLayout;
    ImageView backArrow;


    EditText propertyPriceEditText,propertyTitleEditText,propertyDescribtionEditText,areaEditText,areaUnitToEditText,
            floorEditText,totalFloorsEditText;
    ImageView iconPropertyTitle, iconPropertyDescribtion;
    TextView priceTextView,currencyTextView;

    Button btnNext;



    public  static SharedPreferences sharedPreferences;
    SharedPreferences.Editor service_Property_editor;


    boolean isPropertyTypeSelected = false;
    boolean isAreaTypeSelected = false;
    //............Varialbe for store info...............
    String price;
    String propertyPostType = "Rent";
    String propertyType;
    String areaType;
    String Area;
    String areaUnit;
    String furnished = "yes";
    String floor;
    String totalFloors;
    String title;
    String Description;

    boolean isFloorShow = false,isFurnishedShow=false;
    View view;

    SharedPreferences profileSharedPreferences;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_screen2_property, container, false);
        context = getContext();

        onBackPressed(view);



        //........................Shared preferences..........................
        sharedPreferences = getContext().getSharedPreferences(Constants.SERVICE_PROPERTY_NEW_POST, Context.MODE_PRIVATE);
        service_Property_editor = sharedPreferences.edit();
        //........................Shared preferences..........................

        initializeAllViews();

        //...............Get profile data && Set Currency in cost...............................
        profileSharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        String Currency = profileSharedPreferences.getString(Constants.CURRENCY,"");
        currencyTextView.setText(Currency);





        //...................AnimationView for Visible and gone.................

        Animation scaleDown = AnimationUtils.loadAnimation(getContext(),R.anim.scale_down);
        Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        //...................AnimationView for Visible and gone.................




        //.................iconAppearDisapear.....................
        iconAppearDisAppear(propertyTitleEditText,iconPropertyTitle);
        iconAppearDisAppear(propertyDescribtionEditText,iconPropertyDescribtion);
        //.................iconAppearDisapear......................
        setAlertDialogForIcon(iconPropertyTitle,"write a suitable title for property Ad");
        setAlertDialogForIcon(iconPropertyDescribtion,"Write about your property in details");


        //............setSpinner Text............................................

        setPropertyCategory();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propertyCategory.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_default_text,
                        getContext()));
        propertyCategory.setOnItemSelectedListener(this);

        setAreaCategory(true);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaCategory.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_default_text,
                        getContext()));
        areaCategory.setOnItemSelectedListener(this);


        //............setSpinner Text...........................................


        //....................ButtonOnclickListener.............................
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                propertyPostType = "Sell";
                priceTextView.setText("Price :");

                sellButton.setBackgroundResource(R.drawable.button_selected);
                sellButton.setTextColor(getResources().getColor(R.color.Theme1));
                rentButton.setBackgroundResource(R.drawable.button_unselected);
                rentButton.setTextColor(getResources().getColor(R.color.text_light_gray));
            }
        });
        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                propertyPostType = "Rent";
                priceTextView.setText("Rent :");
                rentButton.setBackgroundResource(R.drawable.button_selected);
                rentButton.setTextColor(getResources().getColor(R.color.Theme1));
                sellButton.setBackgroundResource(R.drawable.button_unselected);
                sellButton.setTextColor(getResources().getColor(R.color.text_light_gray));
            }
        });
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                furnished = "yes";
                yesButton.setBackgroundResource(R.drawable.button_selected);
                yesButton.setTextColor(getResources().getColor(R.color.Theme1));
                noButton.setBackgroundResource(R.drawable.button_unselected);
                noButton.setTextColor(getResources().getColor(R.color.text_light_gray));
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                furnished = "no";
                noButton.setBackgroundResource(R.drawable.button_selected);
                noButton.setTextColor(getResources().getColor(R.color.Theme1));
                yesButton.setBackgroundResource(R.drawable.button_unselected);
                yesButton.setTextColor(getResources().getColor(R.color.text_light_gray));
            }
        });

        //....................ButtonOnclickListener.............................

        //...................Go to Next Fragment.................................

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(propertyPriceEditText.getText().toString().equals(""))
                {
                    propertyPriceEditText.setError("This field is required");
                    propertyPriceEditText.requestFocus();
                    return ;
                }else
                {
                    price = propertyPriceEditText.getText().toString();
                }
                if(!isPropertyTypeSelected)
                {
                    TextView errorText = (TextView)propertyCategory.getSelectedView();
                    errorText.setError("This field is required");
                    errorText.requestFocus();
                    return;
                }
                if(!isAreaTypeSelected)
                {
                    TextView errorText = (TextView)areaCategory.getSelectedView();
                    errorText.setError("This field is required");
                    errorText.requestFocus();
                    return;
                }


                if(areaEditText.getText().toString().equals(""))
                {
                    areaEditText.setError("This field is required");
                    areaEditText.requestFocus();
                    return ;
                }else
                {
                    Area = areaEditText.getText().toString();


                }
                if(areaUnitToEditText.getText().toString().equals(""))
                {
                    areaUnitToEditText.setError("This field is required");
                    areaUnitToEditText.requestFocus();
                    return ;
                }else
                {
                    areaUnit = areaUnitToEditText.getText().toString();
                }
                if(areaUnitToEditText.getText().toString().equals(""))
                {
                    areaUnitToEditText.setError("This field is required");
                    areaUnitToEditText.requestFocus();
                    return ;
                }else
                {
                    areaUnit = areaUnitToEditText.getText().toString();
                }
                if(floorEditText.getText().toString().equals("") && isFloorShow && isFurnishedShow )
                {
                    floorEditText.setError("This field is required");
                    floorEditText.requestFocus();
                    return ;
                }else
                {
                    floor = floorEditText.getText().toString();
                }
                if(totalFloorsEditText.getText().toString().equals("") && isFloorShow && isFurnishedShow)
                {
                    totalFloorsEditText.setError("This field is required");
                    totalFloorsEditText.requestFocus();
                    return ;
                }else
                {
                    totalFloors = totalFloorsEditText.getText().toString();
                }

                if(propertyTitleEditText.getText().toString().equals(""))
                {
                    propertyTitleEditText.setError("This field is required");
                    propertyTitleEditText.requestFocus();
                    return ;
                }else
                {
                    title = propertyTitleEditText.getText().toString();
                }
                if(propertyDescribtionEditText.getText().toString().equals(""))
                {
                    propertyDescribtionEditText.setError("This field is required");
                    propertyDescribtionEditText.requestFocus();
                    return ;
                }else
                {
                    Description = propertyDescribtionEditText.getText().toString();
                }

                storeAllInfo();




               goToNextFragment();
            }
        });
        //...................Go to Next Fragment.................................

        return view;

    }



    private void setPropertyCategory() {
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.propertycategory, android.R.layout.simple_spinner_item);
    }
    private void setAreaCategory(boolean s) {
        if(s)
        {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.areacategory, android.R.layout.simple_spinner_item);
        }
        else
        {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.areacategoryWithOutAgriculture, android.R.layout.simple_spinner_item);
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position > 0) {

            if(parent.getItemAtPosition(1).equals(getResources().getString(R.string.plots_land))){

                String text = parent.getItemAtPosition(position).toString();
                isAreaTypeSelected = false;
                if(position == 1)
                {
                    isFurnishedShow = false;
                    isFloorShow = false;
                    furnishedFloorLayout.setVisibility(View.GONE);
                    setAreaCategory(true);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaCategory.setAdapter(
                            new NothingSelectedSpinnerAdapter(
                                    adapter,
                                    R.layout.spinner_default_text,
                                    getContext()));
                }else
                {
                    isFurnishedShow = true;
                    isFloorShow = true;
                    furnishedFloorLayout.setVisibility(View.VISIBLE);
                    setAreaCategory(false);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaCategory.setAdapter(
                            new NothingSelectedSpinnerAdapter(
                                    adapter,
                                    R.layout.spinner_default_text,
                                    getContext()));
                    areaCategory.setOnItemSelectedListener(this);
                }
                if(position == 4)
                {
                    isFloorShow = false;
                    furnishedLayout.setVisibility(View.GONE);
                }else
                {
                    isFloorShow = true;
                    furnishedLayout.setVisibility(View.VISIBLE);
                }
                isPropertyTypeSelected = true;
                propertyType = text;
            }
            else {
                String text = parent.getItemAtPosition(position).toString();
                areaType = text;
                isAreaTypeSelected = true;

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


    public void initializeAllViews()
    {

        propertyCategory = view.findViewById(R.id.propertyTypeSpinner);
        areaCategory = view.findViewById(R.id.areaTypeSpinner);

        rentButton = view.findViewById(R.id.btnRent);
        sellButton = view.findViewById(R.id.btnSell);
        yesButton = view.findViewById(R.id.btnYes);
        noButton = view.findViewById(R.id.btnNo);

        priceTextView = view.findViewById(R.id.priceTextViewProperty);
        currencyTextView = view.findViewById(R.id.currencyTextView);
        propertyPriceEditText = view.findViewById(R.id.propertyPriceEditText);
        propertyTitleEditText = view.findViewById(R.id.editTextPropertyTitle);
        propertyDescribtionEditText = view.findViewById(R.id.descriptionPropertyEditText);
        areaEditText = view.findViewById(R.id.areaEditText);
        areaUnitToEditText = view.findViewById(R.id.areaUnitEditText);
        floorEditText = view.findViewById(R.id.floorEditText);
        totalFloorsEditText = view.findViewById(R.id.totalFloorsEditText);
        iconPropertyTitle = view.findViewById(R.id.iconImagePropertyTitle);
        iconPropertyDescribtion = view.findViewById(R.id.iconImagePropertyDescription);
        btnNext = view.findViewById(R.id.btnNext);
        backArrow = view.findViewById(R.id.backArrowImageView);
        furnishedFloorLayout = view.findViewById(R.id.all_furnished_layout);
        furnishedLayout = view.findViewById(R.id.furnishedLayout);

    }

    
    public void storeAllInfo(){
        service_Property_editor.putString(Constants.PROERTY_PRICE,price);
        service_Property_editor.putString(Constants.PROPERTY_POST_TYPE,propertyPostType);
        service_Property_editor.putString(Constants.PROPERTY_TYPE,propertyType);
        service_Property_editor.putString(Constants.AREA_TYPE,areaType);
        service_Property_editor.putString(Constants.AREA,Area);
        service_Property_editor.putString(Constants.AREA_UNIT,areaUnit);
        service_Property_editor.putString(Constants.FURNISHED,furnished);
        service_Property_editor.putString(Constants.FLOOR,floor);
        service_Property_editor.putString(Constants.TOTAL_FLOOR,totalFloors);
        service_Property_editor.putString(Constants.PROPERTY_TITLE,title);
        service_Property_editor.putString(Constants.PROPERTY_DESCRIPTION,Description);
        service_Property_editor.apply();

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
    public void goToNextFragment()
    {
        Screen3_PostPhoto screen3_postPhoto = new Screen3_PostPhoto();
        androidx.fragment.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.framelayout_post_task, screen3_postPhoto);
        transaction.addToBackStack("tag");
        transaction.commit();
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

}
