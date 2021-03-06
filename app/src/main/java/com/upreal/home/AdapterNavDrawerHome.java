package com.upreal.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.utils.BlurImages;
import com.upreal.utils.CircleTransform;
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.IPDefiner;
import com.upreal.utils.User;

/**
 * Created by Elyo on 06/02/2015.
 */
public class AdapterNavDrawerHome extends RecyclerView.Adapter<AdapterNavDrawerHome.ViewHolder> {

    private ConnectionDetector cd;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private Context context;
    private User user;

    private String mNavTitles[];
    private String mNavAccount;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        TextView textView;
        TextView connexion_name;
        ImageView mImageViewProfile;
        ImageView mImageBlurred;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);

            if (ViewType == TYPE_ITEM) {
                itemView.setSelected(true);
                textView = (TextView) itemView.findViewById(R.id.rowText);
                Holderid = 1;
            }
            else {
                mImageBlurred = (ImageView) itemView.findViewById(R.id.imageblurred);
                connexion_name = (TextView) itemView.findViewById(R.id.name_profile);
                mImageViewProfile = (ImageView) itemView.findViewById(R.id.image_profile);
                Holderid = 0;
            }
        }
    }

    public AdapterNavDrawerHome(String Account, String Titles[], Context context, User user) {
        this.mNavAccount = Account;
        this.mNavTitles = Titles;
        this.context = context;
        this.user = user;
        cd = new ConnectionDetector(context);
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
                if (user.getPicture() != null && user.getPicture().length() > 15) {
                    Picasso.with(this.context).load(user.getPicture()).transform(new BlurImages(this.context, 25)).into(holder.mImageBlurred);
                    holder.connexion_name.setText(this.mNavAccount);
                    Picasso.with(this.context)
                            .load(this.user.getPicture()).transform(new CircleTransform())
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(holder.mImageViewProfile);
                } else {
                    if (cd.isConnectedToInternet()) {
                        Picasso.with(this.context)
                                .load(new IPDefiner().getIP() + "Symfony/web/images/User/" + this.user.getPicture())
                                .transform(new BlurImages(this.context, 25))
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(holder.mImageBlurred);
                        holder.connexion_name.setText(this.mNavAccount);
                        Picasso.with(this.context)
                                .load(new IPDefiner().getIP() + "Symfony/web/images/User/" + this.user.getPicture())
                                .transform(new CircleTransform())
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(holder.mImageViewProfile);
                    }                }
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
