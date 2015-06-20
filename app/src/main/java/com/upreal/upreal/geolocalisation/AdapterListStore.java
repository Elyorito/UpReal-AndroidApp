package com.upreal.upreal.geolocalisation;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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

        RelativeLayout layout;
        TextView address_id;
        TextView address_name;
        TextView address_distance;
        TextView product_price;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            layout = (RelativeLayout) itemView.findViewById(R.id.layout);
            address_id = (TextView) itemView.findViewById(R.id.address_id);
            address_name = (TextView) itemView.findViewById(R.id.address_name);
            address_distance = (TextView) itemView.findViewById(R.id.address_distance);
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
    public void onBindViewHolder(final AdapterListStore.ViewHolder holder, final int position) {
        holder.address_id.setText("" + addresses.get(position).getId());
        holder.address_name.setText(addresses.get(position).getAddress());
        holder.address_distance.setText(distances.get(position) + " km");
        holder.product_price.setText(prices.get(position) + " €");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("GeolocalisationActivity", "Item touched at " + addresses.get(position).getId() + ".");
/*                Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
                intent.putExtra("id_adress", addresses.get(position).getId());
                getApplicationContext().startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }
}
