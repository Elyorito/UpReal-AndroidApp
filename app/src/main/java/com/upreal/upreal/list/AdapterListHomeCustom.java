package com.upreal.upreal.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upreal.upreal.R;

/**
 * Created by Elyo on 18/05/2015.
 */
public class AdapterListHomeCustom  extends RecyclerView.Adapter<AdapterListHomeCustom.ViewHolder>{

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CUSTOM = 1;

    //private AlertDialog.Builder builder;
    private String mDelimiter[];
    private String mListCust[];

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int HolderId;

        TextView list_name;
        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == TYPE_CUSTOM) {
                list_name = (TextView) itemView.findViewById(R.id.rowtextlist);
                HolderId = 1;
            } else
            if (viewType == TYPE_HEADER){
                list_name = (TextView) itemView.findViewById(R.id.rowtextlist);
                HolderId = 0;
            }
        }
    }

    AdapterListHomeCustom(String[] list_cust, String delimiter[]) {
        this.mDelimiter = delimiter;
        this.mListCust = list_cust;
    }

    @Override
    public AdapterListHomeCustom.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CUSTOM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_home, parent, false);
            ViewHolder vhcust = new ViewHolder(v, viewType);

            return vhcust;
        } else
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_home, parent, false);
            ViewHolder vhitem = new ViewHolder(v, viewType);

            return vhitem;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(AdapterListHomeCustom.ViewHolder holder, int position) {
//        holder.list_name.setText(mBase_list[position]);
        if (holder.HolderId == 1) {
            holder.list_name.setText(mListCust[position]);
        } else
        if (holder.HolderId == 0) {
            holder.list_name.setText(mDelimiter[0]);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_CUSTOM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return mListCust.length;
    }
}
