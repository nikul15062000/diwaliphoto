package com.bestpearlstudio.diwaliphotoframe.utility;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class diwali_Utilities {

    public static ArrayList<String> IMAGEALLARY = new ArrayList<String>();
    public static float MinTime;
    public static float MaxTime;
    public static float TotalTime;
    public static float TotalSlowTime;
    public static float TotalFastTime;
    public static int SplitVideo;
    public static boolean forAct;
    public static float AudioStartTime;
    public static float AudioTotalTime;
    public static String newRotate;


    public static boolean createDirIfNotExists(String onlypath) {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), onlypath);
        if (!file.exists()) {
            file.mkdirs();
            if (!file.mkdirs()) {
                Log.e("Problem :: ", "Problem creating Image folder");
                ret = false;
            } else {
                Log.v("Done directory ", "Path - " + onlypath);
            }
        }
        return ret;
    }

    public static void DeleteRecursive(File fileOrDirectory) {
        Log.e("In delete REcursive", "In delete REcursive");
        if (fileOrDirectory.exists()) {
            Log.i("Deleted..", "Deleted...");
            if (fileOrDirectory.isDirectory()) {
                for (File child : fileOrDirectory.listFiles())
                    DeleteRecursive(child);
            }
            fileOrDirectory.delete();
            Log.e("Delete this Path" + fileOrDirectory, "Delete this...");
        }
    }

    public static void listAllImages(File filepath) {
        File[] files = filepath.listFiles();
        if (files != null) {

            for (int j = files.length - 1; j >= 0; j--) {
                String ss = files[j].toString();
                File check = new File(ss);
                Log.d("" + check.length(), "" + check.length());
                if (check.length() > 1024) {
                    if (check.toString().contains(".jpg") || check.toString().contains(".png") || check.toString().contains(".jpg")) {
                        IMAGEALLARY.add(ss);
                    }
                } else {
                    Log.i("Invalid Image", "Delete Image");
                }
                System.out.println(ss);
            }
        } else

        {
            System.out.println("Empty Folder");
        }

    }

    public static void SaveScreenShot(Bitmap finalBitmap) {

        String timeStamp = new SimpleDateFormat("HH-mm-ss_dd-MM-yyyy").format(new Date());
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/VEditor/Images/");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String fname = String.format("SnapShot_" + timeStamp + ".jpg");
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
