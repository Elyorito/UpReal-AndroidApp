package com.upreal.upreal.list;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;


import com.upreal.upreal.R;

/**
 * Created by Elyo on 11/05/2015.
 */
public class ListActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerViewList;
    private RecyclerView.Adapter mAdapterList;
    private RecyclerView.LayoutManager mLayoutManager;
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

/*        mRecyclerViewList = (RecyclerView) findViewById(R.id.recyclerlist);
        mRecyclerViewList.setHasFixedSize(true);
        //mRecyclerViewList.setLayoutManager();

        mRecyclerViewList.setLayoutManager(new LinearLayoutManager(this));
        mAdapterList = new AdapterListHome(base_list, delimiter);
        mRecyclerViewList.setAdapter(mAdapterList);

        mRecyclerViewList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
        });*/
    }
}