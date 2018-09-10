package com.scribner.util;

import android.os.Environment;

public class FilePaths {

    //"storage/emulated/0"
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public String CAMERA = ROOT_DIR + "/DCIM/camera";

    public String PICTURES  = ROOT_DIR + "/Pictures";

}
