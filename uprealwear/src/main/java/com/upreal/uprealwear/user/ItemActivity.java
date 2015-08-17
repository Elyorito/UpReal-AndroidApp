package com.upreal.uprealwear.user;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.upreal.uprealwear.R;
import com.upreal.uprealwear.server.ProductManager;
import com.upreal.uprealwear.utils.Item;
import com.upreal.uprealwear.utils.Items;
import com.upreal.uprealwear.utils.ListItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 17/08/2015.
 */
public class ItemActivity extends Activity implements WearableListView.ClickListener {

    private List<Item> absItems;

    private WearableListView listView;
    private ListItemAdapter adapter;

    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.util_list);

        absItems = new ArrayList<Item>();
        listView = (WearableListView) findViewById(R.id.wearable_list);
        adapter = new ListItemAdapter(this, absItems);

        item = getIntent().getExtras().getParcelable("item");

        new RetrieveItemList().execute();
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

    private class RetrieveItemList extends AsyncTask<Void, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(Void... params) {
            List<Item> list = new ArrayList<Item>();
            List<Items> iList = new ArrayList<Items>();

            ProductManager pm = new ProductManager();

            for (Items i : iList) {

//                Product p = pm.getProductInfo(i.getIdProduct());

//                list.add(new Item(i.getId(), 7, p.getName(), pm.getProductPicture(p.getId())));
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Item> res) {
            super.onPostExecute(res);
            Log.e("ItemActivity", "WebService called. Result:");
            absItems.clear();
            for (Item i : res) {
                absItems.add(i);
                Log.e("ItemActivity", i.getId() + ":" + i.getName() + " // " + i.getImagePath());
            }
            if (absItems.isEmpty()) {
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
