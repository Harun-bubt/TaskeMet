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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.NothingSelectedSpinnerAdapter;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.constant.Constants;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.IndicatorStayLayout;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Screen2_ShoppingDetails extends Fragment implements AdapterView.OnItemSelectedListener {
    Button btnUsed,btnNew,btnNext;
    IndicatorSeekBar seekBar;
    IndicatorStayLayout indicatorStayLayout;
    TextView seekBarHeading;
    EditText postTitleEditText,postPriceEditText,postDescriptionEditText;
    ImageView icon_info_title,icon_info_price,icon_info_description;
    Spinner categorySpinner;
    ImageView backArrow;
    Context context;
    ArrayAdapter<CharSequence> adapter;
    LinearLayout conditionLayout;
    TextView currency;

    View view;


    //.................Store all info Shared preferences.................
    SharedPreferences sharedPreferences;
    SharedPreferences profileSharedPreferences;
    SharedPreferences.Editor shop_detail_editor;
    SharedPreferences mainCategorySharedPreferences ;

    String title,description,price;
    String product_Condition="New";
    String product_Condition_meter="10";
    Boolean isNew = true;

    boolean isCategorySelected = false;
    String item ;
    String subCategoryName;
    String isEditAd;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_screen2_post_details, container, false);


        onBackPressed(view);
        backArrow = view.findViewById(R.id.backArrowImageView);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        //..................Retrive the main Category............................
        mainCategorySharedPreferences = getContext().getSharedPreferences(Constants.SELECTING_CATEGORY_SERVICE_SHOP,Context.MODE_PRIVATE);
        item = mainCategorySharedPreferences.getString(Constants.MAIN_CATEGORY,null);
        subCategoryName = mainCategorySharedPreferences.getString(Constants.SHOP_SUB_CATEGORY,null);

        //.......................................................................
        initializeAllView();


        //...................Store all info SharedPreferences...........................
        sharedPreferences = getContext().getSharedPreferences(Constants.SHOP_NEW_POST, Context.MODE_PRIVATE);
        profileSharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        shop_detail_editor = sharedPreferences.edit();

        //...................Retrive profile data and set currency...........................
        profileSharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        String getCurrecny = profileSharedPreferences.getString(Constants.CURRENCY,"");
        currency.setText(getCurrecny);



        setSubCategorySpinner();

        controlEditTextIconVisibility(postTitleEditText,icon_info_title);
        controlPriceEditTextandIconVisibility(postPriceEditText,icon_info_price);
        controlEditTextIconVisibility(postDescriptionEditText,icon_info_description);

        //.........Currency text set..............................
            currency.setText(profileSharedPreferences.getString(Constants.CURRENCY,null));

        //.........Currency text set..............................

        controlButtonUsed();
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              controlButtonNew();

            }
        });

        btnUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlButtonUsed();
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = postTitleEditText.getText().toString();
                if(title.equals("")){
                    postTitleEditText.setError("This field is required");
                    postTitleEditText.requestFocus();
                    return ;
                }

                if(!isCategorySelected)
                {
                    TextView errorText = (TextView)categorySpinner.getSelectedView();
                    errorText.setError("");
                    errorText.requestFocus();
                    Toast.makeText(getContext(), "Select Category", Toast.LENGTH_SHORT).show();
                    return;

                }


                price = postPriceEditText.getText().toString().trim();
                if(price.equals("")){
                    postPriceEditText.setError("This field is required");
                    postPriceEditText.requestFocus();
                    return ;
                }else
                {
                    int priceInt = Integer.parseInt(price);
                    if(priceInt < 1500)
                    {
                        postPriceEditText.setError("price greater than 1500");
                        postPriceEditText.requestFocus();
                        return ;
                    }
                    if(priceInt > 250000)
                    {
                        postPriceEditText.setError("price less than 250000");
                        postPriceEditText.requestFocus();
                        return ;
                    }
                }
                description = postDescriptionEditText.getText().toString();
                if(description.equals("")){
                    postDescriptionEditText.setError("This field is required");
                    postDescriptionEditText.requestFocus();
                    return ;
                }

                //................Store all info........................
                storeAllInfo();
                goToNext();
            }
        });

        setAlertDialogForIcon(icon_info_title,"Write a suitable title for your Ad");
        setAlertDialogForIcon(icon_info_price,"Place a suitable price for your product");
        setAlertDialogForIcon(icon_info_description,"Write a detailed description about your product such as condition & color etc.");

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position > 0){
            String text = parent.getItemAtPosition(position).toString();
            shop_detail_editor.putString(Constants.SHOP_SUB_CATEGORY,text);
            shop_detail_editor.apply();
            isCategorySelected = true;
        }



    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setSubCategoryName(){


        if (item.equals(getResources().getString(R.string.mobile))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.mobiles, android.R.layout.simple_spinner_item);
        } else if (item.equals(getResources().getString(R.string.computer))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.computers, android.R.layout.simple_spinner_item);
        } else if (item.equals(getResources().getString(R.string.vehicle))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.vehicles, android.R.layout.simple_spinner_item);
        } else if (item.equals(getResources().getString(R.string.furniture))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.furnitures, android.R.layout.simple_spinner_item);
        } else if (item.equals(getResources().getString(R.string.appliances))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.appliances, android.R.layout.simple_spinner_item);
        } else if (item.equals(getResources().getString(R.string.bike))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.bikes, android.R.layout.simple_spinner_item);
        } else if (item.equals(getResources().getString(R.string.sports))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.sports, android.R.layout.simple_spinner_item);
        } else if (item.equals(getResources().getString(R.string.pets_animals))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.animals, android.R.layout.simple_spinner_item);
        } else if (item.equals(getResources().getString(R.string.fashion))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.fashions, android.R.layout.simple_spinner_item);
        } else if (item.equals(getResources().getString(R.string.kids))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.kids, android.R.layout.simple_spinner_item);
        } else if (item.equals(getResources().getString(R.string.books))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.books, android.R.layout.simple_spinner_item);
        }


    }
    public void storeAllInfo(){

        shop_detail_editor.putString(Constants.SHOP_TITLE,title);
        shop_detail_editor.putString(Constants.SHOP_CONDITION,product_Condition);
        shop_detail_editor.putString(Constants.SHOP_CONDITION_METER,product_Condition_meter);
        shop_detail_editor.putString(Constants.SHOP_PRICE,price);
        shop_detail_editor.putString(Constants.SHOP_DESCRIPTION,description);
        shop_detail_editor.apply();
    }
    public void goToNext(){
        Screen3_PostPhoto screen3_postPhoto = new Screen3_PostPhoto();
        androidx.fragment.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.framelayout_post_task, screen3_postPhoto);
        transaction.addToBackStack("tag");
        transaction.commit();
    }

    public void initializeAllView(){
        context = getContext();

        seekBar = view.findViewById(R.id.seekBar);
        indicatorStayLayout = view.findViewById(R.id.indicatorStayLayout);
        seekBarHeading = view.findViewById(R.id.seekBarHeading);
        postTitleEditText = view.findViewById(R.id.postTitle);
        icon_info_title = view.findViewById(R.id.post_title_info_icon);
        btnNext = view.findViewById(R.id.btnNext);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        icon_info_price = view.findViewById(R.id.iconImagePostPrice);
        icon_info_description = view.findViewById(R.id.iconImagePostDescription);

        postPriceEditText = view.findViewById(R.id.postPrice);
        postDescriptionEditText = view.findViewById(R.id.postDescription);
        conditionLayout = view.findViewById(R.id.conditionLayout);
        currency = view.findViewById(R.id.currencyText);


        if(!item.equals(null) && item.equals(getResources().getString(R.string.pets_animals))){
            conditionLayout.setVisibility(View.GONE);
            product_Condition = "other";
            product_Condition_meter = "11";
        }else{
            conditionLayout.setVisibility(View.VISIBLE);
        }

        seekBarHeading.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);
        indicatorStayLayout.setVisibility(View.GONE);

        btnNew = view.findViewById(R.id.btnNew);
        btnNew.setBackgroundResource(R.drawable.button_selected);

        btnUsed = view.findViewById(R.id.btnUsed);
        btnUsed.setBackgroundResource(R.drawable.button_unselected);
        btnUsed.setTextColor(getResources().getColor(R.color.black));
        
    }
    public void controlEditTextIconVisibility(EditText editText,ImageView imageView){
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
    public void controlPriceEditTextandIconVisibility(EditText editText,ImageView imageView){
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
                    long total = Long.parseLong(editText.getText().toString());
                    if(total > 500000)
                    {
                        editText.setError("Maximum price 500000");
                        editText.requestFocus();
                        return;
                    }
                    imageView.setVisibility(View.GONE);
                }
            }
        });

    }
    public void setSubCategorySpinner(){
        setSubCategoryName();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_default_text,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        getContext()));
        categorySpinner.setOnItemSelectedListener(this);
        
    }
    public void controlButtonNew(){
        isNew = true;
        product_Condition = "New";
        product_Condition_meter = "10";

        btnNew.setBackgroundResource(R.drawable.button_selected);
        btnUsed.setBackgroundResource(R.drawable.button_unselected);
        btnNew.setTextColor(getResources().getColor(R.color.Theme1));
        btnUsed.setTextColor(getResources().getColor(R.color.text_light_gray));

        //SeekBar
        seekBarHeading.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);
        indicatorStayLayout.setVisibility(View.GONE);
    }
    public void controlButtonUsed(){
        btnUsed.setBackgroundResource(R.drawable.button_selected);
        btnNew.setBackgroundResource(R.drawable.button_unselected);
        btnUsed.setTextColor(getResources().getColor(R.color.Theme1));
        btnNew.setTextColor(getResources().getColor(R.color.text_light_gray));

        //SeekBar
        seekBarHeading.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
        indicatorStayLayout.setVisibility(View.VISIBLE);

        isNew = false;
        product_Condition = "Used";

        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

                product_Condition_meter = Integer.toString(seekParams.progress);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

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
