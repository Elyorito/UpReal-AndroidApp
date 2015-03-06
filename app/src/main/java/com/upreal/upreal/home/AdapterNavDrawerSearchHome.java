package com.upreal.upreal.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.upreal.upreal.R;
import com.upreal.upreal.product.ProductSearchActivity;

import static android.widget.TextView.OnEditorActionListener;

/**
 * Created by Elyo on 15/02/2015.
 */
public class AdapterNavDrawerSearchHome extends RecyclerView.Adapter<AdapterNavDrawerSearchHome.ViewHolder> implements OnEditorActionListener, View.OnClickListener{

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PRICE = 1;
    private static final int TYPE_RATING = 2;
    private static final int TYPE_BUT_DONE = 3;

    private Context context;

    public class ViewHolder extends  RecyclerView.ViewHolder{
        int HolderId;

        Button but_done;

        EditText search;
        EditText price_min;
        EditText price_max;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            if (ViewType == TYPE_BUT_DONE) {
                but_done = (Button) itemView.findViewById(R.id.but_done_search);
                HolderId = 3;
            }
            if (ViewType == TYPE_RATING) {
                HolderId = 2;
            } else if (ViewType == TYPE_PRICE) {
                price_max = (EditText) itemView.findViewById(R.id.price_search_max);
                price_min = (EditText) itemView.findViewById(R.id.price_search_min);
                HolderId = 1;
            } else if (ViewType == TYPE_HEADER){
                search = (EditText) itemView.findViewById(R.id.edittext_search);
                HolderId = 0;
            }
        }
    }

    AdapterNavDrawerSearchHome(Context mContext) {
        context = mContext;
    }

    @Override
    public AdapterNavDrawerSearchHome.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_BUT_DONE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.done_search_home, parent, false);

            ViewHolder vhDone = new ViewHolder(v, viewType);
            vhDone.but_done.setOnClickListener(this);
            return vhDone;

        } else if (viewType == TYPE_RATING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_search_home, parent, false);

            ViewHolder vhRate = new ViewHolder(v, viewType);
            return vhRate;
        } else if (viewType == TYPE_PRICE) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_search_home, parent, false);

            ViewHolder vhPrice = new ViewHolder(v, viewType);
            return vhPrice;
        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_search_home, parent, false);

            ViewHolder vhHeader = new ViewHolder(v, viewType);
            vhHeader.search.setOnEditorActionListener(AdapterNavDrawerSearchHome.this);
            vhHeader.search.setHint("produit, marque, magasin, utilisateur");
            vhHeader.search.setTag(vhHeader);
            return vhHeader;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final AdapterNavDrawerSearchHome.ViewHolder holder, int position) {
        //holder.search.setText("Search !");


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_done_search:
                Intent intent = new Intent(v.getContext(), ProductSearchActivity.class);
                v.getContext().startActivity(intent);
                return;
            default:
                return;
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if(actionId == EditorInfo.IME_ACTION_DONE) {
            ViewHolder holder = (ViewHolder) v.getTag();
            holder.search.setText("Searching!...");
            Intent intent = new Intent(v.getContext(), ProductSearchActivity.class);
            v.getContext().startActivity(intent);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        else if (position == 1)
            return TYPE_PRICE;
        else if (position == 2)
            return TYPE_RATING;
        else
            return TYPE_BUT_DONE;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
