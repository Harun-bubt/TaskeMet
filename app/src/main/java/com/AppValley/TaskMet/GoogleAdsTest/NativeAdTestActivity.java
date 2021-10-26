package com.AppValley.TaskMet.GoogleAdsTest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.AppValley.TaskMet.R;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.NativeAd;

public class NativeAdTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad_test);


        ColorDrawable cd = new ColorDrawable(0xFFFF6666);

        AdLoader adLoader_small = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                        TemplateView template = findViewById(R.id.my_template_small);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                    }
                })
                .build();



        AdLoader adLoader_medium = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                        TemplateView template = findViewById(R.id.my_template_medium);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                    }
                })
                .build();

        adLoader_small.loadAd(new AdRequest.Builder().build());
        adLoader_medium.loadAd(new AdRequest.Builder().build());

    }



}