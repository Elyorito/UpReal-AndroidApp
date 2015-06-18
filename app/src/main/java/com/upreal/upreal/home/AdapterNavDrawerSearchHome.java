package com.upreal.upreal.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.upreal.upreal.R;
import com.upreal.upreal.product.AdapterOption;
import com.upreal.upreal.product.ProductSearchActivity;

import static android.widget.TextView.OnEditorActionListener;

/**
 * Created by Elyo on 15/02/2015.
 */
public class AdapterNavDrawerSearchHome extends RecyclerView.Adapter<AdapterNavDrawerSearchHome.ViewHolder> implements View.OnClickListener{

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PRICE = 1;
    private static final int TYPE_RATING = 2;
    private static final int TYPE_BUT_DONE = 3;

    private Context context;
    private String mSearchName;

    public class ViewHolder extends  RecyclerView.ViewHolder{
        int HolderId;

        Button but_done;

        EditText search;
        EditText price_min;
        EditText price_max;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            if (ViewType == TYPE_BUT_DONE) {
                but_done = (Button) itemView.findViewById(R.id.but_done_search);
                HolderId = 3;
            }
            if (ViewType == TYPE_RATING) {
                HolderId = 2;
            } else if (ViewType == TYPE_PRICE) {
                price_max = (EditText) itemView.findViewById(R.id.price_search_max);
                price_min = (EditText) itemView.findViewById(R.id.price_search_min);
                HolderId = 1;
            } else if (ViewType == TYPE_HEADER){
                search = (EditText) itemView.findViewById(R.id.edittext_search);
                search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            ((Activity) v.getContext()).onBackPressed();
                            mSearchName = search.getText().toString();
                        }
                    }
                });
                HolderId = 0;
            }
        }
    }

    public AdapterNavDrawerSearchHome(String searchProduct) {
        this.mSearchName = searchProduct;
    }
    AdapterNavDrawerSearchHome(Context mContext) {
        context = mContext;
    }

    @Override
    public AdapterNavDrawerSearchHome.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_BUT_DONE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.done_search_home, parent, false);

            ViewHolder vhDone = new ViewHolder(v, viewType);
            vhDone.but_done.setOnClickListener(this);
            return vhDone;

        } else if (viewType == TYPE_RATING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_search_home, parent, false);

            ViewHolder vhRate = new ViewHolder(v, viewType);
            return vhRate;
        } else if (viewType == TYPE_PRICE) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_search_home, parent, false);

            ViewHolder vhPrice = new ViewHolder(v, viewType);
            return vhPrice;
        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_search_home, parent, false);

            ViewHolder vhHeader = new ViewHolder(v, viewType);
            vhHeader.search.setHint("produit, marque, magasin, utilisateur");
            vhHeader.search.setTag(vhHeader);
            return vhHeader;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final AdapterNavDrawerSearchHome.ViewHolder holder, int position) {
        //holder.search.setText("Search !");
        /*holder.but_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.search.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "Veuillez rentrer une recherche valide", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent intent = new Intent(v.getContext(), ProductSearchActivity.class);
                    intent.putExtra("searchname", holder.search.getText().toString());
                    v.getContext().startActivity(intent);
                }
            }
        });*/
    }


    @Override
    public void onClick(View v) {
        if (mSearchName == null || mSearchName.isEmpty()) {
            Toast.makeText(v.getContext(), "Veuillez rentrer une recherche valide", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(v.getContext(), mSearchName.toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), ProductSearchActivity.class);
            intent.putExtra("searchname", mSearchName);
            v.getContext().startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        else if (position == 1)
            return TYPE_PRICE;
        else if (position == 2)
            return TYPE_RATING;
        else
            return TYPE_BUT_DONE;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
