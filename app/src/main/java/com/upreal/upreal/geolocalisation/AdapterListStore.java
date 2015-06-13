package com.upreal.upreal.geolocalisation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upreal.upreal.R;

/**
 * Created by Kyosukke on 13/06/2015.
 */
public class AdapterListStore extends RecyclerView.Adapter<AdapterListStore.ViewHolder> {

    private String base_list[];
    private String delimiter[];

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int HolderId;

        TextView store_name;
        TextView store_distance;
        TextView product_price;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            store_name = (TextView) itemView.findViewById(R.id.store_name);
            store_distance = (TextView) itemView.findViewById(R.id.store_distance);
            product_price = (TextView) itemView.findViewById(R.id.product_price);
            HolderId = 0;
        }
    }

    AdapterListStore(String base_list[], String delimiter[]) {
        this.base_list = base_list;
        this.delimiter = delimiter;
    }

    @Override
    public AdapterListStore.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_compare, parent, false);
        ViewHolder vHolder = new ViewHolder(v, viewType);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(AdapterListStore.ViewHolder holder, int position) {
        holder.store_name.setText(base_list[position]);
        holder.store_distance.setText(base_list[position]);
        holder.product_price.setText(base_list[position]);
    }

    @Override
    public int getItemCount() {
        return base_list.length;
    }
}
