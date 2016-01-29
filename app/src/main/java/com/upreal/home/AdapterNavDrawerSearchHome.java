package com.upreal.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.product.ProductSearchActivity;

import static android.widget.TextView.OnEditorActionListener;

/**
 * Created by Elyo on 15/02/2015.
 */
public class AdapterNavDrawerSearchHome extends RecyclerView.Adapter<AdapterNavDrawerSearchHome.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_BUT_DONE = 1;
    private static final int TYPE_ICON = 2;

    private Context context;
    private String mSearchName;
    private EditText search;
    private ImageView icon;


    public class ViewHolder extends  RecyclerView.ViewHolder{
        int HolderId;

        Button but_done;


        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            if (ViewType == TYPE_BUT_DONE) {
                but_done = (Button) itemView.findViewById(R.id.but_done_search);
                HolderId = 1;
            } else if (ViewType == TYPE_HEADER){
                search = (EditText) itemView.findViewById(R.id.edittext_search);
                search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            mSearchName = search.getText().toString();
                        }
                    }
                });
                HolderId = 0;
            } else if (ViewType == TYPE_ICON){
                icon = (ImageView) itemView.findViewById(R.id.imageView);
                Log.v("LOL", icon.getWidth() + " " + icon.getHeight());
                HolderId = 2;
            }
        }
    }

    AdapterNavDrawerSearchHome(Context mContext) {
        context = mContext;
    }

    @Override
    public AdapterNavDrawerSearchHome.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_BUT_DONE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.done_search_home, parent, false);

            ViewHolder vhDone = new ViewHolder(v, viewType);
            return vhDone;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_search_home, parent, false);

            ViewHolder vhHeader = new ViewHolder(v, viewType);
            search.setHint("produit, marque, magasin, utilisateur");
            search.setTag(vhHeader);
            return vhHeader;
        } else if (viewType == TYPE_ICON){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_search_home, parent, false);
            ViewHolder vhIcon = new ViewHolder(v, viewType);
            return vhIcon;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final AdapterNavDrawerSearchHome.ViewHolder holder, int position) {
        if (holder.HolderId == 0) {
            search.setOnEditorActionListener(new OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        mSearchName =  /*holder.*/search.getText().toString();
                        return false;
                    }
                    return false;
                }
            });
        }

        if (holder.HolderId == 1) {
            holder.but_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSearchName = search.getText().toString();
                    if (mSearchName == null || mSearchName.isEmpty()) {
                        Toast.makeText(v.getContext(), "Veuillez rentrer une recherche valide", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Intent intent = new Intent(v.getContext(), ProductSearchActivity.class);
                        intent.putExtra("searchname", mSearchName);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }

        if (holder.HolderId == 2) {
            Picasso.with(context)
                    .load(R.drawable.flat_magnifier_icon)
                    .resize(800,566)
                    .into(icon);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        else if (position == 1)
            return TYPE_ICON;
        else
            return TYPE_BUT_DONE;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
