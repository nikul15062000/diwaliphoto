package com.bestpearlstudio.diwaliphotoframe.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by pc1 on 14/4/16.
 */
public class diwali_Utility {


    public static ProgressDialog progressDialog;
    public static ImageView.ScaleType IMG_CurrentScaletype;
    public static int DEVICE_WIDTH;
    public static int DEVICE_HEIGHT;
    public static String Edit_Folder_name = "diwaliDualPhotoFrame";

    public static byte[] byteArray;

    public static Uri finalurl;

    public static String getRealPathFromUri(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();


        int columnIndex = cursor.getColumnIndex(projection[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    public static int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH) / 2;
        int left = (int) (imgViewW - actW) / 2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Typeface GetBLACKJAR(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/BLACKJAR.TTF");
        return custom_font;
    }

    public static Typeface GetBLKCHCRY(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/BLKCHCRY.TTF");
        return custom_font;
    }

    public static Typeface Getconstanb(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/constanb.TTF");
        return custom_font;
    }

    public static Typeface Gethemi_head(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/hemi_head.TTF");
        return custom_font;
    }

    public static Typeface Gethotpizza(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/hotpizza.TTF");
        return custom_font;
    }

    public static Typeface GetRINGM(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/RINGM.TTF");
        return custom_font;
    }

    public static Typeface GetSFSportsNightNS(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/SFSportsNightNS.TTF");
        return custom_font;
    }

    public static Typeface GetShindlerFont(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/ShindlerFont.TTF");
        return custom_font;
    }

    public static Typeface GetScriptbl(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/SCRIPTBL.ttf");
        return custom_font;
    }

    public static Typeface GetFont3(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/font3.ttf");
        return custom_font;
    }

    public static Typeface GetFont5(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/font5.ttf");
        return custom_font;
    }

    public static Typeface GetFont6(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/font6.ttf");
        return custom_font;
    }

    public static Typeface GetFont8(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/font8.ttf");
        return custom_font;
    }

    public static Typeface GetFont16(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/font16.ttf");
        return custom_font;
    }

    public static Typeface GetFont17(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/font17.ttf");
        return custom_font;
    }

    public static Typeface GetFont20(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/font20.ttf");
        return custom_font;
    }

    public static Typeface GetFont29(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/font29.ttf");
        return custom_font;
    }

    public static Typeface GetFont30(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/font30.ttf");
        return custom_font;
    }

    public static Typeface GetFont32(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/font32.ttf");
        return custom_font;
    }

    public static Typeface GetFont34(Context _context) {
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(), "font/font34.ttf");
        return custom_font;
    }

    static void showProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    static void dissmissDialog() {
        if (progressDialog.isShowing() && progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    Log.w("INTERNET:", String.valueOf(i));
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.w("INTERNET:", "connected!");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
