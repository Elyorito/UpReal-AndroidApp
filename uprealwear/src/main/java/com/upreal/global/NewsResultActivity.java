package com.upreal.global;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.upreal.server.GlobalManager;
import com.upreal.R;
import com.upreal.utils.Article;
import com.upreal.utils.Item;
import com.upreal.utils.ListItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 17/08/2015.
 */
public class NewsResultActivity extends Activity implements WearableListView.ClickListener {

    private List<Item> absArticle;

    private WearableListView listView;
    private ListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.util_list);

        absArticle = new ArrayList<Item>();
        listView = (WearableListView) findViewById(R.id.wearable_list);
        adapter = new ListItemAdapter(this, absArticle);

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
                GlobalManager gm = new GlobalManager();
                List<Article> aList = gm.searchNews("");

                for (Article a : aList) {
                    list.add(new Item(a.getId(), 4, a.getTitle(), gm.getPicture(a.getId(), 4)));
                }

            return list;
        }

        @Override
        protected void onPostExecute(List<Item> res) {
            super.onPostExecute(res);
            Log.e("NewsResultActivity", "WebService called. Result:");
            absArticle.clear();
            for (Item i : res) {
                absArticle.add(i);
                Log.e("NewsResultActivity", i.getId() + ":" + i.getName() + " // " + i.getImagePath());
            }
            if (absArticle.isEmpty()) {
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
