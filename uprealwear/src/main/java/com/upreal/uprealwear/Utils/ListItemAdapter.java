package com.upreal.uprealwear.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.upreal.uprealwear.R;
import com.upreal.uprealwear.global.AddListActivity;
import com.upreal.uprealwear.global.DeleteListActivity;
import com.upreal.uprealwear.global.NewsActivity;
import com.upreal.uprealwear.global.RateActivity;
import com.upreal.uprealwear.product.ProductActivity;
import com.upreal.uprealwear.store.StoreActivity;
import com.upreal.uprealwear.user.AchievementActivity;
import com.upreal.uprealwear.user.ItemActivity;
import com.upreal.uprealwear.user.UserActivity;

import java.util.List;

/**
 * Created by Kyosukke on 31/07/2015.
 */
public final class ListItemAdapter extends WearableListView.Adapter {
    private List<Item> dataset;
    private final Context context;
    private final LayoutInflater inflater;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListItemAdapter(Context context, List<Item> dataset) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataset = dataset;
    }

    // Provide a reference to the type of views you're using
    public static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView textView;
        private ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.name);
            imageView = (ImageView) itemView.findViewById(R.id.circle);
        }
    }

    // Create new views for list items
    // (invoked by the WearableListView's layout manager)
    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // Inflate our custom layout for list items
        return new ItemViewHolder(inflater.inflate(R.layout.util_list_item, null));
    }

    // Replace the contents of a list item
    // Instead of creating new views, the list tries to recycle existing ones
    // (invoked by the WearableListView's layout manager)
    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder,
                                 final int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        TextView text = itemHolder.textView;
        ImageView image = itemHolder.imageView;

        text.setText(dataset.get(position).getName());
        if (dataset.get(position).getImagePath() != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            image.setImageBitmap(BitmapFactory.decodeFile(dataset.get(position).getImagePath(), options));
        }
        else
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.picture_unavailable));
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //TODO Bad initialization, need correction
                Intent intent = new Intent(context, ProductActivity.class);

                Log.e("ListItemAdapter", "Item clicked at " + position + ".");

                switch (dataset.get(position).getTargetType()) {
                    case 1:
                        intent = new Intent(context, UserActivity.class);
                        break ;
                    case 2:
                        intent = new Intent(context, ProductActivity.class);
                        break ;
                    case 3:
                        intent = new Intent(context, StoreActivity.class);
                        break ;
                    case 4:
                        intent = new Intent(context, NewsActivity.class);
                        break ;
                    case 5:
                        intent = new Intent(context, RateActivity.class);
                        break ;
                    case 6:
                        intent = new Intent(context, ItemActivity.class);
                        break ;
                    case 7:
                        intent = new Intent(context, DeleteListActivity.class);
                        break ;
                    case 8:
                        intent = new Intent(context, AddListActivity.class);
                         break ;
                    case 9:
                        intent = new Intent(context, AchievementActivity.class);
                        break ;
                    default:
                        break ;
                }

                intent.putExtra("item", dataset.get(position));
                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset
    // (invoked by the WearableListView's layout manager)
    @Override
    public int getItemCount() {
        return dataset.size();
    }
}