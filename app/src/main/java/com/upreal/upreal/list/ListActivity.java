package com.upreal.upreal.list;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import com.github.clans.fab.FloatingActionButton;
import com.upreal.upreal.R;

/**
 * Created by Elyo on 11/05/2015.
 */
public class ListActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;

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

    private String base_list[];
    private String delimiter[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        base_list = new String[] {getString(R.string.liked_product)
                , getString(R.string.followed_user)
                , getString(R.string.product_seen_history)
                , getString(R.string.my_commentary)
                , getString(R.string.my_barter_product_list)};
        delimiter = new String[] {getString(R.string.customized_list)};

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new list");
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

        String[] listCustom = new String[] {"list 1", "list 2", "list 3", "list 4", "list 5", "list 6"};

        mRecyclerViewListCust = (RecyclerView) findViewById(R.id.recyclerlistCust);
        mRecyclerViewListCust.setHasFixedSize(true);
        //mRecyclerViewList.setLayoutManager();
        mRecyclerViewListCust.setLayoutManager(new LinearLayoutManager(this));
        mAdapterListCust = new AdapterListHomeCustom(listCustom, delimiter);
        mRecyclerViewListCust.setAdapter(mAdapterListCust);
    }

    @Override
    public void onClick(View v) {
         switch (v.getId()) {
             case R.id.fabaddlist:
                 builder.create().show();
                 return;
             default:
                 return;
            }
    }
}