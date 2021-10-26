package com.AppValley.TaskMet.Registration;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.AppValley.TaskMet.R;

public class Fragment1_SignIn extends Fragment {
    Context context;
    Button btnPhone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment_sign_in,container,false);

        context = getContext();
        btnPhone = view.findViewById(R.id.phoneButton);
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Fragment2_EnterNumber fragment2EnterNumber = new Fragment2_EnterNumber();
                    androidx.fragment.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    transaction.replace(R.id.framelayout, fragment2EnterNumber);
                    transaction.addToBackStack("tag");
                    transaction.commit();
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Are you sure you want to Exit?");
                    alertDialog.setMessage("Press yes for exist!");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    getActivity().finish();
                                    System.exit(0);
                                }
                            });

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            });
                    alertDialog.show();

                    return true;
                }
                return false;
            }
        } );

        return  view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // TextView textView = getView().findViewById(R.id.register_textView);
    }
}


