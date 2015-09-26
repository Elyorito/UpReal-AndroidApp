package com.upreal.global;

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
import com.upreal.utils.Item;
import com.upreal.utils.ListItemAdapter;
import com.upreal.utils.Rate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 17/08/2015.
 */
public class RateActivity extends Activity implements WearableListView.ClickListener {

    private List<Item> absRate;

    private WearableListView listView;
    private ListItemAdapter adapter;

    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.util_list);

        item = getIntent().getExtras().getParcelable("item");

        absRate = new ArrayList<Item>();
        listView = (WearableListView) findViewById(R.id.wearable_list);
        adapter = new ListItemAdapter(this, absRate);

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

            List<Rate> rList = gm.getRate(item.getId(), item.getTargetType());

            UserManager um = new UserManager();

            for (Rate a : rList) {
                list.add(new Item(a.getId(), 5, a.getCommentary(), um.getUserPicture(a.getIdUser())));
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Item> res) {
            super.onPostExecute(res);
            Log.e("RateActivity", "WebService called. Result:");
            absRate.clear();
            for (Item i : res) {
                absRate.add(i);
                Log.e("RateActivity", i.getId() + ":" + i.getName() + " // " + i.getImagePath());
            }
            if (absRate.isEmpty()) {
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
