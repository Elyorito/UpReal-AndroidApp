package com.upreal.upreal.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.upreal.upreal.R;

/**
 * Created by Elyo on 06/02/2015.
 */
public class AdapterNavDrawerConnectHome extends RecyclerView.Adapter<AdapterNavDrawerConnectHome.ViewHolder> implements View.OnClickListener {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_LIST_ITEM= 1;

    private String mNavWithoutAccount[];
    private String mNavAccount[];

    public static class ViewHolder extends RecyclerView.ViewHolder{
        int Holderid;

        TextView list_item;
        Button but_account;


        public ViewHolder(View itemView, int ViewType) {
            super(itemView);

            if (ViewType == TYPE_LIST_ITEM) {
                list_item = (TextView) itemView.findViewById(R.id.rowText_connect);
                Holderid = 1;
            }
            else {

                but_account = (Button) itemView.findViewById(R.id.but_account_connect);
                Holderid = 0;
            }
        }
    }

    AdapterNavDrawerConnectHome(String Account[], String Titles[]) {
        this.mNavAccount = Account;
        this.mNavWithoutAccount= Titles;
    }

    @Override
    public AdapterNavDrawerConnectHome.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LIST_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connect_home, parent, false); //Inflating the layout
            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view
            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_home_connect, parent, false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created
        }
        return null;
    }

    @Override
    public void onBindViewHolder(AdapterNavDrawerConnectHome.ViewHolder holder, int position) {
        if (holder.Holderid == 1) {
            holder.list_item.setText(mNavWithoutAccount[position]);
        }
        else {
            holder.but_account.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_account_connect:
                   break;
        }
    }

    @Override
    public int getItemCount() {
        return mNavWithoutAccount.length;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_LIST_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
