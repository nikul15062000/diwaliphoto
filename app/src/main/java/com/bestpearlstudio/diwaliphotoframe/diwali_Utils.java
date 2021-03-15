package com.bestpearlstudio.diwaliphotoframe;

import android.net.Uri;

import java.io.File;

/**
 * @author GT
 */
public class diwali_Utils {

    public static Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }
}
