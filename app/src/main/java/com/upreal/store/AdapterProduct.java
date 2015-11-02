package com.upreal.store;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.utils.CircleTransform;
import com.upreal.utils.Product;
import com.upreal.utils.StoreSell;

import java.util.List;

/**
 * Created by Kyosukke on 01/11/2015.
 */
public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ViewHolder> {

    Context context;
    List<StoreSell> listStoreSell;
    List<Product> listProduct;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int HolderId;

        ImageView productImg;
        TextView productName;
        RatingBar productRate;
        Button productPrice;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            productImg = (ImageView) itemView.findViewById(R.id.product_image);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            productRate = (RatingBar) itemView.findViewById(R.id.product_rate);
            productPrice = (Button) itemView.findViewById(R.id.product_price);
            HolderId = 0;
        }
    }

    public AdapterProduct(Context context, List<StoreSell> listStoreSell, List<Product> listProduct) {
        this.listStoreSell = listStoreSell;
        this.listProduct = listProduct;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (getItemCount() == 0) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            ViewHolder vhCom = new ViewHolder(v, viewType);
            return vhCom;
        }
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product, viewGroup, false);

        ViewHolder vhCom = new ViewHolder(v, viewType);

        return vhCom;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Picasso.with(context).load("http://163.5.84.202/Symfony/web/images/Product/" + listProduct.get(position).getPicture()).transform(new CircleTransform()).into(holder.productImg);
        holder.productName.setText(this.listProduct.get(position).getName());
        holder.productRate.setRating(2f);
        holder.productPrice.setText(this.listStoreSell.get(position).getPrice() + " €");
        holder.productPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (v.getId()) {
                    case R.id.product_price:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listStoreSell.size();
    }
}
