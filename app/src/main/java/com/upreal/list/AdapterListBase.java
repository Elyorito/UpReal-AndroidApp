package com.upreal.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.upreal.upreal.R;

import java.util.ArrayList;

/**
 * Created by Elyo on 23/06/2015.
 */
public class AdapterListBase extends UltimateViewAdapter {

    private ArrayList<String[]> prod;

    public AdapterListBase(ArrayList<String[]> prodList) {
        this.prod = prodList;
    }

    class ViewHolder extends UltimateRecyclerviewViewHolder {

        TextView nameList;
        public ViewHolder(View itemView) {
            super(itemView);
            nameList = (TextView) itemView.findViewById(
                    R.id.text_listcut);
        }
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_custom_drag, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return prod.size();
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).nameList.setText(prod.get(position)[position]);
        ((ViewHolder) holder).nameList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }
}
