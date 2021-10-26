package com.AppValley.TaskMet.Registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.AppValley.TaskMet.R;

public class Activity2_Registration extends AppCompatActivity {

    Button phoneButton;
    public static String phoneNumber;

    int count = 0;
    int imgArr[] = new int[]{
            R.drawable.bg_img_2,
            R.drawable.bg_img_3,
            R.drawable.bg_img_4,
            R.drawable.bg_img_5,
            R.drawable.bg_img_6,
            R.drawable.bg_img_7
    };

    ViewFlipper mViewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Background images
        mViewFlipper = findViewById(R.id.flipper);

        for(int i=0 ; i<imgArr.length ; i++)
        {
            //  This will create dynamic image view and add them to ViewFlipper
            setFlipperImage(imgArr[i]);
        }

        //set Default Fragment for FramLayout in Registration Activity
        defaultFragmentSet();

        // Set Background  image Animation
        backGroundAnimation();

    }

    private void setFlipperImage(int res) {

        ImageView image = new ImageView(getApplicationContext());
        image.setBackgroundResource(res);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        mViewFlipper.addView(image);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void defaultFragmentSet()
    {

        Fragment1_SignIn signInFragment = new Fragment1_SignIn();
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, signInFragment);
        transaction.addToBackStack("tag");
        transaction.commit();

    }

    public void backGroundAnimation(){
        Animation fadeIn = AnimationUtils.loadAnimation(Activity2_Registration.this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(Activity2_Registration.this, R.anim.fade_out);

        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(10000);
        mViewFlipper.setInAnimation(fadeIn);
        mViewFlipper.setOutAnimation(fadeOut);
        mViewFlipper.startFlipping();

    }

    public void VerifyClicked(View view)
    {
       /* Intent intent = new Intent(this, SetupProfileActivity.class);
        startActivity(intent);
        finish();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewFlipper.stopFlipping();
    }
}