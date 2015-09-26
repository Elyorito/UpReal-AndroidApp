package com.upreal.user;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.upreal.server.UserUtilManager;
import com.upreal.R;
import com.upreal.utils.Achievement;
import com.upreal.utils.Item;
import com.upreal.utils.ListItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 18/08/2015.
 */
public class AchievementListActivity extends Activity implements WearableListView.ClickListener {


    private List<Item> absAchievement;

    private WearableListView listView;
    private ListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.util_list);

        absAchievement = new ArrayList<Item>();
        listView = (WearableListView) findViewById(R.id.wearable_list);
        adapter = new ListItemAdapter(this, absAchievement);

        new RetrieveList().execute();
        listView.setAdapter(adapter);
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
            UserUtilManager uum = new UserUtilManager();

            List<Achievement> aList = uum.getAchievement();

            for (Achievement a : aList) {
                list.add(new Item(a.getId(), 9, "" + a.getName(), null));
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Item> res) {
            super.onPostExecute(res);
            Log.e("AchievementListActivity", "WebService called. Result:");
            absAchievement.clear();
            for (Item i : res) {
                absAchievement.add(i);
                Log.e("AchievementListActivity", i.getId() + ":" + i.getName() + " // " + i.getImagePath());
            }
            if (absAchievement.isEmpty()) {
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
