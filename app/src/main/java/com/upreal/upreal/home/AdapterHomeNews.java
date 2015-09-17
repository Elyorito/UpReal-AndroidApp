package com.upreal.upreal.home;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.upreal.upreal.R;
import com.upreal.upreal.news.NewsActivity;
import com.upreal.upreal.utils.Article;

import java.util.List;

/**
 * Created by Sofiane on 31/07/2015.
 */
public class AdapterHomeNews extends RecyclerView.Adapter<AdapterHomeNews.ViewHolder> {

    private List<Article> articles;
    private Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {

        Button type;
        TextView title;
        ImageView image;
        LinearLayout news;
        public ViewHolder(View itemView) {
            super(itemView);
            type = (Button) itemView.findViewById(R.id.fab);
            title = (TextView) itemView.findViewById(R.id.titlenews);
            image = (ImageView) itemView.findViewById(R.id.imagenews);
            news = (LinearLayout) itemView.findViewById(R.id.newsarticle);
        }
    }

    AdapterHomeNews(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @Override
    public AdapterHomeNews.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_news, viewGroup, false);
        ViewHolder vhnews = new ViewHolder(v);
        return vhnews;
    }

    @Override
    public void onBindViewHolder(AdapterHomeNews.ViewHolder viewHolder, final int i) {
        Picasso.with(context).load("http://163.5.84.202/Symfony/web/images/News/" + this.articles.get(i).getPicture()).placeholder(R.drawable.logo_small).into(viewHolder.image);
        viewHolder.title.setText(this.articles.get(i).getTitle());
        viewHolder.type.setText(Integer.toString(this.articles.get(i).getType()));
        viewHolder.news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Id=" + Integer.toString(i), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), NewsActivity.class);
                intent.putExtra("listnews", articles.get(i));
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
