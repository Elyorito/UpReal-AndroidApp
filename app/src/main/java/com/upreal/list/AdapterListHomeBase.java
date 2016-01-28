package com.upreal.list;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upreal.R;

import java.util.ArrayList;

/**
 * Created by Elyo on 11/05/2015.
 */
public class AdapterListHomeBase extends RecyclerView.Adapter<AdapterListHomeBase.ViewHolder>{

    private static final int TYPE_BASE = 0;
    private static final int TYPE_CUSTOM = 1;

    private String mBase_list[];
    private String mDelimiter[];
    private ArrayList<ArrayList<String[]>> mBaseLists;
    private Intent intent;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int HolderId;

        TextView list_name;
        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == TYPE_BASE){
                list_name = (TextView) itemView.findViewById(R.id.rowtextlist);
                HolderId = 0;
            }
        }
    }

    AdapterListHomeBase(ArrayList<ArrayList<String[]>> baseLists, String base_list[], String delimiter[]) {
        this.mBase_list = base_list;
        this.mDelimiter = delimiter;
        this.mBaseLists = baseLists;
    }

    @Override
    public AdapterListHomeBase.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BASE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_home, parent, false);
            ViewHolder vhitem = new ViewHolder(v, viewType);

            return vhitem;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(AdapterListHomeBase.ViewHolder holder, final int position) {
        if (holder.HolderId == 0) {
            holder.list_name.setText(mBase_list[position]);

            holder.list_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.rowtextlist://Product liked
                            intent = new Intent(v.getContext(), ListBaseActivity.class);
                            intent.putExtra("listcustom", mBaseLists.get(0));
                            intent.putExtra("namelist", v.getContext().getString(R.string.liked_product));
                            v.getContext().startActivity(intent);
                            break;
                        case 1:// Follow User
                            intent  = new Intent(v.getContext(), ListCustomActivity.class);
                            intent.putExtra("listcustom", mBaseLists.get(1));
                            v.getContext().startActivity(intent);
                            break;
                        case 2:// History
                            intent = new Intent(v.getContext(), ListCustomActivity.class);
                            intent.putExtra("listcustom", mBaseLists.get(2));
                            v.getContext().startActivity(intent);
                            break;
                        case 3:// Commentary
                            intent = new Intent(v.getContext(), ListCustomActivity.class);
                            intent.putExtra("listcustom", mBaseLists.get(3));
                            v.getContext().startActivity(intent);
                            break;
                        case 4:// List troc product
                            intent = new Intent(v.getContext(), ListCustomActivity.class);
                            intent.putExtra("listcustom", mBaseLists.get(4));
                            v.getContext().startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_BASE;
        return TYPE_BASE;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return mBase_list.length;
    }
}
