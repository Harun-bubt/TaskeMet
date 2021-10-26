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

public class Wallet_GC_Package extends Fragment {

    View view;
    PackagesModel gc_packages;
    String currency, coin;
    int gc_quantity, gc_cost;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    RelativeLayout gc_package_1, gc_package_2, gc_package_3, gc_package_4;

    TextView p1gc, p2gc, p3gc, p4gc, p1PriceGc, p2PriceGc, p3PriceGc, p4PriceGc;

    public Wallet_GC_Package(PackagesModel gc_packages, String currency) {
        this.gc_packages = gc_packages;
        this.currency = currency;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.wallet_gc_package, container, false);
        initialiazeAllViews(view);

        backButtonControl();

        p1gc.setText(gc_packages.getBasicCoins() + " GC");
        p2gc.setText(gc_packages.getStandardCoins() + " GC");
        p3gc.setText(gc_packages.getPlusCoins() + " GC");
        p4gc.setText(gc_packages.getSolidCoins() + " GC");

        if (currency.equals("Tk")) {

            p1PriceGc.setText(currency + " " + gc_packages.getBasicPriceBDT());
            p2PriceGc.setText(currency + " " + gc_packages.getStandardPriceBDT());
            p3PriceGc.setText(currency + " " + gc_packages.getPlusPriceBDT());
            p4PriceGc.setText(currency + " " + gc_packages.getSolidPriceBDT());

        } else if (currency.equals("Rs")) {

            p1PriceGc.setText(currency + " " + gc_packages.getBasicPricePKR());
            p2PriceGc.setText(currency + " " + gc_packages.getStandardPricePKR());
            p3PriceGc.setText(currency + " " + gc_packages.getPlusPricePKR());
            p4PriceGc.setText(currency + " " + gc_packages.getSolidPricePKR());

        } else {

            p1PriceGc.setText(currency + " " + gc_packages.getBasicPriceINR());
            p2PriceGc.setText(currency + " " + gc_packages.getStandardPriceINR());
            p3PriceGc.setText(currency + " " + gc_packages.getPlusPriceINR());
            p4PriceGc.setText(currency + " " + gc_packages.getSolidPriceINR());

        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        coin = "GC";

        gc_package_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gc_quantity = gc_packages.getBasicCoins();

                if (currency.equals("Tk")) {

                    gc_cost = gc_packages.getBasicPriceBDT();

                } else if (currency.equals("Rs")) {

                    gc_cost = gc_packages.getBasicPricePKR();

                } else {

                    gc_cost = gc_packages.getBasicPriceINR();

                }

                openPaymentScreen(coin, gc_quantity, gc_cost);

            }
        });

        gc_package_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gc_quantity = gc_packages.getBasicCoins();

                if (currency.equals("Tk")) {

                    gc_cost = gc_packages.getStandardPriceBDT();
                } else if (currency.equals("Rs")) {

                    gc_cost = gc_packages.getStandardPricePKR();

                } else {

                    gc_cost = gc_packages.getStandardPriceINR();

                }


                gc_quantity = gc_packages.getStandardCoins();

                openPaymentScreen(coin, gc_quantity, gc_cost);

            }
        });

        gc_package_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gc_quantity = gc_packages.getPlusCoins();

                if (currency.equals("Tk")) {

                    gc_cost = gc_packages.getPlusPriceBDT();

                } else if (currency.equals("Rs")) {

                    gc_cost = gc_packages.getPlusPricePKR();

                }
                else {

                    gc_cost = gc_packages.getPlusPriceINR();

                }

                openPaymentScreen(coin, gc_quantity, gc_cost);

            }
        });

        gc_package_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gc_quantity = gc_packages.getSolidCoins();

                if (currency.equals("Tk")) {

                    gc_cost = gc_packages.getSolidPriceBDT();

                } else if (currency.equals("Rs")) {

                    gc_cost = gc_packages.getSolidPricePKR();

                }
                else {

                    gc_cost = gc_packages.getSolidPriceINR();

                }

                openPaymentScreen(coin, gc_quantity, gc_cost);

            }
        });

    }

    public void initialiazeAllViews(View view) {

        //Support Fragment Manager
        fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();

        gc_package_1 = view.findViewById(R.id.gc_package_1);
        gc_package_2 = view.findViewById(R.id.gc_package_2);
        gc_package_3 = view.findViewById(R.id.gc_package_3);
        gc_package_4 = view.findViewById(R.id.gc_package_4);

        p1gc = view.findViewById(R.id.p1gc);
        p2gc = view.findViewById(R.id.p2gc);
        p3gc = view.findViewById(R.id.p3gc);
        p4gc = view.findViewById(R.id.p4gc);
        p1PriceGc = view.findViewById(R.id.p1PriceGc);
        p2PriceGc = view.findViewById(R.id.p2PriceGc);
        p3PriceGc = view.findViewById(R.id.p3PriceGc);
        p4PriceGc = view.findViewById(R.id.p4PriceGc);

    }

    public void backButtonControl() {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

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