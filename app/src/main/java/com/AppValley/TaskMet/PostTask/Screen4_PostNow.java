package com.AppValley.TaskMet.PostTask;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.AppValley.TaskMet.PostPurchases.PostAdLastScreen;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.AdPostResponse;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PostNowPrice;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.Utils.ImageCompression;
import com.AppValley.TaskMet.constant.Constants;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Screen4_PostNow extends Fragment implements DatePickerDialog.OnDateSetListener {


    Switch showNumber, showLocation;

    ArrayList<String> trashImages = new ArrayList<>();

    TextView expireDateTextView;

    TextView locationTextShow;
    SharedPreferences userPreferences;

    ImageView backArrow, expireDateEditImage;
    br.com.simplepass.loadingbutton.customViews.CircularProgressButton btnPost;

    //......................UserInfo............................
    String id;
    String number;
    String uid;
    String name;
    String email;
    String email_verified;
    String profile_pic;
    String country_name;
    String city_name;
    String country_code;
    String address;
    String currency;
    String latitude;
    String longitude;
    String token;

    //................Main Category ShardPreferences and common for all.........................................
    SharedPreferences mainCategorySharedPreferences;
    ArrayList<String> photoArray = new ArrayList<>();
    String currentDate;
    String expireDate;
    int isShowLocation = 1;
    int isShowNumber = 0;


    //..........................Shop details Data need to store at server.............................
    String mainCategory;
    String shop_title;
    String shop_subCategory;
    String shop_condition;
    String shop_condition_meter;
    String shop_price;
    String shop_Description;
    SharedPreferences shopSharedPreferences;


    //...........sharedpreferences for serivce post..................................

    //...................Job details retrive variable...................................

    String jobType;
    String jobCategory;
    String positionType;
    String job_title;
    String salary_From;
    String salary_to;
    String salary_period;
    String job_description;
    String company_name;
    SharedPreferences serviceJobSharedpreferences;

    //...................Job details retrive variable...................................


    //............Variable for marriage data.................
    SharedPreferences MarriageSharedPreferences;
    String looking_for;
    String maritial_status;
    String marriagePostTitle;
    String my_height;
    String my_age;
    String my_religion;
    String my_cast_clan;
    String my_education;
    String my_job;
    String my_salary;
    String write_myself;
    String partner_height;
    String partner_age;
    String partner_religion;
    String partner_cast_clan;
    String partner_education;
    String partner_job;
    String partner_write_about_partner;
    //......................................................

    //.............Variable for Common Service...............................
    String commonserviceTitle;
    String serviceCost;
    String serviceDescription;
    SharedPreferences serviceCommmonSharedpreferences;
    //........................................................................

    //................variable for property service..........................
    String propertyPostType = "Rent";
    String propertyPrice;
    String propertyType;
    String areaType;
    String Area;
    String areaUnit;
    String furnished = "yes";
    String floor;
    String totalFloors;
    String property_title;
    String property_Description, account_type;
    SharedPreferences servicePropertySharedpreferences;


    TaskMetServer taskMetServer;
    String deviceToken;

    FragmentManager fragmentManager;

    //------------- Date session ---------

    SimpleDateFormat curFormater;

    //-----------------------------------

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_screen4_post_now, container, false);

        //Support Fragment Manager
        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

        //................ Main Category ShardPreferences.........................................
        mainCategorySharedPreferences = getContext().getSharedPreferences(Constants.SELECTING_CATEGORY_SERVICE_SHOP, Context.MODE_PRIVATE);
        mainCategory = mainCategorySharedPreferences.getString(Constants.MAIN_CATEGORY, null);


        showNumber = view.findViewById(R.id.showPhoneNumberToggleButton);
        showLocation = view.findViewById(R.id.showLocationToggleButton);
        expireDateTextView = view.findViewById(R.id.expireDateTextView);

        locationTextShow = view.findViewById(R.id.locationTextView);
        btnPost = view.findViewById(R.id.btn_post);
        backArrow = view.findViewById(R.id.backArrowImageView);

        expireDateEditImage = view.findViewById(R.id.expireDateEditImage);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager.popBackStackImmediate();

            }
        });


        //--------------------------------- Normal & Premium date setup ----------------------------
        Calendar currentCal = Calendar.getInstance();
        curFormater = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = curFormater.format(currentCal.getTime());

        //Expire date for Normal Ad.
        currentCal.add(Calendar.DATE, 7);
        expireDate = curFormater.format(currentCal.getTime());
        expireDateTextView.setText("Ad Expire On: " + expireDate);

        //............ Button to open Date picker ...........


        expireDateEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (account_type.equals("PREMIUM")) {

                    try {
                        showDatePickerDialog(currentDate, curFormater);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {

                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setContentText("Get Premium Account to set custom expire date.")
                            .setConfirmText("ok")
                            .show();

                }


            }
        });

        //------------------------------------------------------------------------------------------

        //...................retrive token.........................

        deviceToken = FirebaseMessaging.getInstance().getToken().toString();

        //.........................................................

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        //................get user Details SharedPrefrerences ..........................................

        userPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        id = userPreferences.getString(Constants.ID, null);
        number = userPreferences.getString(Constants.NUMBER, null);
        uid = userPreferences.getString(Constants.UID, null);
        name = userPreferences.getString(Constants.NAME, null);
        email = userPreferences.getString(Constants.EMAIL, null);
        email_verified = userPreferences.getString(Constants.EMAIL_VERIFIED, null);
        profile_pic = userPreferences.getString(Constants.PROFILE_PIC, null);
        country_name = userPreferences.getString(Constants.COUNTRY_NAME, null);
        city_name = userPreferences.getString(Constants.CITY_NAME, null);
        country_code = userPreferences.getString(Constants.COUNTRY_CODE, null);
        address = userPreferences.getString(Constants.ADDRESS, null);
        currency = userPreferences.getString(Constants.CURRENCY, null);
        latitude = userPreferences.getString(Constants.LATITUDE, null);
        longitude = userPreferences.getString(Constants.LONGITUDE, null);
        token = userPreferences.getString(Constants.TOKEN, null);
        account_type = userPreferences.getString(Constants.ACCOUNT_TYPE, null);

        locationTextShow.setText(city_name + "," + country_name);

        //................get user Details SharedPrefrerences ..........................................


        //.........................retrive photo for all Screen..........................................


        Gson gson = new Gson();
        String jsonText = mainCategorySharedPreferences.getString(Constants.POST_PHOTOARRAY, null);

        String[] imageString = gson.fromJson(jsonText, String[].class);
        for (String string : imageString) {
            photoArray.add(string);
        }
        //........................Retrive Data for shopping post..................................................

        shopSharedPreferences = getContext().getSharedPreferences(Constants.SHOP_NEW_POST, Context.MODE_PRIVATE);
        shop_title = shopSharedPreferences.getString(Constants.SHOP_TITLE, null);
        shop_subCategory = shopSharedPreferences.getString(Constants.SHOP_SUB_CATEGORY, null);
        shop_Description = shopSharedPreferences.getString(Constants.SHOP_DESCRIPTION, null);
        shop_price = shopSharedPreferences.getString(Constants.SHOP_PRICE, null);
        shop_condition = shopSharedPreferences.getString(Constants.SHOP_CONDITION, null);
        shop_condition_meter = shopSharedPreferences.getString(Constants.SHOP_CONDITION_METER, null);

        //....................................................................................................


        //......................Retrive data from Service job post....................................
        serviceJobSharedpreferences = getContext().getSharedPreferences(Constants.SERVICE_JOB_NEW_POST, Context.MODE_PRIVATE);
        jobType = serviceJobSharedpreferences.getString(Constants.SERVICE_JOB_TYPE, null);
        jobCategory = serviceJobSharedpreferences.getString(Constants.SERVICE_JOB_CATEGORY, null);
        positionType = serviceJobSharedpreferences.getString(Constants.SERVICE_POSITION_TYPE, null);
        job_title = serviceJobSharedpreferences.getString(Constants.SERVICE_JOB_TITLE, null);
        salary_From = serviceJobSharedpreferences.getString(Constants.SERVICE_SALARY_FROM, null);
        salary_to = serviceJobSharedpreferences.getString(Constants.SERVICE_SALARY_TO, null);
        salary_period = serviceJobSharedpreferences.getString(Constants.SERVICE_SALARY_PERIOD, null);
        job_description = serviceJobSharedpreferences.getString(Constants.SERVICE_JOB_DESCRIPTION, null);
        company_name = serviceJobSharedpreferences.getString(Constants.SERVICE_COMPANY_NAME, null);
        //............................................................................................

        //........................Retrive data for Marriage Service...................................
        MarriageSharedPreferences = getContext().getSharedPreferences(Constants.SERVICE_MARRIAGE_NEW_POST, Context.MODE_PRIVATE);
        looking_for = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_LOOKING_FOR, null);
        maritial_status = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_MARTIAL_STATUS, null);
        marriagePostTitle = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_POST_TITLE, null);
        my_height = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_MY_HEIGHT, null);
        my_age = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_MY_AGE, null);
        my_religion = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_MY_RELIGION, null);
        my_cast_clan = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_MY_CAST_CLAN, null);
        my_education = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_MY_EDUCATION, null);
        my_job = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_MY_JOB, null);
        my_salary = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_MY_SALARY, null);
        write_myself = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_WRITE_MYSELF, null);
        partner_height = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_PARTNER_HEIGHT, null);
        partner_age = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_PARTNER_AGE, null);
        partner_religion = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_PARTNER_RELIGION, null);
        partner_cast_clan = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_PARTNER_CAST_CLAN, null);
        partner_education = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_PARTNER_EDUCATION, null);
        partner_job = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_PARTNER_JOB, null);
        partner_write_about_partner = MarriageSharedPreferences.getString(Constants.SERVICE_MARRIAGE_PARTNER_WRITE_ABOUT_PARTNER, null);


        //........................Retrive data for Marriage Service...................................


        //........................Retrive data for  Common Service........................................................
        serviceCommmonSharedpreferences = getContext().getSharedPreferences(Constants.SERVICE_COMMON_NEW_POST, Context.MODE_PRIVATE);
        commonserviceTitle = serviceCommmonSharedpreferences.getString(Constants.SERVICE_COMMON_POST_TITLE, null);
        serviceCost = serviceCommmonSharedpreferences.getString(Constants.SERVICE_COMMON_COST, null);
        serviceDescription = serviceCommmonSharedpreferences.getString(Constants.SERVICE_COMMON_DESCRIPTION, null);
        //........................Retrive data for  Common Service........................................................

        //.........................Retrive data for Property Screen.......................................................
        servicePropertySharedpreferences = getContext().getSharedPreferences(Constants.SERVICE_PROPERTY_NEW_POST, Context.MODE_PRIVATE);
        propertyPostType = servicePropertySharedpreferences.getString(Constants.PROPERTY_POST_TYPE, null);
        propertyPrice = servicePropertySharedpreferences.getString(Constants.PROERTY_PRICE, null);
        propertyType = servicePropertySharedpreferences.getString(Constants.PROPERTY_TYPE, null);
        areaType = servicePropertySharedpreferences.getString(Constants.AREA_TYPE, null);
        Area = servicePropertySharedpreferences.getString(Constants.AREA, null);
        areaUnit = servicePropertySharedpreferences.getString(Constants.AREA_UNIT, null);
        furnished = servicePropertySharedpreferences.getString(Constants.FURNISHED, null);
        floor = servicePropertySharedpreferences.getString(Constants.FLOOR, null);
        totalFloors = servicePropertySharedpreferences.getString(Constants.TOTAL_FLOOR, null);
        property_title = servicePropertySharedpreferences.getString(Constants.PROPERTY_TITLE, null);
        property_Description = servicePropertySharedpreferences.getString(Constants.PROPERTY_DESCRIPTION, null);


        showNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isShowNumber = 1;
                } else {
                    isShowNumber = 0;
                }
            }
        });


        showLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isShowLocation = 1;
                } else {
                    isShowLocation = 0;
                }
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = mainCategorySharedPreferences.getString(Constants.SELECTING_TYPE, null);


                btnPost.startAnimation();

                if (!type.equals(null)) {
                    if (type.equals(Constants.SHOP_TYPE)) {
                        shoppingPost();
                    } else if (type.equals(Constants.JOB_TYPE)) {
                        jobServicePost();
                    } else if (type.equals(Constants.MARRIAGE_TYPE)) {
                        marriageServicePost();
                    } else if (type.equals(Constants.COMMON_TYPE)) {
                        commonServicePost();
                    } else if (type.equals(Constants.PROPERTY_TYPE_SELECTING)) {
                        propertyServicePost();
                    }

                }
            }
        });


        Log.d("user_info",
                "\nid:" + id + "\nnumber:" + number + "\nuid:" + uid + "\nname:" + name + "\nemail:" + email +
                        "\nemail_verified:" + email_verified + "\nprofile_pic:" + profile_pic + "\ncountry_name:" + country_name +
                        "\ncity_name:" + city_name + "\ncountry_code:" + country_code + "\naddress:" + address + "\ncurrency:" +
                        currency + "\nlatitude:" + latitude + "\nlongitude:" + longitude + "\ntoken:" + token);

        return view;
    }

    private File getBitmapFile(Bitmap reducedImage) {

        Random rand = new Random();
        int rnd1 = rand.nextInt(100000);
        int rnd2 = rand.nextInt(100000);

        File file = new File(getContext().getExternalFilesDir(null).getAbsolutePath() + File.separator + "TaskMet_Post_Image_" + String.valueOf(rnd1) + String.valueOf(rnd2));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        reducedImage.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        byte[] bitmapData = bos.toByteArray();

        try {
            boolean isCreated = file.createNewFile();

            if (isCreated) {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bitmapData);
                fos.flush();
                fos.close();
                return file;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public void shoppingPost() {

        Log.d("myShopData", "\nmainCategory: " + mainCategory + "\ntitle:" + "shop_title" + "\nsubCategory:" + shop_subCategory + "\ncondition:" + shop_condition +
                "\ncondition_meter:" + shop_condition_meter + "\nprice:" + shop_price + "\ndescription:" + shop_Description +
                "\ncountryName:" + country_name + "\ncurrentdate:" + currentDate + "\nexpireDate:" + expireDate);

        MultipartBody.Part[] images = new MultipartBody.Part[photoArray.size()];

        for (int i = 0; i < photoArray.size(); i++) {

            Bitmap fullSizeImage = BitmapFactory.decodeFile(photoArray.get(i));
            Bitmap compressedImage = ImageCompression.reduceBitmapSize(fullSizeImage, 307200);
            File file = getBitmapFile(compressedImage);

            //File file = new File(filePaths.get(i));

            RequestBody imageBody = RequestBody.create(file, MediaType.parse("image/*"));
            images[i] = MultipartBody.Part.createFormData("images[]", file.getName(), imageBody);

            String path = file.getAbsolutePath();
            trashImages.add(path);

        }

        RequestBody request_number = RequestBody.create(number, MediaType.parse("text/plan"));
        RequestBody request_token = RequestBody.create(token, MediaType.parse("text/plan"));
        RequestBody request_title = RequestBody.create(shop_title, MediaType.parse("text/plan"));
        RequestBody request_mainCategory = RequestBody.create(mainCategory, MediaType.parse("text/plan"));
        RequestBody request_subCategory = RequestBody.create(shop_subCategory, MediaType.parse("text/plan"));
        RequestBody request_condition = RequestBody.create(shop_condition, MediaType.parse("text/plan"));
        RequestBody request_condition_meter = RequestBody.create(shop_condition_meter, MediaType.parse("text/plan"));
        RequestBody request_price = RequestBody.create(shop_price, MediaType.parse("text/plan"));
        RequestBody request_description = RequestBody.create(shop_Description, MediaType.parse("text/plan"));
        RequestBody request_countryName = RequestBody.create(country_name, MediaType.parse("text/plan"));
        RequestBody request_cityName = RequestBody.create(city_name, MediaType.parse("text/plan"));
        RequestBody request_address = RequestBody.create(address, MediaType.parse("text/plan"));
        RequestBody request_currentDate = RequestBody.create(currentDate, MediaType.parse("text/plan"));
        RequestBody request_expireDate = RequestBody.create(expireDate, MediaType.parse("text/plan"));
        RequestBody request_latitude = RequestBody.create(latitude, MediaType.parse("text/plan"));
        RequestBody request_longitude = RequestBody.create(longitude, MediaType.parse("text/plan"));
        RequestBody request_isShowLocation = RequestBody.create(String.valueOf(isShowLocation), MediaType.parse("text/plan"));
        RequestBody request_isShowNumber = RequestBody.create(String.valueOf(isShowNumber), MediaType.parse("text/plan"));
        RequestBody request_deviceToken = RequestBody.create(deviceToken, MediaType.parse("text/plan"));
        RequestBody request_key = RequestBody.create(Constants.SHOP_KEY, MediaType.parse("text/plan"));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Call<AdPostResponse> call = taskMetServer.createShopPost(images, request_number, request_token, request_title,
                        request_mainCategory, request_subCategory, request_condition, request_condition_meter, request_price, request_description, request_countryName, request_cityName, request_address,
                        request_currentDate, request_expireDate, request_latitude, request_longitude, request_isShowLocation, request_isShowNumber, request_deviceToken, request_key);
                call.enqueue(new Callback<AdPostResponse>() {
                    @Override
                    public void onResponse(Call<AdPostResponse> call, Response<AdPostResponse> response) {

                        if (response.isSuccessful()) {

                            AdPostResponse adPostResponse = response.body();

                            setLoadingButton(1);

                            assert adPostResponse != null;
                            String msg = adPostResponse.getStatus();

                            if (msg.equals(Constants.TRUE)) {

                                SharedPreferences.Editor editor = shopSharedPreferences.edit();
                                editor.clear();
                                editor.apply();

                                deleteTrashImages();

                                SharedPreferences preferences = getContext().getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
                                SharedPreferences.Editor post_data = preferences.edit();

                                post_data.putInt(Constants.ID, adPostResponse.getId());
                                post_data.putString(Constants.SHOP_TITLE, adPostResponse.getTitle());
                                post_data.putString(Constants.EXPIRE, adPostResponse.getExpireDate());
                                post_data.putString(Constants.MAIN_CATEGORY, adPostResponse.getMainCategory());
                                post_data.putString(Constants.KEY, adPostResponse.getKey());
                                post_data.putBoolean(Constants.IS_WAITING, adPostResponse.isWaiting());

                                PostNowPrice price = adPostResponse.getPostNow();
                                post_data.putInt(Constants.POST_NOW_COST_GC, price.getGC());
                                post_data.putInt(Constants.POST_NOW_COST_RC, price.getRC());

                                post_data.apply();

                                OpenNextScreen();

                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<AdPostResponse> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        setLoadingButton(0);

                    }
                });

            }
        });
        thread.start();
    }

    public void jobServicePost() {


        MultipartBody.Part[] images = new MultipartBody.Part[photoArray.size()];

        for (int i = 0; i < photoArray.size(); i++) {

            Bitmap fullSizeImage = BitmapFactory.decodeFile(photoArray.get(i));
            Bitmap compressedImage = ImageCompression.reduceBitmapSize(fullSizeImage, 307200);
            File file = getBitmapFile(compressedImage);

            //File file = new File(filePaths.get(i));

            RequestBody imageBody = RequestBody.create(file, MediaType.parse("image/*"));
            images[i] = MultipartBody.Part.createFormData("images[]", file.getName(), imageBody);

            String path = file.getAbsolutePath();
            trashImages.add(path);

        }

        RequestBody request_number = RequestBody.create(number, MediaType.parse("text/plan"));
        RequestBody request_token = RequestBody.create(token, MediaType.parse("text/plan"));
        RequestBody request_title = RequestBody.create(job_title, MediaType.parse("text/plan"));
        RequestBody request_mainCategory = RequestBody.create(mainCategory, MediaType.parse("text/plan"));
        RequestBody request_type = RequestBody.create(jobType, MediaType.parse("text/plan"));
        RequestBody request_jobCategory = RequestBody.create(jobCategory, MediaType.parse("text/plan"));
        RequestBody request_positionType = RequestBody.create(positionType, MediaType.parse("text/plan"));
        RequestBody request_salaryFrom = RequestBody.create(salary_From, MediaType.parse("text/plan"));
        RequestBody request_salaryTo = RequestBody.create(salary_to, MediaType.parse("text/plan"));
        RequestBody request_salaryPeriod = RequestBody.create(salary_period, MediaType.parse("text/plan"));
        RequestBody request_expectedSalary = RequestBody.create("00", MediaType.parse("text/plan"));
        RequestBody request_companyName = RequestBody.create(company_name, MediaType.parse("text/plan"));
        RequestBody request_description = RequestBody.create(job_description, MediaType.parse("text/plan"));
        RequestBody request_countryName = RequestBody.create(country_name, MediaType.parse("text/plan"));
        RequestBody request_cityName = RequestBody.create(city_name, MediaType.parse("text/plan"));
        RequestBody request_address = RequestBody.create(address, MediaType.parse("text/plan"));
        RequestBody request_currentDate = RequestBody.create(currentDate, MediaType.parse("text/plan"));
        RequestBody request_expireDate = RequestBody.create(expireDate, MediaType.parse("text/plan"));
        RequestBody request_latitude = RequestBody.create(latitude, MediaType.parse("text/plan"));
        RequestBody request_longitude = RequestBody.create(longitude, MediaType.parse("text/plan"));
        RequestBody request_isShowLocation = RequestBody.create(String.valueOf(isShowLocation), MediaType.parse("text/plan"));
        RequestBody request_isShowNumber = RequestBody.create(String.valueOf(isShowNumber), MediaType.parse("text/plan"));
        RequestBody request_deviceToken = RequestBody.create(deviceToken, MediaType.parse("text/plan"));
        RequestBody request_key = RequestBody.create(Constants.JOB_KEY, MediaType.parse("text/plan"));


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Call<AdPostResponse> call = taskMetServer.createJobServicePost(images, request_number, request_token, request_title,
                        request_mainCategory, request_type, request_jobCategory, request_positionType, request_salaryFrom, request_salaryTo, request_salaryPeriod, request_expectedSalary,
                        request_companyName, request_description, request_countryName, request_cityName, request_address,
                        request_currentDate, request_expireDate, request_latitude, request_longitude, request_isShowLocation, request_isShowNumber, request_deviceToken, request_key);
                call.enqueue(new Callback<AdPostResponse>() {
                    @Override
                    public void onResponse(Call<AdPostResponse> call, Response<AdPostResponse> response) {


                        if (response.isSuccessful()) {

                            AdPostResponse adPostResponse = response.body();

                            setLoadingButton(1);

                            assert adPostResponse != null;
                            String msg = adPostResponse.getStatus();

                            if (msg.equals(Constants.TRUE)) {
                                SharedPreferences.Editor editor = serviceJobSharedpreferences.edit();
                                editor.clear();
                                editor.apply();

                                deleteTrashImages();

                                SharedPreferences preferences = getContext().getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
                                SharedPreferences.Editor post_data = preferences.edit();

                                post_data.putInt(Constants.ID, adPostResponse.getId());
                                post_data.putString(Constants.SHOP_TITLE, adPostResponse.getTitle());
                                post_data.putString(Constants.EXPIRE, adPostResponse.getExpireDate());
                                post_data.putString(Constants.MAIN_CATEGORY, adPostResponse.getMainCategory());
                                post_data.putString(Constants.KEY, adPostResponse.getKey());
                                post_data.putBoolean(Constants.IS_WAITING, adPostResponse.isWaiting());

                                PostNowPrice price = adPostResponse.getPostNow();
                                post_data.putInt(Constants.POST_NOW_COST_GC, price.getGC());
                                post_data.putInt(Constants.POST_NOW_COST_RC, price.getRC());

                                post_data.apply();

                                OpenNextScreen();


                            }


                        }


                    }

                    @Override
                    public void onFailure(Call<AdPostResponse> call, Throwable t) {
                        setLoadingButton(0);
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
        thread.start();

    }

    public void marriageServicePost() {

        Log.d("service_marriage", "\nServicemainCategory:" + mainCategory + "\nlooking_for:" + looking_for + "\nmaritial_status:" +
                maritial_status + "\npostTile:" + marriagePostTitle + "\nmy_height:" + my_height + "\nmy_age:" + my_age + "\nmy_religion:" + my_religion +
                "\nmy_cast_clan:" + my_cast_clan + "\nmy_education:" + my_education + "\nmy_job:" + my_job + "\nmy_salary:" + my_salary +
                "\nwrite_myself:" + write_myself + "\npartner_height:" + partner_height + "\npartner_age:" + partner_age +
                "\npartner_religion:" + partner_religion + "\npartner_cast_clan:" + partner_cast_clan + "\npartner_education:" +
                partner_education + "\npartner_job:" + partner_job + "\npartner_write_about_partner:" + partner_write_about_partner);


        MultipartBody.Part[] images = new MultipartBody.Part[photoArray.size()];

        for (int i = 0; i < photoArray.size(); i++) {

            Bitmap fullSizeImage = BitmapFactory.decodeFile(photoArray.get(i));
            Bitmap compressedImage = ImageCompression.reduceBitmapSize(fullSizeImage, 307200);
            File file = getBitmapFile(compressedImage);

            //File file = new File(filePaths.get(i));

            RequestBody imageBody = RequestBody.create(file, MediaType.parse("image/*"));
            images[i] = MultipartBody.Part.createFormData("images[]", file.getName(), imageBody);

            String path = file.getAbsolutePath();
            trashImages.add(path);


        }
        RequestBody request_number = RequestBody.create(number, MediaType.parse("text/plan"));
        RequestBody request_token = RequestBody.create(token, MediaType.parse("text/plan"));
        RequestBody request_title = RequestBody.create(marriagePostTitle, MediaType.parse("text/plan"));
        RequestBody request_mainCategory = RequestBody.create(mainCategory, MediaType.parse("text/plan"));
        RequestBody request_lookingFor = RequestBody.create(looking_for, MediaType.parse("text/plan"));
        RequestBody request_maritialStatus = RequestBody.create(maritial_status, MediaType.parse("text/plan"));
        RequestBody request_myHeight = RequestBody.create(my_height, MediaType.parse("text/plan"));
        RequestBody request_myAge = RequestBody.create(my_age, MediaType.parse("text/plan"));
        RequestBody request_myReligion = RequestBody.create(my_religion, MediaType.parse("text/plan"));
        RequestBody request_myCastClan = RequestBody.create(my_cast_clan, MediaType.parse("text/plan"));
        RequestBody request_myEducation = RequestBody.create(my_education, MediaType.parse("text/plan"));
        RequestBody request_myJob = RequestBody.create(my_job, MediaType.parse("text/plan"));
        RequestBody request_mySalary = RequestBody.create(my_salary, MediaType.parse("text/plan"));
        RequestBody request_writeMyself = RequestBody.create(write_myself, MediaType.parse("text/plan"));
        RequestBody request_partnerHeight = RequestBody.create(partner_height, MediaType.parse("text/plan"));
        RequestBody request_partnerAge = RequestBody.create(partner_age, MediaType.parse("text/plan"));
        RequestBody request_partnerReligion = RequestBody.create(partner_religion, MediaType.parse("text/plan"));
        RequestBody request_partnerCastClan = RequestBody.create(partner_cast_clan, MediaType.parse("text/plan"));
        RequestBody request_partnerEducation = RequestBody.create(partner_education, MediaType.parse("text/plan"));
        RequestBody request_partnerJob = RequestBody.create(partner_job, MediaType.parse("text/plan"));
        RequestBody request_aboutPartner = RequestBody.create(partner_write_about_partner, MediaType.parse("text/plan"));
        RequestBody request_countryName = RequestBody.create(country_name, MediaType.parse("text/plan"));
        RequestBody request_cityName = RequestBody.create(city_name, MediaType.parse("text/plan"));
        RequestBody request_address = RequestBody.create(address, MediaType.parse("text/plan"));
        RequestBody request_currentDate = RequestBody.create(currentDate, MediaType.parse("text/plan"));
        RequestBody request_expireDate = RequestBody.create(expireDate, MediaType.parse("text/plan"));
        RequestBody request_latitude = RequestBody.create(latitude, MediaType.parse("text/plan"));
        RequestBody request_longitude = RequestBody.create(longitude, MediaType.parse("text/plan"));
        RequestBody request_isShowLocation = RequestBody.create(String.valueOf(isShowLocation), MediaType.parse("text/plan"));
        RequestBody request_isShowNumber = RequestBody.create(String.valueOf(isShowNumber), MediaType.parse("text/plan"));
        RequestBody request_deviceToken = RequestBody.create(deviceToken, MediaType.parse("text/plan"));
        RequestBody request_key = RequestBody.create(Constants.MARRIAGE_KEY, MediaType.parse("text/plan"));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Call<AdPostResponse> call = taskMetServer.createMarriageServicePost(images, request_number, request_token, request_title, request_mainCategory,
                        request_lookingFor, request_maritialStatus, request_myHeight, request_myAge, request_myReligion, request_myCastClan, request_myEducation, request_myJob,
                        request_mySalary, request_writeMyself, request_partnerHeight, request_partnerAge, request_partnerReligion, request_partnerCastClan,
                        request_partnerEducation, request_partnerJob, request_aboutPartner, request_countryName, request_cityName, request_address,
                        request_currentDate, request_expireDate, request_latitude, request_longitude, request_isShowLocation, request_isShowNumber, request_deviceToken, request_key);

                call.enqueue(new Callback<AdPostResponse>() {
                    @Override
                    public void onResponse(Call<AdPostResponse> call, Response<AdPostResponse> response) {

                        if (response.isSuccessful()) {

                            AdPostResponse adPostResponse = response.body();

                            setLoadingButton(1);

                            assert adPostResponse != null;
                            String msg = adPostResponse.getStatus();

                            if (msg.equals(Constants.TRUE)) {

                                SharedPreferences.Editor editor = MarriageSharedPreferences.edit();
                                editor.clear();
                                editor.apply();

                                deleteTrashImages();

                                SharedPreferences preferences = getContext().getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
                                SharedPreferences.Editor post_data = preferences.edit();

                                post_data.putInt(Constants.ID, adPostResponse.getId());
                                post_data.putString(Constants.SHOP_TITLE, adPostResponse.getTitle());
                                post_data.putString(Constants.EXPIRE, adPostResponse.getExpireDate());
                                post_data.putString(Constants.MAIN_CATEGORY, adPostResponse.getMainCategory());
                                post_data.putString(Constants.KEY, adPostResponse.getKey());
                                post_data.putBoolean(Constants.IS_WAITING, adPostResponse.isWaiting());

                                PostNowPrice price = adPostResponse.getPostNow();
                                post_data.putInt(Constants.POST_NOW_COST_GC, price.getGC());
                                post_data.putInt(Constants.POST_NOW_COST_RC, price.getRC());

                                post_data.apply();

                                OpenNextScreen();

                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<AdPostResponse> call, Throwable t) {
                        setLoadingButton(0);
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        thread.start();
    }

    public void commonServicePost() {


        Log.d("common_service", "\nServicemainCategory:" + mainCategory + "\nserviceTitle:" + commonserviceTitle + "\nserviceCost:"
                + serviceCost + "\nserviceDescription:" + serviceDescription);
        MultipartBody.Part[] images = new MultipartBody.Part[photoArray.size()];

        for (int i = 0; i < photoArray.size(); i++) {

            Bitmap fullSizeImage = BitmapFactory.decodeFile(photoArray.get(i));
            Bitmap compressedImage = ImageCompression.reduceBitmapSize(fullSizeImage, 307200);
            File file = getBitmapFile(compressedImage);

            //File file = new File(filePaths.get(i));

            RequestBody imageBody = RequestBody.create(file, MediaType.parse("image/*"));
            images[i] = MultipartBody.Part.createFormData("images[]", file.getName(), imageBody);

            String path = file.getAbsolutePath();
            trashImages.add(path);


        }

        RequestBody request_number = RequestBody.create(number, MediaType.parse("text/plan"));
        RequestBody request_token = RequestBody.create(token, MediaType.parse("text/plan"));
        RequestBody request_title = RequestBody.create(commonserviceTitle, MediaType.parse("text/plan"));
        RequestBody request_mainCategory = RequestBody.create(mainCategory, MediaType.parse("text/plan"));
        RequestBody request_cost = RequestBody.create(serviceCost, MediaType.parse("text/plan"));
        RequestBody request_description = RequestBody.create(serviceDescription, MediaType.parse("text/plan"));
        RequestBody request_countryName = RequestBody.create(country_name, MediaType.parse("text/plan"));
        RequestBody request_cityName = RequestBody.create(city_name, MediaType.parse("text/plan"));
        RequestBody request_address = RequestBody.create(address, MediaType.parse("text/plan"));
        RequestBody request_currentDate = RequestBody.create(currentDate, MediaType.parse("text/plan"));
        RequestBody request_expireDate = RequestBody.create(expireDate, MediaType.parse("text/plan"));
        RequestBody request_latitude = RequestBody.create(latitude, MediaType.parse("text/plan"));
        RequestBody request_longitude = RequestBody.create(longitude, MediaType.parse("text/plan"));
        RequestBody request_isShowLocation = RequestBody.create(String.valueOf(isShowLocation), MediaType.parse("text/plan"));
        RequestBody request_isShowNumber = RequestBody.create(String.valueOf(isShowNumber), MediaType.parse("text/plan"));
        RequestBody request_deviceToken = RequestBody.create(deviceToken, MediaType.parse("text/plan"));
        RequestBody request_key = RequestBody.create(Constants.COMMON_KEY, MediaType.parse("text/plan"));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Call<AdPostResponse> call = taskMetServer.createCommonService(images, request_number, request_token, request_title, request_mainCategory,
                        request_cost, request_description, request_countryName, request_cityName, request_address,
                        request_currentDate, request_expireDate, request_latitude, request_longitude, request_isShowLocation, request_isShowNumber, request_deviceToken, request_key);
                call.enqueue(new Callback<AdPostResponse>() {
                    @Override
                    public void onResponse(Call<AdPostResponse> call, Response<AdPostResponse> response) {

                        if (response.isSuccessful()) {

                            AdPostResponse adPostResponse = response.body();

                            setLoadingButton(1);

                            assert adPostResponse != null;
                            String msg = adPostResponse.getStatus();

                            if (msg.equals(Constants.TRUE)) {
                                SharedPreferences.Editor editor = serviceCommmonSharedpreferences.edit();
                                editor.clear();
                                editor.apply();

                                deleteTrashImages();

                                SharedPreferences preferences = getContext().getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
                                SharedPreferences.Editor post_data = preferences.edit();

                                post_data.putInt(Constants.ID, adPostResponse.getId());
                                post_data.putString(Constants.SHOP_TITLE, adPostResponse.getTitle());
                                post_data.putString(Constants.EXPIRE, adPostResponse.getExpireDate());
                                post_data.putString(Constants.MAIN_CATEGORY, adPostResponse.getMainCategory());
                                post_data.putString(Constants.KEY, adPostResponse.getKey());
                                post_data.putBoolean(Constants.IS_WAITING, adPostResponse.isWaiting());

                                if(adPostResponse.isWaiting()){
                                    Toast.makeText(getContext(), "True", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getContext(), "false", Toast.LENGTH_SHORT).show();
                                }

                                PostNowPrice price = adPostResponse.getPostNow();
                                post_data.putInt(Constants.POST_NOW_COST_GC, price.getGC());
                                post_data.putInt(Constants.POST_NOW_COST_RC, price.getRC());

                                post_data.apply();

                                OpenNextScreen();


                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<AdPostResponse> call, Throwable t) {
                        setLoadingButton(0);
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
        thread.start();
    }

    public void propertyServicePost() {


        Log.d("propertyTEXt", propertyPostType + " " + propertyPrice + " " + propertyType + " " + areaType + " " + " " + Area + " " + " " + areaUnit + " "
                + " " + furnished + " " + " " + floor + " " + " " + totalFloors + " " + " " + property_title + " " + property_Description);
        MultipartBody.Part[] images = new MultipartBody.Part[photoArray.size()];

        for (int i = 0; i < photoArray.size(); i++) {

            Bitmap fullSizeImage = BitmapFactory.decodeFile(photoArray.get(i));
            Bitmap compressedImage = ImageCompression.reduceBitmapSize(fullSizeImage, 307200);
            File file = getBitmapFile(compressedImage);

            //File file = new File(filePaths.get(i));

            RequestBody imageBody = RequestBody.create(file, MediaType.parse("image/*"));
            images[i] = MultipartBody.Part.createFormData("images[]", file.getName(), imageBody);

            String path = file.getAbsolutePath();
            trashImages.add(path);


        }

        RequestBody request_number = RequestBody.create(number, MediaType.parse("text/plan"));
        RequestBody request_token = RequestBody.create(token, MediaType.parse("text/plan"));
        RequestBody request_title = RequestBody.create(property_title, MediaType.parse("text/plan"));
        RequestBody request_mainCategory = RequestBody.create(mainCategory, MediaType.parse("text/plan"));
        RequestBody request_description = RequestBody.create(property_Description, MediaType.parse("text/plan"));
        RequestBody request_cost = RequestBody.create(propertyPrice, MediaType.parse("text/plan"));
        RequestBody request_propertyPostType = RequestBody.create(propertyPostType, MediaType.parse("text/plan"));
        RequestBody request_propertyType = RequestBody.create(propertyType, MediaType.parse("text/plan"));
        RequestBody request_areaType = RequestBody.create(areaType, MediaType.parse("text/plan"));
        RequestBody request_area = RequestBody.create(Area, MediaType.parse("text/plan"));
        RequestBody request_areaUnit = RequestBody.create(areaUnit, MediaType.parse("text/plan"));
        RequestBody request_furnished = RequestBody.create(furnished, MediaType.parse("text/plan"));
        RequestBody request_floorLevel = RequestBody.create(floor, MediaType.parse("text/plan"));
        RequestBody request_totalFloors = RequestBody.create(totalFloors, MediaType.parse("text/plan"));
        RequestBody request_countryName = RequestBody.create(country_name, MediaType.parse("text/plan"));
        RequestBody request_cityName = RequestBody.create(city_name, MediaType.parse("text/plan"));
        RequestBody request_address = RequestBody.create(address, MediaType.parse("text/plan"));
        RequestBody request_currentDate = RequestBody.create(currentDate, MediaType.parse("text/plan"));
        RequestBody request_expireDate = RequestBody.create(expireDate, MediaType.parse("text/plan"));
        RequestBody request_latitude = RequestBody.create(latitude, MediaType.parse("text/plan"));
        RequestBody request_longitude = RequestBody.create(longitude, MediaType.parse("text/plan"));
        RequestBody request_isShowLocation = RequestBody.create(String.valueOf(isShowLocation), MediaType.parse("text/plan"));
        RequestBody request_isShowNumber = RequestBody.create(String.valueOf(isShowNumber), MediaType.parse("text/plan"));
        RequestBody request_deviceToken = RequestBody.create(deviceToken, MediaType.parse("text/plan"));
        RequestBody request_key = RequestBody.create(Constants.PROPERTY_KEY, MediaType.parse("text/plan"));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Call<AdPostResponse> call = taskMetServer.createPropertyService(images, request_number, request_token, request_title, request_mainCategory, request_description, request_cost,
                        request_propertyPostType, request_propertyType, request_areaType, request_area, request_areaUnit, request_furnished, request_floorLevel, request_totalFloors, request_countryName, request_cityName, request_address,
                        request_currentDate, request_expireDate, request_latitude, request_longitude, request_isShowLocation, request_isShowNumber, request_deviceToken, request_key);
                call.enqueue(new Callback<AdPostResponse>() {
                    @Override
                    public void onResponse(Call<AdPostResponse> call, Response<AdPostResponse> response) {

                        if (response.isSuccessful()) {

                            AdPostResponse adPostResponse = response.body();

                            setLoadingButton(1);

                            assert adPostResponse != null;
                            String msg = adPostResponse.getStatus();

                            if (msg.equals(Constants.TRUE)) {
                                SharedPreferences.Editor editor = servicePropertySharedpreferences.edit();
                                editor.clear();
                                editor.apply();

                                deleteTrashImages();

                                SharedPreferences preferences = getContext().getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
                                SharedPreferences.Editor post_data = preferences.edit();

                                post_data.putInt(Constants.ID, adPostResponse.getId());
                                post_data.putString(Constants.SHOP_TITLE, adPostResponse.getTitle());
                                post_data.putString(Constants.EXPIRE, adPostResponse.getExpireDate());
                                post_data.putString(Constants.MAIN_CATEGORY, adPostResponse.getMainCategory());
                                post_data.putString(Constants.KEY, adPostResponse.getKey());
                                post_data.putBoolean(Constants.IS_WAITING, adPostResponse.isWaiting());

                                PostNowPrice price = adPostResponse.getPostNow();
                                post_data.putInt(Constants.POST_NOW_COST_GC, price.getGC());
                                post_data.putInt(Constants.POST_NOW_COST_RC, price.getRC());

                                post_data.apply();

                                OpenNextScreen();

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<AdPostResponse> call, Throwable t) {
                        setLoadingButton(0);
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
        thread.start();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void setLoadingButton(int check) {
        if (check == 1) {
            Bitmap icon = drawableToBitmap(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_done, null));
            btnPost.doneLoadingAnimation(R.color.Theme1, icon);
        } else {
            btnPost.revertAnimation();
            btnPost.setBackgroundResource(R.drawable.button_next);

        }
    }

    private void deleteTrashImages() {

        for (int i = 0; i < trashImages.size(); i++) {

            File trash_file = new File(trashImages.get(i));
            trash_file.delete();

        }
        trashImages.clear();

    }


    public void showDatePickerDialog(String my_current_date, SimpleDateFormat my_date_format)
            throws ParseException {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                Screen4_PostNow.this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));


        //Setting min date selection as today's date
        Date minDateLimit = my_date_format.parse(my_current_date);
        datePickerDialog.getDatePicker().setMinDate(minDateLimit.getTime());

        //Setting max date selection as current date + 30 days.
        Calendar currentCal = Calendar.getInstance();
        currentCal.add(Calendar.DATE, 30);
        String getting_max_date = curFormater.format(currentCal.getTime());

        Date maxDateLimit = my_date_format.parse(getting_max_date);
        datePickerDialog.getDatePicker().setMaxDate(maxDateLimit.getTime());

        //Shwoing date dialog
        datePickerDialog.show();

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        month = month + 1;

        String customized_date = year +"-"+ month + "-" + dayOfMonth ;

        expireDateTextView.setText("Ad Expire On: " + customized_date);

    }

    private void OpenNextScreen() {

        PostAdLastScreen lastScreen = new PostAdLastScreen();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.framelayout_post_task, lastScreen);
        transaction.addToBackStack(null);
        transaction.commit();

    }

}

