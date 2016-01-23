package com.upreal.user;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.upreal.R;
import com.upreal.server.GlobalManager;
import com.upreal.utils.Item;
import com.upreal.utils.ListItemAdapter;
import com.upreal.utils.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 17/08/2015.
 */
public class ListActivity extends Activity implements WearableListView.ClickListener {

    private List<Item> absLists;

    private WearableListView listView;
    private ListItemAdapter adapter;

    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.util_list);

        absLists = new ArrayList<Item>();
        listView = (WearableListView) findViewById(R.id.wearable_list);
        adapter = new ListItemAdapter(this, absLists);

        if (getIntent().getExtras() != null)
            item = getIntent().getExtras().getParcelable("item");

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

            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());
            int targetType = (item == null) ? (6) : (8);

            if (userSession != null && userSession.isLogged()) {
                GlobalManager gm = new GlobalManager();
                List<Item> list = new ArrayList<Item>();
                List<Lists> lList = gm.getUserList(userSession.getUserId());

                for (Lists l : lList) {
                    // if there is no item in Extras, then it's delete
                    // if there is an item in Extras, then it's add
                    list.add(new Item(l.getId(), targetType, l.getName(), (item == null) ? (null) : ("" + item.getId())));
                }

                return list;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Item> res) {
            super.onPostExecute(res);
            Log.e("ListActivity", "WebService called. Result:");
            if (res != null) {
                absLists.clear();
                for (Item i : res) {
                    absLists.add(i);
                    Log.e("ListActivity", i.getId() + ":" + i.getName() + " // " + i.getImagePath());
                }
            }
            if (absLists.isEmpty()) {
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
