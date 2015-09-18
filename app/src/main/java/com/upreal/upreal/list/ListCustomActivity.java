package com.upreal.upreal.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.DragDropTouchListener;
import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.SwipeableRecyclerViewTouchListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.upreal.upreal.R;
import com.upreal.upreal.product.ProductActivity;
import com.upreal.upreal.utils.Product;
import com.upreal.upreal.utils.database.DatabaseHelper;
import com.upreal.upreal.utils.database.DatabaseQuery;

import java.util.ArrayList;

/**
 * Created by Elyo on 27/05/2015.
 */
public class ListCustomActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String[] listcustom;

    /*private RecyclerView mRecyclerViewList;
    private RecyclerView.Adapter mAdapterList;*/

    private UltimateRecyclerView mUltimateRecyclerView;
    private UltimateViewAdapter mAdapterList;

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private DatabaseQuery mDbQuery;

    private DragDropTouchListener dragDropTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_custom);

        listcustom = getIntent().getExtras().getStringArray("listcustom");
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(listcustom[0]);
        setSupportActionBar(toolbar);

        mDbHelper = new DatabaseHelper(this);
        mDbQuery = new DatabaseQuery(mDbHelper);
        mDatabase = mDbHelper.openDataBase();

        //Todo Retrieve list item
        String getListId[] = mDbQuery.QueryGetElement("lists", new String[]{"id", "name", "public", "nb_items", "id_user", "type"}, "name=? AND type=?", new String[]{listcustom[0], "8"}, null, null, null);
        String getid[] = mDbQuery.MyRawQuery("SELECT id FROM LISTS WHERE NAME=" + "'" + listcustom[0] + "'");

//        Toast.makeText(getApplicationContext(),"ListID=" + getListId[0],Toast.LENGTH_SHORT).show();
  //      Toast.makeText(getApplicationContext(),"getID=" + getid[0],Toast.LENGTH_SHORT).show();
        String listItem[][] = mDbQuery.QueryGetElements("items", new String[]{"id_list", "id_product", "id_user"}, "id_list=?", new String[]{getListId[0]}, null, null, null);

    //    Toast.makeText(getApplicationContext(), "Nb_Item=" + Integer.toString(listItem.length), Toast.LENGTH_SHORT).show();
        final ArrayList<Product> products = new ArrayList<>();
        Product objProd = new Product();
        String[] prod = null;

        for (int i = 0; i < listItem.length; i++) {
            if (listItem[i][1] != null) {
                prod = mDbQuery.QueryGetElement("product", new String[]{"name", "ean", "picture", "brand", "product_id"}, "product_id=?", new String[]{listItem[i][1]}, null, null, null);
                Toast.makeText(getApplicationContext(), "ProductName=" + prod[0], Toast.LENGTH_SHORT).show();
                objProd.setName(prod[0]);
                objProd.setEan(prod[1]);
                objProd.setPicture(prod[2]);
                objProd.setBrand(prod[3]);
                objProd.setId(Integer.parseInt(prod[4]));
                products.add(objProd);
                objProd = new Product();
            }
        }
      //  Toast.makeText(getApplicationContext(), "products.Size()= " +  Integer.toString(products.size()), Toast.LENGTH_LONG).show();

        mAdapterList = new AdapterListCustom(products);
        mUltimateRecyclerView = (UltimateRecyclerView) findViewById(R.id.ultimate_recyclerlistCust);
        mUltimateRecyclerView.setHasFixedSize(false);
        mUltimateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUltimateRecyclerView.setAdapter(mAdapterList);
        mDatabase.close();

        mUltimateRecyclerView.addOnItemTouchListener(new SwipeableRecyclerViewTouchListener(mUltimateRecyclerView.mRecyclerView,
                new SwipeableRecyclerViewTouchListener.SwipeListener() {
                    @Override
                    public boolean canSwipe(int i) {
                        if (i >= 0)
                            return true;
                        else
                            return false;
                    }

                    @Override
                    public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] ints) {
                        for (final int position : ints) {
//                            Toast.makeText(recyclerView.getContext(), "SwapDoneLeft :)", Toast.LENGTH_LONG).show();
                            mAdapterList.remove(products, position);
                            Snackbar.make(recyclerView.getRootView(), "Voulez vous vraiment supprimer?", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //mAdapterList.insert(products, products.get(0), position);
                                }
                            }).show();
                        }
                        mAdapterList.notifyDataSetChanged();
                    }

                    @Override
                    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] ints) {
                        for (int position : ints) {
  //                          Toast.makeText(recyclerView.getContext(), "SwapDoneLeft :)", Toast.LENGTH_LONG).show();
                        }
                        mAdapterList.notifyDataSetChanged();
                    }
                }));


        ItemTouchListenerAdapter itemTouchListenerAdapter = new ItemTouchListenerAdapter(mUltimateRecyclerView.mRecyclerView,
                new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView recyclerView, View view, int i) {
                        Intent intent = new Intent(view.getContext(), ProductActivity.class);
                        intent.putExtra("listprod", products.get(i));
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

        dragDropTouchListener = new DragDropTouchListener(mUltimateRecyclerView.mRecyclerView, this) {
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }

            @Override
            protected void onItemSwitch(RecyclerView recyclerView, int i, int i1) {
                if (i >= 0 && i1 >= 0) {
                    mAdapterList.swapPositions(products, i, i1);
                }
            }

            @Override
            protected void onItemDrop(RecyclerView recyclerView, int i) {
                mUltimateRecyclerView.enableDefaultSwipeRefresh(true);
                mAdapterList.notifyDataSetChanged();
            }
        };
        //dragDropTouchListener.setCustomDragHighlight();
        mUltimateRecyclerView.mRecyclerView.addOnItemTouchListener(dragDropTouchListener);

 /*       mAdapterList = new AdapterListCustRecycler(products);
        mRecyclerViewList = (RecyclerView) findViewById(R.id.recyclerlistCust);
        mRecyclerViewList.setHasFixedSize(true);
        mRecyclerViewList.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewList.setAdapter(mAdapterList);*/
    }
}
