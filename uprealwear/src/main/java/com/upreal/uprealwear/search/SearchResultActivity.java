package com.upreal.uprealwear.search;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.upreal.uprealwear.R;
import com.upreal.uprealwear.server.ProductManager;
import com.upreal.uprealwear.server.StoreManager;
import com.upreal.uprealwear.server.UserManager;
import com.upreal.uprealwear.utils.Item;
import com.upreal.uprealwear.utils.ListItemAdapter;
import com.upreal.uprealwear.utils.Product;
import com.upreal.uprealwear.utils.Store;
import com.upreal.uprealwear.utils.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 28/07/2015.
 */
public class SearchResultActivity extends Activity implements WearableListView.ClickListener {

    private List<Item> absProduct;

    private WearableListView listView;
    private ListItemAdapter adapter;

    private String searchText;
    private boolean user;
    private boolean product;
    private boolean store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.util_list);

        searchText = getIntent().getStringExtra("search_text");
        user = getIntent().getBooleanExtra("user", true);
        product = getIntent().getBooleanExtra("product", true);
        store = getIntent().getBooleanExtra("store", true);

        if (user == false && product == false && store == false) {
            user = true;
            product = true;
            store = true;
        }

        absProduct = new ArrayList<Item>();
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

    private class RetrieveList extends AsyncTask<Void, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(Void... params) {

            List<Item> list = new ArrayList<Item>();

            if (user == true) {
                UserManager um = new UserManager();
                List<User> uList = um.getUserByUsername(searchText);

                for (User u : uList) {
                    list.add(new Item(u.getId(), 1, u.getFirstname() + " " + u.getLastname(), null));
                }
            }

            if (product == true) {
                ProductManager pm = new ProductManager();
                List<Product> pList = pm.getProduct(searchText);

                for (Product p : pList) {
                    list.add(new Item(p.getId(), 2, p.getName(), pm.getProductPicture(p.getId())));
                }
            }

            if (store == true) {
                StoreManager sm = new StoreManager();
                List<Store> sList = sm.getStoreByName(searchText);

                for (Store s : sList) {
                    list.add(new Item(s.getId(), 3, s.getName(), null));
                }
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Item> res) {
            super.onPostExecute(res);
            Log.e("SearchResultActivity", "WebService called. Result:");
            absProduct.clear();
            for (Item i : res) {
                absProduct.add(i);
                Log.e("SearchResultActivity", i.getId() + ":" + i.getName() + " // " + i.getImagePath());
            }
            if (absProduct.isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), ConfirmationActivity.class);
                intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.FAILURE_ANIMATION);
                intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(R.string.search_empty));
                startActivity(intent);
                finish();
            }
            adapter.notifyDataSetChanged();
        }
    }
}
