package com.bestpearlstudio.diwaliphotoframe;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.bestpearlstudio.diwaliphotoframe.utility.diwali_Utility.byteArray;

public class diwali_FacecropActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener
{
    ImageView im_crop_image_view;
    Path clipPath;
    Bitmap bmp;
    Bitmap alteredBitmap;
    Canvas canvas;
    Paint paint;
    float downx = 0;
    float downy = 0;
    float tdownx = 0;
    float tdowny = 0;
    float upx = 0;
    float upy = 0;
    long lastTouchDown = 0;
    int CLICK_ACTION_THRESHHOLD = 100;
    Display display;
    Point size;
    int screen_width, screen_height;
    Button btn_ok;
    ArrayList<diwali_CropModel> diwaliCropModelArrayList;
    float smallx, smally, largex, largey;
    Paint cpaint;
    Bitmap temporary_bitmap;
    private ProgressDialog pDialog;
    String path;
    private ImageView icon;

    ProgressDialog dialog;



    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diwali_facecut_acitvity);
        init();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        int cx = (screen_width - bmp.getWidth()) >> 1;
        int cy = (screen_height - bmp.getHeight()) >> 1;
        canvas.drawBitmap(bmp, cx, cy, null);

        //im_crop_image_view.setImageBitmap(path);
        im_crop_image_view.setOnTouchListener(this);
    }

    private void showCroppedImage(String mImagePath) {
        if (mImagePath != null) {
            bmp = BitmapFactory.decodeFile(mImagePath);
            im_crop_image_view.setImageBitmap(bmp);
        }
    }

    private void init() {
        Intent intent = getIntent();
        path = intent.getStringExtra(diwali_GOTOConstants.IntentExtras.IMAGE_PATH);
        pDialog = new ProgressDialog(diwali_FacecropActivity.this);
        im_crop_image_view = (ImageView) findViewById(R.id.im_crop_image_view);
        diwaliCropModelArrayList = new ArrayList<>();
        btn_ok = (Button) findViewById(R.id.btn_ok);
        icon = (ImageView) findViewById(R.id.icon);
        icon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                icon.setVisibility(View.GONE);
                return false;
            }
        });
        btn_ok.setOnClickListener(this);
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        screen_width = size.x;
        screen_height = size.y;
        showCroppedImage(path);
        initcanvas();
    }

    public void initcanvas() {
        alteredBitmap = Bitmap.createBitmap(screen_width, screen_height, bmp.getConfig());
        canvas = new Canvas(alteredBitmap);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{15.0f, 15.0f}, 0));

    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:


                downx = event.getX();
                downy = event.getY();
                clipPath = new Path();
                clipPath.moveTo(downx, downy);
                tdownx = downx;
                tdowny = downy;
                smallx = downx;
                smally = downy;
                largex = downx;
                largey = downy;
                lastTouchDown = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_MOVE:

                upx = event.getX();
                upy = event.getY();
                diwaliCropModelArrayList.add(new diwali_CropModel(upx, upy));
                clipPath = new Path();
                clipPath.moveTo(tdownx, tdowny);
                for (int i = 0; i < diwaliCropModelArrayList.size(); i++) {
                    clipPath.lineTo(diwaliCropModelArrayList.get(i).getY(), diwaliCropModelArrayList.get(i).getX());
                }
                canvas.drawPath(clipPath, paint);
                im_crop_image_view.invalidate();
                downx = upx;
                downy = upy;
                break;
            case MotionEvent.ACTION_UP:
                if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {
                    diwaliCropModelArrayList.clear();
                    initcanvas();
                    int cx = (screen_width - bmp.getWidth()) >> 1;
                    int cy = (screen_height - bmp.getHeight()) >> 1;
                    canvas.drawBitmap(bmp, cx, cy, null);
                    im_crop_image_view.setImageBitmap(alteredBitmap);

                } else {
                    if (upx != upy) {
                        upx = event.getX();
                        upy = event.getY();


                        canvas.drawLine(downx, downy, upx, upy, paint);
                        clipPath.lineTo(upx, upy);
                        im_crop_image_view.invalidate();

                        crop();
                    }

                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    public void crop() {

        clipPath.close();
        clipPath.setFillType(Path.FillType.INVERSE_WINDING);

        for (int i = 0; i < diwaliCropModelArrayList.size(); i++) {
            if (diwaliCropModelArrayList.get(i).getY() < smallx) {

                smallx = diwaliCropModelArrayList.get(i).getY();
            }
            if (diwaliCropModelArrayList.get(i).getX() < smally) {

                smally = diwaliCropModelArrayList.get(i).getX();
            }
            if (diwaliCropModelArrayList.get(i).getY() > largex) {

                largex = diwaliCropModelArrayList.get(i).getY();
            }
            if (diwaliCropModelArrayList.get(i).getX() > largey) {

                largey = diwaliCropModelArrayList.get(i).getX();
            }
        }

        temporary_bitmap = alteredBitmap;
        cpaint = new Paint();
        cpaint.setAntiAlias(true);
        cpaint.setColor(getResources().getColor(R.color.colorAccent));
        cpaint.setAlpha(100);
        canvas.drawPath(clipPath, cpaint);

        canvas.drawBitmap(temporary_bitmap, 0, 0, cpaint);

    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_ok:
//                save();
//
//            default:
//                break;
//        }
//
//    }


    private void save() {

        if (clipPath != null) {
            final int color = Color.BLUE;
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPath(clipPath, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            canvas.drawBitmap(alteredBitmap, 0, 0, paint);

            float w = largex - smallx;
            float h = largey - smally;
            alteredBitmap = Bitmap.createBitmap(alteredBitmap, (int) smallx, (int) smally, (int) w, (int) h);

        } else {
            alteredBitmap = bmp;
        }

                Bitmap bitmap = alteredBitmap;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
                byteArray = stream.toByteArray();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:

                new SaveImageTask(diwali_FacecropActivity.this).execute();



            default:
                break;
        }
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
            dialog = new ProgressDialog(diwali_FacecropActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Please Wait...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            save();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            Intent intent = new Intent(diwali_FacecropActivity.this, diwali_FtameListActivity.class);
            startActivity(intent);

        }
    }
}
