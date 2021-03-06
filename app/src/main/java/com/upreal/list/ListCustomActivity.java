package com.upreal.list;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.DragDropTouchListener;
import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.SwipeableRecyclerViewTouchListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.upreal.R;
import com.upreal.product.ProductActivity;
import com.upreal.utils.Items;
import com.upreal.utils.Lists;
import com.upreal.utils.Product;
import com.upreal.utils.SoapGlobalManager;
import com.upreal.utils.SoapProductManager;
import com.upreal.utils.database.DatabaseHelper;
import com.upreal.utils.database.DatabaseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 27/05/2015.
 */
public class ListCustomActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private UltimateRecyclerView mUltimateRecyclerView;
    private UltimateViewAdapter mAdapterList;

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private DatabaseQuery mDbQuery;

    private DragDropTouchListener dragDropTouchListener;

    private Lists listcustom = new Lists();
    private List<Product> listProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_custom);

        listcustom = getIntent().getExtras().getParcelable("listcustom");
        new RetrieveProductById().execute();
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(listcustom.getName());
        setSupportActionBar(toolbar);

        mDbHelper = new DatabaseHelper(this);
        mDbQuery = new DatabaseQuery(mDbHelper);
        mDatabase = mDbHelper.openDataBase();

        //Todo Retrieve list item
        mAdapterList = new AdapterListCustom(listProducts);
        mUltimateRecyclerView = (UltimateRecyclerView) findViewById(R.id.ultimate_recyclerlistCust);
        mUltimateRecyclerView.setHasFixedSize(false);
        mUltimateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUltimateRecyclerView.setAdapter(mAdapterList);
        mDatabase.close();

        mUltimateRecyclerView.addOnItemTouchListener(new SwipeableRecyclerViewTouchListener(mUltimateRecyclerView.mRecyclerView,
                new SwipeableRecyclerViewTouchListener.SwipeListener() {
                    @Override
                    public boolean canSwipe(int i) {
                        return i >= 0;
                    }

                    @Override
                    public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] ints) {
                        for (final int position : ints) {
                            mAdapterList.remove(listProducts, position);
                            //new DeleteItem().execute(listProducts.get(position).);
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
                        mAdapterList.notifyDataSetChanged();
                    }
                }));


        ItemTouchListenerAdapter itemTouchListenerAdapter = new ItemTouchListenerAdapter(mUltimateRecyclerView.mRecyclerView,
                new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView recyclerView, View view, int i) {
                        Intent intent = new Intent(view.getContext(), ProductActivity.class);
                        intent.putExtra("prod", listProducts.get(i));
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
                    mAdapterList.swapPositions(listProducts, i, i1);
                }
            }

            @Override
            protected void onItemDrop(RecyclerView recyclerView, int i) {
                mUltimateRecyclerView.enableDefaultSwipeRefresh(true);
                mAdapterList.notifyDataSetChanged();
            }
        };
        mUltimateRecyclerView.mRecyclerView.addOnItemTouchListener(dragDropTouchListener);
    }

    private class DeleteItem extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... integers) {
            SoapGlobalManager gm = new SoapGlobalManager();
            return gm.deleteItem(integers[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }
    }

    private class RetrieveProductById extends AsyncTask<Void, Void, List<Product>> {

        @Override
        protected List<Product> doInBackground(Void... voids) {
            SoapGlobalManager gm = new SoapGlobalManager();
            List<Items> listItem = gm.getItemsLists(listcustom.getId());
            SoapProductManager pm = new SoapProductManager();
            for (Items item : listItem) {
                listProducts.add(pm.getProductInfo(item.getId_product()));
            }
            return listProducts;
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            super.onPostExecute(products);
            if (products != null) {
                mAdapterList = new AdapterListCustom(products);
                mUltimateRecyclerView.setAdapter(mAdapterList);
            }
        }
    }
}
