package com.bestpearlstudio.diwaliphotoframe;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PARTH DESAI on 3/16/2017.
 */
public class diwali_ParseJSON {

    public static String[] id;
    public static String[] message;
    public static String[] appicon;
    public  static  String[] applink;

    //  public static String[] emails;

    public static final String JSON_ARRAY = "posts";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "message";
    public static final String KEY_ICON = "appicon";
    public  static  final String KEY_LINK = "applink";

    //public static final String KEY_EMAIL = "email";

    private JSONArray users = null;

    private String json;

    public diwali_ParseJSON(String json){
        this.json = json;
    }

    protected void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);

            id = new String[users.length()];
            message = new String[users.length()];
            // emails = new String[users.length()];

            appicon = new String[users.length()];
            applink = new String[users.length()];

            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                //id[i] = jo.getString(KEY_ID);
                message[i] = jo.getString(KEY_NAME);
                appicon[i] = jo.getString(KEY_ICON);
                applink[i] = jo.getString(KEY_LINK);
                Log.e("appicon","appicon:"+appicon);
                // emails[i] = jo.getString(KEY_EMAIL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
