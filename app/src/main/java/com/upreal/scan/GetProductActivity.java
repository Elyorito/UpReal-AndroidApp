package com.upreal.scan;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.upreal.R;
import com.upreal.product.ProductActivity;
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.Product;
import com.upreal.utils.SoapProductManager;

import java.io.ByteArrayOutputStream;

/**
 * Created by Elyo on 19/09/15.
 */
public class GetProductActivity extends AppCompatActivity {

    private ConnectionDetector cd;
    AnimatedCircleLoadingView animatedCircleLoadingView;
    private byte[] mBytes = null;
    private String mImageFileLocation;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getproduct);
        context = this;
        cd = new ConnectionDetector(context);
//        mBytes = getIntent().getExtras().getByteArray("bytes");
        mImageFileLocation = getIntent().getExtras().getString("imageLocation");
        // Get Bitmap from file location
        Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Resize
        photoCapturedBitmap = Bitmap.createScaledBitmap(photoCapturedBitmap, 400, 700, false);
        photoCapturedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        mBytes = stream.toByteArray();
        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        animatedCircleLoadingView.startIndeterminate();
        if (cd.isConnectedToInternet()) {
            new RetrieveProductFromImage().execute(mBytes);
        } else
            Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.retry_retrieve_connection), Toast.LENGTH_SHORT).show();
//        animatedCircleLoadingView.setVisibility(View.VISIBLE);
//        animatedCircleLoadingView.resetLoading();
    }

    private class RetrieveProductFromImage extends AsyncTask<byte[], Void, Product> {

        Product product = new Product();
        @Override
        protected Product doInBackground(byte[]... params) {

            SoapProductManager pm = new SoapProductManager();
            product = pm.scanProduct(params[0]);
            return product;
        }

        @Override
        protected void onPostExecute(Product product) {
            super.onPostExecute(product);
            String not = "notfound";
            if (product == null || product.getName() == null || not.equals(product.getName())) {
                if (context != null) {
                    Intent intent = new Intent(getApplicationContext(), AddProductFromScan.class);
                    intent.putExtra("listprod", new Product(""));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                    finish();
                }
            }
            else {
                if (context != null) {
                    Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                    intent.putExtra("listprod", product);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                    finish();
                }
            }
        }
    }
}
