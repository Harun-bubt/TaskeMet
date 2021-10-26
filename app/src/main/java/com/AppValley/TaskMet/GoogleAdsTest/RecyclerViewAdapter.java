package com.AppValley.TaskMet.GoogleAdsTest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.AppValley.TaskMet.R;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_MY_AD = 0;
    private static final int ITEM_TYPE_BANNER_AD = 1;


    private ArrayList<Object> mList;
    private MyRecyclerViewItemClickListener mItemClickListener;

    MyRecyclerViewAdapter(ArrayList<Object> listItems, MyRecyclerViewItemClickListener itemClickListener) {
        this.mList = listItems;
        this.mItemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {

            case ITEM_TYPE_BANNER_AD:

                //Inflate ad banner container
                View bannerLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_ad_row, parent, false);

                //Create View Holder
                MyAdViewHolder myAdViewHolder = new MyAdViewHolder(bannerLayoutView);

                return myAdViewHolder;

            case ITEM_TYPE_MY_AD:
            default:

                //Inflate RecyclerView row
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.county_row, parent, false);

                //Create View Holder
                final MyCommonViewHolder commonViewHolder = new MyCommonViewHolder(view);

                //Item Clicks
                commonViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClicked((Country) mList.get(commonViewHolder.getLayoutPosition()));
                    }
                });

                return commonViewHolder;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {

            case ITEM_TYPE_BANNER_AD:

                if (mList.get(position) instanceof AdView) {

                    MyAdViewHolder bannerHolder = (MyAdViewHolder) holder;
                    AdView adView = (AdView) mList.get(position);
                    ViewGroup adCardView = (ViewGroup) bannerHolder.itemView;

                    if (adCardView.getChildCount() > 0) {
                        adCardView.removeAllViews();
                    }
                    if (adView.getParent() != null) {
                        ((ViewGroup) adView.getParent()).removeView(adView);
                    }

                    // Add the banner ad to the ad view.
                    adCardView.addView(adView);
                }
                break;

            case ITEM_TYPE_MY_AD:
            default:

                if (mList.get(position) instanceof Country) {

                    MyCommonViewHolder myCountryViewHolder = (MyCommonViewHolder) holder;
                    Country country = (Country) mList.get(position);

                    //Set Country Name
                    myCountryViewHolder.textViewName.setText(country.getName());

                    //Set Capital
                    String capital = "Capital: " + country.getCapital();
                    myCountryViewHolder.textViewCapital.setText(capital);

                    //Set Currency
                    String currency = "Currency: " + country.getCurrency();
                    myCountryViewHolder.textViewCurrency.setText(currency);

                    //Set Continent
                    String continent = "Continent: " + country.getContinent();
                    myCountryViewHolder.textViewContinent.setText(continent);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0 || mList.get(position) instanceof Country) {

            return ITEM_TYPE_MY_AD;

        } else {

            return (position % MainActivity.ITEMS_PER_AD == 0) ? ITEM_TYPE_BANNER_AD : ITEM_TYPE_MY_AD;

        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //Country View Holder
    class MyCommonViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewCapital;
        private TextView textViewCurrency;
        private TextView textViewContinent;

        MyCommonViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.countryName);
            textViewCapital = itemView.findViewById(R.id.capital);
            textViewCurrency = itemView.findViewById(R.id.currency);
            textViewContinent = itemView.findViewById(R.id.continent);

        }
    }


    //Banner Ad View Holder
    class MyAdViewHolder extends RecyclerView.ViewHolder {
        MyAdViewHolder(View itemView) {
            super(itemView);
        }
    }


    //Country Item Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(Country country);
    }

}