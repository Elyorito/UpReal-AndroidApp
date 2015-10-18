package com.upreal.search;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.upreal.server.GlobalManager;
import com.upreal.server.UserManager;
import com.upreal.R;
import com.upreal.server.ProductManager;
import com.upreal.server.StoreManager;
import com.upreal.utils.Item;
import com.upreal.utils.ListItemAdapter;
import com.upreal.utils.Product;
import com.upreal.utils.Store;
import com.upreal.utils.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 28/07/2015.
 */
public class SearchResultActivity extends Activity implements WearableListView.ClickListener {

    private List<Item> absProduct;

    private WearableListView listView;
    private ListItemAdapter adapter;

    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.util_list);

        item = getIntent().getExtras().getParcelable("item");

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

            if (item.getName().equals("User")) {
                UserManager um = new UserManager();
                List<User> uList = um.getUserByUsername("");
                GlobalManager gm = new GlobalManager();

                for (User u : uList) {
                    list.add(new Item(u.getId(), 1, u.getUsername(), gm.getPicture(u.getId(), 1)));
                }
            }
            else if (item.getName().equals("Store")) {
                StoreManager sm = new StoreManager();
                List<Store> sList = sm.getStoreByName("");
                GlobalManager gm = new GlobalManager();

                for (Store s : sList) {
                    list.add(new Item(s.getId(), 3, s.getName(), gm.getPicture(s.getId(), 3)));
                }
            }
            else {
                ProductManager pm = new ProductManager();
                List<Product> pList = pm.getProduct(item.getName());
                GlobalManager gm = new GlobalManager();

                for (Product p : pList) {
                    list.add(new Item(p.getId(), 2, p.getName(), gm.getPicture(p.getId(), 2)));
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
