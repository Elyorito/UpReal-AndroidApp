package com.upreal.store;

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
import com.upreal.utils.IPDefiner;
import com.upreal.utils.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 30/07/2015.
 */
public class StoreSearchAdapter extends RecyclerView.Adapter<StoreSearchAdapter.ViewHolder> {
    private List<Store> list = new ArrayList<>();
    private AlertDialog.Builder builder;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        int holderId;

        TextView mNameUser;
        CardView mCardview;
        Button userUsername;
        Button shareUser;
        ImageView imageStore;


        public ViewHolder(View itemView, int ViewType) {
            super(itemView);

            mCardview = (CardView) itemView.findViewById(R.id.cardview_search);
/*            mNameProduct = (TextView) itemView.findViewById(R.id.but_cardview_go_product);*/
            userUsername = (Button) itemView.findViewById(R.id.but_cardview_go_product);
            imageStore = (ImageView) itemView.findViewById(R.id.image_cardview_product);
            shareUser = (Button) itemView.findViewById(R.id.but_cardview_share);
        }
    }

    StoreSearchAdapter(/*String product[]*/List<Store> liststore, Context context) {
        /*this.mProduct = product;*/

        this.list = liststore;
        this.context = context;
    }

    @Override
    public StoreSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_product_search, parent, false);
        ViewHolder vhitem = new ViewHolder(v, viewType);

        return vhitem;
    }

    @Override
    public void onBindViewHolder(StoreSearchAdapter.ViewHolder holder, final int position) {

        Picasso.with(context).load(new IPDefiner().getIP() + "Symfony/web/images/Store/" + list.get(position).getPicture()).placeholder(R.drawable.connection_img).resize(400, 600).into(holder.imageStore);
        holder.userUsername.setText(list.get(position).getName());
        holder.userUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.but_cardview_go_product:
                        Toast.makeText(v.getContext(), "Fiche Store :[" + list.get(position).getName() + "| " + Integer.toString(list.get(position).getId_address()) + "]", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(v.getContext(), StoreActivity.class);
                        intent.putExtra("store", list.get(position));
                        v.getContext().startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        holder.shareUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);

                i.putExtra(Intent.EXTRA_TEXT, "Venez voir le magasin : " + list.get(position).getName() + " sur UpReal");
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
