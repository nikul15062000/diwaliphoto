package com.bestpearlstudio.diwaliphotoframe;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.bestpearlstudio.diwaliphotoframe.MyTouch.diwali_MultiTouchListener;
import com.bestpearlstudio.diwaliphotoframe.dialog.diwali_AddTextDialog;
import com.bestpearlstudio.diwaliphotoframe.stickerview.diwali_diwali_StickerImageView;
import com.bestpearlstudio.diwaliphotoframe.stickerview.diwali_diwali_StickerTextView;
import com.bestpearlstudio.diwaliphotoframe.utility.diwali_HorizontalListView;
import com.bestpearlstudio.diwaliphotoframe.utility.diwali_Utility;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import yuku.ambilwarna.AmbilWarnaDialog;

import static android.view.View.GONE;
import static com.bestpearlstudio.diwaliphotoframe.R.id.grid_frame;
import static com.bestpearlstudio.diwaliphotoframe.utility.diwali_Utility.finalurl;

public class diwali_ImageEditingActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final int RE_CAMERA = 1;
    private static final int RE_GALLERY = 2;
    private static final int MY_REQUEST_CODE = 5;
    private static final int FINAL_SAVE = 20;

    static final int REQ_CODE_CSDK_IMAGE_EDITOR = 3001;

    ProgressDialog dialog;

    private RelativeLayout mainFrm;
    private ImageView ivImg,ivImg1, iv_Back_Save,iv_secImg;
    private ImageView ivFrm, ivOverlay;
    private ImageView ivTheme,iv_frame;
    private ImageView iv_gallery;
    private ImageView iv_camera;
    private ImageView iv_effect, iv_text, iv_brightness, iv_overlay;
    private ImageView ivSave;
    private ImageView ivBack;
    private Bundle bundle;
    private int frmId=0;
    private String mCurrentPhotoPath;
    private ImageView imgsave, iv_imagesave;

    ArrayList<Integer> stickerviewId = new ArrayList<>();
  //  private InterstitialAd mInterstitialAd;
    private static final String TAG = "ImageEditingActivity";
    public static Bitmap finalbitmap;
    public static String _url;
    private boolean isAlreadySave;
    private Boolean isImgSelected = false;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 4;
    public static Bitmap finalEditedBitmapImage;//for store image after editing
    private SeekBar seekBrightness;

    diwali_diwali_StickerImageView sticker_Imageview;

    public static boolean isFirst = false;

    public float[] mainMatrix = {
            1, 0, 0, 0, 0, //red
            0, 1, 0, 0, 0, //green
            0, 0, 1, 0, 0, //blue
            0, 0, 0, 1, 0    //alpha
    };

    private diwali_HorizontalListView grid_Theme;
    private diwali_HorizontalListView grid_Frame;
    private diwali_HorizontalListView grid_Overlay;
    private diwali_ThemeAdapter diwaliThemeAdapter;
    ArrayList<diwali_FrameModel> themeList,frameLIst;
    ArrayList<diwali_FrameModel> overlayList;

    private Boolean flagForFrame = true;
    private Boolean flagForOverlay = true;
    private SeekBar seekThemeOpacity;
    FrameLayout canvas;
    private Boolean flagForBrightness = true;

    private static final String SAMPLE_CROPPED_IMAGE_NAME = "divaliphotoframe";

    LinearLayout ll_Select_Theme;

//    TransparentProgressDialog transparentProgressDialog;

    private AdView mAdView;
    public static com.google.android.gms.ads.InterstitialAd mInterstitialAd;

    public interface AddText {
        void setTextview(String _text, int _color, String font, String texture);

        void selectcolor(String _text, String font);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.diwali_activity_image_editing);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_Ad_Crop);
        mAdView = (AdView) findViewById(R.id.adView);

        //sticker_Imageview = new StickerImageView(this);
        //canvas = (FrameLayout) findViewById(R.id.framelayout);
        //sticker_Imageview.setImageResource(R.drawable.cmp1);
        //canvas.addView(sticker_Imageview);

        if (isOnline()) {
           // showfbbanner();
            showFullAd();
          //  showFullAd();
        }
        //
        bundle = getIntent().getExtras();
        frmId = bundle.getInt("FrmID");
        bindView();


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
        removeBorder();
    }


    private void bindView() {


        ll_Select_Theme = (LinearLayout) findViewById(R.id.ll_Select_Theme);

        iv_gallery = (ImageView) findViewById(R.id.iv_gallery);
        iv_gallery.setOnClickListener(this);
        iv_camera = (ImageView) findViewById(R.id.iv_camera);
        iv_camera.setOnClickListener(this);
        /*iv_effect = (ImageView) findViewById(R.id.iv_effect);
        iv_effect.setOnClickListener(this);*/
        iv_overlay = (ImageView) findViewById(R.id.iv_overlay);
        iv_overlay.setOnClickListener(this);
        iv_brightness = (ImageView) findViewById(R.id.iv_brightness);
        iv_brightness.setOnClickListener(this);
        iv_text = (ImageView) findViewById(R.id.iv_text);
        iv_text.setOnClickListener(this);

        ivTheme = (ImageView) findViewById(R.id.iv_Theme);

        grid_Frame = (diwali_HorizontalListView) findViewById(grid_frame);

        iv_frame=(ImageView)findViewById(R.id.iv_frame);
        iv_frame.setOnClickListener(this );
        setThemeList();

        setArraylistForTheme1();

        ivOverlay = (ImageView) findViewById(R.id.iv_Overlay);
        setOverlayList();
        seekThemeOpacity = (SeekBar) findViewById(R.id.sb_Theme_Opacity);
        seekThemeOpacity.setVisibility(GONE);
        seekThemeOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ivTheme.setAlpha((int) (progress * 2.5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekThemeOpacity.setProgress(50);

        mainFrm = (RelativeLayout) findViewById(R.id.main_frm);
        mainFrm.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                removeBorder();

                if (sticker_Imageview != null) {
                    sticker_Imageview.setControlItemsHidden(true);
                }
                grid_Overlay.setVisibility(GONE);
                grid_Theme.setVisibility(GONE);
                grid_Frame.setVisibility(GONE);

                flagForFrame = true;

                return false;
            }
        });
        /*mainFrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeBorder();
                if (sticker_Imageview != null) {
                    sticker_Imageview.setControlItemsHidden(true);
                }
                grid_Overlay.setVisibility(GONE);
                grid_Theme.setVisibility(GONE);
                grid_Frame.setVisibility(GONE);

                flagForFrame = true;
            }
        });*/

        iv_Back_Save = (ImageView) findViewById(R.id.ivBack);
        iv_Back_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //iv_secImg=(ImageView)findViewById(R.id.iv_secondImg) ;
        //iv_secImg.setOnClickListener(this);
        //iv_secImg.setOnClickListener(new MultiTouchListener());

        ivImg = (ImageView) findViewById(R.id.iv_img);

        ivImg1 = (ImageView) findViewById(R.id.iv_img1);

        imgsave = (ImageView) findViewById(R.id.iv_imagesave);
        imgsave.setOnClickListener(this);
        ivImg.setOnClickListener(this);
        ivImg.setOnTouchListener(new diwali_MultiTouchListener());
        ivFrm = (ImageView) findViewById(R.id.iv_frm);
        ivFrm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                removeBorder();
                return false;
            }
        });

        ivImg1.setOnTouchListener(new diwali_MultiTouchListener());

        ivImg.setImageURI(finalurl);



      /*  sticker_Imageview = new StickerImageView(ImageEditingActivity.this);
        canvas = (FrameLayout) findViewById(R.id.framelayout);
        canvas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                removeBorder();
                return false;
            }
        });

        Random r = new Random();
        text_id = r.nextInt();
        if (text_id < 0) {
            text_id = text_id - (text_id * 2);
        }
        sticker_Imageview.setId(text_id);
        stickerviewId.add(text_id);
        sticker_Imageview.setImageURI(finalurl);
        canvas.addView(sticker_Imageview);*/


        // ivImg.setImageBitmap(bmp);

        isImgSelected = true;

        int ThemeId = diwali_FtameListActivity.themeList.get(frmId).getFrmId();
        ivFrm.setImageDrawable(getResources().getDrawable(ThemeId));
        //Glide.with(getApplicationContext()).load(frmId).into(ivFrm);

        //ivFrm.setImageDrawable(getResources().getDrawable(frmId));
        //ivFrm.setOnTouchListener(new MultiTouchListener());

        ivSave = (ImageView) findViewById(R.id.iv_Save);
        ivSave.setOnClickListener(this);
        seekBrightness = (SeekBar) findViewById(R.id.seek_brightness);
        seekBrightness.setVisibility(GONE);
        seekBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setBlackAndWhite(ivImg, i + 100);
                seekBrightness.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setArraylistForTheme1()
    {
        frameLIst = new ArrayList<>();


        frameLIst.add(new diwali_FrameModel(R.drawable.foto_1, R.drawable.thumb_1));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_2, R.drawable.thumb_2));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_3, R.drawable.thumb_3));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_4, R.drawable.thumb_4));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_5, R.drawable.thumb_5));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_6, R.drawable.thumb_6));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_7, R.drawable.thumb_7));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_8, R.drawable.thumb_8));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_9, R.drawable.thumb_9));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_10, R.drawable.thumb_10));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_11, R.drawable.thumb_11));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_12, R.drawable.thumb_12));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_13, R.drawable.thumb_13));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_14, R.drawable.thumb_14));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_15, R.drawable.thumb_15));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_16, R.drawable.thumb_16));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_17, R.drawable.thumb_17));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_18, R.drawable.thumb_18));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_19, R.drawable.thumb_19));
        frameLIst.add(new diwali_FrameModel(R.drawable.foto_20, R.drawable.thumb_20));


        diwaliThemeAdapter = new diwali_ThemeAdapter(this, frameLIst);
        grid_Frame.setAdapter(diwaliThemeAdapter);
        grid_Frame.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //showfbbanner();

                int ThemeId = frameLIst.get(position).getFrmId();
                ivFrm.setImageDrawable(getResources().getDrawable(ThemeId));

                grid_Frame.setVisibility(GONE);
                flagForFrame = true;


            }
        });
    }

    private void setOverlayList() {
        setArraylistForOverlay();
        grid_Overlay = (diwali_HorizontalListView) findViewById(R.id.grid_overlay);
        diwaliThemeAdapter = new diwali_ThemeAdapter(this, overlayList);
        grid_Overlay.setAdapter(diwaliThemeAdapter);
        grid_Overlay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // showfbbanner();
                removeBorder();

                sticker_Imageview = new diwali_diwali_StickerImageView(diwali_ImageEditingActivity.this);
                canvas = (FrameLayout) findViewById(R.id.framelayout);
                canvas.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                       removeBorder();
                        return false;
                    }
                });

                Random r = new Random();
                text_id = r.nextInt();
                if (text_id < 0) {
                    text_id = text_id - (text_id * 2);
                }
                sticker_Imageview.setId(text_id);
                stickerviewId.add(text_id);
                sticker_Imageview.setImageResource(overlayList.get(position).getFrmId());
                canvas.addView(sticker_Imageview);

                grid_Overlay.setVisibility(GONE);
                flagForFrame = true;
            }
        });
    }


    private void setThemeList() {
        setArraylistForTheme();
        grid_Theme = (diwali_HorizontalListView) findViewById(R.id.grid_Theme);
        diwaliThemeAdapter = new diwali_ThemeAdapter(this, themeList);
        grid_Theme.setAdapter(diwaliThemeAdapter);
        grid_Theme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  showfbbanner();
                if (position == 0) {
                    ivTheme.setImageResource(0);
                } else {
                    ivTheme.setImageResource(0);
                    //showBannerAd();
                    ivTheme.setImageResource(themeList.get(position).getFrmId());
                }
            }
        });
    }

    private void setArraylistForOverlay() {
        overlayList = new ArrayList<>();
        overlayList.add(new diwali_FrameModel(R.drawable.o_1, R.drawable.o_1));
        overlayList.add(new diwali_FrameModel(R.drawable.o_2, R.drawable.o_2));
        overlayList.add(new diwali_FrameModel(R.drawable.o_3, R.drawable.o_3));
        overlayList.add(new diwali_FrameModel(R.drawable.o_4, R.drawable.o_4));
        overlayList.add(new diwali_FrameModel(R.drawable.o_5, R.drawable.o_5));
        overlayList.add(new diwali_FrameModel(R.drawable.o_6, R.drawable.o_6));
        overlayList.add(new diwali_FrameModel(R.drawable.o_7, R.drawable.o_7));
        overlayList.add(new diwali_FrameModel(R.drawable.o_8, R.drawable.o_8));
        overlayList.add(new diwali_FrameModel(R.drawable.o_9, R.drawable.o_9));
        overlayList.add(new diwali_FrameModel(R.drawable.o_10, R.drawable.o_10));
        overlayList.add(new diwali_FrameModel(R.drawable.o_11, R.drawable.o_11));
        overlayList.add(new diwali_FrameModel(R.drawable.o_12, R.drawable.o_12));
        overlayList.add(new diwali_FrameModel(R.drawable.o_13, R.drawable.o_13));
        overlayList.add(new diwali_FrameModel(R.drawable.o_14, R.drawable.o_14));
        overlayList.add(new diwali_FrameModel(R.drawable.o_15, R.drawable.o_15));
        overlayList.add(new diwali_FrameModel(R.drawable.o_16, R.drawable.o_16));
        overlayList.add(new diwali_FrameModel(R.drawable.o_17, R.drawable.o_17));
        overlayList.add(new diwali_FrameModel(R.drawable.o_18, R.drawable.o_18));

    }

    private void setArraylistForTheme() {
        themeList = new ArrayList<>();
        themeList.add(new diwali_FrameModel(R.drawable.ic_panel_none, R.drawable.ic_panel_none));
        themeList.add(new diwali_FrameModel(R.drawable.t_e1, R.drawable.e1));
        themeList.add(new diwali_FrameModel(R.drawable.t_e2, R.drawable.e2));
        themeList.add(new diwali_FrameModel(R.drawable.t_e3, R.drawable.e3));
        themeList.add(new diwali_FrameModel(R.drawable.t_e4, R.drawable.e4));
        themeList.add(new diwali_FrameModel(R.drawable.t_e5, R.drawable.e5));
        themeList.add(new diwali_FrameModel(R.drawable.t_e6, R.drawable.e6));
        themeList.add(new diwali_FrameModel(R.drawable.t_e7, R.drawable.e7));
        themeList.add(new diwali_FrameModel(R.drawable.t_e8, R.drawable.e8));
        themeList.add(new diwali_FrameModel(R.drawable.t_e9, R.drawable.e9));
        themeList.add(new diwali_FrameModel(R.drawable.t_e10, R.drawable.e10));
        themeList.add(new diwali_FrameModel(R.drawable.t_e11, R.drawable.e11));
        themeList.add(new diwali_FrameModel(R.drawable.t_e12, R.drawable.e12));
        themeList.add(new diwali_FrameModel(R.drawable.t_e13, R.drawable.e13));
        themeList.add(new diwali_FrameModel(R.drawable.t_e14, R.drawable.e14));
        themeList.add(new diwali_FrameModel(R.drawable.t_e15, R.drawable.e15));
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.iv_text:
                removeBorder();

                seekThemeOpacity.setVisibility(GONE);
                seekBrightness.setVisibility(GONE);
                grid_Theme.setVisibility(GONE);
                grid_Frame.setVisibility(GONE);


                diwali_AddTextDialog diwaliAddTextDialog = new diwali_AddTextDialog(this, "", _addtext);
                diwaliAddTextDialog.show();
                break;
            case R.id.iv_camera:
                removeBorder();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkAndRequestPermissions()) {
                        opencamera();
                        // carry on the normal flow, as the case of  permissions  granted.
                    }
                } else {
                    opencamera();
                }
                break;

            case R.id.iv_gallery:
//                showFullAd();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        openGallery();
                    } else if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

//                        *//**
//                         * request for permission
//                         * MY_REQUEST_CODE some unique constant integer
//                         *//*
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_REQUEST_CODE);
                    }
                } else {
                    openGallery();
                }
                break;

            case R.id.iv_Save:

                removeBorder();

                seekThemeOpacity.setVisibility(GONE);
                seekBrightness.setVisibility(GONE);
                grid_Theme.setVisibility(GONE);
                grid_Frame.setVisibility(GONE);

                grid_Overlay.setVisibility(GONE);

                new SaveImageTask1(diwali_ImageEditingActivity.this).execute();


                break;

            case R.id.iv_imagesave:
                // showFullAd();

                // imagesave();

                removeBorder();


                new SaveImageTask(diwali_ImageEditingActivity.this).execute();

                //Toast.makeText(this, "Your Image is Save", Toast.LENGTH_LONG).show();
//                adViewfb.setVisibility(View.GONE);
                /*seekThemeOpacity.setVisibility(View.GONE);
                seekBrightness.setVisibility(View.GONE);
                grid_Theme.setVisibility(View.GONE);
                grid_Overlay.setVisibility(View.GONE);
                if (isImgSelected) {
                    removeBorder();
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {
                            create_Save_Image();
                        } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            *//**
             * request for permission
             * MY_REQUEST_CODE some unique constant integer
             *//*
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_REQUEST_CODE);
                        }
                    } else {
                    }
                } else {
                    Toast.makeText(this, "Image is not selected ", Toast.LENGTH_LONG).show();
                }*/
                break;
            case R.id.iv_frame:

                if(isOnline())
                {
                   // showFullAd();
                }

                removeBorder();
                if (flagForFrame) {
                    grid_Theme.setVisibility(View.GONE);
                    grid_Frame.setVisibility(View.VISIBLE);
                    flagForFrame = false;
                } else {
                    grid_Frame.setVisibility(View.GONE);
                    flagForFrame = true;

                }
                seekBrightness.setVisibility(GONE);
                seekThemeOpacity.setVisibility(GONE);
                grid_Overlay.setVisibility(GONE);

                break;

              /*case R.id.iv_effect:
                //showFullAd();
                removeBorder();
                if (flagForFrame) {
                    grid_Frame.setVisibility(View.GONE);
                    grid_Theme.setVisibility(View.VISIBLE);
                    flagForFrame = false;
                } else {
                    grid_Theme.setVisibility(GONE);

                    flagForFrame = true;

                }

                seekBrightness.setVisibility(GONE);
                seekThemeOpacity.setVisibility(GONE);
                grid_Overlay.setVisibility(GONE);

                break;*/

            case R.id.iv_overlay:
                //   showFullAd();
                removeBorder();
                if (flagForOverlay) {
                    grid_Overlay.setVisibility(View.VISIBLE);
                    flagForOverlay = false;
                } else {
                    grid_Overlay.setVisibility(GONE);
                    flagForOverlay = true;

                }

                seekBrightness.setVisibility(GONE);
                seekThemeOpacity.setVisibility(GONE);
                grid_Theme.setVisibility(GONE);
                grid_Frame.setVisibility(GONE);

                break;

            case R.id.iv_brightness:
                removeBorder();

                //   showFullAd();
                if (flagForBrightness) {
                    seekBrightness.setVisibility(View.VISIBLE);
                    seekThemeOpacity.setVisibility(GONE);
                    grid_Theme.setVisibility(GONE);
                    grid_Frame.setVisibility(GONE);

                    grid_Overlay.setVisibility(GONE);
                    flagForFrame = true;
                    flagForBrightness = false;
//                    flagForSticker = true;/                    flagForFrame = true;

                }
                else
                {
                    seekBrightness.setVisibility(GONE);
                    seekThemeOpacity.setVisibility(GONE);
                    grid_Theme.setVisibility(GONE);
                    grid_Frame.setVisibility(GONE);

                    flagForBrightness = true;

                    flagForFrame = true;
                }

                break;
        }
    }

    //create bitmap image from flContainer
    private void create_Save_Image() {

//        Toast.makeText(this, "Your Image is Save", Toast.LENGTH_LONG).show();
        //  Log.v("TAG", "saveImageInCache is called");
        finalEditedBitmapImage = getMainFrameBitmap();
        saveImage(finalEditedBitmapImage);
        Intent i2 = new Intent(diwali_ImageEditingActivity.this, diwali_ImageSaveFinalActivity.class);
        startActivity(i2);
      /*  try {
            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    private void imagesave() {

        //Toast.makeText(this, "Your Image is Save", Toast.LENGTH_LONG).show();
        Log.v("TAG", "saveImageInCache is called");
        finalEditedBitmapImage = getMainFrameBitmap();
        saveImage(finalEditedBitmapImage);
    }

    private Bitmap getMainFrameBitmap() {

        mainFrm.setDrawingCacheEnabled(true);

        Bitmap bitmap = Bitmap.createBitmap(mainFrm.getDrawingCache());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bitmap.setConfig(Bitmap.Config.ARGB_8888);
        }
        mainFrm.setDrawingCacheEnabled(false);
        Bitmap bmp = bitmap;

        int imgHeight = bmp.getHeight();
        int imgWidth = bmp.getWidth();
        int smallX = 0, largeX = imgWidth, smallY = 0, largeY = imgHeight;
        int left = imgWidth, right = imgWidth, top = imgHeight, bottom = imgHeight;
        for (int i = 0; i < imgWidth; i++) {
            for (int j = 0; j < imgHeight; j++) {
                if (bmp.getPixel(i, j) != Color.TRANSPARENT) {
                    if ((i - smallX) < left) {
                        left = (i - smallX);
                    }
                    if ((largeX - i) < right) {
                        right = (largeX - i);
                    }
                    if ((j - smallY) < top) {
                        top = (j - smallY);
                    }
                    if ((largeY - j) < bottom) {
                        bottom = (largeY - j);
                    }
                }
            }
        }
        Log.d("Trimed bitmap", "left:" + left + " right:" + right + " top:" + top + " bottom:" + bottom);
        bmp = Bitmap.createBitmap(bmp, left, top, imgWidth - left - right, imgHeight - top - bottom);
        return bmp;


    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void saveImage(Bitmap bitmap2) {
        Log.e(TAG, "saveImage: ");
        isAlreadySave = true;
        Log.v("TAG", "saveImageInCache is called");
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
        String FileName = ts + ".jpg";
        File file = new File(dir, FileName);
        file.renameTo(file);
        String _uri = "file://" + filepath.getAbsolutePath() + "/" + diwali_Utility.Edit_Folder_name + "/" + FileName;
        //for share image
        String _uri2 = filepath.getAbsolutePath() + "/" + diwali_Utility.Edit_Folder_name + "/" + FileName;
        _url = _uri2;//used in share image
        Log.d("cache uri=", _uri);
        try {

            output = new FileOutputStream(file);

            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
            //finish();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(_uri))));
//            Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void removeBorder() {
        for (int i = 0; i < stickerviewId.size(); i++) {
            View view = mainFrm.findViewById(stickerviewId.get(i));
            if (view instanceof diwali_diwali_StickerImageView) {
                diwali_diwali_StickerImageView stickerView = (diwali_diwali_StickerImageView) view;
                stickerView.setControlItemsHidden(true);
            }
            if (view instanceof diwali_diwali_StickerTextView) {
                diwali_diwali_StickerTextView stickerView = (diwali_diwali_StickerTextView) view;
                stickerView.setControlItemsHidden(true);
            }
        }
    }

    private int text_id;
    AddText _addtext = new AddText() {
        @Override
        public void setTextview(String _text, int _color, String font, String texture) {

            final diwali_diwali_StickerTextView tv = new diwali_diwali_StickerTextView(diwali_ImageEditingActivity.this);
            tv.setText(_text);

            mainFrm.addView(tv);
            tv.setColor(_color);
            tv.setFont(font);
            Random r = new Random();
            text_id = r.nextInt();
            if (text_id < 0) {
                text_id = text_id - (text_id * 2);
            }
            tv.setId(text_id);
            stickerviewId.add(text_id);
        }

        @Override
        public void selectcolor(final String _text, final String font) {
            final AmbilWarnaDialog dialog = new AmbilWarnaDialog(diwali_ImageEditingActivity.this, 0xFF4488CC, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    // color is the color selected by the user.
                    diwali_AddTextDialog diwaliAddTextDialog = new diwali_AddTextDialog(diwali_ImageEditingActivity.this, _addtext, color, _text, font);
                    diwaliAddTextDialog.show();
                }

                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                    // cancel was selected by the user
                    diwali_AddTextDialog diwaliAddTextDialog = new diwali_AddTextDialog(diwali_ImageEditingActivity.this, _addtext, 0xFF4488CC, _text, font);
                    diwaliAddTextDialog.show();
                }
            });
            dialog.show();
        }

    };

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RE_GALLERY);
    }

    private void opencamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, "opencamera: ", ex);
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created

            if (photoFile != null)
            {
                if(Build.VERSION.SDK_INT <= 17 )
                {
                    // Do some stuff
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(cameraIntent, RE_CAMERA);

                }
                else
                {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(diwali_ImageEditingActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider", photoFile));
                    startActivityForResult(cameraIntent, RE_CAMERA);
                }

            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPG_" + timeStamp + "_";
        File filepath = Environment.getExternalStorageDirectory();
        File storageDir = new File(filepath.getAbsolutePath() + "/" +diwali_Utility.Edit_Folder_name + "/Camera");
        storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:/" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FINAL_SAVE:
                    bundle = data.getExtras();
                    if (bundle.getBoolean("ToHome"))
                        finish();
                    break;

                    case RE_CAMERA:
                          isImgSelected = true;

                        if (mCurrentPhotoPath != null)
                        {
                          Uri selectedUri = Uri.parse(new File(mCurrentPhotoPath).toString());
                            if (selectedUri != null) {
                                startCropActivity(selectedUri);
                            } else {
                                Toast.makeText(diwali_ImageEditingActivity.this,"can not find image", Toast.LENGTH_SHORT).show();
                            }
                        }

                        break;
                    case RE_GALLERY:
                        Uri uri = data.getData();
                        mCurrentPhotoPath = diwali_Utility.getRealPathFromUri(this, uri);
                         isImgSelected = true;
                        if (mCurrentPhotoPath != null) {
                    /* 1) Create a new Intent */

                            final Uri selectedUri = Uri.parse(new File(mCurrentPhotoPath.replace("file:/", "")).toString());
                            if (uri != null) {
                                startCropActivity(uri);
                            } else {
                                Toast.makeText(diwali_ImageEditingActivity.this,"can not find image", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }

                if (requestCode == UCrop.REQUEST_CROP)
                {
                    handleCropResult(data);
                }

            }
    }

    private void startCropActivity(@NonNull Uri uri)
    {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);

        uCrop.start(diwali_ImageEditingActivity.this);
    }

    /**
     * In most cases you need only to set crop aspect ration and max size for resulting image.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    private UCrop basisConfig(@NonNull UCrop uCrop) {



        return uCrop;
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();

        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);



        return uCrop.withOptions(options);
    }

    private void handleCropResult(@NonNull Intent result)
    {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null)
        {
            diwali_Utility.finalurl = resultUri;

//            SetImageView(Utility.finalurl);

            if(isFirst ==false)
            {
                isFirst = true;
                ivImg1.setImageURI(null);
                ivImg1.setImageURI(finalurl);
            }
            else  if(isFirst ==true)
            {
                isFirst = false;
                ivImg.setImageURI(null);
                ivImg.setImageURI(finalurl);
            }
        }
        else
        {
            Toast.makeText(diwali_ImageEditingActivity.this,"not found", Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    void SetImageView(Uri url) {
        ivImg.setImageURI(url);
        ivImg.setOnTouchListener(new diwali_MultiTouchListener());
        //ivImg.bringToFront();
    }

    private Bitmap RotateImage(Bitmap _bitmap, int angle) {

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        _bitmap = Bitmap.createBitmap(_bitmap, 0, 0, _bitmap.getWidth(), _bitmap.getHeight(), matrix, true);
        return _bitmap;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == MY_REQUEST_CODE) {
//            Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted");
                        opencamera();
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("SMS and Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
                break;
            }

            case MY_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_REQUEST_CODE);
                    }
                }
                break;
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void setBlackAndWhite(ImageView iv, int value) {

        float brightness = (float) (value - 255);

        mainMatrix[4] = brightness;
        mainMatrix[9] = brightness;
        mainMatrix[14] = brightness;
        ColorFilter colorFilter = new ColorMatrixColorFilter(mainMatrix);
        iv.setColorFilter(colorFilter);
    }

    private void setcolor(ImageView iv, int value) {
        float redValue = ((float) value) / 255;
        mainMatrix[0] = redValue;
        ColorFilter colorFilter = new ColorMatrixColorFilter(mainMatrix);
        iv.setColorFilter(colorFilter);
    }

    private void setSaturation(ImageView iv, int value) {
        float Saturation = value - 10;
        Log.d("colorvalue:", Saturation + " >> " + value);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(Saturation);
        Log.d("saturaion >>", Arrays.toString(colorMatrix.getArray()));

        float[] saturationMatrix = colorMatrix.getArray();

        mainMatrix[0] = saturationMatrix[0];
        mainMatrix[1] = saturationMatrix[1];
        mainMatrix[2] = saturationMatrix[2];

        mainMatrix[5] = saturationMatrix[5];
        mainMatrix[6] = saturationMatrix[6];
        mainMatrix[7] = saturationMatrix[7];

        mainMatrix[10] = saturationMatrix[10];
        mainMatrix[11] = saturationMatrix[11];
        mainMatrix[12] = saturationMatrix[12];

        ColorFilter colorFilter = new ColorMatrixColorFilter(mainMatrix);
        iv.setColorFilter(colorFilter);

    }


    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
/*
        Intent i = new Intent(ImageEditingActivity.this, FtameListActivity.class);
        startActivity(i);*/
        finish();
    }

    public class SaveImageTask extends AsyncTask<Void, Void, Void> {
        Context context;

        public SaveImageTask(Context context) {
            this.context = context;
            // dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(diwali_ImageEditingActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Please Wait...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            imagesave();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            try {
                if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(context, "Your Image is Save", Toast.LENGTH_SHORT).show();
        }
    }



    private class SaveImageTask1 extends AsyncTask<Void, Void, Void>
    {
        Context context;

            public SaveImageTask1(Context context) {
            this.context = context;
            // dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(diwali_ImageEditingActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Please Wait...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SAveData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            try {
                if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(context, "Your Image is Save", Toast.LENGTH_SHORT).show();
        }
    }

    private void SAveData() {
        // showFullAd();

//                adViewfb.setVisibility(View.GONE);

        if (isImgSelected) {

            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    create_Save_Image();
                } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    /**
                     * request for permission
                     * MY_REQUEST_CODE some unique constant integer
                     */
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_REQUEST_CODE);
                }
            } else {
                create_Save_Image();
            }
        }
        else
        {
            Toast.makeText(this, "Image is not selected ", Toast.LENGTH_LONG).show();
        }
    }
}

