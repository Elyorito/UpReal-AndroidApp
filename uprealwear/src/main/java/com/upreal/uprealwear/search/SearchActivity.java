package com.upreal.uprealwear.search;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.upreal.uprealwear.R;
import com.upreal.uprealwear.utils.Product;
import com.upreal.uprealwear.utils.ProductManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 28/07/2015.
 */
public class SearchActivity extends Activity {

    private List<Product> absProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        absProduct = new ArrayList<Product>();

        new RetrieveList().execute();
    }

    private class RetrieveList extends AsyncTask<Void, Void, List<Product>> {

        @Override
        protected List<Product> doInBackground(Void... params) {

            ProductManager pm = new ProductManager();
            return pm.getProduct("");
        }

        @Override
        protected void onPostExecute(List<Product> res) {
            super.onPostExecute(res);
            Log.e("SearchActivity", "WebService called. Result:");
            for (Product p : res) {
                absProduct.add(p);
                Log.e("SearchActivity", p.getId() + ":" + p.getName());
            }
        }
    }
}
