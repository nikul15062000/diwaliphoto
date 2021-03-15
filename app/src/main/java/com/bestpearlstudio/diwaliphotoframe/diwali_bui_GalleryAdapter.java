package com.bestpearlstudio.diwaliphotoframe;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class diwali_bui_GalleryAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Activity dactivity;
    private int imageSize;
    ArrayList<String> imagegallary = new ArrayList();
    SparseBooleanArray mSparseBooleanArray;
    MediaMetadataRetriever metaRetriever;
    View vi;

    static class ViewHolder {
        ImageView imgDelete;
        ImageView imgIcon;
        ImageView imgShare;
        ViewHolder() {
        }
    }

    public diwali_bui_GalleryAdapter(Activity dAct, ArrayList<String> dUrl) {
        this.dactivity = dAct;
        this.imagegallary = dUrl;
        inflater = (LayoutInflater) this.dactivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mSparseBooleanArray = new SparseBooleanArray(this.imagegallary.size());
    }

    public int getCount() {
        return this.imagegallary.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(this.dactivity).inflate(R.layout.diwali_bui_list_gallery, parent, false);
            holder = new ViewHolder();
            holder.imgIcon = row.findViewById(R.id.imgIcon);
            holder.imgDelete = row.findViewById(R.id.imgDelete);
            holder.imgShare = row.findViewById(R.id.imgShare);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.imgShare.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Builder alertDialogBuilder = new Builder(diwali_bui_GalleryAdapter.this.dactivity);
                alertDialogBuilder.setTitle("Share");
                alertDialogBuilder.setMessage("Do You Want to Share ?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        diwali_bui_GalleryAdapter.this.shareImage(dactivity.getResources().getString(R.string.app_name) + " Created By :\"https://play.google.com/store/apps/developer?id="+dactivity.getPackageName(), diwali_bui_GalleryAdapter.this.imagegallary.get(position));
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialogBuilder.create().show();
            }
        });

        holder.imgDelete.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Builder alertDialogBuilder = new Builder(diwali_bui_GalleryAdapter.this.dactivity);
                alertDialogBuilder.setTitle("Delete");
                alertDialogBuilder.setMessage("Do You Want to Delete ?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File fD = new File(diwali_bui_GalleryAdapter.this.imagegallary.get(position));
                        if (fD.exists()) {
                            fD.delete();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                Uri contentUri = Uri.fromFile(fD);
                                mediaScanIntent.setData(contentUri);
                                dactivity.sendBroadcast(mediaScanIntent);
                            } else {
                                dactivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                            }
                        }
                        diwali_bui_GalleryAdapter.this.imagegallary.remove(position);
                        diwali_bui_GalleryAdapter.this.notifyDataSetChanged();
                        if (diwali_bui_GalleryAdapter.this.imagegallary.size() == 0) {
                            Toast.makeText(diwali_bui_GalleryAdapter.this.dactivity, "No Image Found..", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialogBuilder.create().show();
            }
        });
        Glide.with(this.dactivity).load(this.imagegallary.get(position)).into(holder.imgIcon);
        System.gc();
        return row;
    }

    public void shareImage(final String title, String path) {
        MediaScannerConnection.scanFile(this.dactivity, new String[]{path}, null, new OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                Intent shareIntent = new Intent("android.intent.action.SEND");
                shareIntent.setType("image/*");
                shareIntent.putExtra("android.intent.extra.TEXT", title);
                shareIntent.putExtra("android.intent.extra.STREAM", uri);
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                diwali_bui_GalleryAdapter.this.dactivity.startActivity(Intent.createChooser(shareIntent, "Share Image"));
            }
        });
    }
}
