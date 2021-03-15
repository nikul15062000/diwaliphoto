package com.bestpearlstudio.diwaliphotoframe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

/*
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
*/

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class diwali_FtameListActivity extends AppCompatActivity
{


    private GridView grid_Frame;
    private diwali_FrameAdapter diwaliFrameAdapter;

    private Activity activity;
    private ProgressDialog pDialog;

    static ArrayList<diwali_FrameModel> themeList;
    private AdView mAdView;
    LinearLayout linearLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diwali_activity_ftame_list);

        activity = diwali_FtameListActivity.this;
        linearLayout = (LinearLayout) findViewById(R.id.ll_Ad_Crop);
//        setArraylistForFrame();

        grid_Frame = (GridView) findViewById(R.id.grid);


        setArraylistForTheme();

        diwaliFrameAdapter = new diwali_FrameAdapter(diwali_FtameListActivity.this, themeList);
        grid_Frame.setAdapter(diwaliFrameAdapter);


//        frameAdapter = new FrameAdapter(FtameListActivity.this, frame);
//        grid_Frame.setAdapter(frameAdapter);

       // mAdView = findViewById(R.id.adView);
       // AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        if (isOnline())
        {
           // mAdView.loadAd(adRequest);

        }
        else
        {
            linearLayout.setVisibility(View.GONE);
        }
        grid_Frame.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(diwali_FtameListActivity.this, diwali_ImageEditingActivity.class);
                intent.putExtra("FrmID", position);
                startActivity(intent);
            }
        });
    }



    private void setArraylistForTheme() {
        themeList = new ArrayList<>();


        themeList.add(new diwali_FrameModel(R.drawable.foto_1, R.drawable.thumb_1));
        themeList.add(new diwali_FrameModel(R.drawable.foto_2, R.drawable.thumb_2));
        themeList.add(new diwali_FrameModel(R.drawable.foto_3, R.drawable.thumb_3));
        themeList.add(new diwali_FrameModel(R.drawable.foto_4, R.drawable.thumb_4));
        themeList.add(new diwali_FrameModel(R.drawable.foto_5, R.drawable.thumb_5));
        themeList.add(new diwali_FrameModel(R.drawable.foto_6, R.drawable.thumb_6));
        themeList.add(new diwali_FrameModel(R.drawable.foto_7, R.drawable.thumb_7));
        themeList.add(new diwali_FrameModel(R.drawable.foto_8, R.drawable.thumb_8));
        themeList.add(new diwali_FrameModel(R.drawable.foto_9, R.drawable.thumb_9));
        themeList.add(new diwali_FrameModel(R.drawable.foto_10, R.drawable.thumb_10));
        themeList.add(new diwali_FrameModel(R.drawable.foto_11, R.drawable.thumb_11));
        themeList.add(new diwali_FrameModel(R.drawable.foto_12, R.drawable.thumb_12));
        themeList.add(new diwali_FrameModel(R.drawable.foto_13, R.drawable.thumb_13));
        themeList.add(new diwali_FrameModel(R.drawable.foto_14, R.drawable.thumb_14));
        themeList.add(new diwali_FrameModel(R.drawable.foto_15, R.drawable.thumb_15));
        themeList.add(new diwali_FrameModel(R.drawable.foto_16, R.drawable.thumb_16));
        themeList.add(new diwali_FrameModel(R.drawable.foto_17, R.drawable.thumb_17));
        themeList.add(new diwali_FrameModel(R.drawable.foto_18, R.drawable.thumb_18));
        themeList.add(new diwali_FrameModel(R.drawable.foto_19, R.drawable.thumb_19));
        themeList.add(new diwali_FrameModel(R.drawable.foto_20, R.drawable.thumb_20));
       /* themeList.add(new FrameModel(R.drawable.thumb_11, R.drawable.foto_11));
        themeList.add(new FrameModel(R.drawable.thumb_12, R.drawable.foto_12));
        themeList.add(new FrameModel(R.drawable.thumb_13, R.drawable.foto_13));
        themeList.add(new FrameModel(R.drawable.thumb_14, R.drawable.foto_14));
        themeList.add(new FrameModel(R.drawable.thumb_15, R.drawable.foto_15));
        themeList.add(new FrameModel(R.drawable.thumb_16, R.drawable.foto_16));
        themeList.add(new FrameModel(R.drawable.thumb_17, R.drawable.foto_17));
        themeList.add(new FrameModel(R.drawable.thumb_18, R.drawable.foto_18));
        themeList.add(new FrameModel(R.drawable.thumb_19, R.drawable.foto_19));
        themeList.add(new FrameModel(R.drawable.thumb_20, R.drawable.foto_20));*/
    }





    /*public void FrameJson() {

        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("please wait.......");
        pDialog.show();

        frameList = new ArrayList<>();

        RequestQueue rQ = Volley.newRequestQueue(activity);

        StringRequest sReq = new StringRequest(Request.Method.POST, "http://androword.com/admin_api/neww.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("androword........................."+response.toString());

                        try {
                            JSONObject joResp = new JSONObject(response);

                            System.out.println("response.." + response);
                            int success = joResp.getInt("success");

                            if (success == 1) {

                                JSONArray jaDetail = joResp.getJSONArray("posts");

                                if (jaDetail.length() > 0) {

                                    for (int i = 0; i < jaDetail.length(); i++) {
                                        JSONObject joDetail = jaDetail.getJSONObject(i);

                                        frameList.add(new FrameModel(joDetail.optString("id"), joDetail.optString("message")));
                                    }


                                    frameAdapter = new FrameAdapter(FtameListActivity.this, frameList);
                                    grid_Frame.setAdapter(frameAdapter);
                                } else {
                                }

                            } else {


                            }

                            pDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            }
        }) {

        };

        sReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        rQ.add(sReq);
    }*/

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }



}
