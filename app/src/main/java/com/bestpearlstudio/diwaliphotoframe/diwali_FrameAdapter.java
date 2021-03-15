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
 * Created by PARTH DESAI on 3/11/2017.
 */
public class diwali_FrameAdapter extends BaseAdapter {
    Context context;
    ArrayList<diwali_FrameModel> frames;
    private ViewHolder viewholder;
    private LayoutInflater inflater;

    public diwali_FrameAdapter(Context context, ArrayList<diwali_FrameModel> frames) {
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
        return frames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.diwali_frame_item, null);
        }
        ImageView img_editing = (ImageView) convertView.findViewById(R.id.img_frm);

       // Glide.with(context).load(frames.get(position).getMessage()).placeholder(R.mipmap.ic_launcher).into(img_editing);


        Bitmap mBitmap = null;

        int resource = frames.get(position).getThumbId();
        mBitmap = BitmapFactory.decodeResource(
                context.getResources(), resource);
        img_editing.setImageBitmap(mBitmap);

//        Bitmap mBitmap = null;
////        if (img_editing != null) {
////            if (mBitmap != null && !mBitmap.isRecycled()) {
////                mBitmap.recycle();
////                mBitmap = null;
////            }
////            ((BitmapDrawable) img_editing.getDrawable()).getBitmap().recycle();
////        }
//        int resource = frames.get(position).getThumbId();
//        mBitmap = BitmapFactory.decodeResource(
//                context.getResources(), resource);
//        img_editing.setImageBitmap(mBitmap);
        //     img_editing.setImageResource(resource);
        return convertView;
    }


    public class ViewHolder {
        ImageView img_gallery;
    }
}
