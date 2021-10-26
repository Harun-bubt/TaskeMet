package com.AppValley.TaskMet.Wallet;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.AppValley.TaskMet.Payment.Confirm_Payment;
import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.TaskMetWebService.ResponseClasses.PackagesModel;

public class Wallet_RC_Package extends Fragment {

    View view;
    String currency,coin;
    int rc_quantity,rc_cost;

    PackagesModel rc_packages;
    TextView p1rc,p2rc,p3rc,p4rc,p1Price,p2Price,p3Price,p4Price;

    RelativeLayout rc_package_1, rc_package_2, rc_package_3, rc_package_4;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    public Wallet_RC_Package(PackagesModel rc_packages,String currency) {
        this.rc_packages = rc_packages;
        this.currency = currency;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.wallet_rc_package, container, false);
        initialiazeAllViews(view);

        backButtonControl();

        p1rc.setText(rc_packages.getBasicCoins()+ " RC");
        p2rc.setText(rc_packages.getStandardCoins()+ " RC");
        p3rc.setText(rc_packages.getPlusCoins()+ " RC");
        p4rc.setText(rc_packages.getSolidCoins()+ " RC");

        if(currency.equals("Tk")) {

            p1Price.setText(currency +" "+rc_packages.getBasicPriceBDT());
            p2Price.setText(currency +" "+rc_packages.getStandardPriceBDT());
            p3Price.setText(currency +" "+rc_packages.getPlusPriceBDT());
            p4Price.setText(currency +" "+rc_packages.getSolidPriceBDT());

        }
        else if(currency.equals("Rs")) {

            p1Price.setText(currency +" "+rc_packages.getBasicPricePKR());
            p2Price.setText(currency +" "+rc_packages.getStandardPricePKR());
            p3Price.setText(currency +" "+rc_packages.getPlusPricePKR());
            p4Price.setText(currency +" "+rc_packages.getSolidPricePKR());

        }
        else {

            p1Price.setText(currency +" "+rc_packages.getBasicPriceINR());
            p2Price.setText(currency +" "+rc_packages.getStandardPricePKR());
            p3Price.setText(currency +" "+rc_packages.getPlusPricePKR());
            p4Price.setText(currency +" "+rc_packages.getSolidPricePKR());

        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        coin = "RC";

        rc_package_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rc_quantity = rc_packages.getBasicCoins();

                if (currency.equals("Tk")) {

                    rc_cost = rc_packages.getBasicPriceBDT();

                } else if (currency.equals("Rs")) {

                    rc_cost = rc_packages.getBasicPricePKR();

                } else {

                    rc_cost = rc_packages.getBasicPriceINR();

                }

                openPaymentScreen(coin, rc_quantity, rc_cost);

            }
        });

        rc_package_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rc_quantity = rc_packages.getBasicCoins();

                if (currency.equals("Tk")) {

                    rc_cost = rc_packages.getStandardPriceBDT();
                } else if (currency.equals("Rs")) {

                    rc_cost = rc_packages.getStandardPricePKR();

                } else {

                    rc_cost = rc_packages.getStandardPriceINR();

                }


                rc_quantity = rc_packages.getStandardCoins();

                openPaymentScreen(coin, rc_quantity, rc_cost);

            }
        });

        rc_package_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rc_quantity = rc_packages.getPlusCoins();

                if (currency.equals("Tk")) {

                    rc_cost = rc_packages.getPlusPriceBDT();

                } else if (currency.equals("Rs")) {

                    rc_cost = rc_packages.getPlusPricePKR();

                }
                else {

                    rc_cost = rc_packages.getPlusPriceINR();

                }

                openPaymentScreen(coin, rc_quantity, rc_cost);

            }
        });

        rc_package_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rc_quantity = rc_packages.getSolidCoins();

                if (currency.equals("Tk")) {

                    rc_cost = rc_packages.getSolidPriceBDT();

                } else if (currency.equals("Rs")) {

                    rc_cost = rc_packages.getSolidPricePKR();

                }
                else {

                    rc_cost = rc_packages.getSolidPriceINR();

                }

                openPaymentScreen(coin, rc_quantity, rc_cost);

            }
        });

    }
    
    public void initialiazeAllViews(View view) {

        //Support Fragment Manager
        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

        rc_package_1 = view.findViewById(R.id.rc_package_1);
        rc_package_2 = view.findViewById(R.id.rc_package_2);
        rc_package_3 = view.findViewById(R.id.rc_package_3);
        rc_package_4 = view.findViewById(R.id.rc_package_4);
        
        p1rc = view.findViewById(R.id.p1rc);
        p2rc = view.findViewById(R.id.p2rc);
        p3rc = view.findViewById(R.id.p3rc);
        p4rc = view.findViewById(R.id.p4rc);
        p1Price = view.findViewById(R.id.p1Price);
        p2Price = view.findViewById(R.id.p2Price);
        p3Price = view.findViewById(R.id.p3Price);
        p4Price = view.findViewById(R.id.p4Price);
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

    public void openPaymentScreen(String coin_type, int quantity, int cost) {

        transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_framLayout, new Confirm_Payment(coin_type, quantity, cost));
        transaction.commit();

    }

}
