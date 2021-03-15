package com.bestpearlstudio.diwaliphotoframe;

import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by PARTH DESAI on 3/11/2017.
 */
public class diwali_Gallary_Adapter extends BaseAdapter {

    private Activity dactivity;
    private static LayoutInflater inflater = null;
    SparseBooleanArray mSparseBooleanArray;
    ArrayList<String> imagegallary = new ArrayList<String>();
    MediaMetadataRetriever metaRetriever;
    View vi;
    private int imageSize;

    public diwali_Gallary_Adapter(Activity dAct, ArrayList<String> dUrl) {
        dactivity = dAct;
        this.imagegallary = dUrl;

        inflater = (LayoutInflater) dactivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSparseBooleanArray = new SparseBooleanArray(imagegallary.size());
    }

    public int getCount() {
        return imagegallary.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;
        DisplayMetrics metrics = dactivity.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        if (row == null) {

            row = LayoutInflater.from(dactivity).inflate(R.layout.diwali_list_gallary, parent, false);
            holder = new ViewHolder();

            holder.image = (ImageView) row.findViewById(R.id.imgSaved);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Glide.with(dactivity)
                .load(imagegallary.get(position))
                .centerCrop()

                .crossFade()
                .into(holder.image);


        System.gc();
        return row;

    }

    static class ViewHolder {

        ImageView image;

    }
}

