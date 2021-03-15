package com.bestpearlstudio.diwaliphotoframe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by pmb on 13/7/16.
 */
public class diwali_ThemeAdapter extends BaseAdapter {
    Context context;
    ArrayList<diwali_FrameModel> frames;

    private LayoutInflater inflater;

    public diwali_ThemeAdapter(Context context, ArrayList<diwali_FrameModel> frames) {
        this.context = context;
        this.frames = frames;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return frames.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        if (convertView == null) {
            convertView = inflater.inflate(R.layout.diwali_theme_item, null);
        }
        ImageView img_editing = (ImageView) convertView.findViewById(R.id.img_theme);

        Bitmap mBitmap = null;
//        if (img_editing != null) {
//            if (mBitmap != null && !mBitmap.isRecycled()) {
//                mBitmap.recycle();
//                mBitmap = null;
//            }
//            ((BitmapDrawable) img_editing.getDrawable()).getBitmap().recycle();
//        }
        int resource = frames.get(position).getThumbId();
        mBitmap = BitmapFactory.decodeResource(
                context.getResources(), resource);
        img_editing.setImageBitmap(mBitmap);
        //     img_editing.setImageResource(resource);
        return convertView;
    }



}
