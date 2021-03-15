package com.bestpearlstudio.diwaliphotoframe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class diwali_DisplayCropActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diwali_activity_display_crop);

        ImageView im_crop = (ImageView) findViewById(R.id.im_crop);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        im_crop.setImageBitmap(bmp);
    }
}
