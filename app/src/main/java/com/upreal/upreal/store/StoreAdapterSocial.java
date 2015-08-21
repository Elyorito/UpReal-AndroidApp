package com.upreal.upreal.store;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upreal.upreal.R;
import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.Store;
import com.upreal.upreal.utils.User;
import com.upreal.upreal.utils.database.DatabaseHelper;
import com.upreal.upreal.utils.database.DatabaseQuery;

/**
 * Created by Elyo on 21/08/15.
 */
public class StoreAdapterSocial extends RecyclerView.Adapter<StoreAdapterSocial.ViewHolder> implements View.OnClickListener {

    private String mSOCIALOPT[];
    private Store mStore;
    private SessionManagerUser sessionManagerUser;

    private AlertDialog.Builder builder;
    private String listLike;
    private Boolean isLiked = false;
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private DatabaseQuery mDbQuery;

    public StoreAdapterSocial(String SOCIALOPT[], Store store, SessionManagerUser sessionManagerUser) {
        this.mSOCIALOPT = SOCIALOPT;
        this.mStore = store;
        this.sessionManagerUser = sessionManagerUser;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_social;
        CardView mCardview;
        public ViewHolder(View itemView) {
            super(itemView);
            text_social = (TextView) itemView.findViewById(R.id.text_social);
            mCardview = (CardView) itemView.findViewById(R.id.cardview_social);

        }
    }

    @Override
    public StoreAdapterSocial.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // View v = LayoutInflater.from(parent.getContext()).inflate(/*Rajouter Layout ex: R.layout.item_product_social*/, parent, false);

        //ViewHolder vhStoreSocial = new ViewHolder(v);
        //return vhStoreSocial;
        return null;
    }

    @Override
    public void onBindViewHolder(StoreAdapterSocial.ViewHolder holder, int position) {
        holder.text_social.setText(mSOCIALOPT[position]);
        holder.mCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Put logic
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSOCIALOPT.length;
    }

    @Override
    public void onClick(View v) {

    }
}
