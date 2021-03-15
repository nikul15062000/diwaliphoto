package com.bestpearlstudio.diwaliphotoframe;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



/**
 * Created by PARTH DESAI on 3/16/2017.
 */
public class diwali_CustomList extends ArrayAdapter<String> {
    private Activity dactivity;
    private String[] id;
    private String[] message;
    private String[] appicon;
    private  String[] applink;


    // private String[] emails;
    private Activity context;

    public diwali_CustomList(Activity context, String[] id, String[] message, String[] appicon, String[] applink) {
        super(context, R.layout.diwali_list_view_layout, id);
        this.context = context;
        this.id = id;
        this.message = message;
        this.appicon = appicon;
        this.applink = applink;
        // this.emails = emails;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.diwali_list_view_layout, null, true);
//        TextView textViewId = (TextView) listViewItem.findViewById(R.id.textViewId);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        // TextView textViewEmail = (TextView) listViewItem.findViewById(R.id.textViewEmail);
        ImageView img = (ImageView) listViewItem.findViewById(R.id.img);

//        TextView link = (TextView) listViewItem.findViewById(R.id.link);


       /* Picasso.with(context)
                .load(appicon[position])
                .into(img);
*/
//        textViewId.setText(id[position]);
        textViewName.setText(message[position]);
        //  link.setText(applink[position]);
        // textViewEmail.setText(emails[position]);

        return listViewItem;
    }
}