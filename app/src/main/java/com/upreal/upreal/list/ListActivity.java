package com.upreal.upreal.list;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.github.clans.fab.FloatingActionButton;
import com.upreal.upreal.R;
import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.database.DatabaseHelper;
import com.upreal.upreal.utils.database.DatabaseQuery;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Elyo on 11/05/2015.
 */
public class ListActivity extends ActionBarActivity implements View.OnClickListener {

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

    private EditText editList;

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        sessionManagerUser = new SessionManagerUser(getApplicationContext());

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

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.list);
/*
        getSupportActionBar().setHomeButtonEnabled(true);
*/
        toolbar.setNavigationIcon(R.drawable.ic_action_camera);
        setSupportActionBar(toolbar);/*
        toolbar.
*/
        floatingButtonAddList = (FloatingActionButton) findViewById(R.id.fabaddlist);
        floatingButtonAddList.setOnClickListener(this);
        mRecyclerViewList = (RecyclerView) findViewById(R.id.recyclerlist);
        mRecyclerViewList.setHasFixedSize(true);
        //mRecyclerViewList.setLayoutManager();
        mRecyclerViewList.setLayoutManager(new LinearLayoutManager(this));
        mAdapterList = new AdapterListHomeBase(base_list, delimiter);
        mRecyclerViewList.setAdapter(mAdapterList);
        mRecyclerViewList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
        });

        lists = mDbQuery.QueryGetElements("lists", new String[]{"name", "public", "nb_items", "id_user"}, null, null, null, null, null);
//        Toast.makeText(getApplicationContext(), "TEST" + lists[0][0] + "length " + lists.length, Toast.LENGTH_SHORT).show();
        mDatabase.close();
        mRecyclerViewListCust = (RecyclerView) findViewById(R.id.recyclerlistCust);
        mRecyclerViewListCust.setHasFixedSize(true);
        mRecyclerViewListCust.setLayoutManager(new LinearLayoutManager(this));
        mAdapterListCust = new AdapterListHomeCustom(lists, delimiter);
        mRecyclerViewListCust.setAdapter(mAdapterListCust);
    }

    @Override
    public void onClick(View v) {
         switch (v.getId()) {
             case R.id.fabaddlist:
                 builder = new AlertDialog.Builder(ListActivity.this);
                 view = getLayoutInflater().inflate(R.layout.dialog_addlist, null);
                 editList = (EditText) view.findViewById(R.id.namelist);
                 builder.setCancelable(false).setTitle(R.string.add_list).setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {

                     @Override
                     public void onClick(DialogInterface dialog, int which) {
/*
                Toast.makeText(getApplicationContext(),"Res:" + editList.getText().toString() + "ID:[" + Integer.toString(sessionManagerUser.getUserId()) + "]", Toast.LENGTH_SHORT).show();
*/
                         if (editList.getText().length() <= 0) {
                             builderErrorShortList.create().show();
                             dialog.cancel();
                         }
                         mDatabase = mDbHelper.openDataBase();
                         mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user"}, new String[]{editList.getText().toString(), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId())});
                         lists = mDbQuery.QueryGetElements("lists", new String[]{"name", "public", "nb_items", "id_user"}, null, null, null, null, null);
                         // TODO Auto-generated method stub
                         /*Refresh list item [BUG]*/
                         mAdapterListCust = new AdapterListHomeCustom(lists, delimiter);
                         mAdapterListCust.notifyDataSetChanged();
                         mRecyclerViewListCust.getAdapter().notifyDataSetChanged();
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
                 mAdapterListCust.notifyDataSetChanged();
                 //Toast.makeText(v.getContext(), mDbQuery.MyRawQuery("SELECT * FROM PRODUCT")[0], Toast.LENGTH_SHORT).show();
                 return;
             default:
                 return;
            }
    }
}