package com.bestpearlstudio.diwaliphotoframe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.bestpearlstudio.diwaliphotoframe.utility.diwali_TransparentProgressDialog;
import com.bestpearlstudio.diwaliphotoframe.utility.diwali_Utilities;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;

public class diwali_MyCreationActivity extends Activity implements AdapterView.OnItemClickListener {

    private String TAG = diwali_MyCreationActivity.class.getSimpleName();
    GridView gridSavedGallary;
    diwali_Gallary_Adapter gAdp;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    public static int pos;

    diwali_TransparentProgressDialog diwaliTransparentProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diwali_activity_my_creation);

        backButton();

        showfullAd();




        mAdView = (AdView) findViewById(R.id.adView);
        gridSavedGallary = (GridView) findViewById(R.id.gridSavedGallary);
        gridSavedGallary.setOnItemClickListener(this);


    }

    private void showfullAd()
    {
        diwaliTransparentProgressDialog = new diwali_TransparentProgressDialog(this, R.drawable.loading);

        diwaliTransparentProgressDialog.setCancelable(false);
        diwaliTransparentProgressDialog.show();


        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
                diwaliTransparentProgressDialog.dismiss();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.i("Ads", "onAdOpened");
                diwaliTransparentProgressDialog.dismiss();
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
                diwaliTransparentProgressDialog.dismiss();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.i("Ads", "onAdClosed");
                diwaliTransparentProgressDialog.dismiss();

            }

        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();


            diwaliTransparentProgressDialog.dismiss();
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        diwali_Utilities.IMAGEALLARY.clear();
        diwali_Utilities.listAllImages(new File("/mnt/sdcard/diwaliDualPhotoFrame/"));
        gAdp = new diwali_Gallary_Adapter(diwali_MyCreationActivity.this, diwali_Utilities.IMAGEALLARY);

        gridSavedGallary.setAdapter(gAdp);
        if (isOnline()) {

            showBannerAd();
        } else {
            mAdView.setVisibility(View.GONE);
        }
    }


    private void showBannerAd() {

        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

    }

    public void backButton() {

        ImageView back = (ImageView) findViewById(R.id.ivBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        Intent fullscreen = new Intent(diwali_MyCreationActivity.this, diwali_FullScreenViewActivity.class);
        pos = position;
        fullscreen.putExtra("position", position);
        startActivity(fullscreen);
        finish();
    }

    @Override
    public void onBackPressed() {

        finish();

    }
}
