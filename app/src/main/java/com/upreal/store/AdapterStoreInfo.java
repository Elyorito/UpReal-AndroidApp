package com.upreal.store;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upreal.R;
import com.upreal.utils.Address;
import com.upreal.utils.Company;
import com.upreal.utils.Store;

/**
 * Created by Kyosukke on 21/11/2015.
 */
public class AdapterStoreInfo extends RecyclerView.Adapter<AdapterStoreInfo.ViewHolder> {

    Context context;
    Store store;
    Address address;
    Company company;
    int size = 1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int HolderId;

        TextView typeName;
        TextView typeValue;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            typeName = (TextView) itemView.findViewById(R.id.type_name);
            typeValue = (TextView) itemView.findViewById(R.id.type_value);

            HolderId = 0;
        }
    }

    public AdapterStoreInfo(Context context, Store store, Address address, Company company) {
        this.context = context;
        this.store = store;

        if (store != null) {
            this.address = address;
            size++;
        }

        if (company != null) {
            this.company = company;
            size++;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (getItemCount() == 0) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            ViewHolder vhCom = new ViewHolder(v, viewType);
            return vhCom;
        }
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_specification, viewGroup, false);

        ViewHolder vhCom = new ViewHolder(v, viewType);

        return vhCom;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String value = "";

        if (position == 0) {
            holder.typeName.setText("Store Information");

            if (store == null)
                return ;
            if (store.getName() != "")
                value += store.getName() + "\n";
            if (store.getWebsite() != "")
                value += store.getWebsite();

            holder.typeValue.setText((value == "") ? (context.getString(R.string.not_defined)) : (value));
        }
        else {
            if (address != null && position == 1) {
                holder.typeName.setText("Address Information");

                if (address.getAddress() != "")
                    value += address.getAddress() + "\n";
                if (address.getPostalCode() > 0)
                    value += address.getPostalCode() + ", ";
                if (address.getCity() != "")
                    value += address.getCity() + "\n";
                if (address.getCountry() != "")
                    value += address.getCountry();

                holder.typeValue.setText((value == "") ? (context.getString(R.string.not_defined)) : (value));
            }
            else {
                holder.typeName.setText("Company Information");

                if (company == null)
                    return ;
                if (company.getName() != "")
                    value += company.getName() + "\n";
                if (company.getWebsite() != "")
                    value += company.getWebsite();

                holder.typeValue.setText((value == "") ? (context.getString(R.string.not_defined)) : (value));
            }
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }
}
