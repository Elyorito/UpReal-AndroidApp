package com.upreal.upreal.scan;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.upreal.upreal.R;
import com.upreal.upreal.product.ProductActivity;
import com.upreal.upreal.utils.Product;
import com.upreal.upreal.utils.SoapProductManager;

/**
 * Created by Elyo on 19/09/15.
 */
public class GetProductActivity extends AppCompatActivity {

     AnimatedCircleLoadingView animatedCircleLoadingView;
    private byte[] mBytes = null;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getproduct);
        context = this;
        mBytes = getIntent().getExtras().getByteArray("bytes");
        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        animatedCircleLoadingView.startIndeterminate();
        new RetrieveProductFromImage().execute(mBytes);
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
            if (product.getName() == null || not.equals(product.getName())) {
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
