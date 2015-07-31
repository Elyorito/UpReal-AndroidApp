package com.upreal.upreal.home;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.upreal.upreal.R;

/**
 * Created by Sofiane on 31/07/2015.
 */
public class AdapterHomeNews extends RecyclerView.Adapter<AdapterHomeNews.ViewHolder> {

    String title[];
    String imageimg[];
    int typenews[];

    public static class ViewHolder extends RecyclerView.ViewHolder {

        Button type;
        TextView title;
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            type = (Button) itemView.findViewById(R.id.fab);
            title = (TextView) itemView.findViewById(R.id.titlenews);
            image = (ImageView) itemView.findViewById(R.id.imagenews);
        }
    }

    AdapterHomeNews(String title[], String imageimg[], int typenews[]) {
        this.title = title;
        this.imageimg = imageimg;
        this.typenews = typenews;
    }

    @Override
    public AdapterHomeNews.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_news, viewGroup, false);
        ViewHolder vhnews = new ViewHolder(v);
        return vhnews;
    }

    @Override
    public void onBindViewHolder(AdapterHomeNews.ViewHolder viewHolder, int i) {
        viewHolder.title.setText(title[i]);
        //viewHolder.image
        viewHolder.type.setText(Integer.toString(typenews[i]));

    }

    @Override
    public int getItemCount() {
        return title.length;
    }
}
