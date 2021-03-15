package com.bestpearlstudio.diwaliphotoframe;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.bestpearlstudio.diwaliphotoframe.utility.diwali_Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class diwali_ImageSaveFinalActivity extends AppCompatActivity implements View.OnClickListener {

    Uri uri;
    private TextView tvTitle;
    private ImageView ivBack;
    private String _url;
    private Bitmap bitmap;
    private ImageView iv_home;
    private boolean isAlreadySave = false;
    private ImageView ivPopUp;
    private ImageView iv_Final_Image;
    private ImageView ivFacebook, ivWhatsapp, ivInstagram, ivShareMore, ivHike, ivTwitter;

    private ImageView ivBanner;
    private int index = 0;
    private Uri uriBanner;
    private String banner = null;
    private AdView mAdView;
    private InterstitialAd interstitialAd;
    private InterstitialAd mInterstitialAd;
    LinearLayout nativeAdContainer;
    public static com.google.android.gms.ads.InterstitialAd mNativeadd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diwali_activity_image_save_final);
        mAdView = (AdView) findViewById(R.id.adView);
        nativeAdContainer = (LinearLayout)findViewById(R.id.native_ad_container);

        if(isOnline())
        {
           // showFullAd();
            showGOOGLEAdvance(nativeAdContainer);
            //showBannerAd();
        }
       // showFullAd();
        bindView();
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    private void showFullAd() {

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
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.i("Ads", "onAdClosed");
            }

        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
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

    private void showBannerAd() {

        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

    }
    private void bindView() {
        ivBanner = (ImageView) findViewById(R.id.iv_Show_Image);
        ivBack = (ImageView) findViewById(R.id.iv_Back_Save);
        ivBack.setOnClickListener(this);

        iv_home = (ImageView) findViewById(R.id.iv_Home);
        iv_home.setOnClickListener(this);

        ivInstagram = (ImageView) findViewById(R.id.iv_instagram);
        ivInstagram.setOnClickListener(this);
        ivWhatsapp = (ImageView) findViewById(R.id.iv_whatsapp);
        ivWhatsapp.setOnClickListener(this);
        ivFacebook = (ImageView) findViewById(R.id.iv_facebook);
        ivFacebook.setOnClickListener(this);
        ivShareMore = (ImageView) findViewById(R.id.iv_Share_More);
        ivShareMore.setOnClickListener(this);
        ivHike = (ImageView) findViewById(R.id.iv_Hike);
        ivHike.setOnClickListener(this);
        ivTwitter = (ImageView) findViewById(R.id.iv_Twitter);
        ivTwitter.setOnClickListener(this);
//        ivMain = (ImageView) findViewById(R.id.iv_holder);
        SetImageView();
    }

    void SetImageView() {

        iv_Final_Image = (ImageView) findViewById(R.id.iv_Show_Image);
        bitmap = diwali_ImageEditingActivity.finalEditedBitmapImage;
        iv_Final_Image.setImageBitmap(bitmap);
        iv_Final_Image.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public void onClick(View v) {

        Intent shareIntent;

        if(Build.VERSION.SDK_INT <= 17 )
        {
            // Do some stuff
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(diwali_ImageEditingActivity._url)));
            shareIntent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+getPackageName());

        }
        else
        {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID  + ".provider",new File(diwali_ImageEditingActivity._url)));
            shareIntent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+getPackageName());

        }



        switch (v.getId())

        {
            case R.id.iv_Back_Save:
                finish();
                break;

            case R.id.iv_Home:

                Intent intent = new Intent(getApplicationContext(), diwali_ImageViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("ToHome", true);
                startActivity(intent);
                finish();
                break;

            case R.id.iv_instagram:
                try {
                    shareIntent.setPackage("com.instagram.android");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+getPackageName());
                    startActivity(shareIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Instagram doesn't installed", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.iv_Hike:
                try {
                    shareIntent.setPackage("com.bsb.hike");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+getPackageName());
                    startActivity(shareIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Hike doesn't installed", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.iv_Twitter:
                try {
                    shareIntent.setPackage("com.twitter.android");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+getPackageName());
                    startActivity(shareIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Twitter doesn't installed", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.iv_whatsapp:
                try {
                    shareIntent.setPackage("com.whatsapp");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+getPackageName());
                    startActivity(shareIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "WhatsApp doesn't installed", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.iv_facebook:
                try {
                    shareIntent.setPackage("com.facebook.katana");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+getPackageName());
                    startActivity(shareIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Facebook doesn't installed", Toast.LENGTH_LONG).show();
                }
                break;


            case R.id.iv_Share_More:
//                startActivity(Intent.createChooser(shareIntent, "Share Image using"));
//                shareImage();
//                if (!isAlreadySave)
//                    saveImage(ImageEditingActivity.finalbitmap);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(diwali_ImageEditingActivity._url)));
                shareIntent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="+getPackageName());
                startActivity(Intent.createChooser(sharingIntent, "Share Image using"));
                break;
        }

    }

    private void saveImage(Bitmap bitmap2) {
        isAlreadySave = true;
//        Log.v("TAG", "saveImageInCache is called");
        Bitmap bitmap;
        OutputStream output;
        // Retrieve the image from the res folder
        bitmap = bitmap2;
        File filepath = Environment.getExternalStorageDirectory();
        // Create a new folder in SD Card
        File dir = new File(filepath.getAbsolutePath() + "/" + diwali_Utility.Edit_Folder_name);
        dir.mkdirs();
        // Create a name for the saved image
        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String FileName = ts + ".jpeg";
        File file = new File(dir, FileName);
        file.renameTo(file);
        String _uri = "file://" + filepath.getAbsolutePath() + "/" + diwali_Utility.Edit_Folder_name + "/" + FileName;
        String _uri2 = filepath.getAbsolutePath() + "/" + diwali_Utility.Edit_Folder_name + "/" + FileName;
        _url = _uri2;
        Log.d("cache uri=", _uri);
        try {

            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            //finish();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(_uri))));
            Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd unifiedNativeAd, UnifiedNativeAdView unifiedNativeAdView) {
        VideoController videoController = unifiedNativeAd.getVideoController();
        videoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                super.onVideoEnd();
            }
        });
        com.google.android.gms.ads.formats.MediaView mediaView = (MediaView) unifiedNativeAdView.findViewById(R.id.ad_media);
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

}
