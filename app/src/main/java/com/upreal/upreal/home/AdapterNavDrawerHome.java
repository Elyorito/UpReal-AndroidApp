package com.upreal.upreal.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.upreal.upreal.R;

import com.upreal.upreal.utils.BlurImages;
import com.upreal.upreal.utils.CircleImageView;
import com.upreal.upreal.utils.CircleTransform;
import com.upreal.upreal.utils.User;

/**
 * Created by Elyo on 06/02/2015.
 */
public class AdapterNavDrawerHome extends RecyclerView.Adapter<AdapterNavDrawerHome.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private Context context;
    private User user;

    private String mNavTitles[];
    private String mNavAccount;
    private CircleImageView mCircle;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        TextView textView;
        TextView connexion_name;
        ImageView mImageViewProfile;
        //LinearLayout layoutBackground;
        private SparseBooleanArray selectedItems;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);

            if (ViewType == TYPE_ITEM) {
                itemView.setSelected(true);
                textView = (TextView) itemView.findViewById(R.id.rowText);
                Holderid = 1;
            }
            else {
                //layoutBackground = (LinearLayout) itemView.findViewById(R.id.header_image);
                connexion_name = (TextView) itemView.findViewById(R.id.name_profile);
                mImageViewProfile = (ImageView) itemView.findViewById(R.id.image_profile);
                Holderid = 0;
            }
        }
    }

    AdapterNavDrawerHome(String Account, String Titles[], Context context, User user) {
        this.mNavAccount = Account;
        this.mNavTitles = Titles;
        this.context = context;
        this.user = user;
    }

    @Override
    public AdapterNavDrawerHome.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_home,parent,false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_home,parent,false); //Inflating the layout
            ViewHolder vhHeader = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created
        }
        return null;
    }


    @Override
    public void onBindViewHolder(AdapterNavDrawerHome.ViewHolder holder, int position) {
        if (holder.Holderid == 1) {
            holder.textView.setText(mNavTitles[position - 1]);
        }
        else {
            if (position == 0) {
                /*ImageView imageBackground = new ImageView(this.context);
                Picasso.with(this.context).load("http://163.5.84.202/Symfony/web/images/User/" +this.user.getPicture()).into(imageBackground);
                holder.layoutBackground.addView(imageBackground);
                */
                holder.connexion_name.setText(this.mNavAccount);
                /*Bitmap bmp = ((BitmapDrawable) holder.mImageViewProfile.getDrawable()).getBitmap();
                Bitmap output = mCircle.TranformImagetoCircleShape(bmp, bmp.getWidth() * 2);
                holder.mImageViewProfile.setImageBitmap(output);
                */
                Picasso.with(this.context).load("http://163.5.84.202/Symfony/web/images/User/" +this.user.getPicture()).transform(new CircleTransform()).into(holder.mImageViewProfile);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mNavTitles.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
