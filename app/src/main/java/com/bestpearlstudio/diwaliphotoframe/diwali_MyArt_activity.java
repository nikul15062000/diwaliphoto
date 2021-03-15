package com.bestpearlstudio.diwaliphotoframe;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bestpearlstudio.diwaliphotoframe.utility.diwali_Utility;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.io.File;
import java.util.ArrayList;

public class diwali_MyArt_activity extends AppCompatActivity {
    private ImageView ivBack;
    public static ArrayList<String> IMAGEALLARY = new ArrayList<>();
    public static int pos;
    private diwali_bui_GalleryAdapter buiGalleryAdapter;
    private GridView lv;
    LinearLayout nativeAdContainer;
    CardView adView;
    LinearLayout adChoicesContainer;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diwali_activity_my_art);

        ivBack = (ImageView) findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        nativeAdContainer = (LinearLayout)findViewById(R.id.native_ad_container);
        if (isOnline()) {
            showGOOGLEAdvance(nativeAdContainer);
        }
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
       /* TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("My Creation");*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        this.lv = findViewById(R.id.gridview);
        this.buiGalleryAdapter = new diwali_bui_GalleryAdapter(this, IMAGEALLARY);
        IMAGEALLARY.clear();
        listAllImages(new File((Environment.getExternalStorageDirectory() + "/"+ diwali_Utility.Edit_Folder_name)));
        this.lv.setAdapter(this.buiGalleryAdapter);
        this.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                diwali_MyArt_activity.this.buiGalleryAdapter.getItemId(position);
                diwali_MyArt_activity.pos = position;
                @SuppressLint("ResourceType") Dialog dialog = new Dialog(diwali_MyArt_activity.this, 16973839);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                diwali_MyArt_activity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int i = (int) (((double) displayMetrics.heightPixels) * 1.0d);
                int i2 = (int) (((double) displayMetrics.widthPixels) * 1.0d);
                dialog.requestWindowFeature(1);
                dialog.getWindow().setFlags(1024, 1024);
                dialog.setContentView(R.layout.diwali_bui_activity_full_screen_view);
                dialog.getWindow().setLayout(i2, i);
                dialog.setCanceledOnTouchOutside(true);
                ((ImageView) dialog.findViewById(R.id.iv_image)).setImageURI(Uri.parse(diwali_MyArt_activity.IMAGEALLARY.get(diwali_MyArt_activity.pos)));
                dialog.show();
            }
        });
    }
    private void populateUnifiedNativeAdView(UnifiedNativeAd unifiedNativeAd, UnifiedNativeAdView unifiedNativeAdView) {
        VideoController videoController = unifiedNativeAd.getVideoController();
        videoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                super.onVideoEnd();
            }
        });
        com.google.android.gms.ads.formats.MediaView mediaView = (com.google.android.gms.ads.formats.MediaView) unifiedNativeAdView.findViewById(R.id.ad_media);
        ImageView imageView = (ImageView) unifiedNativeAdView.findViewById(R.id.ad_image);
        if (videoController.hasVideoContent()) {
            unifiedNativeAdView.setMediaView(mediaView);
            imageView.setVisibility(View.VISIBLE);
        } else {
            unifiedNativeAdView.setImageView(imageView);
            mediaView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(((com.google.android.gms.ads.formats.NativeAd.Image) unifiedNativeAd.getImages().get(0)).getDrawable());
        }
        unifiedNativeAdView.setHeadlineView(unifiedNativeAdView.findViewById(R.id.ad_headline));
        unifiedNativeAdView.setBodyView(unifiedNativeAdView.findViewById(R.id.ad_body));
        unifiedNativeAdView.setCallToActionView(unifiedNativeAdView.findViewById(R.id.ad_call_to_action));
        unifiedNativeAdView.setIconView(unifiedNativeAdView.findViewById(R.id.ad_app_icon));
        unifiedNativeAdView.setPriceView(unifiedNativeAdView.findViewById(R.id.ad_price));
        unifiedNativeAdView.setStarRatingView(unifiedNativeAdView.findViewById(R.id.ad_stars));
        unifiedNativeAdView.setStoreView(unifiedNativeAdView.findViewById(R.id.ad_store));
        unifiedNativeAdView.setAdvertiserView(unifiedNativeAdView.findViewById(R.id.ad_advertiser));
        ((TextView) unifiedNativeAdView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
        ((TextView) unifiedNativeAdView.getBodyView()).setText(unifiedNativeAd.getBody());
        ((Button) unifiedNativeAdView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
        if (unifiedNativeAd.getIcon() == null) {
            unifiedNativeAdView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) unifiedNativeAdView.getIconView()).setImageDrawable(unifiedNativeAd.getIcon().getDrawable());
            unifiedNativeAdView.getIconView().setVisibility(View.VISIBLE);
        }
        if (unifiedNativeAd.getPrice() == null) {
            unifiedNativeAdView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            unifiedNativeAdView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) unifiedNativeAdView.getPriceView()).setText(unifiedNativeAd.getPrice());
        }
        if (unifiedNativeAd.getStore() == null) {
            unifiedNativeAdView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            unifiedNativeAdView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) unifiedNativeAdView.getStoreView()).setText(unifiedNativeAd.getStore());
        }
        if (unifiedNativeAd.getStarRating() == null) {
            unifiedNativeAdView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) unifiedNativeAdView.getStarRatingView()).setRating(unifiedNativeAd.getStarRating().floatValue());
            unifiedNativeAdView.getStarRatingView().setVisibility(View.VISIBLE);
        }
        if (unifiedNativeAd.getAdvertiser() == null) {
            unifiedNativeAdView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) unifiedNativeAdView.getAdvertiserView()).setText(unifiedNativeAd.getAdvertiser());
            unifiedNativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        unifiedNativeAdView.setNativeAd(unifiedNativeAd);
    }

    private void showGOOGLEAdvance(final LinearLayout linearLayout) {
        AdLoader.Builder builder = new AdLoader.Builder((Context) this, getString(R.string.Native_Ad_screen));
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {


            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                UnifiedNativeAdView unifiedNativeAdView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.diwali_ad_unit_admob_med, null);
                populateUnifiedNativeAdView(unifiedNativeAd, unifiedNativeAdView);
                linearLayout.removeAllViews();
                linearLayout.addView(unifiedNativeAdView);
            }
        });
        builder.withNativeAdOptions(new NativeAdOptions.Builder().setVideoOptions(new VideoOptions.Builder().build()).build());
        builder.withAdListener(new AdListener() {
            public void onAdFailedToLoad(int i) {
                StringBuilder sb = new StringBuilder();
                sb.append("onAdFailedToLoad: ");
                sb.append(i);
                Log.i("dsityadmobnative", sb.toString());
            }
        }).build().loadAd(new AdRequest.Builder().build());
    }

    private void listAllImages(File file) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE") == -1) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 20);
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (int j = files.length - 1; j >= 0; j--) {
                String ss = files[j].toString();
                File check = new File(ss);
                Log.d("" + check.length(), "" + check.length());
                if (check.length() <= 1024) {
                    Log.e("Invalid Image", "Delete Image");
                } else if (check.toString().contains(".jpg") || check.toString().contains(".png") || check.toString().contains(".jpeg")) {
                    IMAGEALLARY.add(ss);
                }
                System.out.println(ss);
            }
            return;
        }
        System.out.println("Empty Folder");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private boolean isOnline() {
        @SuppressLint({"MissingPermission", "WrongConstant"}) NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}