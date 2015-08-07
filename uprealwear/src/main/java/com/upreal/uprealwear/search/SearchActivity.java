package com.upreal.uprealwear.search;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.upreal.uprealwear.R;
import com.upreal.uprealwear.server.ProductManager;
import com.upreal.uprealwear.utils.ListItemAdapter;
import com.upreal.uprealwear.utils.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 28/07/2015.
 */
public class SearchActivity extends Activity implements WearableListView.ClickListener {

    private List<Product> absProduct;

    private WearableListView listView;
    private ListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.util_list);

        absProduct = new ArrayList<Product>();
        listView = (WearableListView) findViewById(R.id.wearable_list);
        adapter = new ListItemAdapter(this, absProduct);

        new RetrieveList().execute();
        listView.setAdapter(adapter);
        listView.setClickListener(this);
    }

    @Override
    public void onClick(WearableListView.ViewHolder v) {
        Integer tag = (Integer) v.itemView.getTag();
    }

    @Override
    public void onTopEmptyRegionClick() {
    }

    private class RetrieveList extends AsyncTask<Void, Void, List<Product>> {

        @Override
        protected List<Product> doInBackground(Void... params) {

            ProductManager pm = new ProductManager();
            List<Product> pList = pm.getProduct("");

            for (Product p : pList) {
                p.setPicture(pm.getProductPicture(p.getId()));
            }

            return pList;
        }

        @Override
        protected void onPostExecute(List<Product> res) {
            super.onPostExecute(res);
            Log.e("SearchActivity", "WebService called. Result:");
            absProduct.clear();
            for (Product p : res) {
                absProduct.add(p);
                Log.e("SearchActivity", p.getId() + ":" + p.getName() + " // " + p.getPicture());
            }
            adapter.notifyDataSetChanged();
        }
    }
}
