package com.upreal.miscellaneous;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.upreal.R;
import com.upreal.utils.Loyalty;

import java.util.List;

/**
 * Created by Eric on 15/11/2015.
 */
public class AdapterLoyaltyCard extends RecyclerView.Adapter<AdapterLoyaltyCard.ViewHolder> {

    private List<Loyalty> loyalties;
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;
        LinearLayout lcard;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.loyalty_name);
            image = (ImageView) itemView.findViewById(R.id.image_loyalty);
            lcard = (LinearLayout) itemView.findViewById(R.id.loyalty_card);
        }
    }
    public AdapterLoyaltyCard(List<Loyalty> loyalties, Context context) {
        this.loyalties = loyalties;
        this.context = context;
    }
    @Override
    public AdapterLoyaltyCard.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_loyalty, parent, false);
        ViewHolder vhnews = new ViewHolder(v);
        return vhnews;
    }

    @Override
    public void onBindViewHolder(AdapterLoyaltyCard.ViewHolder holder, final int position) {
        Bitmap bitmap = this.loyalties.get(position).getBarcode();
        holder.image.setImageBitmap(bitmap);
        holder.title.setText(this.loyalties.get(position).getName());
        holder.lcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoyaltyCardActivity.class);
                intent.putExtra("loyalty", loyalties.get(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return loyalties.size();
    }

}
