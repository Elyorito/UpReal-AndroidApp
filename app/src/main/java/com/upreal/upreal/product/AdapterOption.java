package com.upreal.upreal.product;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upreal.upreal.R;

/**
 * Created by Elyo on 16/03/2015.
 */
public class AdapterOption extends RecyclerView.Adapter<AdapterOption.ViewHolder> implements View.OnClickListener{

    private String mOPTION[];

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int holderId;

        TextView text_option;
        CardView mCardOption;
        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            text_option = (TextView) itemView.findViewById(R.id.text_option);
        }
    }

    AdapterOption(String OPTION[]) {
        this.mOPTION = OPTION;
    }

    @Override
    public AdapterOption.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product_option, viewGroup, false);

        ViewHolder vhOption = new ViewHolder(v, i);
        return vhOption;
    }

    @Override
    public void onBindViewHolder(AdapterOption.ViewHolder viewHolder, int i) {
        viewHolder.text_option.setText(this.mOPTION[i]);
    }

    @Override
    public int getItemCount() {
        return mOPTION.length;
    }

    @Override
    public void onClick(View v) {
    }
}
