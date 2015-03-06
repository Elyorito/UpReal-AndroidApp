package com.upreal.upreal.product;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upreal.upreal.R;

/**
 * Created by Elyo on 16/02/2015.
 */
public class ProductSearchAdapter extends RecyclerView.Adapter<ProductSearchAdapter.ViewHolder> {

    private String mProduct[];

    public class ViewHolder extends RecyclerView.ViewHolder{
        int holderId;

        TextView mNameProduct;
        CardView mCardview;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);

            mCardview = (CardView) itemView.findViewById(R.id.cardview_search);
            mNameProduct = (TextView) itemView.findViewById(R.id.text_cardview_search);
        }
    }

    ProductSearchAdapter(String product[]) {
        this.mProduct = product;
    }

    @Override
    public ProductSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_product_search, parent, false);

        ViewHolder vhitem = new ViewHolder(v, viewType);

        return vhitem;
    }

    @Override
    public void onBindViewHolder(ProductSearchAdapter.ViewHolder holder, int position) {
        holder.mNameProduct.setText(mProduct[position]);
    }

    @Override
    public int getItemCount() {
        return mProduct.length;
    }
}
