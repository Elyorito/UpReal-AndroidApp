package com.upreal.product;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.upreal.R;
import com.upreal.home.NavigationBar;
import com.upreal.utils.Product;
import com.upreal.utils.SendImageTask;
import com.upreal.utils.SoapProductManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by Eric on 23/06/2015.
 */
public class ProductUpdateActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int ACTIVITY_START_CAMERA = 0;
    private static final int PERMISSIONS_REQUEST = 1;

    private EditText name;
    private EditText ean;
    private EditText brand;
    private Button cancel;
    private Button update;
    private Product mProduct;
    private ImageView imageProduct;
    private Spinner spinner;

    private Bitmap bitmap;
    private byte[] image;
    private String mImageFileLocation;
    private String category;

    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update);

        mProduct = getIntent().getExtras().getParcelable("prod");

        name = (EditText) findViewById(R.id.name);
        ean = (EditText) findViewById(R.id.code);
        brand = (EditText) findViewById(R.id.brand);
        cancel = (Button) findViewById(R.id.cancel);
        update = (Button) findViewById(R.id.update);
        imageProduct = (ImageView) findViewById(R.id.image_product);
        spinner = (Spinner) findViewById(R.id.spinner);

        // Setting Initial values
        name.setText("");
        brand.setText("");
        ean.setText("");
        new Product.getCategory(spinner, this).execute();
        new Product.getProductCategory(mProduct.getId(), spinner).execute();
        bitmap = null;

        name.setText(mProduct.getName());
        ean.setText(mProduct.getEan());
        brand.setText(mProduct.getBrand());

        spinner.setOnItemSelectedListener(this);
        cancel.setOnClickListener(this);
        update.setOnClickListener(this);
        imageProduct.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.barcode:
                ean.requestFocus();
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.image_product:
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    try {
                        photoFile = NavigationBar.createImageFile();
                        mImageFileLocation = photoFile.getAbsolutePath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(intent, ACTIVITY_START_CAMERA);
                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA)) {

                        Toast.makeText(this, R.string.permission_camera_storage, Toast.LENGTH_SHORT).show();
                    }
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            PERMISSIONS_REQUEST);
                }
                break;
            case R.id.update:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Information non complète");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                if (name.getText().toString().equals("")) {
                    builder.setMessage("Remplissez le nom du produit s'il vous plait");
                    if (name.requestFocus()) {
                        name.setBackgroundColor(getResources().getColor(R.color.red));
                        brand.setBackgroundColor(getResources().getColor(R.color.white));
                        ean.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    builder.show();
                }
                else if (brand.getText().toString().equals("")) {
                    builder.setMessage("Remplissez la marque du produit s'il vous plait");
                    if (brand.requestFocus()) {
                        brand.setBackgroundColor(getResources().getColor(R.color.red));
                        name.setBackgroundColor(getResources().getColor(R.color.white));
                        ean.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    builder.show();
                }
                else if (ean.getText().toString().equals("")) {
                    builder.setMessage("Remplissez le code barre du produit s'il vous plait");
                    if (ean.requestFocus()) {
                        ean.setBackgroundColor(getResources().getColor(R.color.red));
                        brand.setBackgroundColor(getResources().getColor(R.color.white));
                        name.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    builder.show();
                }
                else {
                    mProduct.setName(name.getText().toString());
                    mProduct.setBrand(brand.getText().toString());
                    mProduct.setEan(ean.getText().toString());
                    if (bitmap != null)
                        new SendImageTask(mImageFileLocation, image, "2_" + mProduct.getId()).execute();
                    new Product.setProductCategory(mProduct.getId(), category).execute();
                    new updateProduct(mProduct, this).execute();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private class updateProduct extends AsyncTask<Void, Void, Boolean> {
        Product product;
        Context context;

        public updateProduct(Product product, Context context) {
            this.product = product;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SoapProductManager pm = new SoapProductManager();

            return pm.updateProduct(product);
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(context, "Le produit à bien été changé", Toast.LENGTH_SHORT).show();
                Log.v("Product name", product.getName());
                Log.v("Product brand", product.getBrand());
                Log.v("Product ean", product.getEan());
                finish();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Une erreur s'est produite !")
                        .setMessage("Réessayez plus tard s'il vous plait")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_START_CAMERA) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                int targetImageViewWidth = imageProduct.getWidth();
                int targetImageViewHeight = imageProduct.getHeight();

                BitmapFactory.Options bfOptions = new BitmapFactory.Options();
                bfOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mImageFileLocation, bfOptions);

                int cameraImageWidth = bfOptions.outWidth;
                int cameraImageHeight = bfOptions.outHeight;

                int scaleFactor = Math.min(cameraImageWidth/targetImageViewWidth, cameraImageHeight/targetImageViewHeight);
                bfOptions.inSampleSize = scaleFactor;
                bfOptions.inJustDecodeBounds = false;

                bitmap = BitmapFactory.decodeFile(mImageFileLocation, bfOptions);
                if (bitmap != null)
                    imageProduct.setImageBitmap(bitmap);
            }
        }
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
                        photoFile = NavigationBar.createImageFile();
                        mImageFileLocation = photoFile.getAbsolutePath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
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
