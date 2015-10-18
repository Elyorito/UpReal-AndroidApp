package com.upreal.list;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upreal.R;

import java.util.ArrayList;

/**
 * Created by Elyo on 18/05/2015.
 */
public class AdapterListHomeCustom  extends RecyclerView.Adapter<AdapterListHomeCustom.ViewHolder>{

    private static final int TYPE_CUSTOM = 1;

    private ArrayList<String[]> mListCust;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int HolderId;

        TextView list_name;
        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == TYPE_CUSTOM) {
                list_name = (TextView) itemView.findViewById(R.id.rowtextlist);
                HolderId = 1;
            }
        }
    }

    AdapterListHomeCustom(ArrayList<String[]> list_cust, String delimiter[]) {

        this.mListCust = list_cust;
    }

    @Override
    public AdapterListHomeCustom.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CUSTOM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_home, parent, false);
            ViewHolder vhcust = new ViewHolder(v, viewType);

            return vhcust;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(AdapterListHomeCustom.ViewHolder holder, final int position) {
        if (holder.HolderId == 1) {
            holder.list_name.setText(mListCust.get(position)[0]);
        }
        holder.list_name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switch (v.getId()) {
                    case R.id.rowtextlist:
                        //Toast.makeText(v.getContext(),mListCust.get(position -1)[0], Toast.LENGTH_SHORT).show();
                        mListCust.remove(position);
                        notifyItemRemoved(position);
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        holder.list_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rowtextlist:
                        Intent intent = new Intent(v.getContext(), ListCustomActivity.class);
                        intent.putExtra("listcustom", mListCust.get(position));
                        v.getContext().startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_CUSTOM;
    }

    @Override
    public int getItemCount() {/*
        if (mListCust.length == 0)
            return mListCust.length;*/
        return mListCust.size();
    }
}