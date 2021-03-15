package com.bestpearlstudio.diwaliphotoframe.stickerview;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.bestpearlstudio.diwaliphotoframe.stickerview.util.diwali_AutoResizeTextView;
import com.bestpearlstudio.diwaliphotoframe.utility.diwali_Utility;


/**
 * Created by cheungchingai on 6/15/15.
 */
public class diwali_diwali_StickerTextView extends diwali_StickerView {
    private diwali_AutoResizeTextView tv_main;


    public diwali_diwali_StickerTextView(Context context) {
        super(context);
    }

    public diwali_diwali_StickerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public diwali_diwali_StickerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View getMainView() {
        if (tv_main != null)
            return tv_main;

        tv_main = new diwali_AutoResizeTextView(getContext());
        tv_main.setTextSize(300);
        // tv_main.setTextColor(Color.WHITE);
        tv_main.setGravity(Gravity.CENTER);
        tv_main.setMinTextSize(30);
        //  tv_main.setTextSize(100);
        tv_main.setShadowLayer(4, 0, 0, Color.BLACK);
        tv_main.setLines(1);
        // tv_main.setMaxLines(1);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;
        tv_main.setLayoutParams(params);
        if (getImageViewFlip() != null)
            getImageViewFlip().setVisibility(GONE);
        return tv_main;
    }

    public void setText(String text) {
        if (tv_main != null)
            tv_main.setText(text);
        if (text.contains("\n")) {
            tv_main.setMaxLines(2);
        }
    }

    public String getText() {
        if (tv_main != null)
            return tv_main.getText().toString();

        return null;
    }

    public void setColor(int _color) {
        tv_main.setTextColor(_color);
    }

    public void setFont(String font) {
        if (font.equalsIgnoreCase("BLACKJAR")) {
            tv_main.setTypeface(diwali_Utility.GetBLACKJAR(getContext()), Typeface.BOLD);
        }
        if (font.equalsIgnoreCase("Black Jar")) {
            tv_main.setTypeface(diwali_Utility.GetBLACKJAR(getContext()));
        }
        if (font.equalsIgnoreCase("BLK CHCRY")) {
            tv_main.setTypeface(diwali_Utility.GetBLKCHCRY(getContext()));
        }
        if (font.equalsIgnoreCase("Constanb")) {
            tv_main.setTypeface(diwali_Utility.Getconstanb(getContext()));
        }
        if (font.equalsIgnoreCase("Sans serif")) {
            tv_main.setTypeface(Typeface.SANS_SERIF);
        }
        if (font.equalsIgnoreCase("Monospace")) {
            tv_main.setTypeface(Typeface.MONOSPACE);
        }
        if (font.equalsIgnoreCase("Serif")) {
            tv_main.setTypeface(Typeface.SERIF);
        }
        if (font.equalsIgnoreCase("Normal")) {
            tv_main.setTypeface(Typeface.DEFAULT);
        }

        if (font.equalsIgnoreCase("hemi_head")) {
            tv_main.setTypeface(diwali_Utility.Gethemi_head(getContext()));
        }
        if (font.equalsIgnoreCase("hotpizza")) {
            tv_main.setTypeface(diwali_Utility.Gethotpizza(getContext()));
        }
        if (font.equalsIgnoreCase("RINGM")) {
            tv_main.setTypeface(diwali_Utility.GetRINGM(getContext()));
        }

        if (font.equalsIgnoreCase("SFSportsNightNS")) {
            tv_main.setTypeface(diwali_Utility.GetSFSportsNightNS(getContext()));
        }

        if (font.equalsIgnoreCase("ShindlerFont")) {
            tv_main.setTypeface(diwali_Utility.GetShindlerFont(getContext()));
        }
        if (font.equalsIgnoreCase("Font Style1")) {
            tv_main.setTypeface(diwali_Utility.GetFont3(getContext()));
        }
        if (font.equalsIgnoreCase("Font Style2")) {
            tv_main.setTypeface(diwali_Utility.GetFont5(getContext()));
        }
        if (font.equalsIgnoreCase("Font Style3")) {
            tv_main.setTypeface(diwali_Utility.GetFont6(getContext()));
        }
        if (font.equalsIgnoreCase("Font Style4")) {
            tv_main.setTypeface(diwali_Utility.GetFont8(getContext()));
        }
        if (font.equalsIgnoreCase("Font Style5")) {
            tv_main.setTypeface(diwali_Utility.GetFont16(getContext()));
        }
        if (font.equalsIgnoreCase("Font Style6")) {
            tv_main.setTypeface(diwali_Utility.GetFont17(getContext()));
        }
        if (font.equalsIgnoreCase("Font Style7")) {
            tv_main.setTypeface(diwali_Utility.GetFont20(getContext()));
        }
        if (font.equalsIgnoreCase("Font Style8")) {
            tv_main.setTypeface(diwali_Utility.GetFont29(getContext()));
        }
        if (font.equalsIgnoreCase("Font Style9")) {
            tv_main.setTypeface(diwali_Utility.GetFont30(getContext()));
        }
        if (font.equalsIgnoreCase("Font Style10")) {
            tv_main.setTypeface(diwali_Utility.GetFont32(getContext()));
        }
        if (font.equalsIgnoreCase("Font Style11")) {
            tv_main.setTypeface(diwali_Utility.GetFont34(getContext()));
        }
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    @Override
    protected void onScaling(boolean scaleUp) {
        super.onScaling(scaleUp);
    }
}
