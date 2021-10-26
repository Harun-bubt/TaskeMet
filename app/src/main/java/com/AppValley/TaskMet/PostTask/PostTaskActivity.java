package com.AppValley.TaskMet.PostTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.AppValley.TaskMet.Home.HomeFragments.ViewMyPostDetails;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.MyPostModel;
import com.AppValley.TaskMet.constant.Constants;

public class PostTaskActivity extends AppCompatActivity {

    String action="none",type = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_task);


        boolean shop = getIntent().getBooleanExtra("shop",false);
        boolean common_service = getIntent().getBooleanExtra("service",false);
        boolean job = getIntent().getBooleanExtra("job",false);
        boolean mariage = getIntent().getBooleanExtra("mariage",false);
        boolean property = getIntent().getBooleanExtra("property",false);
        boolean edit = getIntent().getBooleanExtra(Constants.EDIT_POST,false);
        boolean viewMyPost = getIntent().getBooleanExtra(Constants.VIEW_AD,false);

        String id = getIntent().getStringExtra(Constants.ID);
        String key = getIntent().getStringExtra(Constants.KEY);
        action = getIntent().getStringExtra(Constants.POST_ACTION);
        type = getIntent().getStringExtra(Constants.IS_PREMIUM);

        if(viewMyPost)
        {
            ViewPostFragment(id,key,action,type);
        }
        else if(edit)
        {
            EditPostFragment(id,key);
        }
        else if(shop){

            shoping_Fragment();

        }else if(common_service)
        {
            commonFragment();

        }else if(job)
        {
            jobFragment();

        }else if(mariage){
            mariageFragment();
        }
        else if(property)
        {
            propertyFragment();
        }
       // propertyFragment();


    }
    public void shoping_Fragment()
    {
        Screen2_ShoppingDetails screen2_shoppingDetails = new Screen2_ShoppingDetails();
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_post_task, screen2_shoppingDetails);
        transaction.addToBackStack("tag");
        transaction.commit();
    }
    public void jobFragment()
    {
        Screen2_Jobdetails screen2_jobdetails = new Screen2_Jobdetails();
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_post_task, screen2_jobdetails);
        transaction.addToBackStack("tag");
        transaction.commit();
    }
    public void commonFragment()
    {
        Screen2_CommonScreen screen2_commonScreen = new Screen2_CommonScreen();
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_post_task, screen2_commonScreen);
        transaction.addToBackStack("tag");
        transaction.commit();
    }
    public void mariageFragment()
    {
        Screen2_Matrimonial screen2_matrimonial = new Screen2_Matrimonial();
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_post_task, screen2_matrimonial);
        transaction.addToBackStack("tag");
        transaction.commit();
    }
    public void propertyFragment()
    {
        Screen2_Property screen2_property = new Screen2_Property();
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_post_task, screen2_property);
        transaction.addToBackStack("tag");
        transaction.commit();
    }
    public void Screen4Fragment()
    {
        Screen4_PostNow screen4_postNow = new Screen4_PostNow();
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_post_task, screen4_postNow);
        transaction.addToBackStack("tag");
        transaction.commit();
    }
    public void EditPostFragment(String id,String key)
    {
        Edit_Post edit_post = new Edit_Post(id,key);
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_post_task, edit_post);
        transaction.addToBackStack("tag");
        transaction.commit();
    }
    public void ViewPostFragment(String id, String key,String ac, String ad_type)
    {
        ViewMyPostDetails viewMyPostDetails = new ViewMyPostDetails(id,key,ac,ad_type);
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_post_task, viewMyPostDetails);
        transaction.addToBackStack("tag");
        transaction.commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onBackPressed() {
        this.finish();
    }
}