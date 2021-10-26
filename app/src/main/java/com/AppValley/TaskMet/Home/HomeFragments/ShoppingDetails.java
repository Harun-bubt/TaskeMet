package com.AppValley.TaskMet.Home.HomeFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AppValley.TaskMet.Home.Adopters.ShopAdopter;
import com.AppValley.TaskMet.Home.DetailsClass.ShoppingServiceInfo;
import com.AppValley.TaskMet.ViewAdActivities.Screen2_Shopping_Ads_Activity;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.constant.Constants;

import java.util.ArrayList;

public class ShoppingDetails extends Fragment {

    View view;

    RecyclerView recyclerView;
    ShopAdopter shopAdopter;
    ArrayList<ShoppingServiceInfo> allShopCategories = new ArrayList<>();

    ListView listView;
    ImageView backArrow;
    ArrayAdapter<String> adapter;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    AnimationSet set = new AnimationSet(true);
    Animation animation = new AlphaAnimation(0.0f, 1.0f);
    LayoutAnimationController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_shopping_details, container, false);

        animation.setDuration(200);
        set.addAnimation(animation);

        recyclerView = view.findViewById(R.id.shoppingItemRecyclerView);
        listView = view.findViewById(R.id.mobile_list);
        backArrow = view.findViewById(R.id.backArrowShopping);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.mobile));

        gettingShopCategory();

        shopAdopter = new ShopAdopter(getContext(), allShopCategories, new ShopAdopter.OnCategorySelection() {
            @Override
            public void onItemClicked(ShoppingServiceInfo model) {

                categorySelection(model.getId());

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(shopAdopter);

        controller = new LayoutAnimationController(set, 0.3f);

        adapter = new ArrayAdapter<String>(getContext(), R.layout.my_list_style, getResources().getStringArray(R.array.mobiles));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                editor.putString(Constants.SELECTED_SUB_CATEGORY, parent.getItemAtPosition(position).toString());
                editor.apply();
                startActivity(new Intent(getActivity(), Screen2_Shopping_Ads_Activity.class));

            }
        });

        return view;
    }

    private void categorySelection(int id) {

        switch (id) {
            case 0:

                adapter = new ArrayAdapter<String>(getContext(), R.layout.my_list_style, getResources().getStringArray(R.array.mobiles));
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.mobile));
                listView.setLayoutAnimation(controller);

                break;
            case 1:
                adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.my_list_style, getResources().getStringArray(R.array.computers));
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.computer));
                listView.setLayoutAnimation(controller);

                break;

            case 2:
                adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.my_list_style, getResources().getStringArray(R.array.vehicles));
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.vehicle));
                listView.setLayoutAnimation(controller);

                break;

            case 3:
                adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.my_list_style, getResources().getStringArray(R.array.furnitures));
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.furniture));

                listView.setLayoutAnimation(controller);

                break;

            case 4:
                adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.my_list_style, getResources().getStringArray(R.array.appliances));
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.appliances));

                listView.setLayoutAnimation(controller);

                break;

            case 5:
                adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.my_list_style, getResources().getStringArray(R.array.bikes));
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.bike));

                listView.setLayoutAnimation(controller);

                break;

            case 6:
                adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.my_list_style, getResources().getStringArray(R.array.sports));
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.sports));

                listView.setLayoutAnimation(controller);

                break;

            case 7:
                adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.my_list_style, getResources().getStringArray(R.array.animals));
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.pets_animals));

                listView.setLayoutAnimation(controller);

                break;

            case 8:
                adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.my_list_style, getResources().getStringArray(R.array.fashions));
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.fashion));

                listView.setLayoutAnimation(controller);

                break;

            case 9:
                adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.my_list_style, getResources().getStringArray(R.array.kids));
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.kids));

                listView.setLayoutAnimation(controller);

                break;

            case 10:
                adapter = new ArrayAdapter<String>(getContext(),
                        R.layout.my_list_style, getResources().getStringArray(R.array.books));
                editor.putString(Constants.SELECTED_MAIN_CATEGORY, getResources().getString(R.string.books));

                listView.setLayoutAnimation(controller);

                break;
        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editor.putString(Constants.SELECTED_SUB_CATEGORY, parent.getItemAtPosition(position).toString());
                editor.apply();
                startActivity(new Intent(getActivity(), Screen2_Shopping_Ads_Activity.class));
            }
        });

    }

    public void gettingShopCategory() {

        allShopCategories.add(new ShoppingServiceInfo(0,(R.drawable.icon_mobile), getResources().getString(R.string.mobile)));
        allShopCategories.add(new ShoppingServiceInfo(1,(R.drawable.icon_computer), getResources().getString(R.string.computer)));
        allShopCategories.add(new ShoppingServiceInfo(2,(R.drawable.icon_vehicle), getResources().getString(R.string.vehicle)));
        allShopCategories.add(new ShoppingServiceInfo(3,(R.drawable.icon_furniture), getResources().getString(R.string.furniture)));
        allShopCategories.add(new ShoppingServiceInfo(4,(R.drawable.icon_appliances), getResources().getString(R.string.appliances)));
        allShopCategories.add(new ShoppingServiceInfo(5,(R.drawable.icon_bike), getResources().getString(R.string.bike)));
        allShopCategories.add(new ShoppingServiceInfo(6,(R.drawable.icon_sports), getResources().getString(R.string.sports)));
        allShopCategories.add(new ShoppingServiceInfo(7,(R.drawable.icon_animals), getResources().getString(R.string.pets_animals)));
        allShopCategories.add(new ShoppingServiceInfo(8,(R.drawable.icon_fashion), getResources().getString(R.string.fashion)));
        allShopCategories.add(new ShoppingServiceInfo(9,(R.drawable.icon_kids), getResources().getString(R.string.kids)));
        allShopCategories.add(new ShoppingServiceInfo(10,(R.drawable.icon_books), getResources().getString(R.string.books)));

    }

}
