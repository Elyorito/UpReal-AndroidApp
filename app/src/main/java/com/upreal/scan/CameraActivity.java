package com.upreal.scan;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.upreal.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Elyo on 17/05/2015.
 */
public class CameraActivity extends Activity {
    private Intent intent;
    // Camera
    private static final int ACTIVITY_START_CAMERA = 0;
    private String mImageFileLocation = "";

    // Storage
    private static final int PERMISSIONS_REQUEST = 1;
    private File photoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                photoFile = createImageFile();
                mImageFileLocation = photoFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(intent, ACTIVITY_START_CAMERA);
        } else {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(CameraActivity.this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(this, R.string.permission_camera_storage, Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(CameraActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }
    }
    static public File createImageFile() throws IOException {
        String FOLDER_LOCATION = "Upreal Pictures";
        File imageFolder;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";

        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        imageFolder = new File(storageDirectory, FOLDER_LOCATION);
        if (!imageFolder.exists())
            imageFolder.mkdirs();

        File myImage = File.createTempFile(imageFileName, ".jpg", imageFolder);
        return myImage;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("NavigationBar", String.valueOf(requestCode));
        if (requestCode == ACTIVITY_START_CAMERA && resultCode == RESULT_OK) {
/*          Thumbnail
            Bundle extras = data.getExtras();
            Bitmap photoCapturedBitmap = (Bitmap) extras.get("data");
*/
            // Get Bitmap from File
/*
            Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoCapturedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
*/
            intent = new Intent(this, GetProductActivity.class);
//            intent.putExtra("bytes", byteArray);
            intent.putExtra("imageLocation", mImageFileLocation);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // storage-related task you need to do.

                    try {
                        photoFile = createImageFile();
                        mImageFileLocation = photoFile.getAbsolutePath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(intent, ACTIVITY_START_CAMERA);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle(R.string.scan).setMessage(R.string.no_permission).create().show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
