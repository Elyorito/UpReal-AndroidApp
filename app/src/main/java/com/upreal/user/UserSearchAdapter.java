package com.upreal.user;

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
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.IPDefiner;
import com.upreal.utils.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 26/05/2015.
 */
public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.ViewHolder> {

    private List<User> list = new ArrayList<User>();
    private AlertDialog.Builder builder;
    private Context context;
    private ConnectionDetector cd;

    public class ViewHolder extends RecyclerView.ViewHolder {
        int holderId;

        TextView mNameUser;
        CardView mCardview;
        Button userUsername;
        Button shareUser;
        ImageView imageUser;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);

            mCardview = (CardView) itemView.findViewById(R.id.cardview_search);
            userUsername = (Button) itemView.findViewById(R.id.but_cardview_go_product);
            shareUser = (Button) itemView.findViewById(R.id.but_cardview_share);
            imageUser = (ImageView) itemView.findViewById(R.id.image_cardview_product);
        }
    }

    UserSearchAdapter(List<User> listuser, Context context) {


        this.list = listuser;
        this.context = context;
        cd = new ConnectionDetector(context);
    }

    @Override
    public UserSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_product_search, parent, false);
        ViewHolder vhitem = new ViewHolder(v, viewType);

        return vhitem;
    }

    @Override
    public void onBindViewHolder(UserSearchAdapter.ViewHolder holder, final int position) {
        if (cd.isConnectedToInternet())
            Picasso.with(context).load(new IPDefiner().getIP() + "Symfony/web/images/User/" + list.get(position).getPicture()).placeholder(R.drawable.connection_img).resize(400, 600).into(holder.imageUser);
        else
            Toast.makeText(context, R.string.no_internet_connection + R.string.please_reload, Toast.LENGTH_SHORT).show();

        holder.userUsername.setText(list.get(position).getUsername());
        holder.userUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.but_cardview_go_product:
                        Toast.makeText(v.getContext(), "Fiche User :[" + list.get(position).getUsername() + "| " + Integer.toString(list.get(position).getId_address()) + "]", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(v.getContext(), UserActivity.class);
                        intent.putExtra("user", list.get(position));
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

                i.putExtra(Intent.EXTRA_TEXT, "Venez voir l'utilisateur : " + list.get(position).getUsername() + " sur UpReal");
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
        return list.size();
    }
}