package com.upreal.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.utils.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 16/02/2015.
 */
public class ProductSearchAdapter extends RecyclerView.Adapter<ProductSearchAdapter.ViewHolder> {

/*
    private String mProduct[];
*/
    private List<Product> list = new ArrayList<Product>();
    private AlertDialog.Builder builder;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        int holderId;

        TextView mNameProduct;
        CardView mCardview;
        Button descProduct;
        Button shareProduct;
        ImageView imageProduct;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);

            mCardview = (CardView) itemView.findViewById(R.id.cardview_search);
            imageProduct = (ImageView) itemView.findViewById(R.id.image_cardview_product);
            descProduct = (Button) itemView.findViewById(R.id.but_cardview_go_product);
            shareProduct = (Button) itemView.findViewById(R.id.but_cardview_share);
        }
    }

    ProductSearchAdapter(/*String product[]*/List<Product> listprod, Context context) {
        /*this.mProduct = product;*/

        this.list = listprod;
        this.context = context;
    }

    @Override
    public ProductSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_product_search, parent, false);
        ViewHolder vhitem = new ViewHolder(v, viewType);

        return vhitem;
    }

    @Override
    public void onBindViewHolder(ProductSearchAdapter.ViewHolder holder, final int position) {
/*
        holder.mNameProduct.setText(mProduct[position]);
*/
/*        holder.mNameProduct.setText(list.get(position).getName());*/
        //Log.v("Picture", list.get(position).getPicture());
        Picasso.with(context).load("http://163.5.84.202/Symfony/web/images/Product/" + list.get(position).getPicture()).placeholder(R.drawable.connection_img).resize(400, 600).into(holder.imageProduct);
                holder.descProduct.setText(list.get(position).getName());
//        holder.mCardview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.but_cardview_go_product:
//                        Toast.makeText(v.getContext(), "Fiche Product :[" + list.get(position).getName() + "]", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(v.getContext(), ProductActivity.class);
//                        intent.putExtra("listprod", list.get(position));
//                        v.getContext().startActivity(intent);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
        holder.descProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.but_cardview_go_product:
                        Toast.makeText(v.getContext(), "Fiche Product :[" + list.get(position).getName() + "]", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(v.getContext(), ProductActivity.class);
                        intent.putExtra("prod", list.get(position));
                        v.getContext().startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        holder.shareProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, "Venez voir le produit : " + list.get(position).getName() + " sur UpReal");
                i.setType("text/plain");
                try {
                    v.getContext().startActivity(Intent.createChooser(i, "Partager ce produit avec ..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.need_mail_app)
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        /*return mProduct.length;*/
        return list.size();
    }
}