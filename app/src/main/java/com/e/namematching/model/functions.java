package com.e.namematching.model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaDrm;
import android.media.UnsupportedSchemeException;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.util.Random;
import java.util.UUID;

public class functions {

    public functions() {}

    public String getUniqueID() {
        Random r = new Random();

        StringBuilder sb = new StringBuilder(10);

        for(int i = 0; i < 10; i++) {
            char tmp = (char) ('a' + r.nextInt('z' - 'a'));
            sb.append(tmp);
        }

        return sb.toString() + String.valueOf(r.nextInt()) + String.valueOf(r.nextInt());
    }


    //출처: https://taehyun71.tistory.com/21 [코딩하는 블로그]

    public String bitmapToString(Bitmap bitmap) {
        if (bitmap!=null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte [] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array,Base64.DEFAULT);
        }

        return "";
    }

    public Bitmap StringToBitmap(String str) {
        try {
            byte[] decode = Base64.decode(str, 0);
            return BitmapFactory.decodeByteArray(decode, 0, decode.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
