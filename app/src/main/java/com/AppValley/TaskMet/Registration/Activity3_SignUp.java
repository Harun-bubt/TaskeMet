package com.AppValley.TaskMet.Registration;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AppValley.TaskMet.Chat_Module.User;
import com.AppValley.TaskMet.constant.Constants;
import com.AppValley.TaskMet.GoogleMaps.MapModels.AddressData;
import com.AppValley.TaskMet.Home.HomeScreen;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.Status_Response;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.PlacePicker;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
Activity3_SignUp extends AppCompatActivity {

    SweetAlertDialog progress;

    public Boolean isImageSelect = false;

    private static final String TAG = "sign_up_activity";
    final Context context = this;
    String number, Uid, CityName, CountryName, Address, Country_code, Currency,email,name;
    double Latitude = 0, Longitude = 0;

    String mCountryCode, fullAddress, shortAddress;

    TaskMetServer taskMetServer;
    public static SharedPreferences sharedPreferences;

    CircleImageView myImage;
    ImageView backArrow;
    TextView myNumber, myLocation;
    EditText myName, myEmail;
    Button btnSignUp;

    String filePath = "", actualFilePath = "";
    Uri uri, cropResultUri;

    FirebaseAuth firebaseAuth;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3_sign_up);

        progress = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progress.setCancelable(true);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        Uid = firebaseAuth.getUid();

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);

        //------------------------------------ Store use get Data ----------------------------------
        sharedPreferences = getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);

        number = sharedPreferences.getString(Constants.NUMBER, Constants.NULL);
        //------------------------------------------------------------------------------------------

        myImage = findViewById(R.id.myImage);
        myNumber = findViewById(R.id.myNumber);
        myName = findViewById(R.id.myName);
        myEmail = findViewById(R.id.myEmail);
        //   myCurrency = findViewById(R.id.myCurrency);
        myLocation = findViewById(R.id.myLocation);
        btnSignUp = findViewById(R.id.btnSignUp);
        backArrow = findViewById(R.id.backArrow);

        myNumber.setText(number);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Activity2_Registration.class));
                finish();
            }
        });

  /*      myCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickCurrency();
            }
        });*/


        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLocationRequest();

            }
        });


        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Ask for permission if storage permission is not enabled
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                    openGalleryIntent.setType("image/*");
                    openGalleryForImage.launch(openGalleryIntent);

                } else {

                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

                }

            }
        });



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number = myNumber.getText().toString();
                email = myEmail.getText().toString();
                name = myName.getText().toString();

                double latitude = Latitude;
                double longitude = Longitude;

                if (TextUtils.isEmpty(number)) {
                    myNumber.setError("Please enter number");
                    myNumber.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    myName.setError("Please enter name");
                    myName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    myEmail.setError("Please enter email");
                    myEmail.requestFocus();
                    return;
                }

                if (latitude == 0.0 || longitude == 0.0) {
                    myLocation.setError("select Location");
                    myLocation.requestFocus();
                    return;
                }

                if (isImageSelect) {

                    uploadImage();

                }
                else {

                    myImage.requestFocus();
                    Toast.makeText(getApplicationContext(), "Please upload your Image", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


   /* private void pickCurrency() {

        CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");
        picker.setListener(new CurrencyPickerListener() {
            @Override
            public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {

                Currency = symbol;
                myCurrency.setError(null);
                myCurrency.setText(symbol + " (" + name + ")");
                picker.dismiss();

            }
        });

        picker.show(getSupportFragmentManager(), "Select Currency");

    }*/

    //  Image upload
    public void uploadImage() {

        filePath = getRealPath(cropResultUri, Activity3_SignUp.this);

        File file = new File(filePath);

        progress.setTitleText("Creating User");
        progress.show();

        RequestBody mFile = RequestBody.create(file, MediaType.parse("image/*"));
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profile_pic", file.getName(), mFile);
        RequestBody my_number = RequestBody.create(number, MediaType.parse("text/plan"));
        RequestBody my_uid = RequestBody.create(Uid, MediaType.parse("text/plan"));
        RequestBody my_name = RequestBody.create(name, MediaType.parse("text/plan"));
        RequestBody my_email = RequestBody.create(email, MediaType.parse("text/plan"));
        RequestBody my_country = RequestBody.create(CountryName, MediaType.parse("text/plan"));
        RequestBody my_country_code = RequestBody.create(Country_code, MediaType.parse("text/plan"));
        RequestBody my_shortAddress = RequestBody.create(shortAddress, MediaType.parse("text/plan"));
        RequestBody my_address = RequestBody.create(Address, MediaType.parse("text/plan"));
        RequestBody my_currency = RequestBody.create(Currency,MediaType.parse("text/plan"));
        RequestBody my_latitude = RequestBody.create(String.valueOf(Latitude), MediaType.parse("text/plan"));
        RequestBody my_longitude = RequestBody.create(String.valueOf(Longitude), MediaType.parse("text/plan"));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Call<Status_Response> call = taskMetServer.createUser(fileToUpload, my_number, my_uid, my_name, my_email, my_country, my_shortAddress, my_country_code, my_address, my_currency, my_latitude, my_longitude);
                call.enqueue(new Callback<Status_Response>() {
                    @Override
                    public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                        if (response.isSuccessful()) {

                            Status_Response response1 = response.body();

                            String response_msg = response1.getStatus();

                            Log.d(TAG, response.code() + "    " + response_msg);

                            progress.setTitleText("Creating Database");

                            if (response_msg.equals(Constants.TRUE)) {

                                //.............storage data at db.............................................................
                                StorageReference reference = storage.getReference().child("Profiles").child(auth.getUid());
                                reference.putFile(cropResultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String imageUrl = uri.toString();

                                                    String uid = auth.getUid();
                                                    String phone = auth.getCurrentUser().getPhoneNumber();
                                                    String name = myName.getText().toString();

                                                    User user = new User(uid, name, phone, imageUrl);

                                                    database.getReference()
                                                            .child("users")
                                                            .child(uid)
                                                            .setValue(user)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    progress.dismissWithAnimation();

                                                                    SharedPreferences.Editor user_detail_editor = sharedPreferences.edit();
                                                                    user_detail_editor.putString(Constants.CALL_API, Constants.TRUE);
                                                                    user_detail_editor.apply();

                                                                    Intent intent = new Intent(new Intent(context, HomeScreen.class));
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivity(intent);
                                                                    finish();

                                                                }
                                                            })

                                                   /* .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                            new SweetAlertDialog(Activity3_SignUp.this, SweetAlertDialog.ERROR_TYPE)
                                                                    .setContentText("Your profile is not Created Successfully. Please try again.")
                                                                    .show();

                                                        }
                                                    })*/;

                                                }
                                            });
                                        }
                                    }
                                });

                                //......................................................................


                            }


                        }

                    }

                    @Override
                    public void onFailure(Call<Status_Response> call, Throwable t) {

                        Toast.makeText(context, "Request Timeout", Toast.LENGTH_SHORT).show();

                        new SweetAlertDialog(Activity3_SignUp.this, SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Your profile is not Created Successfully. Please try again.")
                                .show();


                    }
                });

            }
        });
        thread.start();


    }

    //To enable & disable gps
    protected void createLocationRequest() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                openGoogleMaps();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {

                    try {

                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(Activity3_SignUp.this,
                                Constants.REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {

                    }
                }
            }
        });
    }

    public void openGoogleMaps() {

        Intent intent = new PlacePicker.IntentBuilder()
                .setGoogleMapApiKey(getResources().getString(R.string.map_api_key))
                .setLatLong(33.6844, 73.0479)
                .setMapZoom(14.0f)
                .setAddressRequired(true)
                .setPrimaryTextColor(R.color.black)
                .build(Activity3_SignUp.this);
        openMapsForLocation.launch(intent);

    }

    private void stratCrop(@NotNull Uri uri) {

        Random rand = new Random();
        int n = rand.nextInt(100000);
        int p = rand.nextInt(100000);

        String destinationFileName = String.valueOf(n) + "user_image" + String.valueOf(p);
        destinationFileName += ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File((context.getCacheDir()), destinationFileName)));

        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(480, 480);
        uCrop.start(Activity3_SignUp.this);

    }


    private String getRealPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    private String generateFinalAddress(String address) {
        String strRtr;
        String[] s = address.split(",");
        if (s.length >= 3)
            strRtr = s[1] + "," + s[2];
        else if (s.length == 2)
            strRtr = s[1];
        else
            strRtr = s[0];
        return strRtr;

    }


    ActivityResultLauncher<Intent> openGalleryForImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    //Upload image from gallery
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();

                        assert data != null;
                        uri = data.getData();
                        stratCrop(uri);

                    }

                }
            });


    ActivityResultLauncher<Intent> openMapsForLocation = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    //Getting location from Google Map
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();

                        List<Address> address = new ArrayList<>();

                        if (data != null) {
                            AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);

                            //Decimal format to round value up to 4 decimal points
                            DecimalFormat dFormat = new DecimalFormat("#.#####");

                            //Rounding decimal values of lat & long
                            Latitude = addressData.getLatitude();
                            Latitude = Double.parseDouble(dFormat.format(Latitude));

                            Longitude = addressData.getLongitude();
                            Longitude = Double.parseDouble(dFormat.format(Longitude));


                            Geocoder geoCoder = new Geocoder(Activity3_SignUp.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geoCoder.getFromLocation(Latitude, Longitude, 1);

                                address = addresses;

                                CountryName = address.get(0).getCountryName();

                                if (CountryName.equals("Pakistan") || CountryName.equals("India") || CountryName.equals("Bangladesh")) {


                                    if (addresses != null && addresses.size() != 0) {

                                        fullAddress = addresses.get(0).getAddressLine(0);
                                        shortAddress = generateFinalAddress(fullAddress).trim();
                                        mCountryCode = addresses.get(0).getCountryCode();
                                        myLocation.setError(null);
                                        myLocation.setText(fullAddress);

                                        CityName = address.get(0).getLocality();
                                        Address = address.get(0).getAddressLine(0);
                                        Country_code = address.get(0).getCountryCode();

                                        if(CountryName.equals("Pakistan")){
                                            Currency = "Rs";
                                        }
                                        else if(CountryName.equals("Bangladesh")){
                                            Currency = "Tk";
                                        }
                                        else if(CountryName.equals("India")){
                                            Currency = "₹";
                                        }

                                        Log.d(TAG, "Country: "+CountryName+
                                                "\nCurrency: "+Currency);


                                    }
                                    else {

                                        shortAddress = "";
                                        fullAddress = "";
                                    }

                                }
                                else{

                                    myLocation.setText("");
                                    myLocation.setHint(getResources().getString(R.string.select_location));

                                    Latitude = 0.0;
                                    Longitude = 0.0;
                                    Currency = "";

                                    new SweetAlertDialog(Activity3_SignUp.this, SweetAlertDialog.WARNING_TYPE)
                                            .setContentText("TaskMet Service is only available in Bangladesh, Pakistan and India countries.")
                                            .setConfirmText("Ok")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                }
                                            })
                                            .show();
                                }

                            } catch (Exception e) {
                                shortAddress = "";
                                fullAddress = "";
                                address = null;
                                e.printStackTrace();
                            }

                            Geocoder geocoder = new Geocoder(Activity3_SignUp.this, Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(Latitude, Longitude, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                }
            });


    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                    Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();
                    Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                    openGalleryIntent.setType("image/*");
                    openGalleryForImage.launch(openGalleryIntent);

                } else {

                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setContentText("If you don't grant Storage permission, you Won't be able to upload your image!")
                            .setConfirmText("Ok")
                            .show();

                }
            });

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Setting Cropped image in ImageView
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {

            assert data != null;
            cropResultUri = UCrop.getOutput(data);

            try {


                actualFilePath = getRealPath(cropResultUri, Activity3_SignUp.this);
                assert actualFilePath != null;
                File img = new File(actualFilePath);

                Bitmap bitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
                myImage.setImageBitmap(bitmap);
                isImageSelect = true;

            } catch (Exception e) {
                actualFilePath = null;
                isImageSelect = false;
            }

        }

        //For gps setting
        if (requestCode == Constants.REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {
                openGoogleMaps();

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "Need GPS",
                        Toast.LENGTH_SHORT).show();
                // in case user back press or refuses to open gps
                Log.d("result cancelled", data.toString());
            }
        }

    }


    @Override
    public void onBackPressed() {

        startActivity(new Intent(context, Activity2_Registration.class));
        finish();

    }


}