package com.upreal.user;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.upreal.server.GlobalManager;
import com.upreal.server.UserManager;
import com.upreal.server.UserUtilManager;
import com.upreal.R;
import com.upreal.server.ProductManager;
import com.upreal.server.StoreManager;
import com.upreal.utils.History;
import com.upreal.utils.Item;
import com.upreal.utils.ListItemAdapter;

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

            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            if (userSession.isLogged()) {
                List<Item> list = new ArrayList<Item>();
                UserUtilManager uum = new UserUtilManager();

                List<History> hList = uum.getUserHistory(userSession.getUserId());

                for (History h : hList) {
                    String state = userSession.getUser().getUsername() + " ";

                    switch (h.getActionType()) {
                        case 1:
                            state += getString(R.string.has_consulted);
                            break ;
                        case 2:
                            state += getString(R.string.liked);
                            break ;
                        case 3:
                            state += getString(R.string.disliked);
                            break ;
                        case 4:
                            state += getString(R.string.unliked);
                            break ;
                        case 5:
                            state += getString(R.string.shared);
                            break ;
                        case 6:
                            state += getString(R.string.commented);
                            break ;
                        case 7:
                            state += getString(R.string.modified_profile);
                            break ;
                        case 8:
                            state += getString(R.string.added_product);
                            break ;
                        default:
                            state += getString(R.string.error);
                            break ;
                    }

                    switch (h.getIdType()) {
                        case 1:
                            if (h.getActionType() != 7) {
                                UserManager um = new UserManager();
                                state += " " + getString(R.string.user) + " " + um.getAccountInfo(h.getIdTarget()).getUsername();
                            }
                            break ;
                        case 2:
                            ProductManager pm = new ProductManager();
                            state += " " + getString(R.string.product) + " " + pm.getProductInfo(h.getIdTarget()).getName();
                            break ;
                        case 3:
                            StoreManager sm = new StoreManager();
                            state += " " + getString(R.string.store) + " " + sm.getStoreInfo(h.getIdTarget()).getName();
                            break ;
                        case 4:
                            GlobalManager gm = new GlobalManager();
                            state += " " + getString(R.string.article) + " " + gm.getNewsInfo(h.getIdTarget()).getTitle();
                            break ;
                        default:
                            break ;
                    }

                    list.add(new Item(h.getId(), 10, state + ".", null));
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
