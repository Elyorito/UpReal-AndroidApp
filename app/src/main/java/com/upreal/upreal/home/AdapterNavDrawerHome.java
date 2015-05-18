package com.upreal.upreal.home;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.upreal.upreal.R;

import com.upreal.upreal.utils.CircleImageView;

/**
 * Created by Elyo on 06/02/2015.
 */
public class AdapterNavDrawerHome extends RecyclerView.Adapter<AdapterNavDrawerHome.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[];
    private String mNavAccount[];
    private CircleImageView mCircle;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        TextView textView;
        TextView connexion_name;
        ImageView mImageViewProfile;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);

            if (ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                Holderid = 1;
            }
            else {
                connexion_name = (TextView) itemView.findViewById(R.id.name_profile);
                mImageViewProfile = (ImageView) itemView.findViewById(R.id.image_profile);
                Holderid = 0;
            }
        }
    }

    AdapterNavDrawerHome(String Account[], String Titles[]) {
            this.mNavAccount = Account;
        this.mNavTitles = Titles;

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
                holder.connexion_name.setText("UserTest");
                Bitmap bmp = ((BitmapDrawable) holder.mImageViewProfile.getDrawable()).getBitmap();
                Bitmap output = mCircle.TranformImagetoCircleShape(bmp, bmp.getWidth() * 2);
                holder.mImageViewProfile.setImageBitmap(output);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mNavTitles.length;
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
