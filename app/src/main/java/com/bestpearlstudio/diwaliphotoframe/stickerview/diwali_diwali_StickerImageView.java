package com.bestpearlstudio.diwaliphotoframe.stickerview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class diwali_diwali_StickerImageView extends diwali_StickerView {

    private String owner_id;
    private ImageView iv_main;

    public diwali_diwali_StickerImageView(Context context) {
        super(context);
    }

    public diwali_diwali_StickerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public diwali_diwali_StickerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOwnerId(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwnerId() {
        return this.owner_id;
    }

    @Override
    public View getMainView() {
        if (this.iv_main == null) {
            this.iv_main = new ImageView(getContext());
            this.iv_main.setScaleType(ImageView.ScaleType.FIT_XY);
            
        }
        return iv_main;
    }

    public void setImageBitmap(Bitmap bmp) {
//        this.iv_main.setImageBitmap(bmp);
        Glide.with(getContext())
                .load(String.valueOf(bmp))
                .into(this.iv_main);
    }

    public void setImageResource(int res_id) {
//        Glide.with(getContext())
//                .load(res_id)
//                .into(this.iv_main);
        this.iv_main.setImageResource(res_id);
    }

    public void setImageDrawable(Drawable drawable) {
        this.iv_main.setImageDrawable(drawable);
    }

    public Bitmap getImageBitmap() {
        return ((BitmapDrawable) this.iv_main.getDrawable()).getBitmap();
    }

    public void setImageURI(Uri finalurl)
    {
        this.iv_main.setImageURI(finalurl);
    }
}
