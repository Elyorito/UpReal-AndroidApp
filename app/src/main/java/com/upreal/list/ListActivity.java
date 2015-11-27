package com.upreal.list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.github.clans.fab.FloatingActionButton;
import com.upreal.R;
import com.upreal.utils.DividerItemDecoration;
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
/*
        mDbQuery.MyRawQuery("INSERT INTO Product (name, ean, picture, brand) VALUES ('coca', '53022', 'picture', 'Coca Cola');");
*/

        //mDbQuery.InsertData(new String("product"), new String[]{"name", "ean", "picture", "brand"}, new String[]{"coca", "53022", "picture", "Coca Cola"});

        base_list = new String[] {getString(R.string.liked_product)
                , getString(R.string.followed_user)
                , getString(R.string.product_seen_history)
                , getString(R.string.my_commentary)
                , getString(R.string.my_barter_product_list)};
        delimiter = new String[] {getString(R.string.customized_list)};
        delimiterTextView.setText(getString(R.string.customized_list));
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.list);
/*
        getSupportActionBar().setHomeButtonEnabled(true);
*/
        toolbar.setNavigationIcon(R.drawable.ic_action_camera);
        setSupportActionBar(toolbar);/*
        toolbar.
*/
        getITEMS = null;
        listLike = mDbQuery.QueryGetElement("lists", new String[]{"id", "name", "public", "nb_items", "id_user", "type"}, "type=?", new String[]{"3"}, null, null, null);
        Toast.makeText(getApplicationContext(), "ListLike" + listLike[0],Toast.LENGTH_SHORT).show();
        if (listLike[0] != null) {
            getITEMS = mDbQuery.QueryGetElements("items", new String[]{"id_list", "id_product", "id_user"}, "id_list=? AND id_user=?", new String[]{listLike[0], Integer.toString(sessionManagerUser.getUserId())}, null, null, null);
            if (getITEMS == null)
                listsBase.add(null);
            else {
                listsBase.add(getProduct(getITEMS, mDbQuery));
                //Toast.makeText(getApplicationContext(), "GetItems" + getITEMS.length + "/" + getITEMS[0][0], Toast.LENGTH_SHORT).show();
            }
        /*    Toast.makeText(getApplicationContext(),"GetItem nb=" + getITEMS.length, Toast.LENGTH_SHORT).show();
            */
        } else
            listsBase.add(null);
        getITEMS = null;
        /*listUserFollowed = mDbQuery.QueryGetElement("lists", new String[]{"id", "name", "public", "nb_items", "id_user", "type"}, "type=?", new String[]{"2"}, null, null, null);
        getITEMS = mDbQuery.QueryGetElements("items", new String[]{"id_list", "id_product", "id_user"}, "id_list=?", new String[]{listUserFollowed[0]}, null, null, null);
        listsBase.add(getProduct(getITEMS, mDbQuery));
        getITEMS = null;
        listHistory = mDbQuery.QueryGetElement("lists", new String[]{"id", "name", "public", "nb_items", "id_user", "type"}, "type=?", new String[]{"10"}, null, null, null);
        getITEMS = mDbQuery.QueryGetElements("items", new String[]{"id_list", "id_product", "id_user"}, "id_list=?", new String[]{listHistory[0]}, null, null, null);
        listsBase.add(getProduct(getITEMS, mDbQuery));
        getITEMS = null;
        listCommentary = mDbQuery.QueryGetElement("lists", new String[]{"id", "name", "public", "nb_items", "id_user", "type"}, "type=?", new String[]{"11"}, null, null, null);
        getITEMS = mDbQuery.QueryGetElements("items", new String[]{"id_list", "id_product", "id_user"}, "id_list=?", new String[]{listCommentary[0]}, null, null, null);
        listsBase.add(getProduct(getITEMS, mDbQuery));
        getITEMS = null;
        listBater = mDbQuery.QueryGetElement("lists", new String[]{"id", "name", "public", "nb_items", "id_user", "type"}, "type=?", new String[]{"6"}, null, null, null);
        getITEMS = mDbQuery.QueryGetElements("items", new String[]{"id_list", "id_product", "id_user"}, "id_list=?", new String[]{listBater[0]}, null, null, null);
        listsBase.add(getProduct(getITEMS, mDbQuery));
*/
        floatingButtonAddList = (FloatingActionButton) findViewById(R.id.fabaddlist);
//        mRecyclerViewList = (RecyclerView) findViewById(R.id.recyclerlist);
//        mRecyclerViewList.setHasFixedSize(true);
//        mRecyclerViewList.addItemDecoration(
//                new DividerItemDecoration(this, null));
//        mRecyclerViewList.setLayoutManager(new LinearLayoutManager(this));
//        mAdapterList = new AdapterListHomeBase(listsBase, base_list, delimiter);
//        mRecyclerViewList.setAdapter(mAdapterList);
//        mRecyclerViewList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean b) {
//
//            }
//        });

        lists = mDbQuery.QueryGetElements("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, "type=?", new String[]{"8"}, null, null, null);
        //Toast.makeText(getApplicationContext(), "TEST" + " length= " + lists.length, Toast.LENGTH_SHORT).show();
        // Boucle de toutes les lists
        ArrayList<Lists> bundleLists = new ArrayList<>();
        Lists singleLists = new Lists();
        String[][] tabLists;
        for (int i = 1; i < 8; i++) {
            tabLists = mDbQuery.QueryGetElements("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, "type=?", new String[]{Integer.toString(i)}, null, null, null);
//            Toast.makeText(getApplicationContext(),"LISTYPE=" + tabLists.length + "| Type=" + i, Toast.LENGTH_SHORT).show();
            /*if (tabLists.length > 1) {
                for (int j = 0; j < tabLists.length; j++) {
                    bundleLists.add(transformStringToLists(tabLists[j]));
                }
            }*/
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
//        mAdapterListCust = new AdapterListHomeCustom(arrayListCust, delimiter);
//        mRecyclerViewListCust.setAdapter(mAdapterListCust);
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
//                        mAdapterListCust = new AdapterListHomeCustom(arrayListCust, delimiter);
//                        mRecyclerViewListCust.setAdapter(mAdapterListCust);
//                        mAdapterListCust.notifyDataSetChanged();
                        new RetrieveList().execute();
                         /*dialog.dismiss();*/
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

            if (userSession.isLogged()) {
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
//            for (Lists items : res) {
//                //Toast.makeText(getApplicationContext(), "Name[" + items.getName() + "]|NbItem[" + items.getNb_items() + "]|id[" + items.getId() + "]",Toast.LENGTH_SHORT).show();
//            }
            mAdapterListCust = new AdapterListHomeCustom(res, delimiter);
            mRecyclerViewListCust.setAdapter(mAdapterListCust);
//            if (res != null) {
//                absLists.clear();
//                for (Item i : res) {
//                    absLists.add(i);
//                    Log.e("ListActivity", i.getId() + ":" + i.getName() + " // " + i.getImagePath());
//                }
//            }
//            if (absLists.isEmpty()) {
//                Intent intent = new Intent(getApplicationContext(), ConfirmationActivity.class);
//                intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.FAILURE_ANIMATION);
//                intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(R.string.search_empty));
//                startActivity(intent);
//                finish();
//            }
//            adapter.notifyDataSetChanged();
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
  //          Toast.makeText(getApplicationContext(), "Nombre de listes=" + lists.size(),Toast.LENGTH_SHORT).show();
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
    //        Toast.makeText(getApplicationContext(), "CreateListsResponse=" + Integer.toString(integer), Toast.LENGTH_SHORT).show();
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