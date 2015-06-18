package com.upreal.upreal.geolocalisation;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upreal.upreal.R;
import com.upreal.upreal.utils.Address;

import java.util.List;

/**
 * Created by Kyosukke on 13/06/2015.
 */
public class AdapterListStore extends RecyclerView.Adapter<AdapterListStore.ViewHolder> {

    private List<Address> addresses;
    private List<String> distances;
    private List<String> prices;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int HolderId;

        TextView store_id;
        TextView store_name;
        TextView store_distance;
        TextView product_price;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            store_id = (TextView) itemView.findViewById(R.id.store_id);
            store_name = (TextView) itemView.findViewById(R.id.store_name);
            store_distance = (TextView) itemView.findViewById(R.id.store_distance);
            product_price = (TextView) itemView.findViewById(R.id.product_price);
            HolderId = 0;
        }
    }

    AdapterListStore(List<Address> addresses, List<String> distances, List<String> prices) {
        this.addresses = addresses;
        this.distances = distances;
        this.prices = prices;
    }

    @Override
    public AdapterListStore.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_compare, parent, false);

        return new ViewHolder(v, viewType);
    }

    @Override
    public void onBindViewHolder(AdapterListStore.ViewHolder holder, int position) {
        holder.store_id.setText(addresses.get(position).getId());
        holder.store_name.setText(addresses.get(position).getAddress());
        holder.store_distance.setText(distances.get(position) + " km");
        holder.product_price.setText(prices.get(position) + " €");
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }
}
