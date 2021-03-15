package com.bestpearlstudio.diwaliphotoframe;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.bestpearlstudio.diwaliphotoframe.utility.diwali_Utilities;

import java.io.File;
import java.io.IOException;

import static com.bestpearlstudio.diwaliphotoframe.diwali_MyCreationActivity.pos;

public class diwali_FullScreenViewActivity extends Activity implements View.OnClickListener
{

    private diwali_FullScreenImage_Adapter adapter;
    private ImageView img;
    ImageView btnShare,iv_Home;
    InterstitialAd mInterstitialAd;
    private ImageView btnDelete;
    private ImageView btnSetWallpaper;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diwali_activity_full_screen_view);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_Ad_Crop);
        mAdView = (AdView) findViewById(R.id.adView);
        if (isOnline()) {
            showBannerAd();
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        showFullAd();
        backButton();

        img = (ImageView) findViewById(R.id.iv_image);
        iv_Home= (ImageView)findViewById(R.id.iv_Home);
        iv_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(FullScreenViewActivity.this,StartActivity.class);
                startActivity(i);
                finish();*/
            }
        });
        img.setImageURI(Uri.parse(diwali_Utilities.IMAGEALLARY.get(pos)));
        btnShare = (ImageView) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(this);
        btnDelete = (ImageView) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);
        btnSetWallpaper = (ImageView) findViewById(R.id.btnSetWallpaper);
        btnSetWallpaper.setOnClickListener(this);

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        adapter = new diwali_FullScreenImage_Adapter(diwali_FullScreenViewActivity.this, diwali_Utilities.IMAGEALLARY);
    }
    private void showFullAd() {
        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder().build();
        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void showBannerAd() {

        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
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
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.btnSetWallpaper:
                WallpaperManager wallpaperManager
                        = WallpaperManager.getInstance(getApplicationContext());
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                // get the height and width of screen
                int height = metrics.heightPixels;
                int width = metrics.widthPixels;
                try {
//                    int position2 = viewPager.getCurrentItem();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(diwali_Utilities.IMAGEALLARY.get(pos), options);
                    wallpaperManager.setBitmap(bitmap);

                    wallpaperManager.suggestDesiredDimensions(width / 2, height / 2);
                    Toast.makeText(this,"Wallpaper Set", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnShare:
//                int position = viewPager.getCurrentItem();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);

                intent.setType("image/*");
                //intent.putExtra(Intent.EXTRA_TEXT, Glob.app_name + " Create By : " + Glob.package_name);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(diwali_Utilities.IMAGEALLARY.get(pos))));
                Intent openInChooser = new Intent(intent);
                openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,"Share image using");
                openInChooser.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.GJ05.photoeitotforwwe");
                startActivity(openInChooser);
                break;

            case R.id.btnDelete:
//                int position1 = viewPager.getCurrentItem();
                File file = new File(diwali_Utilities.IMAGEALLARY.get(pos));

                if (file.exists()) {
                    file.delete();
                }
                diwali_Utilities.IMAGEALLARY.remove(pos);
                Intent i = new Intent();
                setResult(Activity.RESULT_OK, i);
                finish();
                break;
        }
    }
}


