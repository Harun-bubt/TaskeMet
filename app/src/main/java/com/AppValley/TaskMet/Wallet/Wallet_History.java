package com.AppValley.TaskMet.Wallet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.AppValley.TaskMet.R;


public class Wallet_History extends Fragment {

    View view;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.wallet_history, container, false);

        getData();
        backButtonControl();

        return view;
    }

    private void getData() {
        //Support Fragment Manager
        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
    }

    public void backButtonControl() {

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    fragmentManager.popBackStackImmediate();
                    return true;
                }
                return false;
            }
        });

    }

}