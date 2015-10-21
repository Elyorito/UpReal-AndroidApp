package com.upreal.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by Eric on 30/09/2015.
 */
public class SendImageTask extends AsyncTask<Void, Void, Void> {

    String mImageFileLocation;
    byte[] mImage;
    String mName;

    public SendImageTask(String imageFileLocation, byte[] image, String name) {
        mImageFileLocation = imageFileLocation;
        mImage = image;
        mName = name;
    }

    @Override
    protected Void doInBackground(Void... params) {
        SoapGlobalManager gm = new SoapGlobalManager();
        Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Resize
        photoCapturedBitmap = Bitmap.createScaledBitmap(photoCapturedBitmap, 400, 700, false);
        photoCapturedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        mImage = stream.toByteArray();
        gm.uploadPicture(mImage, mName);
        return null;
    }
}
