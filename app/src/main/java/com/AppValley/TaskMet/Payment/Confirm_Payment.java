package com.AppValley.TaskMet.Payment;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AppValley.TaskMet.Home.FragmentViewActivity;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.Status_Response;
import com.AppValley.TaskMet.TaskMetWebService.TaskMetServer;
import com.AppValley.TaskMet.constant.Constants;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CLIPBOARD_SERVICE;


public class Confirm_Payment extends Fragment {

    View view;

    EditText tid;
    File img;
    int quantity,cost;
    ImageView coin_icon,save_number;
    TextView detail_info,coin_amount,cost_amount;
    Button btn_screen_shot,btn_confirm_transaction;
    String coin_type,TAG = "confirm_payment",currency,country,image_tid,number
            ,current_date,deviceToken,account_title,account_number;

    TaskMetServer taskMetServer;
    SharedPreferences sharedPreferences;

    String actualFilePath = "";
    Uri uri, cropResultUri;
    public Boolean isImageSelect = false;

    public Confirm_Payment(String coin_type, int quantity, int cost) {
        this.coin_type = coin_type;
        this.quantity = quantity;
        this.cost = cost;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.confrim_payment, container, false);

        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calender = Calendar.getInstance();
        current_date = date_format.format(calender.getTime());

        taskMetServer = TaskMetServer.retrofit.create(TaskMetServer.class);
        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        number = sharedPreferences.getString(Constants.NUMBER, "");
        deviceToken = sharedPreferences.getString(Constants.TOKEN, "");
        country = sharedPreferences.getString(Constants.COUNTRY_NAME, "");

        detail_info = view.findViewById(R.id.detail_info);
        tid = view.findViewById(R.id.tid);
        save_number = view.findViewById(R.id.save_number);
        coin_icon = view.findViewById(R.id.coin_icon);
        coin_amount = view.findViewById(R.id.coin_amount);
        cost_amount = view.findViewById(R.id.cost_amount);
        btn_screen_shot = view.findViewById(R.id.btn_screen_shot);
        btn_confirm_transaction = view.findViewById(R.id.btn_confirm_transaction);

        detail_info.setText(getString(R.string.screenshot_verify1)+" "+String.valueOf(quantity)+" "+coin_type+" "+getString(R.string.screenshot_verify2));

        switch (country) {
            case "Pakistan":
                currency = "Rs";
                account_title = "Waheed";
                account_number = "+923058971088";
                break;
            case "Bangladesh":
                account_title = "Harun";
                account_number = "+8801789266565";
                currency = "Tk";
                break;
            case "India":
                currency = "₹";
                account_title = "Waheed";
                account_number = "+923058971088";
                break;
        }

        coin_amount.setText("Total "+coin_type+" Coins: "+String.valueOf(quantity));
        cost_amount.setText("Total Cost: "+ currency +" "+String.valueOf(cost)+"/-");

        if(coin_type.equals("GC")){
            coin_icon.setImageResource(R.drawable.icon_gc_single);
        }
        else{
            coin_icon.setImageResource(R.drawable.icon_rc_single);
        }

         return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        btn_screen_shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenGallery();

            }
        });

        btn_confirm_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_tid = tid.getText().toString().trim();

                if(!TextUtils.isEmpty(image_tid)){

                    if(isImageSelect) {

                        try {


                            actualFilePath = getRealPath(cropResultUri, getActivity());
                            assert actualFilePath != null;
                            img = new File(actualFilePath);

                            int rc, gc;

                            if(coin_type.equals("RC")){

                                rc = quantity;
                                gc = 0;
                            }
                            else {
                                rc = 0;
                                gc = quantity;
                            }

                            Log.d(TAG, "\nnumber: "+number+
                                            "\nrc: "+rc+
                                            "\ngc: "+gc+
                                            "\nimage_tid: "+image_tid+
                                            "\ncost: "+cost+
                                            "\ncurrent_date: "+current_date+
                                            "\ndeviceToken: "+deviceToken+
                                            "\naccount_title: "+account_title+
                                            "\naccount_number: "+account_number);

                            RequestBody mFile = RequestBody.create(img, MediaType.parse("image/*"));
                            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", img.getName(), mFile);
                            RequestBody my_number = RequestBody.create(number, MediaType.parse("text/plan"));
                            RequestBody my_rc = RequestBody.create(String.valueOf(rc), MediaType.parse("text/plan"));
                            RequestBody my_gc = RequestBody.create(String.valueOf(gc), MediaType.parse("text/plan"));
                            RequestBody my_tid = RequestBody.create(image_tid, MediaType.parse("text/plan"));
                            RequestBody my_cost = RequestBody.create(String.valueOf(cost), MediaType.parse("text/plan"));
                            RequestBody my_date = RequestBody.create(current_date, MediaType.parse("text/plan"));
                            RequestBody my_deviceToken = RequestBody.create(deviceToken, MediaType.parse("text/plan"));
                            RequestBody my_account_title = RequestBody.create(account_title, MediaType.parse("text/plan"));
                            RequestBody my_account_number = RequestBody.create(account_number, MediaType.parse("text/plan"));


                            Thread thread = new Thread(new Runnable() {

                                @Override
                                public void run() {

                                    Call<Status_Response> call = taskMetServer.uploadTidScreenShot(fileToUpload,my_number,my_rc,my_gc,my_tid, my_cost,my_date,my_deviceToken,my_account_title,my_account_number);
                                    call.enqueue(new Callback<Status_Response>() {
                                        @Override
                                        public void onResponse(Call<Status_Response> call, Response<Status_Response> response) {

                                            if(response.isSuccessful()){

                                                Status_Response status_response = response.body();

                                                String msg = status_response.getStatus();

                                                if(msg.equals(Constants.TRUE)){

                                                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                            .setContentText("TaskMet received your TID Screenshot. We'll update your "+coin_type+" status within few minutes.")
                                                            .setConfirmText("Ok")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                    sweetAlertDialog.dismissWithAnimation();
                                                                    Intent intent = new Intent(getContext(), FragmentViewActivity.class);

                                                                    SharedPreferences preferences = getContext().getSharedPreferences(Constants.PREMIUM_POST_DATA, Context.MODE_PRIVATE);
                                                                    SharedPreferences.Editor direction = preferences.edit();
                                                                    direction.putString(Constants.FRAGMENT_NAME, "WalletFragment");
                                                                    direction.apply();

                                                                    startActivity(intent);

                                                                    getActivity().finish();

                                                                }
                                                            })
                                                            .show();

                                                }
                                                else {

                                                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                            .setContentText("Screenshot uploading failed. Please try again.")
                                                            .setConfirmText("Ok")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                    sweetAlertDialog.dismissWithAnimation();

                                                                }
                                                            })
                                                            .show();

                                                }

                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<Status_Response> call, Throwable t) {

                                            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                                    .setContentText("An error occur while uploading transaction ScreenShot. Please try again or contact us")
                                                    .setConfirmText("Ok")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                        }
                                                    })
                                                    .setCancelText("Contact us")
                                                    .showCancelButton(true)
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();

                                                            Intent intent = new Intent(Intent.ACTION_DIAL);
                                                            intent.setData(Uri.parse("tel:"+number));
                                                            startActivity(intent);

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
                    else {

                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Please upload screenshot of transaction first")
                                .setConfirmText("Upload Image")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        OpenGallery();

                                    }
                                })
                                .show();

                    }

                }
                else {

                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Please Enter TID number in above box.")
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
        });

        save_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Account Number", "03058971088");
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getContext(), "Account number copied", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void OpenGallery() {

        //Ask for permission if storage permission is not enabled
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
            openGalleryIntent.setType("image/*");
            openGalleryForImage.launch(openGalleryIntent);

        } else {

            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

        }

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

                        uri = data.getData();
                        stratCrop(uri);


                    }

                }
            });


    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                    Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                    openGalleryIntent.setType("image/*");
                    openGalleryForImage.launch(openGalleryIntent);

                } else {

                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setContentText("If you don't grant Storage permission, you Won't be able to upload your image!")
                            .setConfirmText("Ok")
                            .show();

                }
            });

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



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Upload image from gallery
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {

            assert data != null;
            cropResultUri = UCrop.getOutput(data);

            if(cropResultUri != null){

                btn_confirm_transaction.setBackgroundResource(R.drawable.btn_ad);
                isImageSelect = true;

            }

        }


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


}