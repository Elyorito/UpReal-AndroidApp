package com.upreal.scan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.upreal.R;
import com.upreal.home.HomeActivity;
import com.upreal.product.ProductActivity;
import com.upreal.utils.Product;
import com.upreal.utils.SendImageTask;
import com.upreal.utils.SoapProductManager;
import com.upreal.utils.SoapProductUtilManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by Elyo on 24/04/2015.
 */
public class AddProductFromScan extends Activity implements View.OnClickListener{

    private static final int ACTIVITY_START_CAMERA = 0;

    private EditText productName;
    private EditText brand;
    private EditText desc;
    private EditText barcode;
    private EditText noticedPrice;
    private EditText shopNearby;
    private ImageView imageProduct;

    private String barcodeEAN;
    private Bitmap bitmap;
    private byte[] image;

    private String mImageFileLocation;


    private AlertDialog.Builder builder;
    private Button cancel;
    private Button sendProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_add_product);

        bitmap = null;
        image = null;

        builder = new AlertDialog.Builder(AddProductFromScan.this);
        barcodeEAN = getIntent().getExtras().getString("ean");
        productName = (EditText) findViewById(R.id.product_name);
        brand = (EditText) findViewById(R.id.brand);
        desc = (EditText) findViewById(R.id.description);
        barcode = (EditText) findViewById(R.id.barcode);
        noticedPrice = (EditText) findViewById(R.id.noticedprice);
        shopNearby = (EditText) findViewById(R.id.attheshop);
        imageProduct = (ImageView) findViewById(R.id.image_product);

        // Setting Initial values
        productName.setText("");
        brand.setText("");
        desc.setText("");
        barcode.setText("");
        noticedPrice.setText("");
        shopNearby.setText("");

        cancel = (Button) findViewById(R.id.addprodcancel);
        sendProduct = (Button) findViewById(R.id.addprodok);

        cancel.setOnClickListener(this);
        sendProduct.setOnClickListener(this);
        if (barcodeEAN != null)
            barcode.setText(barcodeEAN.toString());
        imageProduct.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addprodcancel:
                finish();
                break;
            case R.id.image_product:
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = HomeActivity.createImageFile();
                    mImageFileLocation = photoFile.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, ACTIVITY_START_CAMERA);
/*
                Intent intent = new Intent(v.getContext(), Camera2Activity.class);
                intent.putExtra("type", "addImage");
                startActivityForResult(intent, GET_IMAGE_REQUEST);
*/
                break;
            case R.id.addprodok:
                new CreateProductFromScan().execute();
                break;
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

    public class CreateProductFromScan extends AsyncTask<Void, Void, Integer> {
        private Product product;
        private int id;
        private String mName;
        private String mBrand;
        private String mDesc;
        private String mNoticedPrice;
        private String mShopNearby;

        public CreateProductFromScan() {
            mName = productName.getText().toString();
            mBrand = brand.getText().toString();
            mDesc = desc.getText().toString();
            mNoticedPrice = noticedPrice.getText().toString();
            mShopNearby = shopNearby.getText().toString();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            SoapProductManager pm = new SoapProductManager();
            id = pm.registerProduct(mName, mBrand, mDesc, barcodeEAN, mNoticedPrice, mShopNearby);
            product = pm.getProductInfo(id);
            return id;
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);

            new CreateSpec().execute(i);
            if (bitmap != null)
                new SendImageTask(mImageFileLocation, image).execute(i);
            builder.setTitle("Produit Ajouté")
                    .setMessage("Votre produit a été ajouté avec succès !" +
                            "Voulez-vous y accéder?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                            intent.putExtra("listprod", product);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create().show();
        }
    }

    public class CreateSpec extends AsyncTask<Integer, Void, Void> {

        private String mDesc;

        public CreateSpec () {
            mDesc = desc.getText().toString();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            SoapProductUtilManager pm = new SoapProductUtilManager();
            pm.createSpecification(params[0], "Description", mDesc);
            return null;
        }
    }
}
