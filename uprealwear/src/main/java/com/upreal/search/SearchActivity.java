package com.upreal.search;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.upreal.R;
import com.upreal.server.ProductUtilManager;
import com.upreal.utils.Item;
import com.upreal.utils.ListItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 16/08/2015.
 */
public class SearchActivity extends Activity implements WearableListView.ClickListener {

    private List<Item> categories;

    private WearableListView listView;
    private ListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.util_list);

        categories = new ArrayList<Item>();
        listView = (WearableListView) findViewById(R.id.wearable_list);
        adapter = new ListItemAdapter(this, categories);

        new RetrieveCategory().execute();
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

    private class RetrieveCategory extends AsyncTask<Void, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(Void... params) {

            List<Item> list = new ArrayList<Item>();
            ProductUtilManager pum = new ProductUtilManager();
            List<String> cList = pum.getCategory();

            if (!cList.isEmpty()) {
                for (String a : cList) {
                    list.add(new Item(0, 10, a, null));
                }
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Item> res) {
            super.onPostExecute(res);
            Log.e("NewsResultActivity", "WebService called. Result:");
            categories.clear();
            for (Item i : res) {
                categories.add(i);
                categories.add(new Item(0, 10, "Store", null));
                categories.add(new Item(0, 10, "User", null));
                Log.e("NewsResultActivity", i.getId() + ":" + i.getName() + " // " + i.getImagePath());
            }
            if (categories.isEmpty()) {
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
