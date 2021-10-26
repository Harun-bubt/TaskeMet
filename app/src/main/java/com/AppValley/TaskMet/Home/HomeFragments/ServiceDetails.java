package com.AppValley.TaskMet.Home.HomeFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AppValley.TaskMet.Home.Adopters.ShopAdopter;
import com.AppValley.TaskMet.Home.DetailsClass.ShoppingServiceInfo;
import com.AppValley.TaskMet.ViewAdActivities.Screen1_Common_Ads_Activity;
import com.AppValley.TaskMet.ViewAdActivities.Screen2_Shopping_Ads_Activity;
import com.AppValley.TaskMet.ViewAdActivities.Screen3_Job_Ads_Activity;
import com.AppValley.TaskMet.ViewAdActivities.Screen4_Marriage_Ads_Activity;
import com.AppValley.TaskMet.ViewAdActivities.Screen5_Property_Ads_Activity;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.constant.Constants;

import java.util.ArrayList;

public class ServiceDetails extends Fragment {

    View view;
    RecyclerView recyclerView;
    ShopAdopter shopAdopter;
    ArrayList<ShoppingServiceInfo> serviceList = new ArrayList<>();
    ImageView backArrow;
    ListView listView;

    ArrayAdapter<String> adapter;

    String[] marriageSubCategory = {"Bride", "Groom"};

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isJobSelected = false;
    boolean isJMarriageSelected = false;
    boolean isCommonSelected = false;
    boolean isPropertySelected = false;

    AnimationSet set = new AnimationSet(true);
    Animation animation = new AlphaAnimation(0.0f, 1.0f);
    LayoutAnimationController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service_details, container, false);

        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        animation.setDuration(200);
        set.addAnimation(animation);

        recyclerView = view.findViewById(R.id.shoppingItemRecyclerView);
        backArrow = view.findViewById(R.id.backArrowService);
        listView = view.findViewById(R.id.service_listView);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        serviceDataPrepare();

        shopAdopter = new ShopAdopter(getContext(), serviceList, new ShopAdopter.OnCategorySelection() {
            @Override
            public void onItemClicked(ShoppingServiceInfo model) {

                categorySelection(model.getId());

            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(shopAdopter);

        controller = new LayoutAnimationController(set, 0.3f);

        adapter = new ArrayAdapter<String>(getContext(), R.layout.my_list_style, getResources().getStringArray(R.array.jobcategory));
        isJobSelected = true;
        listView.setAdapter(adapter);
        editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.jobs));
        editor.apply();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(isJMarriageSelected)
                {
                    editor.putString(Constants.SELECTED_LOOKING_FOR, parent.getAdapter().getItem(position).toString());
                    editor.apply();
                    startActivity(new Intent(getActivity(), Screen4_Marriage_Ads_Activity.class));

                }
                else if(isJobSelected) {

                    editor.putString(Constants.SELECTED_JOB_CATEGORY, parent.getAdapter().getItem(position).toString());
                    editor.apply();
                    startActivity(new Intent(getActivity(), Screen3_Job_Ads_Activity.class));

                }
                else if(isPropertySelected) {

                    editor.putString(Constants.SELECTED_PROPERTY_TYPE, parent.getAdapter().getItem(position).toString());
                    editor.apply();
                    startActivity(new Intent(getActivity(), Screen5_Property_Ads_Activity.class));

                }

            }
        });

        return view;

    }

    private void categorySelection(int id) {

        switch (id) {

            case 0:
                adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.my_list_style, getResources().getStringArray(R.array.jobcategory));
                isJobSelected = true;
                isJMarriageSelected = false;
                isPropertySelected = false;
                isCommonSelected = false;
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.jobs));
                editor.apply();

                listView.setLayoutAnimation(controller);

                break;

            case 1:
                adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.my_list_style, getResources().getStringArray(R.array.propertycategory));
                isPropertySelected = true;
                isJobSelected = false;
                isJMarriageSelected = false;
                isCommonSelected = false;
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.property));
                editor.apply();

                listView.setLayoutAnimation(controller);

                break;

            case 2:
                isJMarriageSelected = true;
                isJobSelected = false;
                isPropertySelected = false;
                isCommonSelected = false;
                adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.my_list_style, marriageSubCategory);
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.marriage));
                editor.apply();

                listView.setLayoutAnimation(controller);

                break;

            case 3:
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.travel));
                editor.apply();
                startActivity(new Intent(getActivity(), Screen1_Common_Ads_Activity.class));
                break;
            case 4:
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.education));
                editor.apply();
                startActivity(new Intent(getActivity(), Screen1_Common_Ads_Activity.class));
                break;
            case 5:
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.development));
                editor.apply();
                startActivity(new Intent(getActivity(), Screen1_Common_Ads_Activity.class));
                break;
            case 6:
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.electronics));
                editor.apply();
                startActivity(new Intent(getActivity(), Screen1_Common_Ads_Activity.class));
                break;
            case 7:
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.home_delivery));
                editor.apply();
                startActivity(new Intent(getActivity(), Screen1_Common_Ads_Activity.class));
                break;
            case 8:
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.car_rental));
                editor.apply();
                startActivity(new Intent(getActivity(), Screen1_Common_Ads_Activity.class));
                break;
            case 9:
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.events));
                editor.apply();
                startActivity(new Intent(getActivity(), Screen1_Common_Ads_Activity.class));
                break;
            case 10:
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.health));
                editor.apply();
                startActivity(new Intent(getActivity(), Screen1_Common_Ads_Activity.class));
                break;
            case 11:
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.catering));
                editor.apply();
                startActivity(new Intent(getActivity(), Screen1_Common_Ads_Activity.class));
                break;
            case 12:
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.house_office_renovation));
                editor.apply();
                startActivity(new Intent(getActivity(), Screen1_Common_Ads_Activity.class));
                break;


        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isJMarriageSelected)
                {
                    editor.putString(Constants.SELECTED_LOOKING_FOR, parent.getAdapter().getItem(position).toString());
                    editor.apply();
                    startActivity(new Intent(getActivity(), Screen4_Marriage_Ads_Activity.class));

                }else if(isJobSelected)
                {
                    editor.putString(Constants.SELECTED_JOB_CATEGORY, parent.getAdapter().getItem(position).toString());
                    editor.apply();
                    startActivity(new Intent(getActivity(), Screen3_Job_Ads_Activity.class));

                }else if(isPropertySelected)
                {
                    editor.putString(Constants.SELECTED_PROPERTY_TYPE, parent.getAdapter().getItem(position).toString());
                    editor.apply();
                    startActivity(new Intent(getActivity(), Screen5_Property_Ads_Activity.class));
                }

            }
        });

    }

    public void serviceDataPrepare() {
        serviceList.add(new ShoppingServiceInfo(0,(R.drawable.icon_jobs), getResources().getString(R.string.jobs)));
        serviceList.add(new ShoppingServiceInfo(1,(R.drawable.icon_property), getResources().getString(R.string.property)));
        serviceList.add(new ShoppingServiceInfo(2,(R.drawable.icon_marriage), getResources().getString(R.string.marriage)));
        serviceList.add(new ShoppingServiceInfo(3,(R.drawable.icon_travel), getResources().getString(R.string.travel)));
        serviceList.add(new ShoppingServiceInfo(4,(R.drawable.icon_education), getResources().getString(R.string.education)));
        serviceList.add(new ShoppingServiceInfo(5,(R.drawable.icon_development), getResources().getString(R.string.development)));
        serviceList.add(new ShoppingServiceInfo(6,(R.drawable.icon_electronics), getResources().getString(R.string.electronics)));
        serviceList.add(new ShoppingServiceInfo(7,(R.drawable.icon_delivery), getResources().getString(R.string.home_delivery)));
        serviceList.add(new ShoppingServiceInfo(8,(R.drawable.icon_vehicle), getResources().getString(R.string.car_rental)));
        serviceList.add(new ShoppingServiceInfo(9,(R.drawable.icon_event), getResources().getString(R.string.events)));
        serviceList.add(new ShoppingServiceInfo(10,(R.drawable.icon_health), getResources().getString(R.string.health)));
        serviceList.add(new ShoppingServiceInfo(11,(R.drawable.icon_catering), getResources().getString(R.string.catering)));
        serviceList.add(new ShoppingServiceInfo(12,(R.drawable.icon_renovation), getResources().getString(R.string.house_office_renovation)));

    }
}
