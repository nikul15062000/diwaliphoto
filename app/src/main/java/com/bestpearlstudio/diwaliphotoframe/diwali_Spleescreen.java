package com.bestpearlstudio.diwaliphotoframe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
public class diwali_Spleescreen extends AppCompatActivity {
    private static int DURATION = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diwali_activity_spleescreen);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.diwali_activity_spleescreen);
        new Handler().postDelayed(new handler(), (long) DURATION);


    }
    class handler implements Runnable {
        handler() {
        }
        public void run() {
                Intent intent = new Intent(diwali_Spleescreen.this, diwali_ImageViewActivity.class);
                startActivity(intent);

            finish();
        }
    }
}