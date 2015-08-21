package com.upreal.uprealwear.user;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.upreal.uprealwear.R;
import com.upreal.uprealwear.server.UserManager;
import com.upreal.uprealwear.utils.History;
import com.upreal.uprealwear.utils.Item;
import com.upreal.uprealwear.utils.ListItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 18/08/2015.
 */
public class HistoryActivity extends Activity {

    private List<Item> absHistory;

    private WearableListView listView;
    private ListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.util_list);

        absHistory = new ArrayList<Item>();
        listView = (WearableListView) findViewById(R.id.wearable_list);
        adapter = new ListItemAdapter(this, absHistory);

        new RetrieveList().execute();
        listView.setAdapter(adapter);
    }

    private class RetrieveList extends AsyncTask<Void, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(Void... params) {

            UserManager um = new UserManager();
            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            if (userSession.isLogged()) {
                List<Item> list = new ArrayList<Item>();
                List<History> hList = new ArrayList<History>();

                for (History h : hList) {
                    list.add(new Item(h.getId(), 0, "" + h.getId(), null));
                }

                return list;

            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Item> res) {
            super.onPostExecute(res);
            Log.e("HistoryActivity", "WebService called. Result:");
            if (res != null) {
                absHistory.clear();
                for (Item i : res) {
                    absHistory.add(i);
                    Log.e("HistoryActivity", i.getId() + ":" + i.getName() + " // " + i.getImagePath());
                }
            }
            if (absHistory.isEmpty()) {
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
