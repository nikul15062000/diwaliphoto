package com.bestpearlstudio.diwaliphotoframe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.bestpearlstudio.diwaliphotoframe.utility.diwali_Utility;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author GT
 */
public class diwali_ImageViewActivity extends Activity implements diwali_PicModeSelectDialogFragment.IPicModeSelectListener {

    public static final String TAG = "ImageViewActivity";
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static final int REQUEST_CODE_UPDATE_PIC = 0x1;
    private String imgUri;

    private Button mBtnUpdatePic ,btnmyart,btnrate,btnmore;

    private String mCurrentPhotoPath;
    private File mFileTemp,photoFile;

    private static final int MY_REQUEST_CODE = 5;

    private static final int RE_CAMERA = 1;
    private static final int RE_GALLERY = 2;
    public static final int REQUEST_CODE_CROPPED_PICTURE = 0x3;

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 4;

    public static final String ERROR_MSG = "error_msg";
    public static final String ERROR = "error";

    private static final String SAMPLE_CROPPED_IMAGE_NAME = "divaliphotoframe";

    String[] perms = {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"};

    private AdView mAdView;
    LinearLayout nativeAdContainer;
    private InterstitialAd mInterstitialAd;
    public static com.google.android.gms.ads.InterstitialAd mNativeadd;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diwali_activity_image_view);

        mBtnUpdatePic = (Button) findViewById(R.id.btnUpdatePic);
        btnmyart = (Button) findViewById(R.id.btnmyart);
        btnrate = (Button) findViewById(R.id.btnrate);
        btnmore = (Button) findViewById(R.id.btnmore);

        nativeAdContainer = (LinearLayout)findViewById(R.id.native_ad_container);
        showGOOGLEAdvance(nativeAdContainer);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });


        btnmyart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(diwali_ImageViewActivity.this, diwali_MyArt_activity.class);
                startActivity(intent);
            }
        });
        btnrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/developer?id="+getPackageName()));
                startActivity(i);
            }
        });

        btnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/developer?id=best+Pearl+Studio"));
                startActivity(i);
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (isOnline())
        {
           // showfbbanner();
        }
        else
        {
            mAdView.setVisibility(View.GONE);
        }

        mBtnUpdatePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                showAddProfilePicDialog();
            }
        });

        checkPermissions();
    }

    public boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void showfbbanner()
    {
        final AdRequest adRequest = new AdRequest.Builder()
                    .build();

            mAdView.loadAd(adRequest);

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                }

                @Override
                public void onAdLoaded() {

                    mAdView.setVisibility(View.VISIBLE);
                    //mAdView.loadAd(adRequest);
                    super.onAdLoaded();

                }
            });
        }

    @SuppressLint("InlinedApi")
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(perms, 1234);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //--------Private methods --------
    private void showAddProfilePicDialog() {
        diwali_PicModeSelectDialogFragment dialogFragment = new diwali_PicModeSelectDialogFragment();
        dialogFragment.setiPicModeSelectListener(this);
        dialogFragment.show(getFragmentManager(), "picModeSelector");
    }

    private void actionProfilePic(String action)
    {
        if (null != action) {
            switch (action) {
                case diwali_GOTOConstants.IntentExtras.ACTION_CAMERA:
                    getIntent().removeExtra("ACTION");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkAndRequestPermissions()) {
                            opencamera();
                            // carry on the normal flow, as the case of  permissions  granted.
                        }
                    } else {
                        opencamera();
                    }
                    return;
                case diwali_GOTOConstants.IntentExtras.ACTION_GALLERY:
                    getIntent().removeExtra("ACTION");
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
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_REQUEST_CODE);
                        }
                    } else {
                        openGallery();
                    }
                    return;
            }
        }

      //  Intent intent = new Intent(this, ImageCropActivity.class);
      //  intent.putExtra("ACTION", action);
       // startActivityForResult(intent, REQUEST_CODE_UPDATE_PIC);
    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
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

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(diwali_ImageViewActivity.this,
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
        File storageDir = new File(filepath.getAbsolutePath() + "/" + diwali_Utility.Edit_Folder_name + "/Camera");
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
    @Override
        public void onPicModeSelected (String mode){
            String action = mode.equalsIgnoreCase(diwali_GOTOConstants.PicModes.CAMERA) ? diwali_GOTOConstants.IntentExtras.ACTION_CAMERA : diwali_GOTOConstants.IntentExtras.ACTION_GALLERY;
            actionProfilePic(action);
        }
    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RE_GALLERY);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RE_CAMERA:
                    //  isImgSelected = true;
                    if (mCurrentPhotoPath != null) {
//
                        final Uri selectedUri = Uri.parse(new File(mCurrentPhotoPath).toString());
                        if (selectedUri != null) {
                            startCropActivity(selectedUri);
                        } else {
                            Toast.makeText(diwali_ImageViewActivity.this,"can not find image", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case RE_GALLERY:
                    Uri uri = data.getData();
                    mCurrentPhotoPath = diwali_Utility.getRealPathFromUri(this, uri);
                    // isImgSelected = true;
                    if (mCurrentPhotoPath != null) {
                    /* 1) Create a new Intent */
                        final Uri selectedUri = Uri.parse(new File(mCurrentPhotoPath.replace("file:/", "")).toString());
                        if (uri != null) {
                            startCropActivity(uri);
                        } else {
                            Toast.makeText(diwali_ImageViewActivity.this,"can not find image", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
            if (requestCode == UCrop.REQUEST_CROP)
            {
                handleCropResult(data);
            }
        }}
    private void startCropActivity(@NonNull Uri uri)
    {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);

        uCrop.start(diwali_ImageViewActivity.this);
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

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            //ResultActivity.startWithUri(MainActivity.this, resultUri);
            diwali_Utility.finalurl = resultUri;
            Intent intent = new Intent(diwali_ImageViewActivity.this, diwali_FtameListActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(diwali_ImageViewActivity.this,"not found", Toast.LENGTH_SHORT).show();
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
