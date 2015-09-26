package com.upreal.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.DragDropTouchListener;
import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.upreal.product.ProductActivity;
import com.upreal.R;
import com.upreal.utils.Product;

import java.util.ArrayList;

/**
 * Created by Elyo on 23/06/2015.
 */
public class ListBaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<String[]> listcustom;

    private UltimateRecyclerView mUltimateRecyclerView;
    private UltimateViewAdapter mAdapterList;

    private DragDropTouchListener dragDropTouchListener;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_custom);
        listcustom = (ArrayList<String[]>)getIntent().getExtras().get("listcustom");
        String title = getIntent().getExtras().getString("namelist");
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        //Toast.makeText(getApplicationContext(), "NB ELEMENT" + listcustom.size(),Toast.LENGTH_LONG).show();
        if (listcustom != null)
            mAdapterList = new AdapterListBase(listcustom);
        mUltimateRecyclerView = (UltimateRecyclerView) findViewById(R.id.ultimate_recyclerlistCust);
        mUltimateRecyclerView.setHasFixedSize(false);
        mUltimateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUltimateRecyclerView.setAdapter(mAdapterList);

        ItemTouchListenerAdapter itemTouchListenerAdapter = new ItemTouchListenerAdapter(mUltimateRecyclerView.mRecyclerView,
                new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView recyclerView, View view, int i) {
                        product = new Product();
                        product.setName(listcustom.get(i)[0]);
                        product.setEan(listcustom.get(i)[1]);
                        if (listcustom.get(i)[2] == null)
                            product.setPicture("");
                        else
                            product.setPicture(listcustom.get(i)[2]);
                        product.setBrand(listcustom.get(i)[3]);
                        product.setId(Integer.parseInt(listcustom.get(i)[4]));
                        Intent intent = new Intent(view.getContext(), ProductActivity.class);
                        intent.putExtra("listprod", product     );
                        view.getContext().startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(RecyclerView recyclerView, View view, int i) {
                        Vibrator v = (Vibrator) recyclerView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(50);
                        dragDropTouchListener.startDrag();
                        mUltimateRecyclerView.enableDefaultSwipeRefresh(false);
                    }
                });
        mUltimateRecyclerView.mRecyclerView.addOnItemTouchListener(itemTouchListenerAdapter);


    }
}
