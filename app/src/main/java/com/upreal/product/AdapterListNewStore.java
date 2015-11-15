package com.upreal.product;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.common.detector.MathUtils;
import com.upreal.R;
import com.upreal.utils.Address;
import com.upreal.utils.Store;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Elyo on 12/11/15.
 */
public class AdapterListNewStore extends RecyclerView.Adapter<AdapterListNewStore.ViewHolder> {

    private List<List<String>> store;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        int HolderId;

        LinearLayout layout;
        TextView address_id;
        TextView address_name;
        TextView address_distance;
        TextView product_price;
        ImageButton directions;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            address_name = (TextView) itemView.findViewById(R.id.address_name);
            address_distance = (TextView) itemView.findViewById(R.id.address_distance);
            product_price = (TextView) itemView.findViewById(R.id.product_price);
            directions = (ImageButton) itemView.findViewById(R.id.direction);
            HolderId = 0;
        }
    }

    AdapterListNewStore(List<List<String>> store) {
        this.store = store;
    }

    @Override
    public AdapterListNewStore.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_compare, parent, false);

        return new ViewHolder(v, viewType);
    }

    @Override
    public void onBindViewHolder(final AdapterListNewStore.ViewHolder holder, final int position) {
        int distance = (int) Math.ceil(Double.parseDouble(store.get(position).get(5)));
        if (distance < 1) {
            distance = distance * 1000;
            holder.address_distance.setText(distance + " m");
        } else {
            holder.address_distance.setText(distance + " km");
        }
        holder.address_name.setText(store.get(position).get(1));
        holder.product_price.setText(store.get(position).get(0) + "€");

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Choisir mode de navigation")
                        .setItems(new String[] {"Google Maps", "Waze"}, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + store.get(position).get(3) + "," + store.get(position).get(4) + "&mode=d");
                                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                        mapIntent.setPackage("com.google.android.apps.maps");
                                        view.getContext().startActivity(mapIntent);
                                        break;
                                    case 1:
                                        try
                                        {
                                            String url = "waze://?ll=" + store.get(position).get(3) + "," + store.get(position).get(4) + "&navigate=yes";
                                            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(url) );
                                            view.getContext().startActivity( intent );
                                        }
                                        catch ( ActivityNotFoundException ex  )
                                        {
                                            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
                                            view.getContext().startActivity(intent);
                                        }

                                        break;
                                }
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_directions)
                        .show();
            }
        });
    }

    private double CalculationByDistance(Double latitudeS, Double longitudeS, Double latitudeE, Double longitudeE) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = latitudeS;
        double lat2 = latitudeE;
        double lon1 = longitudeS;
        double lon2 = longitudeE;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    @Override
    public int getItemCount() {
        return store.size();
    }
}
