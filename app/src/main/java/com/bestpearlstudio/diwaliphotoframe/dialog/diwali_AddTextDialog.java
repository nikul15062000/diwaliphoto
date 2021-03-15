package com.bestpearlstudio.diwaliphotoframe.dialog;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bestpearlstudio.diwaliphotoframe.diwali_ImageEditingActivity;
import com.bestpearlstudio.diwaliphotoframe.R;
import com.bestpearlstudio.diwaliphotoframe.utility.diwali_Utility;


/**
 * Created by lcom56 on 4/11/15.
 */
public class diwali_AddTextDialog extends Dialog implements View.OnClickListener {
    public Context context;
    public Button positive;
    public Button negative;
    TextView color;
    Spinner font;
    //Spinner texture;
    EditText edit_new_text;
    String _text;
    diwali_ImageEditingActivity.AddText _interface;
    String selected_font = "NexaLight";
    int _color;
    String[] fonatarr = {"Select font", "Black Jar", "BLK CHCRY", "Sans serif",  "Normal", "Themi_head", "ShindlerFont", "Font Style1", "Font Style2", "Font Style4", "Font Style6", "Font Style11"};
    String[] texturearry = {"NONE", "CHEETAH", "GREENCUP", "CONGRUENTOUTLINE", "NEWYEAR", "TREEBARK", "OLDMAP", "CONGRUENTPENTAGON", "FLOWER1", "FLOWER2", "PATTERN1", "PATTERN2", "PATTERN3", "PATTERN4", "PATTERN5", "PATTERN6"};
    String selected_texture;

    public diwali_AddTextDialog(Context context, String text, diwali_ImageEditingActivity.AddText _interface) {
        super(context);
        this.context = context;
        this._interface = _interface;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCanceledOnTouchOutside(false);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.setContentView(R.layout.diwali_newtextdialog);
        //  texture = (Spinner) findViewById(R.id.texture);
        positive = (Button) findViewById(R.id.done_text);
        positive.setOnClickListener(this);
        negative = (Button) findViewById(R.id.cancle_text);
        negative.setOnClickListener(this);
        font = (Spinner) findViewById(R.id.font);
        edit_new_text = (EditText) findViewById(R.id.edit_new_text_dialog);
        edit_new_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (null != edit_new_text.getLayout() && edit_new_text.getLayout().getLineCount() > 2) {
                    edit_new_text.getText().delete(edit_new_text.getText().length() - 1, edit_new_text.getText().length());
                }
            }
        });
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(context, R.layout.diwali_dropdown_item, fonatarr, "Sample Text");
        font.setAdapter(spinnerAdapter);

        //  texture.setAdapter(new SpinnerTextureAdapter(context, R.layout.diwali_dropdown_item, texturearry, "Texture"));

        font.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                if (!fonatarr[position].equalsIgnoreCase("Select font"))
                    selected_font = fonatarr[position];
                if (selected_font.equalsIgnoreCase("Black Jar")) {
                    edit_new_text.setTypeface(diwali_Utility.GetBLACKJAR(getContext()));
                }
                if (selected_font.equalsIgnoreCase("BLK CHCRY")) {
                    edit_new_text.setTypeface(diwali_Utility.GetBLKCHCRY(getContext()));
                }
                if (selected_font.equalsIgnoreCase("Sans serif")) {
                    edit_new_text.setTypeface(Typeface.SANS_SERIF);
                }
                if (selected_font.equalsIgnoreCase("Normal")) {
                    edit_new_text.setTypeface(Typeface.DEFAULT);
                }
                if (selected_font.equalsIgnoreCase("Themi_head")) {
                    edit_new_text.setTypeface(diwali_Utility.Gethemi_head(getContext()));
                }
                if (selected_font.equalsIgnoreCase("ShindlerFont")) {
                    edit_new_text.setTypeface(diwali_Utility.GetShindlerFont(getContext()));
                }
                if (selected_font.equalsIgnoreCase("Font Style1")) {
                    edit_new_text.setTypeface(diwali_Utility.GetFont3(getContext()));
                }
                if (selected_font.equalsIgnoreCase("Font Style2")) {
                    edit_new_text.setTypeface(diwali_Utility.GetFont5(getContext()));
                }
                if (selected_font.equalsIgnoreCase("Font Style4")) {
                    edit_new_text.setTypeface(diwali_Utility.GetFont8(getContext()));
                }
                if (selected_font.equalsIgnoreCase("Font Style6")) {
                    edit_new_text.setTypeface(diwali_Utility.GetFont17(getContext()));
                }
                if (selected_font.equalsIgnoreCase("Font Style11")) {
                    edit_new_text.setTypeface(diwali_Utility.GetFont34(getContext()));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

      /*  texture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                if (!texturearry[position].equalsIgnoreCase("Texture"))
                    selected_texture = texturearry[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });*/
        color = (TextView) findViewById(R.id.color);
        color.setOnClickListener(this);
        this._color = 0xFF4488CC;

        Drawable background = color.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().
                    setColor(_color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).
                    setColor(_color);
        }
        //color.setBackgroundColor(_color);

        edit_new_text.setText(text);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public diwali_AddTextDialog(Context context, diwali_ImageEditingActivity.AddText _interface, int _color, String _text, String fontApply) {
        super(context);
        this._interface = _interface;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCanceledOnTouchOutside(false);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.setContentView(R.layout.diwali_newtextdialog);
        this._color = _color;
        this._text = _text;
        this.selected_font = fontApply;

        edit_new_text = (EditText) findViewById(R.id.edit_new_text_dialog);
        positive = (Button) findViewById(R.id.done_text);
        positive.setOnClickListener(this);
        negative = (Button) findViewById(R.id.cancle_text);
        negative.setOnClickListener(this);
        font = (Spinner) findViewById(R.id.font);
        //texture = (Spinner) findViewById(R.id.texture);


        //  texture.setAdapter(new SpinnerTextureAdapter(context, R.layout.diwali_dropdown_item, texturearry, "Texture"));


       /* texture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                if (!texturearry[position].equalsIgnoreCase("Texture"))
                    selected_texture = texturearry[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });*/
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(context, R.layout.diwali_dropdown_item, fonatarr, "Sample Text");
        font.setAdapter(spinnerAdapter);

        font.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                if (!fonatarr[position].equalsIgnoreCase("Select font"))
                    selected_font = fonatarr[position];
                if (selected_font.equalsIgnoreCase("Black Jar")) {
                    edit_new_text.setTypeface(diwali_Utility.GetBLACKJAR(getContext()));
                }
                if (selected_font.equalsIgnoreCase("BLK CHCRY")) {
                    edit_new_text.setTypeface(diwali_Utility.GetBLKCHCRY(getContext()));
                }
                if (selected_font.equalsIgnoreCase("Sans serif")) {
                    edit_new_text.setTypeface(Typeface.SANS_SERIF);
                }
                if (selected_font.equalsIgnoreCase("Normal")) {
                    edit_new_text.setTypeface(Typeface.DEFAULT);
                }
                if (selected_font.equalsIgnoreCase("Themi_head")) {
                    edit_new_text.setTypeface(diwali_Utility.Gethemi_head(getContext()));
                }
                if (selected_font.equalsIgnoreCase("ShindlerFont")) {
                    edit_new_text.setTypeface(diwali_Utility.GetShindlerFont(getContext()));
                }
                if (selected_font.equalsIgnoreCase("Font Style1")) {
                    edit_new_text.setTypeface(diwali_Utility.GetFont3(getContext()));
                }
                if (selected_font.equalsIgnoreCase("Font Style2")) {
                    edit_new_text.setTypeface(diwali_Utility.GetFont5(getContext()));
                }
                if (selected_font.equalsIgnoreCase("Font Style4")) {
                    edit_new_text.setTypeface(diwali_Utility.GetFont8(getContext()));
                }
                if (selected_font.equalsIgnoreCase("Font Style6")) {
                    edit_new_text.setTypeface(diwali_Utility.GetFont17(getContext()));
                }
                if (selected_font.equalsIgnoreCase("Font Style11")) {
                    edit_new_text.setTypeface(diwali_Utility.GetFont34(getContext()));
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        color = (TextView) findViewById(R.id.color);
        color.setOnClickListener(this);

        Drawable background = color.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().
                    setColor(_color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).
                    setColor(_color);
        }
        color.setBackground(background);
        edit_new_text.setText(_text);
        edit_new_text.setTextColor(_color);
        setFont(selected_font);

    }

    private void setFont(String selected_font) {
        if (selected_font.equalsIgnoreCase("Black Jar")) {
            font.setSelection(1);
            edit_new_text.setTypeface(diwali_Utility.GetBLACKJAR(getContext()));
        }
        if (selected_font.equalsIgnoreCase("BLK CHCRY")) {
            font.setSelection(2);
            edit_new_text.setTypeface(diwali_Utility.GetBLKCHCRY(getContext()));
        }
        if (selected_font.equalsIgnoreCase("Sans serif")) {
            font.setSelection(3);
            edit_new_text.setTypeface(Typeface.SANS_SERIF);
        }
        if (selected_font.equalsIgnoreCase("Normal")) {
            font.setSelection(4);
            edit_new_text.setTypeface(Typeface.DEFAULT);
        }

        if (selected_font.equalsIgnoreCase("Themi_head")) {
            font.setSelection(5);
            edit_new_text.setTypeface(diwali_Utility.Gethemi_head(getContext()));
        }
        if (selected_font.equalsIgnoreCase("ShindlerFont")) {
            font.setSelection(6);
            edit_new_text.setTypeface(diwali_Utility.GetShindlerFont(getContext()));
        }
        if (selected_font.equalsIgnoreCase("Font Style1")) {
            font.setSelection(7);
            edit_new_text.setTypeface(diwali_Utility.GetFont3(getContext()));
        }
        if (selected_font.equalsIgnoreCase("Font Style2")) {
            font.setSelection(8);
            edit_new_text.setTypeface(diwali_Utility.GetFont5(getContext()));
        }
        if (selected_font.equalsIgnoreCase("Font Style4")) {
            font.setSelection(9);
            edit_new_text.setTypeface(diwali_Utility.GetFont8(getContext()));
        }
        if (selected_font.equalsIgnoreCase("Font Style6")) {
            font.setSelection(10);
            edit_new_text.setTypeface(diwali_Utility.GetFont17(getContext()));
        }
        if (selected_font.equalsIgnoreCase("Font Style11")) {
            font.setSelection(11);
            edit_new_text.setTypeface(diwali_Utility.GetFont34(getContext()));
        }
    }


    private boolean isValid() {
        if (edit_new_text.getText().toString() != null && edit_new_text.getText().toString().trim().length() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == positive) {
            if (isValid()) {
                String text = edit_new_text.getText().toString();
                dismiss();
                _interface.setTextview(text, _color, selected_font, selected_texture);
                //onBackPressed();
            } else {
                edit_new_text.setError("Please enter text.");
            }
        }
        if (v == negative) {
            dismiss();
        }
        if (v == color) {
            dismiss();
            String text = edit_new_text.getText().toString();
            _interface.selectcolor(text, selected_font);

        }
    }

    public class SpinnerAdapter extends ArrayAdapter<String> {
        String text;
        String[] fontArr;

        public SpinnerAdapter(Context context, int textViewResourceId, String[] objects, String text) {
            super(context, textViewResourceId, objects);
            this.text = text;
            this.fontArr = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.diwali_dropdown_item, parent, false);
            TextView label = (TextView) row.findViewById(R.id.ab_drop_menu_item_title);
            label.setText(fontArr[position]);
            if (fontArr[position].equalsIgnoreCase("Black Jar")) {
                label.setTypeface(diwali_Utility.GetBLACKJAR(getContext()));
            }
            if (fontArr[position].equalsIgnoreCase("BLK CHCRY")) {
                label.setTypeface(diwali_Utility.GetBLKCHCRY(getContext()));
            }
            if (fontArr[position].equalsIgnoreCase("Sans serif")) {
                label.setTypeface(Typeface.SANS_SERIF);
            }
            if (fontArr[position].equalsIgnoreCase("Normal")) {
                label.setTypeface(Typeface.DEFAULT);
            }
            if (fontArr[position].equalsIgnoreCase("Themi head")) {
                label.setTypeface(diwali_Utility.Gethemi_head(getContext()));
            }
            if (fontArr[position].equalsIgnoreCase("ShindlerFont")) {
                label.setTypeface(diwali_Utility.GetShindlerFont(getContext()));
            }

            if (fontArr[position].equalsIgnoreCase("Font Style1")) {
                label.setTypeface(diwali_Utility.GetFont3(getContext()));
            }
            if (fontArr[position].equalsIgnoreCase("Font Style2")) {
                label.setTypeface(diwali_Utility.GetFont5(getContext()));
            }
            if (fontArr[position].equalsIgnoreCase("Font Style4")) {
                label.setTypeface(diwali_Utility.GetFont8(getContext()));
            }
            if (fontArr[position].equalsIgnoreCase("Font Style6")) {
                label.setTypeface(diwali_Utility.GetFont17(getContext()));
            }
            if (fontArr[position].equalsIgnoreCase("Font Style11")) {
                label.setTypeface(diwali_Utility.GetFont34(getContext()));
            }
//            Bitmap bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.cheetah_tile);
//            Shader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//            label.getPaint().setShader(shader);
            return row;
        }
    }


    public class SpinnerTextureAdapter extends ArrayAdapter<String> {
        String text;
        String[] textureArr;


        public SpinnerTextureAdapter(Context context, int textViewResourceId, String[] objects, String text) {
            super(context, textViewResourceId, objects);
            this.text = text;
            this.textureArr = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.diwali_dropdown_item, parent, false);
            TextView label = (TextView) row.findViewById(R.id.ab_drop_menu_item_title);
            label.setText(textureArr[position]);


            return row;
        }


    }

    public static void setTextureArr(Context context, TextView textView, int drawable) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawable);
        Shader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        textView.getPaint().setShader(shader);
    }
}
