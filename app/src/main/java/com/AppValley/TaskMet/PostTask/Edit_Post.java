package com.AppValley.TaskMet.PostTask;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
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
import android.widget.Toast;

import com.AppValley.TaskMet.Home.HomeFragments.MyPosts;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.NothingSelectedSpinnerAdapter;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostDataModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostOwnerModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.Status_Response;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.ViewMyPostModel;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.Utils.ImageCompression;
import com.AppValley.TaskMet.constant.Constants;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.IndicatorStayLayout;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;
import gun0912.tedimagepicker.builder.type.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Edit_Post extends Fragment implements AdapterView.OnItemSelectedListener {

    View view;
    TaskMetServer taskMetServer;

    String realPath;
    ArrayList<String> imageList = new ArrayList<>();
    ArrayList<String> trashImages = new ArrayList<>();

    String id, key;

    Button btnUpdateImage, btnUpdateAd;

    //................View for Shopping......................
    Button btnUsed, btnNew;
    IndicatorSeekBar seekBar;
    IndicatorStayLayout indicatorStayLayout;
    TextView seekBarHeading;
    EditText postTitleEditText, postPriceEditText, postDescriptionEditText;
    ImageView icon_info_title, icon_info_price, icon_info_description;
    Spinner categorySpinner;
    ImageView backArrow;
    Context context;
    ArrayAdapter<CharSequence> adapter;
    LinearLayout conditionLayout;
    TextView currency;

    //.......................View for Job..............................
    ArrayAdapter<CharSequence> jobAdapter;
    Spinner jobCategorySpinner, positionTypeSpinner, salaryPeriodSpinner;
    Button jobOffer, cvResume;
    LinearLayout salaryFromSalaryToLayout, companyNameLayout;
    TextView describtionAboutJob;
    ImageView JobbackArrow;
    EditText jobTitleEditText, jobDescribtionEditText, salaryFromEditText, salaryToEditText;
    EditText companyNameEditText;
    ImageView iconJobTitle, iconJobDescribtion;


    //........................View for Common...........................
    EditText editTextServiceTitle, editTextServieCost, editTextdescribe_service;
    ImageView iconImageJobTitle, iconImageDescribeService;
    ImageView commonBackArrow;

    //.......................View for Marriage.............................
    Button btnBride, btnGroom, btnWidower, btnMarried;
    Button btnUnmarried, btnDivorced, btnSeparated;
    TextView Marriagecurrency;
    Button marriagebtnUpdateAd;
    ImageView marriageBackArrow;

    EditText marriagePostTitleEditText, myHeghtEditText, myAgeEditText, myCastClanEditText,
              mySalaryEditText, writeMyselfEditText;
    EditText partnerHeghtEditText, partnerAgeEditText,  partnerCastClanEditText,
              partnerWriteAboutPartnerEditText;
    ImageView iconMarriagePostTitle;
    Spinner myReligionSpinner,myEducationSpinner,myJobSpinner,partnerReligionSpinner,partnerEducationSpinner,partnerJobSpinner;


    //.........................view for property............................
    ArrayAdapter<CharSequence> propertyAdapter;
    Spinner propertyCategory, areaCategory;
    Button rentButton, sellButton, yesButton, noButton;
    LinearLayout furnishedFloorLayout, furnishedLayout;
    ImageView propertyBackArrow;
    boolean isFloorShow = false,isFurnishedShow=false;
    boolean isPropertyTypeSelected = false;
    boolean isAreaTypeSelected = false;


    EditText propertyPriceEditText,propertyTitleEditText, propertyDescribtionEditText, areaEditText, areaUnitToEditText,
            floorEditText, totalFloorsEditText;
    ImageView iconPropertyTitle, iconPropertyDescribtion;
    TextView priceTextView,currencyTextView;


    LinearLayout editShoppingLayout, editJobLayout, editMarriageLayout, editCommonLayout, editPropertyLayout;


    public Edit_Post(String id, String key) {
        this.id = id;
        this.key = key;
    }


    //.................Variable for Shopping Screen..........................

    //.................Store all info Shared preferences.................
    SharedPreferences sharedPreferences;
    SharedPreferences profileSharedPreferences;
    SharedPreferences.Editor Store_all_Info_SharedPreferences;
    SharedPreferences mainCategorySharedPreferences;

    String title, description, price;
    String product_Condition = "New";
    String product_Condition_meter = "10";
    Boolean isNew = true;

    boolean isCategorySelected = false;
    String item;
    String subCategoryName;
    String isEditAd;
    String[] subCategoryArray;

    //..............Common Service................
    String commonTitle, commonCost, commonDescription;

    //.............All job info................
    String jobType = "offering Job";
    String jobCategory;
    String positionType;
    String salaryPeriod;
    String job_title;
    String salary_From;
    String salary_to;
    String job_description;
    String company_name = "empty";

    boolean isOfferingJob = true;

    boolean isJobCategorySelected = false;
    boolean isPositiontypeSelected = false;
    boolean isSalaryPeriodSelected = false;


    Animation scaleDown;
    Animation scaleUp;

    //............Variable for marriage data.................
    String looking_for = "Bride";
    String maritial_status = "Unmarried";
    String marriagePostTile;
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
    String partner_religion;
    String partner_cast_clan;
    String partner_education;
    String partner_job;
    String partner_write_about_partner;

    int btn_count = 1;

    //......................................................

    //............property Attribute.....................
    //............Varialbe for store info...............
    String property_cost;
    String propertyPostType;
    String propertyType;
    String areaType;
    String Area;
    String areaUnit;
    String furnished;
    String floor;
    String totalFloors;
    String property_title;
    String propertyDescription;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit__post, container, false);
        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        editShoppingLayout = view.findViewById(R.id.edit_shop);
        editJobLayout = view.findViewById(R.id.edit_job);
        editMarriageLayout = view.findViewById(R.id.edit_marriage);
        editCommonLayout = view.findViewById(R.id.editCommon);
        editPropertyLayout = view.findViewById(R.id.edit_property);

        initializeAllView();

        onBackPressed(view);
        backArrow = view.findViewById(R.id.backArrowImageView);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        //...................Store all info SharedPreferences...........................
        sharedPreferences = getContext().getSharedPreferences(Constants.SHOP_NEW_POST, Context.MODE_PRIVATE);
        Store_all_Info_SharedPreferences = sharedPreferences.edit();

        profileSharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        //...................Store all info SharedPreferences...........................
        scaleDown = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Call<ViewMyPostModel> call = taskMetServer.getMyPostData(id);
                call.enqueue(new Callback<ViewMyPostModel>() {
                    @Override
                    public void onResponse(Call<ViewMyPostModel> call, Response<ViewMyPostModel> response) {

                        if (response.isSuccessful()) {
                            ViewMyPostModel myPostModel = response.body();

                            key = myPostModel.getKey();
                            PostDataModel postData = myPostModel.getPostDataModel();
                            PostOwnerModel postOwner = myPostModel.getPostOwnerModel();

                            if (key.equals("shopping")) {
                                /*item = getResources().getString(R.string.vehicle);*/

                                retriveDataForShop(postData);

                                item = postData.getMainCategory();
                                subCategoryName = postData.getSubCategory();
                                setAllThingsAtPrimary();
                                int conditionMeter = Integer.parseInt(postData.getCondition_meter());
                                if (conditionMeter < 10) {
                                    controlButtonUsed();
                                    seekBar.setProgress(conditionMeter);
                                } else if (conditionMeter == 10) {
                                    controlButtonNew();
                                } else {
                                    conditionLayout.setVisibility(View.GONE);
                                }

                                setSubCategorySpinner();

                                setSubCategorySpinner();

                                controlEditTextIconVisibility(postTitleEditText, icon_info_title);
                                controlPriceEditTextandIconVisibility(postPriceEditText, icon_info_price);
                                controlEditTextIconVisibility(postDescriptionEditText, icon_info_description);

                                //.........Currency text set..............................
                                currency.setText(profileSharedPreferences.getString(Constants.CURRENCY, null));

                                //.........Currency text set..............................


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


                                btnUpdateAd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        title = postTitleEditText.getText().toString();
                                        price = postPriceEditText.getText().toString().trim();
                                        description = postDescriptionEditText.getText().toString();


                                        if (price.equals("")) {
                                            postPriceEditText.setError("Edit price");
                                            postPriceEditText.requestFocus();
                                            return;
                                        } else {
                                            int priceInt = Integer.parseInt(price);
                                            if (priceInt < 100) {
                                                postPriceEditText.setError("Minimum price is 100");
                                                postPriceEditText.requestFocus();
                                                return;
                                            }
                                        }
                                        if (description.equals("")) {
                                            postDescriptionEditText.setError("Edit description");
                                            postDescriptionEditText.requestFocus();
                                            return;
                                        }


                                        if(title.equals(postData.getTitle()) && product_Condition.equals(postData.getCondition()) && product_Condition_meter.equals(postData.getCondition_meter()) && price.equals(postData.getPrice())
                                        && description.equals(postData.getDescription()))
                                        {
                                            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Warning!")
                                                    .setContentText("Please edit this Ad first")
                                                    .show();
                                            return;
                                        }

                                        //.... Show progressBar.................
                                        SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                                        pDialog.setTitleText("Updating Ad");
                                        pDialog.setCancelable(false);
                                        pDialog.show();



                                        Call<Status_Response> call = taskMetServer.editShopPost(id, title,subCategoryName, product_Condition, product_Condition_meter, price, description);
                                        call.enqueue(new Callback<Status_Response>() {
                                            @Override
                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {


                                                if (response.isSuccessful()) {
                                                    Status_Response message = response.body();
                                                    String msg = message.getStatus();
                                                    if (msg.equals(Constants.TRUE)) {
                                                        pDialog.cancel();


                                                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                                .setTitleText("Congratulations")
                                                                .setContentText("Ad is updated successfully. It will be live soon.")
                                                                .setConfirmText(Constants.OK)
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        sDialog.dismissWithAnimation();

                                                                        MyPosts myPosts = new MyPosts();
                                                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                                                        transaction.replace(R.id.framelayout_post_task, myPosts);
                                                                        transaction.addToBackStack("tag");
                                                                        transaction.commit();
                                                                    }
                                                                })
                                                                .show();
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Status_Response> call, Throwable t) {

                                            }
                                        });

                                    }
                                });
                                btnUpdateImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText(getString(R.string.warning))
                                                .setContentText(getString(R.string.edit_image_warning))
                                                .setCancelText(getString(R.string.dialog_cancel))
                                                .setConfirmText(getString(R.string.dialog_ok))
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                       sDialog.dismissWithAnimation();


                                                        title = postTitleEditText.getText().toString();
                                                        price = postPriceEditText.getText().toString().trim();
                                                        description = postDescriptionEditText.getText().toString();


                                                        if (title.equals("") ) {
                                                            postTitleEditText.setError("Edit title");
                                                            postTitleEditText.requestFocus();
                                                            return;
                                                        }


                                                        if (price.equals("")) {
                                                            postPriceEditText.setError("Edit price");
                                                            postPriceEditText.requestFocus();
                                                            return;
                                                        } else {
                                                            int priceInt = Integer.parseInt(price);
                                                            if (priceInt < 100) {
                                                                postPriceEditText.setError("Minimum price is 100");
                                                                postPriceEditText.requestFocus();
                                                                return;
                                                            }
                                                        }
                                                        if (description.equals("") ) {
                                                            postDescriptionEditText.setError("Edit description");
                                                            postDescriptionEditText.requestFocus();
                                                            return;
                                                        }


                                                        TedImagePicker.with(getContext())
                                                                .mediaType(MediaType.IMAGE)
                                                                .cameraTileBackground(R.color.white)
                                                                .cameraTileImage(R.drawable.icon_camera_chat)
                                                                .title("Select New Post Images")
                                                                .max(12,"Maximum limit is 12")
                                                                .savedDirectoryName("/TaskMet/images")
                                                                .buttonText("Upload Ad")
                                                                .startAnimation(R.anim.enter_from_right, R.anim.exit_to_left)
                                                                .finishAnimation(R.anim.enter_from_left, R.anim.exit_to_right)
                                                                .startMultiImage(new OnMultiSelectedListener() {
                                                                    @Override
                                                                    public void onSelected(@NotNull List<? extends Uri> uriList) {

                                                                        for (int i = 0; i < uriList.size(); i++) {

                                                                            realPath = getRealPathFromURIPath(uriList.get(i), getActivity());
                                                                            Log.d("RealPath",realPath);
                                                                            imageList.add(realPath);

                                                                        }
                                                                        MultipartBody.Part[] images = new MultipartBody.Part[imageList.size()];

                                                                        for (int i = 0; i < imageList.size(); i++) {

                                                                            Bitmap fullSizeImage = BitmapFactory.decodeFile(imageList.get(i));
                                                                            Bitmap compressedImage = ImageCompression.reduceBitmapSize(fullSizeImage, 307200);
                                                                            File file = getBitmapFile(compressedImage);

                                                                            //File file = new File(filePaths.get(i));

                                                                            RequestBody imageBody = RequestBody.create(file, okhttp3.MediaType.parse("image/*"));
                                                                            images[i] = MultipartBody.Part.createFormData("images[]", file.getName(), imageBody);

                                                                            String path = file.getAbsolutePath();
                                                                            trashImages.add(path);

                                                                        }

                                                                        RequestBody request_title = RequestBody.create(title, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_condition = RequestBody.create(product_Condition, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_condition_meter = RequestBody.create(product_Condition_meter, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_price = RequestBody.create(price, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_description = RequestBody.create(description, okhttp3.MediaType.parse("text/plan"));


                                                                        Call<Status_Response> call = taskMetServer.editShopWithImagePost(id,images, request_title, request_condition, request_condition_meter, request_price, request_description);
                                                                        call.enqueue(new Callback<Status_Response>() {
                                                                            @Override
                                                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {
                                                                                if (response.isSuccessful()) {
                                                                                    Status_Response message = response.body();
                                                                                    String msg = message.getStatus();
                                                                                    if (msg.equals(Constants.TRUE)) {
                                                                                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                                                                .setTitleText(getString(R.string.contragulations_text))
                                                                                                .setContentText(getString(R.string.image_updated))
                                                                                                .setConfirmText(getString(R.string.dialog_ok))
                                                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                                                        sDialog.dismissWithAnimation();

                                                                                                        MyPosts myPosts = new MyPosts();
                                                                                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                                                                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                                                                                        transaction.replace(R.id.framelayout_post_task, myPosts);
                                                                                                        transaction.addToBackStack("tag");
                                                                                                        transaction.commit();
                                                                                                    }
                                                                                                })
                                                                                                .show();
                                                                                    }

                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<Status_Response> call, Throwable t) {

                                                                            }
                                                                        });

                                                                    }
                                                                });
                                                    }
                                                })
                                                .showCancelButton(true)
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                       sDialog.dismissWithAnimation();
                                                    }
                                                })
                                                .show();


                                    }
                                });


                                editShoppingLayout.setVisibility(View.VISIBLE);
                                editJobLayout.setVisibility(View.GONE);
                                editMarriageLayout.setVisibility(View.GONE);
                                editCommonLayout.setVisibility(View.GONE);
                                editPropertyLayout.setVisibility(View.GONE);
                                postTitleEditText.setText(postData.getTitle());
                                postPriceEditText.setText(postData.getPrice());
                                postDescriptionEditText.setText(postData.getDescription());
                            }
                            else if (key.equals("common_service")) {
                                editCommonLayout.setVisibility(View.VISIBLE);
                                editShoppingLayout.setVisibility(View.GONE);
                                editJobLayout.setVisibility(View.GONE);
                                editMarriageLayout.setVisibility(View.GONE);
                                editPropertyLayout.setVisibility(View.GONE);

                                controlEditTextIconVisibility(editTextServiceTitle, iconImageJobTitle);
                                controlEditTextIconVisibility(editTextdescribe_service, iconImageDescribeService);
                                editTextServiceTitle.setText(postData.getTitle());
                                editTextServieCost.setText(postData.getCost());
                                editTextdescribe_service.setText(postData.getDescription());


                                btnUpdateAd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        commonTitle = editTextServiceTitle.getText().toString();
                                        commonCost = editTextServieCost.getText().toString();
                                        commonDescription = editTextdescribe_service.getText().toString();

                                        if(commonTitle.equals(postData.getTitle()) && commonCost.equals(postData.getCost()) && commonDescription.equals(postData.getDescription()))
                                        {
                                            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Warning!")
                                                    .setContentText("Please edit this Ad first")
                                                    .show();
                                            return;
                                        }

                                        //.... Show progressBar.................
                                        SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                                        pDialog.setTitleText("Updating Ad");
                                        pDialog.setCancelable(false);
                                        pDialog.show();

                                        Call<Status_Response> call = taskMetServer.editCommonPost(id, commonTitle, commonCost, commonDescription);
                                        call.enqueue(new Callback<Status_Response>() {
                                            @Override
                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                                                if (response.isSuccessful()) {
                                                    Status_Response message = response.body();
                                                    String msg = message.getStatus();
                                                    if (msg.equals(Constants.TRUE)) {
                                                        pDialog.cancel();


                                                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                                .setTitleText("Congratulations")
                                                                .setContentText("Ad is updated successfully. It will be live soon.")
                                                                .setConfirmText(Constants.OK)
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        sDialog.dismissWithAnimation();

                                                                        MyPosts myPosts = new MyPosts();
                                                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                                                        transaction.replace(R.id.framelayout_post_task, myPosts);
                                                                        transaction.addToBackStack("tag");
                                                                        transaction.commit();
                                                                    }
                                                                })
                                                                .show();
                                                    }

                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<Status_Response> call, Throwable t) {

                                            }
                                        });
                                    }
                                });
                                btnUpdateImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText(getString(R.string.warning))
                                                .setContentText(getString(R.string.edit_image_warning))
                                                .setCancelText(getString(R.string.dialog_cancel))
                                                .setConfirmText(getString(R.string.dialog_ok))
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();

                                                        commonTitle = editTextServiceTitle.getText().toString();
                                                        commonCost = editTextServieCost.getText().toString();
                                                        commonDescription = editTextdescribe_service.getText().toString();

                                                        TedImagePicker.with(getContext())
                                                                .mediaType(MediaType.IMAGE)
                                                                .cameraTileBackground(R.color.white)
                                                                .cameraTileImage(R.drawable.icon_camera_chat)
                                                                .title("Select New Post Images")
                                                                .max(12,"Maximum limit is 12")
                                                                .savedDirectoryName("/TaskMet/images")
                                                                .buttonText("Upload Ad")
                                                                .startAnimation(R.anim.enter_from_right, R.anim.exit_to_left)
                                                                .finishAnimation(R.anim.enter_from_left, R.anim.exit_to_right)
                                                                .startMultiImage(new OnMultiSelectedListener() {
                                                                    @Override
                                                                    public void onSelected(@NotNull List<? extends Uri> uriList) {

                                                                        for (int i = 0; i < uriList.size(); i++) {

                                                                            realPath = getRealPathFromURIPath(uriList.get(i), getActivity());
                                                                            Log.d("RealPath",realPath);
                                                                            imageList.add(realPath);

                                                                        }
                                                                        MultipartBody.Part[] images = new MultipartBody.Part[imageList.size()];

                                                                        for (int i = 0; i < imageList.size(); i++) {

                                                                            Bitmap fullSizeImage = BitmapFactory.decodeFile(imageList.get(i));
                                                                            Bitmap compressedImage = ImageCompression.reduceBitmapSize(fullSizeImage, 307200);
                                                                            File file = getBitmapFile(compressedImage);

                                                                            //File file = new File(filePaths.get(i));

                                                                            RequestBody imageBody = RequestBody.create(file, okhttp3.MediaType.parse("image/*"));
                                                                            images[i] = MultipartBody.Part.createFormData("images[]", file.getName(), imageBody);

                                                                            String path = file.getAbsolutePath();
                                                                            trashImages.add(path);

                                                                        }

                                                                        RequestBody request_title = RequestBody.create(commonTitle, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_cost = RequestBody.create(commonCost, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_description = RequestBody.create(commonDescription, okhttp3.MediaType.parse("text/plan"));


                                                                        Call<Status_Response> call = taskMetServer.editCommonWithImagePost(id,images, request_title, request_cost, request_description);
                                                                        call.enqueue(new Callback<Status_Response>() {
                                                                            @Override
                                                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {
                                                                                if (response.isSuccessful()) {
                                                                                    Status_Response message = response.body();
                                                                                    String msg = message.getStatus();
                                                                                    if (msg.equals(Constants.TRUE)) {
                                                                                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                                                                .setTitleText(getString(R.string.contragulations_text))
                                                                                                .setContentText(getString(R.string.image_updated))
                                                                                                .setConfirmText(getString(R.string.dialog_ok))
                                                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                                                        sDialog.dismissWithAnimation();

                                                                                                        MyPosts myPosts = new MyPosts();
                                                                                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                                                                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                                                                                        transaction.replace(R.id.framelayout_post_task, myPosts);
                                                                                                        transaction.addToBackStack("tag");
                                                                                                        transaction.commit();
                                                                                                    }
                                                                                                })
                                                                                                .show();
                                                                                    }

                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<Status_Response> call, Throwable t) {

                                                                            }
                                                                        });

                                                                    }
                                                                });
                                                    }
                                                })
                                                .showCancelButton(true)
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                    }
                                                })
                                                .show();


                                    }
                                });

                            }
                            else if (key.equals("job_service")) {
                                editJobLayout.setVisibility(View.VISIBLE);
                                editCommonLayout.setVisibility(View.GONE);
                                editShoppingLayout.setVisibility(View.GONE);
                                editMarriageLayout.setVisibility(View.GONE);
                                editPropertyLayout.setVisibility(View.GONE);

                                jobCategory = postData.getJobCategory();
                                positionType = postData.getPositionType();
                                salaryPeriod = postData.getSalaryPeriod();

                                jobTitleEditText.setText(postData.getTitle());
                                jobDescribtionEditText.setText(postData.getDescription());
                                salaryFromEditText.setText(postData.getSalaryFrom());
                                salaryToEditText.setText(postData.getSalaryTo());
                                companyNameEditText.setText(postData.getCompanyName());

                                controlEditTextIconVisibility(jobTitleEditText, iconJobTitle);
                                controlEditTextIconVisibility(jobDescribtionEditText, iconJobDescribtion);
                                setJobSpinner();
                                jobType = postData.getType();
                                if (jobType.equals("offering Job")) {
                                    controlJobofferButton();
                                }
                                if (jobType.equals("cv Resume")) {
                                    controlCVButton();
                                }
                                jobOffer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        controlJobofferButton();
                                    }
                                });
                                cvResume.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        controlCVButton();
                                    }
                                });


                                //....................ButtonOnclickListener.............................

                                //...................Go to Next Fragment.................................

                                btnUpdateAd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (jobTitleEditText.getText().toString().equals("")) {
                                            jobTitleEditText.setError("Edit title");
                                            jobTitleEditText.requestFocus();
                                            return;
                                        } else {
                                            job_title = jobTitleEditText.getText().toString();
                                        }
                                        if (salaryFromEditText.getText().toString().equals("")) {
                                            salaryFromEditText.setError("Edit Salary From");
                                            salaryFromEditText.requestFocus();
                                            return;
                                        } else {
                                            if (Integer.parseInt(salaryFromEditText.getText().toString()) <= 0) {
                                                salaryFromEditText.setError("Should be greater than 0");
                                                salaryFromEditText.requestFocus();
                                                return;
                                            } else {
                                                salary_From = salaryFromEditText.getText().toString();
                                            }

                                        }
                                        if (salaryToEditText.getText().toString().equals("")) {
                                            salaryToEditText.setError("Edit salary to");
                                            salaryToEditText.requestFocus();
                                            return;
                                        } else {
                                            if (Integer.parseInt(salaryToEditText.getText().toString()) <= 0) {
                                                salaryToEditText.setError("Should be greater than 0");
                                                salaryToEditText.requestFocus();
                                                return;
                                            } else {
                                                salary_to = salaryToEditText.getText().toString();
                                            }

                                        }
                                        int salaryFrom = Integer.parseInt(salaryFromEditText.getText().toString());
                                        int salaryTo = Integer.parseInt(salaryToEditText.getText().toString());
                                        if (salaryFrom >= salaryTo) {
                                            salaryToEditText.setError("Salary From must be less than Salary to");
                                            salaryToEditText.requestFocus();
                                            return;
                                        }


                                        if (jobDescribtionEditText.getText().toString().equals("")) {
                                            jobDescribtionEditText.setError("Write description");
                                            jobDescribtionEditText.requestFocus();
                                            return;
                                        } else {
                                            job_description = jobDescribtionEditText.getText().toString();
                                        }
                                        if (!isOfferingJob) {
                                            company_name = "empty";
                                        } else if (companyNameEditText.getText().toString().equals("")) {
                                            companyNameEditText.setError("Write company name");
                                            companyNameEditText.requestFocus();
                                            return;
                                        } else {
                                            company_name = companyNameEditText.getText().toString();
                                        }

                                        getJobEditTextData();


                                        if(jobType.equals(postData.getType()) && jobCategory.equals(postData.getJobCategory()) && positionType.equals(postData.getPositionType()) &&
                                                salaryPeriod.equals(postData.getSalaryPeriod()) &&job_title.equals(postData.getTitle()) &&salary_From.equals(postData.getSalaryFrom()) &&salary_to.equals(postData.getSalaryTo()) &&
                                                job_description.equals(postData.getDescription()) && company_name.equals(postData.getCompanyName()))
                                        {
                                            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Warning!")
                                                    .setContentText("Please edit this Ad first")
                                                    .show();
                                            return;
                                        }



                                        //.... Show progressBar.................
                                        SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                                        pDialog.setTitleText("Updating Ad");
                                        pDialog.setCancelable(false);
                                        pDialog.show();


                                        Call<Status_Response> call = taskMetServer.editJobPost(id, job_title, jobType, jobCategory,
                                                positionType, salary_From, salary_to, salaryPeriod, company_name, job_description);
                                        call.enqueue(new Callback<Status_Response>() {
                                            @Override
                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {
                                                if (response.isSuccessful()) {
                                                    Status_Response message = response.body();
                                                    String msg = message.getStatus();
                                                    if (msg.equals(Constants.TRUE)) {
                                                        pDialog.cancel();


                                                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                                .setTitleText("Congratulations")
                                                                .setContentText("Ad is updated successfully. It will be live soon.")
                                                                .setConfirmText(Constants.OK)
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        sDialog.dismissWithAnimation();

                                                                        MyPosts myPosts = new MyPosts();
                                                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                                                        transaction.replace(R.id.framelayout_post_task, myPosts);
                                                                        transaction.addToBackStack("tag");
                                                                        transaction.commit();
                                                                    }
                                                                })
                                                                .show();
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Status_Response> call, Throwable t) {

                                            }
                                        });


                                    }
                                });
                                btnUpdateImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText(getString(R.string.warning))
                                                .setContentText(getString(R.string.edit_image_warning))
                                                .setCancelText(getString(R.string.dialog_cancel))
                                                .setConfirmText(getString(R.string.dialog_ok))
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();

                                                        if (jobTitleEditText.getText().toString().equals("")) {
                                                            jobTitleEditText.setError("Edit title");
                                                            jobTitleEditText.requestFocus();
                                                            return;
                                                        } else {
                                                            job_title = jobTitleEditText.getText().toString();
                                                        }
                                                        if (salaryFromEditText.getText().toString().equals("")) {
                                                            salaryFromEditText.setError("Edit Salary From");
                                                            salaryFromEditText.requestFocus();
                                                            return;
                                                        } else {
                                                            if (Integer.parseInt(salaryFromEditText.getText().toString()) <= 0) {
                                                                salaryFromEditText.setError("Should be greater than 0");
                                                                salaryFromEditText.requestFocus();
                                                                return;
                                                            } else {
                                                                salary_From = salaryFromEditText.getText().toString();
                                                            }

                                                        }
                                                        if (salaryToEditText.getText().toString().equals("")) {
                                                            salaryToEditText.setError("Edit salary to");
                                                            salaryToEditText.requestFocus();
                                                            return;
                                                        } else {
                                                            if (Integer.parseInt(salaryToEditText.getText().toString()) <= 0) {
                                                                salaryToEditText.setError("Should be greater than 0");
                                                                salaryToEditText.requestFocus();
                                                                return;
                                                            } else {
                                                                salary_to = salaryToEditText.getText().toString();
                                                            }

                                                        }
                                                        int salaryFrom = Integer.parseInt(salaryFromEditText.getText().toString());
                                                        int salaryTo = Integer.parseInt(salaryToEditText.getText().toString());
                                                        if (salaryFrom >= salaryTo) {
                                                            salaryToEditText.setError("Salary From must be less than Salary to");
                                                            salaryToEditText.requestFocus();
                                                            return;
                                                        }


                                                        if (jobDescribtionEditText.getText().toString().equals("")) {
                                                            jobDescribtionEditText.setError("Write description");
                                                            jobDescribtionEditText.requestFocus();
                                                            return;
                                                        } else {
                                                            job_description = jobDescribtionEditText.getText().toString();
                                                        }
                                                        if (!isOfferingJob) {
                                                            company_name = "empty";
                                                        } else if (companyNameEditText.getText().toString().equals("")) {
                                                            companyNameEditText.setError("Write company name");
                                                            companyNameEditText.requestFocus();
                                                            return;
                                                        } else {
                                                            company_name = companyNameEditText.getText().toString();
                                                        }

                                                        getJobEditTextData();

                                                        TedImagePicker.with(getContext())
                                                                .mediaType(MediaType.IMAGE)
                                                                .cameraTileBackground(R.color.white)
                                                                .cameraTileImage(R.drawable.icon_camera_chat)
                                                                .title("Select New Post Images")
                                                                .max(12,"Maximum limit is 12")
                                                                .savedDirectoryName("/TaskMet/images")
                                                                .buttonText("Upload Ad")
                                                                .startAnimation(R.anim.enter_from_right, R.anim.exit_to_left)
                                                                .finishAnimation(R.anim.enter_from_left, R.anim.exit_to_right)
                                                                .startMultiImage(new OnMultiSelectedListener() {
                                                                    @Override
                                                                    public void onSelected(@NotNull List<? extends Uri> uriList) {

                                                                        for (int i = 0; i < uriList.size(); i++) {

                                                                            realPath = getRealPathFromURIPath(uriList.get(i), getActivity());
                                                                            Log.d("RealPath",realPath);
                                                                            imageList.add(realPath);

                                                                        }
                                                                        MultipartBody.Part[] images = new MultipartBody.Part[imageList.size()];

                                                                        for (int i = 0; i < imageList.size(); i++) {

                                                                            Bitmap fullSizeImage = BitmapFactory.decodeFile(imageList.get(i));
                                                                            Bitmap compressedImage = ImageCompression.reduceBitmapSize(fullSizeImage, 307200);
                                                                            File file = getBitmapFile(compressedImage);

                                                                            //File file = new File(filePaths.get(i));

                                                                            RequestBody imageBody = RequestBody.create(file, okhttp3.MediaType.parse("image/*"));
                                                                            images[i] = MultipartBody.Part.createFormData("images[]", file.getName(), imageBody);

                                                                            String path = file.getAbsolutePath();
                                                                            trashImages.add(path);

                                                                        }

                                                                        RequestBody request_title = RequestBody.create(job_title, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_jobType = RequestBody.create(jobType, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_jobCategory = RequestBody.create(jobCategory, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_positionType = RequestBody.create(positionType, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_salary_From = RequestBody.create(salary_From, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_salary_to = RequestBody.create(salary_to, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_salaryPeriod = RequestBody.create(salaryPeriod, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_company_name = RequestBody.create(company_name, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_job_description = RequestBody.create(job_description, okhttp3.MediaType.parse("text/plan"));



                                                                        Call<Status_Response> call = taskMetServer.editJobWithImagePost(id,images, request_title, request_jobType,request_jobCategory,
                                                                                request_positionType,request_salary_From,request_salary_to,request_salaryPeriod,request_company_name,request_job_description);
                                                                        call.enqueue(new Callback<Status_Response>() {
                                                                            @Override
                                                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {
                                                                                if (response.isSuccessful()) {
                                                                                    Status_Response message = response.body();
                                                                                    String msg = message.getStatus();
                                                                                    if (msg.equals(Constants.TRUE)) {
                                                                                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                                                                .setTitleText(getString(R.string.contragulations_text))
                                                                                                .setContentText(getString(R.string.image_updated))
                                                                                                .setConfirmText(getString(R.string.dialog_ok))
                                                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                                                        sDialog.dismissWithAnimation();

                                                                                                        MyPosts myPosts = new MyPosts();
                                                                                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                                                                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                                                                                        transaction.replace(R.id.framelayout_post_task, myPosts);
                                                                                                        transaction.addToBackStack("tag");
                                                                                                        transaction.commit();
                                                                                                    }
                                                                                                })
                                                                                                .show();
                                                                                    }

                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<Status_Response> call, Throwable t) {

                                                                            }
                                                                        });

                                                                    }
                                                                });
                                                    }
                                                })
                                                .showCancelButton(true)
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                    }
                                                })
                                                .show();


                                    }
                                });
                                //...................Go to Next Fragment.................................

                            }
                            else if (key.equals("marriage_service")) {
                                editMarriageLayout.setVisibility(View.VISIBLE);
                                editCommonLayout.setVisibility(View.GONE);
                                editShoppingLayout.setVisibility(View.GONE);
                                editJobLayout.setVisibility(View.GONE);
                                editPropertyLayout.setVisibility(View.GONE);
                                retriveDataForMarriage(postData);
                                setPrimaryMarriageEditText();
                                setMarriageSpinner();

                                controlEditTextIconVisibility(marriagePostTitleEditText, iconMarriagePostTitle);

                                if (looking_for.equals("Bride")) {
                                    controlMarriagebtnBride();
                                } else if (looking_for.equals("Groom")) {
                                    controlMarriagebtnGroom();
                                }

                                btnBride.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        controlMarriagebtnBride();
                                    }
                                });
                                btnGroom.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        controlMarriagebtnGroom();
                                    }
                                });

                                if (maritial_status.equals("Unmarried")) {
                                    controlMarriagebtnUnmarried();
                                } else if (maritial_status.equals("Widow") || maritial_status.equals("Widower")) {
                                    controlMarriagebtnWidower();
                                } else if (maritial_status.equals("Separated")) {
                                    controlMarriagebtnSeparated();
                                } else if (maritial_status.equals("Married")) {
                                    controlMarriagebtnMarried();
                                } else if (maritial_status.equals("Divorced")) {
                                    controlMarriagebtnDivorced();
                                }

                                btnUnmarried.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        controlMarriagebtnUnmarried();
                                    }
                                });
                                btnWidower.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        controlMarriagebtnWidower();
                                    }
                                });
                                btnSeparated.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        controlMarriagebtnSeparated();
                                    }
                                });
                                btnMarried.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        controlMarriagebtnMarried();
                                    }
                                });
                                btnDivorced.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        controlMarriagebtnDivorced();
                                    }
                                });


                                btnUpdateAd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getMarriageEditTextData();

                                        if(looking_for.equals(postData.getLookingFor()) && maritial_status.equals(postData.getMaritialStatus()) && marriagePostTile.equals(postData.getTitle()) &&
                                                my_height.equals(postData.getMyHeight()) && my_age.equals(postData.getMyAge()) && my_religion.equals(postData.getMyReligion()) &&
                                                my_cast_clan.equals(postData.getMyCastClan()) && my_education.equals(postData.getMyEducation()) && my_job.equals(postData.getMyJob()) &&
                                                my_salary.equals(postData.getMySalary()) && write_myself.equals(postData.getWriteMyself()) && partner_height.equals(postData.getPartnerHeight()) &&
                                                partner_age.equals(postData.getPartnerAge()) && partner_religion.equals(postData.getPartnerReligion()) &&
                                                partner_cast_clan.equals(postData.getPartnerCastClan()) &&  partner_education.equals(postData.getPartnerEducation()) &&  partner_job.equals(postData.getPartnerJob()) &&
                                                partner_write_about_partner.equals(postData.getAboutPartner()))
                                        {
                                            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Warning!")
                                                    .setContentText("Please edit this Ad first")
                                                    .show();
                                            return;
                                        }

                                        //.... Show progressBar.................
                                        SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                                        pDialog.setTitleText("Updating Ad");
                                        pDialog.setCancelable(false);
                                        pDialog.show();

                                        Call<Status_Response> call = taskMetServer.editMarriagePost(id, marriagePostTile, looking_for, maritial_status,
                                                my_height, my_age, my_religion, my_cast_clan, my_education, my_job,
                                                my_salary, write_myself, partner_height, partner_age, partner_religion, partner_cast_clan,
                                                partner_education, partner_job, partner_write_about_partner);

                                        call.enqueue(new Callback<Status_Response>() {
                                            @Override
                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                                                Toast.makeText(getContext(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                                                if (response.isSuccessful()) {

                                                    Status_Response message = response.body();
                                                    String msg = message.getStatus();
                                                    if (msg.equals(Constants.TRUE)) {
                                                        pDialog.cancel();


                                                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                                .setTitleText("Congratulations")
                                                                .setContentText("Ad is updated successfully. It will be live soon.")
                                                                .setConfirmText(Constants.OK)
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        sDialog.dismissWithAnimation();

                                                                        MyPosts myPosts = new MyPosts();
                                                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                                                        transaction.replace(R.id.framelayout_post_task, myPosts);
                                                                        transaction.addToBackStack("tag");
                                                                        transaction.commit();
                                                                    }
                                                                })
                                                                .show();
                                                    }

                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<Status_Response> call, Throwable t) {
                                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                });
                                btnUpdateImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText(getString(R.string.warning))
                                                .setContentText(getString(R.string.edit_image_warning))
                                                .setCancelText(getString(R.string.dialog_cancel))
                                                .setConfirmText(getString(R.string.dialog_ok))
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();

                                                        getMarriageEditTextData();


                                                        TedImagePicker.with(getContext())
                                                                .mediaType(MediaType.IMAGE)
                                                                .cameraTileBackground(R.color.white)
                                                                .cameraTileImage(R.drawable.icon_camera_chat)
                                                                .title("Select New Post Images")
                                                                .max(12,"Maximum limit is 12")
                                                                .savedDirectoryName("/TaskMet/images")
                                                                .buttonText("Upload Ad")
                                                                .startAnimation(R.anim.enter_from_right, R.anim.exit_to_left)
                                                                .finishAnimation(R.anim.enter_from_left, R.anim.exit_to_right)
                                                                .startMultiImage(new OnMultiSelectedListener() {
                                                                    @Override
                                                                    public void onSelected(@NotNull List<? extends Uri> uriList) {

                                                                        for (int i = 0; i < uriList.size(); i++) {

                                                                            realPath = getRealPathFromURIPath(uriList.get(i), getActivity());
                                                                            Log.d("RealPath",realPath);
                                                                            imageList.add(realPath);

                                                                        }
                                                                        MultipartBody.Part[] images = new MultipartBody.Part[imageList.size()];

                                                                        for (int i = 0; i < imageList.size(); i++) {

                                                                            Bitmap fullSizeImage = BitmapFactory.decodeFile(imageList.get(i));
                                                                            Bitmap compressedImage = ImageCompression.reduceBitmapSize(fullSizeImage, 307200);
                                                                            File file = getBitmapFile(compressedImage);

                                                                            //File file = new File(filePaths.get(i));

                                                                            RequestBody imageBody = RequestBody.create(file, okhttp3.MediaType.parse("image/*"));
                                                                            images[i] = MultipartBody.Part.createFormData("images[]", file.getName(), imageBody);

                                                                            String path = file.getAbsolutePath();
                                                                            trashImages.add(path);

                                                                        }

                                                                        RequestBody request_title = RequestBody.create(marriagePostTile, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_looking_for = RequestBody.create(looking_for, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_maritial_status = RequestBody.create(maritial_status, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_my_height = RequestBody.create(my_height, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_my_age = RequestBody.create(my_age, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_my_religion = RequestBody.create(my_religion, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_my_cast_clan = RequestBody.create(my_cast_clan, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_my_education = RequestBody.create(my_education, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_my_job = RequestBody.create(my_job, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_my_salary = RequestBody.create(my_salary, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_write_myself = RequestBody.create(write_myself, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_partner_height = RequestBody.create(partner_height, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_partner_age = RequestBody.create(partner_age, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_partner_religion = RequestBody.create(partner_religion, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_partner_cast_clan = RequestBody.create(partner_cast_clan, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_partner_education = RequestBody.create(partner_education, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_partner_job = RequestBody.create(partner_job, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_partner_write_about_partner = RequestBody.create(partner_write_about_partner, okhttp3.MediaType.parse("text/plan"));

                                                                        Call<Status_Response> call = taskMetServer.editMarriageWithImagePost(id,images, request_title, request_looking_for, request_maritial_status,
                                                                                request_my_height,request_my_age,request_my_religion,request_my_cast_clan,request_my_education,
                                                                                request_my_job,request_my_salary,request_write_myself,request_partner_height,request_partner_age,request_partner_religion,
                                                                                request_partner_cast_clan,request_partner_education,request_partner_job,request_partner_write_about_partner);
                                                                        call.enqueue(new Callback<Status_Response>() {
                                                                            @Override
                                                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {
                                                                                if (response.isSuccessful()) {
                                                                                    Status_Response message = response.body();
                                                                                    String msg = message.getStatus();
                                                                                    if (msg.equals(Constants.TRUE)) {
                                                                                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                                                                .setTitleText(getString(R.string.contragulations_text))
                                                                                                .setContentText(getString(R.string.image_updated))
                                                                                                .setConfirmText(getString(R.string.dialog_ok))
                                                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                                                        sDialog.dismissWithAnimation();

                                                                                                        MyPosts myPosts = new MyPosts();
                                                                                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                                                                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                                                                                        transaction.replace(R.id.framelayout_post_task, myPosts);
                                                                                                        transaction.addToBackStack("tag");
                                                                                                        transaction.commit();
                                                                                                    }
                                                                                                })
                                                                                                .show();
                                                                                    }

                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<Status_Response> call, Throwable t) {

                                                                            }
                                                                        });

                                                                    }
                                                                });
                                                    }
                                                })
                                                .showCancelButton(true)
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                    }
                                                })
                                                .show();


                                    }
                                });


                            }
                            else if (key.equals(Constants.PROPERTY_KEY)) {
                                editPropertyLayout.setVisibility(View.VISIBLE);
                                editMarriageLayout.setVisibility(View.GONE);
                                editCommonLayout.setVisibility(View.GONE);
                                editShoppingLayout.setVisibility(View.GONE);
                                editJobLayout.setVisibility(View.GONE);
                                retriveDataForProperty(postData);

                                if(propertyPostType.equals("Sell"))
                                {
                                    controlSellButton();
                                }
                                if(propertyPostType.equals("Rent"))
                                {
                                    controlRentButton();
                                }
                                if(furnished.equals("yes"))
                                {
                                    controlYesButton();
                                }
                                if(furnished.equals("no"))
                                {
                                    controlNoButton();
                                }

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


                                setPrimaryPropertyEditText();

                                controlEditTextIconVisibility(propertyTitleEditText, iconPropertyTitle);
                                controlEditTextIconVisibility(propertyDescribtionEditText, iconPropertyDescribtion);

                                setPropertySpinner();




                                btnUpdateAd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                       getPropertyEditTextData();

                                        if(property_cost.equals(postData.getCost()) && propertyPostType.equals(postData.getPropertyPostType()) && propertyType.equals(postData.getPropertyType()) &&
                                                areaType.equals(postData.getAreaType()) && Area.equals(postData.getArea()) && areaUnit.equals(postData.getAreaUnit()) &&
                                                furnished.equals(postData.getFurnished()) && floor.equals(postData.getFloorLevel()) && totalFloors.equals(postData.getTotalFloors()) &&
                                                property_title.equals(postData.getTitle()) && propertyDescription.equals(postData.getDescription()))
                                        {
                                            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Warning!")
                                                    .setContentText("Please edit this Ad first")
                                                    .show();
                                            return;
                                        }

                                        SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                                        pDialog.setTitleText("Updating Ad");
                                        pDialog.setCancelable(false);
                                        pDialog.show();

                                        Call<Status_Response> call = taskMetServer.editPropertyService(id,property_title,propertyDescription,property_cost,
                                                propertyPostType,propertyType,areaType,Area,areaUnit,furnished,floor,totalFloors);

                                        call.enqueue(new Callback<Status_Response>() {
                                            @Override
                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                                                Toast.makeText(getContext(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                                                if (response.isSuccessful()) {

                                                    Status_Response message = response.body();
                                                    String msg = message.getStatus();
                                                    if (msg.equals(Constants.TRUE)) {
                                                        pDialog.cancel();


                                                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                                .setTitleText("Congratulations")
                                                                .setContentText("Ad is updated successfully. It will be live soon.")
                                                                .setConfirmText(Constants.OK)
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        sDialog.dismissWithAnimation();

                                                                        MyPosts myPosts = new MyPosts();
                                                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                                                        transaction.replace(R.id.framelayout_post_task, myPosts);
                                                                        transaction.addToBackStack("tag");
                                                                        transaction.commit();
                                                                    }
                                                                })
                                                                .show();
                                                    }

                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<Status_Response> call, Throwable t) {
                                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                });
                                btnUpdateImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText(getString(R.string.warning))
                                                .setContentText(getString(R.string.edit_image_warning))
                                                .setCancelText(getString(R.string.dialog_cancel))
                                                .setConfirmText(getString(R.string.dialog_ok))
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();

                                                        getPropertyEditTextData();



                                                        TedImagePicker.with(getContext())
                                                                .mediaType(MediaType.IMAGE)
                                                                .cameraTileBackground(R.color.white)
                                                                .cameraTileImage(R.drawable.icon_camera_chat)
                                                                .title("Select New Post Images")
                                                                .max(12,"Maximum limit is 12")
                                                                .savedDirectoryName("/TaskMet/images")
                                                                .buttonText("Upload Ad")
                                                                .startAnimation(R.anim.enter_from_right, R.anim.exit_to_left)
                                                                .finishAnimation(R.anim.enter_from_left, R.anim.exit_to_right)
                                                                .startMultiImage(new OnMultiSelectedListener() {
                                                                    @Override
                                                                    public void onSelected(@NotNull List<? extends Uri> uriList) {

                                                                        for (int i = 0; i < uriList.size(); i++) {

                                                                            realPath = getRealPathFromURIPath(uriList.get(i), getActivity());
                                                                            Log.d("RealPath",realPath);
                                                                            imageList.add(realPath);

                                                                        }
                                                                        MultipartBody.Part[] images = new MultipartBody.Part[imageList.size()];

                                                                        for (int i = 0; i < imageList.size(); i++) {

                                                                            Bitmap fullSizeImage = BitmapFactory.decodeFile(imageList.get(i));
                                                                            Bitmap compressedImage = ImageCompression.reduceBitmapSize(fullSizeImage, 307200);
                                                                            File file = getBitmapFile(compressedImage);

                                                                            //File file = new File(filePaths.get(i));

                                                                            RequestBody imageBody = RequestBody.create(file, okhttp3.MediaType.parse("image/*"));
                                                                            images[i] = MultipartBody.Part.createFormData("images[]", file.getName(), imageBody);

                                                                            String path = file.getAbsolutePath();
                                                                            trashImages.add(path);

                                                                        }

                                                                        RequestBody request_title = RequestBody.create(property_title, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_propertyDescription = RequestBody.create(propertyDescription, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_property_cost = RequestBody.create(property_cost, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_propertyPostType = RequestBody.create(propertyPostType, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_propertyType = RequestBody.create(propertyType, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_areaType = RequestBody.create(areaType, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_Area = RequestBody.create(Area, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_areaUnit = RequestBody.create(areaUnit, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_furnished = RequestBody.create(furnished, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_floor = RequestBody.create(floor, okhttp3.MediaType.parse("text/plan"));
                                                                        RequestBody request_totalFloors = RequestBody.create(totalFloors, okhttp3.MediaType.parse("text/plan"));



                                                                        Call<Status_Response> call = taskMetServer.editPropertyWithImagePost(id,images, request_title, request_propertyDescription,
                                                                                request_property_cost,request_propertyPostType,request_propertyType,request_areaType,request_Area,request_areaUnit,
                                                                                request_furnished,request_floor,request_totalFloors);
                                                                        call.enqueue(new Callback<Status_Response>() {
                                                                            @Override
                                                                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {
                                                                                if (response.isSuccessful()) {
                                                                                    Status_Response message = response.body();
                                                                                    String msg = message.getStatus();
                                                                                    if (msg.equals(Constants.TRUE)) {
                                                                                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                                                                .setTitleText(getString(R.string.contragulations_text))
                                                                                                .setContentText(getString(R.string.image_updated))
                                                                                                .setConfirmText(getString(R.string.dialog_ok))
                                                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                                                        sDialog.dismissWithAnimation();

                                                                                                        MyPosts myPosts = new MyPosts();
                                                                                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                                                                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                                                                                        transaction.replace(R.id.framelayout_post_task, myPosts);
                                                                                                        transaction.addToBackStack("tag");
                                                                                                        transaction.commit();
                                                                                                    }
                                                                                                })
                                                                                                .show();
                                                                                    }

                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<Status_Response> call, Throwable t) {

                                                                            }
                                                                        });

                                                                    }
                                                                });
                                                    }
                                                })
                                                .showCancelButton(true)
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
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

                    }

                    @Override
                    public void onFailure(Call<ViewMyPostModel> call, Throwable t) {

                    }
                });


            }
        });
        thread.start();






        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void initialiazeShop() {
        seekBar = view.findViewById(R.id.seekBar);
        indicatorStayLayout = view.findViewById(R.id.indicatorStayLayout);
        seekBarHeading = view.findViewById(R.id.seekBarHeading);
        postTitleEditText = view.findViewById(R.id.editShopPostTitle);
        icon_info_title = view.findViewById(R.id.post_title_info_icon);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        icon_info_price = view.findViewById(R.id.iconImagePostPrice);
        icon_info_description = view.findViewById(R.id.iconImagePostDescription);

        postPriceEditText = view.findViewById(R.id.postPrice);
        postDescriptionEditText = view.findViewById(R.id.postDescription);
        conditionLayout = view.findViewById(R.id.conditionLayout);
        currency = view.findViewById(R.id.editShopCurrencyText);

    }

    public void initialiazeJob() {
        salaryFromEditText = view.findViewById(R.id.salaryFromEditText);
        salaryToEditText = view.findViewById(R.id.salaryToEditText);
        companyNameEditText = view.findViewById(R.id.companyNameEditText);
        jobCategorySpinner = view.findViewById(R.id.jobCategorySpinner);
        positionTypeSpinner = view.findViewById(R.id.positionTypeSpinner);
        salaryPeriodSpinner = view.findViewById(R.id.salaryCategorySpinner);
        jobOffer = view.findViewById(R.id.btnJobOffer);
        cvResume = view.findViewById(R.id.btnCvResume);
        salaryFromSalaryToLayout = view.findViewById(R.id.layoutSalaryFromSalaryTo);
        companyNameLayout = view.findViewById(R.id.jobLayoutCompanyName);
        describtionAboutJob = view.findViewById(R.id.textViewDescribtionAboutJob);
        jobTitleEditText = view.findViewById(R.id.editTextJobTitle);
        jobDescribtionEditText = view.findViewById(R.id.jobDescriptionEditText);
        iconJobTitle = view.findViewById(R.id.iconImageJobTitle);
        iconJobDescribtion = view.findViewById(R.id.iconImageJobDescribtion);
        JobbackArrow = view.findViewById(R.id.backArrowImageView);
    }

    public void initialiazeCommon() {

        editTextServiceTitle = view.findViewById(R.id.editTextJobTitle_CommonScreen);
        editTextServieCost = view.findViewById(R.id.serviceCost);
        editTextdescribe_service = view.findViewById(R.id.describeService_commonScreen);
        iconImageJobTitle = view.findViewById(R.id.iconImageJobTitle_CommonScreen);
        iconImageDescribeService = view.findViewById(R.id.iconImageJobDescribtion_CommonScreen);
        commonBackArrow = view.findViewById(R.id.backArrowImageView);
    }

    public void initialiazeMarraige() {
        marriagePostTitleEditText = view.findViewById(R.id.marriagePostTitle);
        myHeghtEditText = view.findViewById(R.id.myHeight);
        myAgeEditText = view.findViewById(R.id.myAge);
        myCastClanEditText = view.findViewById(R.id.myCast);
        mySalaryEditText = view.findViewById(R.id.mySalary);
        writeMyselfEditText = view.findViewById(R.id.mySelf);
        partnerHeghtEditText = view.findViewById(R.id.partnerHeight);
        partnerAgeEditText = view.findViewById(R.id.partnerAge);
        partnerCastClanEditText = view.findViewById(R.id.partnerCast);
        myReligionSpinner = view.findViewById(R.id.myReligionSpinner);
        myEducationSpinner = view.findViewById(R.id.educationTypeSpinner);
        myJobSpinner = view.findViewById(R.id.jobTypeSpinner);

        partnerReligionSpinner = view.findViewById(R.id.partnerReligionSpinner);
        partnerEducationSpinner = view.findViewById(R.id.partnerEducationTypeSpinner);
        partnerJobSpinner = view.findViewById(R.id.partnerJobTypeSpinner);
        partnerWriteAboutPartnerEditText = view.findViewById(R.id.partnerSelf);

        //.............iconPost Title..............
        iconMarriagePostTitle = view.findViewById(R.id.iconPostTitle);

        btnBride = view.findViewById(R.id.btnBride);
        btnGroom = view.findViewById(R.id.btnGroom);

        //martial status buttons
        btnWidower = view.findViewById(R.id.btnWidower);
        btnMarried = view.findViewById(R.id.btnMarried);
        btnUnmarried = view.findViewById(R.id.btnUnmarried);
        btnDivorced = view.findViewById(R.id.btnDivorced);
        btnSeparated = view.findViewById(R.id.btnSeparated);
        btnBride = view.findViewById(R.id.btnBride);
        btnGroom = view.findViewById(R.id.btnGroom);


        //martial status buttons


    }



    public void initializeAllView() {
        btnUpdateAd = view.findViewById(R.id.btnUpdateAd);
        btnUpdateImage = view.findViewById(R.id.btnUpdateImage);
        initialiazeShop();
        initialiazeJob();
        initialiazeCommon();
        initialiazeMarraige();
        initialiazeProperty();

    }

    public void setSubCategoryName() {


        if (item.equals(getResources().getString(R.string.mobile))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.mobiles, android.R.layout.simple_spinner_item);
            getSubCategoryArray(getResources().getStringArray(R.array.mobiles));
        } else if (item.equals(getResources().getString(R.string.computer))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.computers, android.R.layout.simple_spinner_item);
            getSubCategoryArray(getResources().getStringArray(R.array.computers));
        } else if (item.equals(getResources().getString(R.string.vehicle))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.vehicles, android.R.layout.simple_spinner_item);
            getSubCategoryArray(getResources().getStringArray(R.array.vehicles));
        } else if (item.equals(getResources().getString(R.string.furniture))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.furnitures, android.R.layout.simple_spinner_item);
            getSubCategoryArray(getResources().getStringArray(R.array.furnitures));
        } else if (item.equals(getResources().getString(R.string.appliances))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.appliances, android.R.layout.simple_spinner_item);
            getSubCategoryArray(getResources().getStringArray(R.array.appliances));
        } else if (item.equals(getResources().getString(R.string.bike))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.bikes, android.R.layout.simple_spinner_item);
            getSubCategoryArray(getResources().getStringArray(R.array.bikes));
        } else if (item.equals(getResources().getString(R.string.sports))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.sports, android.R.layout.simple_spinner_item);
            getSubCategoryArray(getResources().getStringArray(R.array.sports));
        } else if (item.equals(getResources().getString(R.string.pets_animals))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.animals, android.R.layout.simple_spinner_item);
            getSubCategoryArray(getResources().getStringArray(R.array.animals));
        } else if (item.equals(getResources().getString(R.string.fashion))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.fashions, android.R.layout.simple_spinner_item);
            getSubCategoryArray(getResources().getStringArray(R.array.fashions));
        } else if (item.equals(getResources().getString(R.string.kids))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.kids, android.R.layout.simple_spinner_item);
            getSubCategoryArray(getResources().getStringArray(R.array.kids));
        } else if (item.equals(getResources().getString(R.string.books))) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.books, android.R.layout.simple_spinner_item);
            getSubCategoryArray(getResources().getStringArray(R.array.books));
        }


    }

    public void goToNext() {
        Screen3_PostPhoto screen3_postPhoto = new Screen3_PostPhoto();
        androidx.fragment.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.framelayout_post_task, screen3_postPhoto);
        transaction.addToBackStack("tag");
        transaction.commit();
    }

    public void setAllThingsAtPrimary() {
        context = getContext();

        if (!item.equals(null) && item.equals(getResources().getString(R.string.pets_animals))) {
            conditionLayout.setVisibility(View.GONE);
            product_Condition = "other";
            product_Condition_meter = "11";
        } else {
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
    public  void retriveDataForShop(PostDataModel myPostModel)
    {
        title = myPostModel.getTitle();
        description = myPostModel.getDescription();
        price = myPostModel.getPrice();
        product_Condition = myPostModel.getCondition();
        product_Condition_meter = myPostModel.getCondition_meter();
        subCategoryName = myPostModel.getSubCategory();
    }

    public void controlEditTextIconVisibility(EditText editText, ImageView imageView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString().equals("")) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
            }
        });

    }

    public void controlPriceEditTextandIconVisibility(EditText editText, ImageView imageView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString().equals("")) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    long total = Long.parseLong(editText.getText().toString());
                    if (total > 500000) {
                        editText.setError("Maximum price 500000");
                        editText.requestFocus();
                        return;
                    }
                    imageView.setVisibility(View.GONE);
                }
            }
        });

    }

    public void setSubCategorySpinner() {
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

    public void controlButtonNew() {
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
    public void setPropertySpinner()
    {
        setPropertyCategory();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propertyCategory.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_default_text,
                        getContext()));
        setAreaCategory(true);
        propertyCategory.setOnItemSelectedListener(this);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaCategory.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_default_text,
                        getContext()));
        areaCategory.setOnItemSelectedListener(this);

    }



    public void controlButtonUsed() {
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

    public void onBackPressed(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position > 0){
            subCategoryName = parent.getItemAtPosition(position).toString();

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
            else{
                String text = parent.getItemAtPosition(position).toString();
                areaType = text;
                isAreaTypeSelected = true;

            }


            if(parent.getItemAtPosition(1).equals(getResources().getString(R.string.online_remote)))
            {
                String text = parent.getItemAtPosition(position).toString();
                jobCategory = text;
                isJobCategorySelected = true;
            }
           if(parent.getItemAtPosition(1).equals(getResources().getString(R.string.contract_base)))
            {
                String text = parent.getItemAtPosition(position).toString();
                positionType = text;
                isPositiontypeSelected = true;

            }
            if(parent.getItemAtPosition(1).equals(getResources().getString(R.string.hourly)))
            {
                String text = parent.getItemAtPosition(position).toString();
                salaryPeriod = text;
                isSalaryPeriodSelected = true;
            }

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

    public void getSubCategoryArray(String[] array) {
        subCategoryArray = array;
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

    public void setJobSpinner() {
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

    }


    public void controlCVButton() {


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

    public void controlJobofferButton() {
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

    public void setPrimaryMarriageEditText() {
        marriagePostTitleEditText.setText(marriagePostTile);
        myHeghtEditText.setText(my_height);
        myAgeEditText.setText(my_age);
        myCastClanEditText.setText(my_cast_clan);
        mySalaryEditText.setText(my_salary);
        writeMyselfEditText.setText(write_myself);
        if (!partner_height.equals("empty")) {
            partnerHeghtEditText.setText(partner_height);
        }
        if (!partner_age.equals("empty")) {
            partnerAgeEditText.setText(partner_age);
        }

        if (!partner_cast_clan.equals("empty")) {
            partnerCastClanEditText.setText(partner_cast_clan);
        }
        if (!partner_write_about_partner.equals("empty")) {
            partnerWriteAboutPartnerEditText.setText(partner_write_about_partner);
        }
    }


    public void retriveDataForMarriage(PostDataModel myPostModel) {
        looking_for = myPostModel.getLookingFor();
        maritial_status = myPostModel.getMaritialStatus();
        marriagePostTile = myPostModel.getTitle();
        my_height = myPostModel.getMyHeight();
        my_age = myPostModel.getMyAge();
        my_religion = myPostModel.getMyReligion();
        my_cast_clan = myPostModel.getMyCastClan();
        my_education = myPostModel.getMyEducation();
        my_job = myPostModel.getMyJob();
        my_salary = myPostModel.getMySalary();
        write_myself = myPostModel.getWriteMyself();
        partner_height = myPostModel.getPartnerHeight();
        partner_age = myPostModel.getPartnerAge();
        partner_religion = myPostModel.getPartnerReligion();
        partner_cast_clan = myPostModel.getPartnerCastClan();
        partner_education = myPostModel.getPartnerEducation();
        partner_job = myPostModel.getPartnerJob();
        partner_write_about_partner = myPostModel.getAboutPartner();

    }

    public void getMarriageEditTextData() {
        marriagePostTile = marriagePostTitleEditText.getText().toString();
        my_height = myHeghtEditText.getText().toString();
        my_age = myAgeEditText.getText().toString();
        my_cast_clan = myCastClanEditText.getText().toString();
        my_salary = mySalaryEditText.getText().toString();
        write_myself = writeMyselfEditText.getText().toString();
        partner_height = setEmptyValueForMarriage(partnerHeghtEditText);
        partner_age = setEmptyValueForMarriage(partnerAgeEditText);
        partner_cast_clan = setEmptyValueForMarriage(partnerCastClanEditText);
        partner_write_about_partner = setEmptyValueForMarriage(partnerWriteAboutPartnerEditText);


    }
    private void setReligionCategory() {
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.religionArrary, android.R.layout.simple_spinner_item);
    } private void setEducationCategory() {
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.educationArray, android.R.layout.simple_spinner_item);
    }
    private void setMarriageJobCategory() {
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.jobArray, android.R.layout.simple_spinner_item);
    }
    public void setMarriageSpinner()
    {
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

        setMarriageJobCategory();
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
    }

    public void getJobEditTextData() {

        String job_title = jobTitleEditText.getText().toString();
        String salary_From = salaryFromEditText.getText().toString();
        String salary_to = salaryToEditText.getText().toString();
        String job_description = jobDescribtionEditText.getText().toString();
        String company_name = companyNameEditText.getText().toString();
    }

    public void controlMarriagebtnBride() {
        looking_for = "Bride";

        if (btn_count == 0) {

            btn_count = 1;
            btnBride.setBackgroundResource(R.drawable.button_selected);
            btnBride.setTextColor(getResources().getColor(R.color.Theme1));
            btnGroom.setBackgroundResource(R.drawable.button_unselected);
            btnGroom.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnWidower.setText("Widower");
            btnMarried.setVisibility(View.VISIBLE);
            Animation mLoadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
            btnMarried.startAnimation(mLoadAnimation);

        }
    }

    public void controlMarriagebtnGroom() {
        looking_for = "Groom";
        if (btn_count == 1) {

            btn_count = 0;
            btnGroom.setBackgroundResource(R.drawable.button_selected);
            btnGroom.setTextColor(getResources().getColor(R.color.Theme1));
            btnBride.setBackgroundResource(R.drawable.button_unselected);
            btnBride.setTextColor(getResources().getColor(R.color.text_light_gray));
            btnWidower.setText("Widow");
            btnMarried.setVisibility(View.GONE);
            Animation mLoadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_out);
            btnMarried.startAnimation(mLoadAnimation);
        }
    }

    public void controlMarriagebtnUnmarried() {
        maritial_status = "Unmarried";
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
    }

    public void controlMarriagebtnDivorced() {
        maritial_status = "Divorced";
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
    }

    public void controlMarriagebtnWidower() {
        if (btn_count == 0) {
            maritial_status = "Widow";
        } else if (btn_count == 1) {
            maritial_status = "Widower";
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
    }

    public void controlMarriagebtnSeparated() {
        maritial_status = "Separated";
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
    }

    public void controlMarriagebtnMarried() {
        maritial_status = "Married";
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
    }

    public String setEmptyValueForMarriage(EditText editText) {
        if (editText.getText().toString().equals("") || editText.getText().toString().equals(null)) {
            return "empty";
        } else {
            return editText.getText().toString();
        }
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
    private File getBitmapFile(Bitmap reducedImage){

        Random rand = new Random();
        int rnd1 = rand.nextInt(100000);
        int rnd2 = rand.nextInt(100000);

        File file = new File(getContext().getExternalFilesDir(null).getAbsolutePath() + File.separator + "TaskMet_Post_Image_"+String.valueOf(rnd1)+String.valueOf(rnd2));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        reducedImage.compress(Bitmap.CompressFormat.JPEG,80,bos);
        byte[] bitmapData = bos.toByteArray();

        try{
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
            return file;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return file;
    }

    private void deleteTrashImages() {

        for(int i=0 ; i<trashImages.size() ; i++){

            File trash_file = new File(trashImages.get(i));
            trash_file.delete();

        }

    }
    public void initialiazeProperty() {
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
        propertyBackArrow = view.findViewById(R.id.backArrowImageView);
        furnishedFloorLayout = view.findViewById(R.id.all_furnished_layout);
        furnishedLayout = view.findViewById(R.id.furnishedLayout);

    }

    public void retriveDataForProperty(PostDataModel myPostModel) {
        property_cost = myPostModel.getCost();
        propertyPostType = myPostModel.getPropertyPostType();
        propertyType = myPostModel.getPropertyType();
        areaType = myPostModel.getAreaType();
        Area = myPostModel.getArea();
        areaUnit = myPostModel.getAreaUnit();
        furnished = myPostModel.getFurnished();
        floor = myPostModel.getFloorLevel();
        totalFloors = myPostModel.getTotalFloors();
        property_title = myPostModel.getTitle();
        propertyDescription = myPostModel.getDescription();
    }
    public void setPrimaryPropertyEditText() {
     /*   rentButton = view.findViewById(R.id.btnRent);
        sellButton = view.findViewById(R.id.btnSell);
        yesButton = view.findViewById(R.id.btnYes);
        noButton = view.findViewById(R.id.btnNo);*/
        propertyPriceEditText.setText(property_cost);
        propertyTitleEditText.setText(property_title);
        propertyDescribtionEditText.setText(propertyDescription);
        areaEditText.setText(Area);
        areaUnitToEditText.setText(areaUnit);
        floorEditText.setText(floor);
        totalFloorsEditText.setText(totalFloors);

      /*  furnishedFloorLayout = view.findViewById(R.id.furnishedLayoutfloorLevelLayout);
        furnishedLayout = view.findViewById(R.id.furnishedLayout);*/
    }
    public void getPropertyEditTextData() {
        property_cost = propertyPriceEditText.getText().toString();
        Area = areaEditText.getText().toString();
        areaUnit =areaUnitToEditText.getText().toString();
        floor = floorEditText.getText().toString();
        totalFloors = totalFloorsEditText.getText().toString();
        property_title = propertyTitleEditText.getText().toString();
        propertyDescription = propertyDescribtionEditText.getText().toString();
    }
    public void controlSellButton()
    {
        propertyPostType = "Sell";
        priceTextView.setText("Price :");

        sellButton.setBackgroundResource(R.drawable.button_selected);
        sellButton.setTextColor(getResources().getColor(R.color.Theme1));
        rentButton.setBackgroundResource(R.drawable.button_unselected);
        rentButton.setTextColor(getResources().getColor(R.color.text_light_gray));
    }
    public void controlRentButton()
    {
        propertyPostType = "Rent";
        priceTextView.setText("Rent :");
        rentButton.setBackgroundResource(R.drawable.button_selected);
        rentButton.setTextColor(getResources().getColor(R.color.Theme1));
        sellButton.setBackgroundResource(R.drawable.button_unselected);
        sellButton.setTextColor(getResources().getColor(R.color.text_light_gray));
    }

    public  void controlYesButton()
    {
        furnished = "yes";
        yesButton.setBackgroundResource(R.drawable.button_selected);
        yesButton.setTextColor(getResources().getColor(R.color.Theme1));
        noButton.setBackgroundResource(R.drawable.button_unselected);
        noButton.setTextColor(getResources().getColor(R.color.text_light_gray));
    }

    public void controlNoButton()
    {
        furnished = "no";
        noButton.setBackgroundResource(R.drawable.button_selected);
        noButton.setTextColor(getResources().getColor(R.color.Theme1));
        yesButton.setBackgroundResource(R.drawable.button_unselected);
        yesButton.setTextColor(getResources().getColor(R.color.text_light_gray));
    }



}