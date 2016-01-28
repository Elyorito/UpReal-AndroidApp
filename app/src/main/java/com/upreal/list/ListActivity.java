package com.upreal.list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.upreal.R;
import com.upreal.utils.Items;
import com.upreal.utils.Lists;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.SoapGlobalManager;
import com.upreal.utils.database.DatabaseHelper;
import com.upreal.utils.database.DatabaseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 11/05/2015.
 */
public class ListActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private DatabaseQuery mDbQuery;

    //RecyclerView List Base
    private RecyclerView mRecyclerViewList;
    private RecyclerView.Adapter mAdapterList;
    private RecyclerView.LayoutManager mLayoutManager;

    //RecyclerView List Custom
    private RecyclerView mRecyclerViewListCust;
    private RecyclerView.Adapter mAdapterListCust;
    private RecyclerView.LayoutManager mLayoutManagerCust;

    private FloatingActionButton floatingButtonAddList;

    private AlertDialog.Builder builder;
    private AlertDialog.Builder builderErrorShortList;

    private SessionManagerUser sessionManagerUser;

    private String base_list[];
    private String delimiter[];

    private String[][] lists;
    private String getITEMS[][];

    private String[] listLike;
    private String[] listUserFollowed;
    private String[] listHistory;
    private String[] listCommentary;
    private String[] listBater;

    private ArrayList<ArrayList<String[]>> listsBase = new ArrayList<>();

    private EditText editList;
    private TextView delimiterTextView;

    private View view;
    private final ArrayList<String[]> arrayListCust = new ArrayList<String[]>();

    private Items item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        sessionManagerUser = new SessionManagerUser(getApplicationContext());

        delimiterTextView = (TextView) findViewById(R.id.custom_list);
        //Init Database
        mDbHelper = new DatabaseHelper(this);
        mDbQuery = new DatabaseQuery(mDbHelper);
        mDatabase = mDbHelper.openDataBase();
        if (!mDatabase.isOpen())
            finish();
        ////

        builderErrorShortList = new AlertDialog.Builder(ListActivity.this);
        builderErrorShortList.setTitle(getString(R.string.list_name_empty))
                .setMessage(getString(R.string.name_list_correct))
                .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        base_list = new String[] {getString(R.string.liked_product)
                , getString(R.string.followed_user)
                , getString(R.string.product_seen_history)
                , getString(R.string.my_commentary)
                , getString(R.string.my_barter_product_list)};
        delimiter = new String[] {getString(R.string.customized_list)};
        delimiterTextView.setText(getString(R.string.customized_list));
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.list);
        toolbar.setNavigationIcon(R.drawable.ic_action_camera);
        setSupportActionBar(toolbar);
        getITEMS = null;
        listLike = mDbQuery.QueryGetElement("lists", new String[]{"id", "name", "public", "nb_items", "id_user", "type"}, "type=?", new String[]{"3"}, null, null, null);
        Toast.makeText(getApplicationContext(), "ListLike" + listLike[0],Toast.LENGTH_SHORT).show();
        if (listLike[0] != null) {
            getITEMS = mDbQuery.QueryGetElements("items", new String[]{"id_list", "id_product", "id_user"}, "id_list=? AND id_user=?", new String[]{listLike[0], Integer.toString(sessionManagerUser.getUserId())}, null, null, null);
            if (getITEMS == null)
                listsBase.add(null);
            else {
                listsBase.add(getProduct(getITEMS, mDbQuery));
            }
        } else
            listsBase.add(null);
        getITEMS = null;
        floatingButtonAddList = (FloatingActionButton) findViewById(R.id.fabaddlist);
        lists = mDbQuery.QueryGetElements("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, "type=?", new String[]{"8"}, null, null, null);

        // Boucle de toutes les lists
        ArrayList<Lists> bundleLists = new ArrayList<>();
        Lists singleLists = new Lists();
        String[][] tabLists;
        for (int i = 1; i < 8; i++) {
            tabLists = mDbQuery.QueryGetElements("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, "type=?", new String[]{Integer.toString(i)}, null, null, null);
        }
        new SendGetDiffListsServer().execute(bundleLists);
        mDatabase.close();
        mRecyclerViewListCust = (RecyclerView) findViewById(R.id.recyclerlistCust);
        mRecyclerViewListCust.setHasFixedSize(true);
        mRecyclerViewListCust.setLayoutManager(new LinearLayoutManager(this));
        arrayListCust.clear();
        for(String[] list : lists){
            arrayListCust.add(list);
        }
        RecyclerView.ItemAnimator animator = mRecyclerViewListCust.getItemAnimator();
        animator.setAddDuration(1500);
        animator.setRemoveDuration(1000);

        new RetrieveList().execute();

        floatingButtonAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayListCust.clear();
                builder = new AlertDialog.Builder(v.getContext());
                view = getLayoutInflater().inflate(R.layout.dialog_addlist, null);
                editList = (EditText) view.findViewById(R.id.namelist);
                builder.setCancelable(false).setTitle(R.string.add_list).setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editList.getText().length() <= 0) {
                            builderErrorShortList.create().show();
                            dialog.cancel();
                            return;
                        }
                        mDatabase = mDbHelper.openDataBase();
                        String[] listsend = {editList.getText().toString(), Integer.toString(1), "8", Integer.toString(0), Integer.toString(sessionManagerUser.getUserId())};
                        mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{editList.getText().toString(), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "8"});
                        lists = mDbQuery.QueryGetElements("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, "type=?", new String[]{"8"}, null, null, null);
                        for (String[] list : lists){
                            arrayListCust.add(list);
                        }
                        new SendCreatedLists().execute(listsend);
                        // TODO Auto-generated method stub

                         /*Refresh list item [BUG]*/
                        new RetrieveList().execute();
                        mDatabase.close();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });
                builder.setView(view).create().show();
            }
        });
    }

    private Lists transformStringToLists(String[] tabLists) {
        Lists lists = new Lists();

        lists.setName(tabLists[0]);
        if (tabLists[1] != null)
            lists.setL_public(Integer.parseInt(tabLists[1]));
        else
            lists.setL_public(1);
        lists.setNb_items(Integer.parseInt(tabLists[2]));
        lists.setId_user(Integer.parseInt(tabLists[3]));
        lists.setType(Integer.parseInt(tabLists[4]));
        //lists.setDate();
        return lists;
    }

    private class RetrieveList extends AsyncTask<Void, Void, List<Lists>> {

        @Override
        protected List<Lists> doInBackground(Void... params) {


            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());
            int targetType = (item == null) ? (6) : (8);

            if (userSession != null && userSession.isLogged()) {
                SoapGlobalManager gm = new SoapGlobalManager();
                List<Lists> lList = gm.getUserList(userSession.getUserId());

                return lList;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Lists> res) {
            super.onPostExecute(res);
            Log.e("ListActivity", "WebService called. Result:");
            mAdapterListCust = new AdapterListHomeCustom(res, delimiter);
            mRecyclerViewListCust.setAdapter(mAdapterListCust);
        }
    }


    private class SendGetDiffListsServer extends AsyncTask<ArrayList<Lists>, Void, ArrayList<Lists>> {

        ArrayList<Lists> lists = new ArrayList<>();
        @Override
        protected ArrayList<Lists> doInBackground(ArrayList<Lists>... params) {
            SoapGlobalManager gm = new SoapGlobalManager();
            lists = gm.getDiffListServer(params[0]);
            return lists;
        }

        @Override
        protected void onPostExecute(ArrayList<Lists> lists) {
            super.onPostExecute(lists);
        }
    }

    private class  SendCreatedLists extends AsyncTask<String[], Void, Integer> {


        @Override
        protected Integer doInBackground(String[]... params) {
            SoapGlobalManager gm = new SoapGlobalManager();
            int response = gm.createLists(params[0][0], Integer.parseInt(params[0][1]), Integer.parseInt(params[0][2]), Integer.parseInt(params[0][3]), Integer.parseInt(params[0][4]));
            return response;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }

    private ArrayList<String[]> getProduct(String[][] items,DatabaseQuery mDbQuery) {

        ArrayList<String[]> products = new ArrayList<>();
        String[] prod = null;

        if (items == null || items.length == 0) {
            products.add(null);
            return products;
        }
        for (int i = 0; i < items.length; i++) {
            prod = mDbQuery.QueryGetElement("product", new String[]{"name", "ean", "picture", "brand", "product_id"}, "product_id=?", new String[]{items[i][1]}, null, null, null);
            products.add(prod);
            if (prod != null)
                Toast.makeText(getApplicationContext(),"Product recup= " + prod[0], Toast.LENGTH_LONG).show();
            prod = null;
        }
        return products;
    }
}