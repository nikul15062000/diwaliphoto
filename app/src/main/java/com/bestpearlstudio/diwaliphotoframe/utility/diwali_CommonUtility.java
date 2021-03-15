package com.bestpearlstudio.diwaliphotoframe.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bestpearlstudio.diwaliphotoframe.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class diwali_CommonUtility {

//    Sha1 Key :: 73:9B:C2:9E:C7:D6:08:5F:3B:52:31:37:07:C6:6E:FB:8C:21:D4:11

    public static Button btn_ok,rat;
    public static LayoutInflater m_inflater;
    public static View m_view;
    public static TextView textView, txt_title;
    public static String ImgyoutubePopup, ImgYoutubeTag;
    public static String ImgAllPopup;


    public static void setFont(Context activity, TextView textView) {

        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/BulletionBord.ttf");
        textView.setTypeface(font);
    }

    public static void setFont(Context activity, RadioButton textView) {

        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/BulletionBord.ttf");
        textView.setTypeface(font);
    }
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isValidEmail(String target) {

        Matcher matcher = Pattern.compile(EMAIL_PATTERN).matcher(target);

        return matcher.matches();

    }



    public static void showAlertDialog(final Activity activity, String alert,
                                       String title, final boolean finish) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        m_inflater = LayoutInflater.from(activity);

        m_view = diwali_CommonUtility.m_inflater
                .inflate(R.layout.diwali_dialog_common, null);

        textView = (TextView) m_view.findViewById(R.id.success_txt);
        txt_title = (TextView) m_view.findViewById(R.id.txt_title);

        txt_title.setText(title);
        textView.setText(alert);

        btn_ok = (Button) m_view.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
                activity. startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

//                if (finish)
//                    activity.finish();

            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.CommonDialog; //style id

        dialog.setContentView(m_view);
        dialog.show();

    }
}