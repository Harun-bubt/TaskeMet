package com.AppValley.TaskMet.ProfileSettings;

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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AppValley.TaskMet.GoogleMaps.MapModels.AddressData;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.Registration.Activity3_SignUp;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.ProfileDataModel;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.Status_Response;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;
import com.AppValley.TaskMet.constant.PlacePicker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class EditProfile extends Fragment {

    View view;
    TextView textView_number, location, textView_currency,country;
    EditText editText_name;
    String currency, number, profile_pic, name, countryName, Country_code, CountryName,
            Currency, CityName, fullAddress, shortAddress, Address, mCountryCode;
    double Latitude, Longitude;

    SharedPreferences sharedPreferences;
    TaskMetServer taskMetServer;
    boolean btnIsClicked = false;
    CircleImageView edit_profile_pic;

    Button btn_update_name, btn_update_location;

    String actualFilePath = "";
    Uri uri, cropResultUri;
    public Boolean isImageSelect = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        onBackPressed(view);

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);
        editText_name = view.findViewById(R.id.nameEditText);
        textView_number = view.findViewById(R.id.numberTextView);
        textView_currency = view.findViewById(R.id.currencyEditText);
        country = view.findViewById(R.id.country);
        location = view.findViewById(R.id.location);
        edit_profile_pic = view.findViewById(R.id.edit_profile_pic);


        btn_update_name = view.findViewById(R.id.btn_update_name);
        btn_update_name.setEnabled(false);
        btn_update_name.setBackgroundResource(R.drawable.btn_disable);
        btn_update_name.setPadding(1, 1, 1, 1);

        btn_update_location = view.findViewById(R.id.btn_update_location);
        btn_update_location.setBackgroundResource(R.drawable.btn_ad);
        btn_update_location.setPadding(1, 1, 1, 1);

        //......................getNumber from sharedPreferences..........................
        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);

        name = sharedPreferences.getString(Constants.NAME, "");
        profile_pic = sharedPreferences.getString(Constants.PROFILE_PIC, "");
        name = sharedPreferences.getString(Constants.NAME, "");
        number = sharedPreferences.getString(Constants.NUMBER, "");
        currency = sharedPreferences.getString(Constants.CURRENCY, "");
        CountryName = sharedPreferences.getString(Constants.COUNTRY_NAME, "");
        Address = sharedPreferences.getString(Constants.ADDRESS, "");

        editText_name.setText(name);

        if (CountryName.equals("Pakistan")) {
            Currency = "Rs";
            textView_currency.setText(Currency+" (Pakistani Rupee)");
        } else if (CountryName.equals("Bangladesh")) {
            Currency = "Tk";
            textView_currency.setText(Currency+" (Bangladeshi Taka)");
        } else if (CountryName.equals("India")) {
            Currency = "₹";
            textView_currency.setText(Currency+" (Indian Rupee)");
        }

        location.setText(Address);
        country.setText(CountryName);
        textView_number.setText(number);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo);

        Glide
                .with(getContext())
                .load(profile_pic)
                .apply(options)
                .into(edit_profile_pic);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createLocationRequest();

            }
        });


        btn_update_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createLocationRequest();

            }
        });


        edit_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Ask for permission if storage permission is not enabled
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                    openGalleryIntent.setType("image/*");
                    openGalleryForImage.launch(openGalleryIntent);

                } else {

                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }

            }
        });

        editText_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (editText_name.getText().toString().equals(name) || editText_name.getText().toString().equals("")) {
                    btn_update_name.setEnabled(false);
                    btn_update_name.setBackgroundResource(R.drawable.btn_disable);
                } else {
                    btn_update_name.setEnabled(true);
                    btn_update_name.setBackgroundResource(R.drawable.btn_ad);
                }

            }
        });

        btn_update_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_update_name.setEnabled(false);
                btn_update_name.setBackgroundResource(R.drawable.btn_disable);

                name = editText_name.getText().toString();

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        Call<Status_Response> call = taskMetServer.updateProfileName(number, name);
                        call.enqueue(new Callback<Status_Response>() {
                            @Override
                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                                if (response.isSuccessful()) {

                                    SharedPreferences.Editor again_call_api = sharedPreferences.edit();
                                    again_call_api.putString(Constants.NAME, name);
                                    again_call_api.apply();

                                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setContentText("Profile Name is updated successfully")
                                            .setConfirmText("Ok")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                }
                                            })
                                            .show();

                                }

                            }

                            @Override
                            public void onFailure(Call<Status_Response> call, Throwable t) {

                                btn_update_name.setEnabled(true);
                                btn_update_name.setBackgroundResource(R.drawable.btn_ad);

                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("An error occur while updating Profile Name. Please try again")
                                        .setConfirmText("Ok")
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
                });
                thread.start();
            }
        });

        btn_update_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createLocationRequest();

            }
        });

    }

    ActivityResultLauncher<Intent> openGalleryForImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    //Upload image from gallery
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();

                        uri = data.getData();
                        stratCrop(uri);

                    }

                }
            });


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Upload image from gallery
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {

            assert data != null;
            cropResultUri = UCrop.getOutput(data);

            try {


                actualFilePath = getRealPath(cropResultUri, getActivity());
                assert actualFilePath != null;
                File img = new File(actualFilePath);

                Bitmap bitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
                edit_profile_pic.setImageBitmap(bitmap);

                RequestBody mFile = RequestBody.create(img, MediaType.parse("image/*"));
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profile_pic", img.getName(), mFile);

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        Call<Status_Response> call = taskMetServer.updateProfilePicture(number, fileToUpload);
                        call.enqueue(new Callback<Status_Response>() {
                            @Override
                            public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                                if (response.isSuccessful()) {

                                    SharedPreferences.Editor again_call_api = sharedPreferences.edit();
                                    again_call_api.putString(Constants.CALL_API, Constants.TRUE);
                                    again_call_api.apply();

                                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setContentText("Your profile picture is updated successfully")
                                            .setConfirmText("Ok")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                }
                                            })
                                            .show();

                                }

                            }

                            @Override
                            public void onFailure(Call<Status_Response> call, Throwable t) {

                                btn_update_name.setEnabled(true);
                                btn_update_name.setBackgroundResource(R.drawable.btn_ad);

                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("An error occur while updating profile picture. Please try again")
                                        .setConfirmText("Ok")
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
                });
                thread.start();


            } catch (Exception e) {
                actualFilePath = null;
                isImageSelect = false;
            }

        }


    }



    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                    Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                    openGalleryIntent.setType("image/*");
                    openGalleryForImage.launch(openGalleryIntent);

                }
                else {

                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setContentText("If you don't grant Storage permission, you Won't be able to upload your image!")
                            .setConfirmText("Ok")
                            .show();

                }
            });


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


    private void stratCrop(@NotNull Uri uri) {

        Random rand = new Random();
        int n = rand.nextInt(100000);
        int p = rand.nextInt(100000);

        String destinationFileName = String.valueOf(n) + "user_image" + String.valueOf(p);
        destinationFileName += ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File((getContext().getCacheDir()), destinationFileName)));

        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(480, 480);
        uCrop.start(getActivity());

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


                            Geocoder geoCoder = new Geocoder(getContext(), Locale.getDefault());
                            try {
                                List<Address> addresses = geoCoder.getFromLocation(Latitude, Longitude, 1);

                                address = addresses;

                                CountryName = address.get(0).getCountryName();

                                if (CountryName.equals("Pakistan") || CountryName.equals("India") || CountryName.equals("Bangladesh")) {


                                    if (addresses != null && addresses.size() != 0) {

                                        fullAddress = addresses.get(0).getAddressLine(0);
                                        shortAddress = generateFinalAddress(fullAddress).trim();
                                        mCountryCode = addresses.get(0).getCountryCode();
                                        location.setError(null);
                                        location.setText(fullAddress);

                                        CityName = address.get(0).getLocality();
                                        Address = address.get(0).getAddressLine(0);
                                        Country_code = address.get(0).getCountryCode();

                                        if (CountryName.equals("Pakistan")) {
                                            Currency = "Rs";
                                            textView_currency.setText(Currency+" (Pakistani Rupee)");
                                        } else if (CountryName.equals("Bangladesh")) {
                                            Currency = "Tk";
                                            textView_currency.setText(Currency+" (Bangladeshi Taka)");
                                        } else if (CountryName.equals("India")) {
                                            Currency = "₹";
                                            textView_currency.setText(Currency+" (Indian Rupee)");
                                        }

                                        Log.d("AdNewLocation",  CountryName + "\n" + CityName + "\n" + Country_code + "\n" + Address + "\n" + String.valueOf(Latitude) + "\n" + String.valueOf(Longitude)+"\n"+Currency);

                                        country.setText(CountryName);


                                        Thread thread = new Thread(new Runnable() {

                                            @Override
                                            public void run() {

                                                Call<Status_Response> call = taskMetServer.updateProfileLocation(number, CountryName, CityName, Country_code, Address, String.valueOf(Latitude), String.valueOf(Longitude),Currency);
                                                call.enqueue(new Callback<Status_Response>() {
                                                    @Override
                                                    public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                                                        if (response.isSuccessful()) {

                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString(Constants.COUNTRY_NAME, CountryName);
                                                            editor.putString(Constants.CITY_NAME, CityName);
                                                            editor.putString(Constants.COUNTRY_CODE, Country_code);
                                                            editor.putString(Constants.ADDRESS, Address);
                                                            editor.putString(Constants.CURRENCY, currency);
                                                            editor.putString(Constants.LATITUDE, String.valueOf(Latitude));
                                                            editor.putString(Constants.LONGITUDE, String.valueOf(Longitude));
                                                            editor.putString(Constants.CALL_API, Constants.TRUE);

                                                            editor.apply();

                                                            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                                    .setContentText("Your location is updated successfully")
                                                                    .setConfirmText("Ok")
                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                        @Override
                                                                        public void onClick(SweetAlertDialog sDialog) {
                                                                            sDialog.dismissWithAnimation();
                                                                        }
                                                                    })
                                                                    .show();

                                                        }

                                                    }

                                                    @Override
                                                    public void onFailure(Call<Status_Response> call, Throwable t) {

                                                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                                .setContentText("Something went wrong while updating your location. Please try again.")
                                                                .setConfirmText("Ok")
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
                                        });
                                        thread.start();


                                    } else {

                                        shortAddress = "";
                                        fullAddress = "";
                                    }

                                }
                                else {

                                    location.setText("");
                                    location.setHint(getResources().getString(R.string.select_location));

                                    Latitude = 0.0;
                                    Longitude = 0.0;

                                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
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

                            }
                            catch (Exception e) {
                                shortAddress = "";
                                fullAddress = "";
                                address = null;
                                e.printStackTrace();
                            }

                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
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


    //To enable & disable gps
    protected void createLocationRequest() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(getContext());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                openGoogleMaps();

            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {

                    try {

                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(getActivity(),
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
                .build(getActivity());
        openMapsForLocation.launch(intent);
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


}