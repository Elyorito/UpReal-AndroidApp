package com.upreal.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by Eric on 30/09/2015.
 */
public class SendImageTask extends AsyncTask<Integer, Void, Void> {

    String mImageFileLocation;
    byte[] mImage;
    String name;

    public SendImageTask(String imageFileLocation, byte[] image) {
        mImageFileLocation = imageFileLocation;
        mImage = image;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        SoapGlobalManager gm = new SoapGlobalManager();
        Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Resize
        photoCapturedBitmap = Bitmap.createScaledBitmap(photoCapturedBitmap, 400, 700, false);
        photoCapturedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        mImage = stream.toByteArray();
        name = "2_" + params[0];
        gm.uploadPicture(mImage, name);
        return null;
    }
}
