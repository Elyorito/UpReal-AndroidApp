package com.upreal.upreal.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upreal.upreal.R;

/**
 * Created by Elyo on 11/05/2015.
 */
public class AdapterListHomeBase extends RecyclerView.Adapter<AdapterListHomeBase.ViewHolder>{

    private static final int TYPE_BASE = 0;
    private static final int TYPE_CUSTOM = 1;

    //private AlertDialog.Builder builder;
    private String mBase_list[];
    private String mDelimiter[];

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

    AdapterListHomeBase(String base_list[], String delimiter[]) {
        this.mBase_list = base_list;
        this.mDelimiter = delimiter;
    }

    @Override
    public AdapterListHomeBase.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*if (viewType == TYPE_CUSTOM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_home, parent, false);
            ViewHolder vhcust = new ViewHolder(v, viewType);

            return vhcust;
        } else */
        if (viewType == TYPE_BASE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_home, parent, false);
            ViewHolder vhitem = new ViewHolder(v, viewType);

            return vhitem;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(AdapterListHomeBase.ViewHolder holder, int position) {
//        holder.list_name.setText(mBase_list[position]);
        /*if (holder.HolderId == 1) {
            holder.list_name.setText(mDelimiter[0]);
        } else */
        if (holder.HolderId == 0) {
            holder.list_name.setText(mBase_list[position]);
        }
        holder.list_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case 0://Product liked
                        break;
                    case 1://Follow User
                        break;
                    case 2:// History
                        break;
                    case 3:// Commentary
                        break;
                    case 4:// List troc product
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_BASE;

        /*return TYPE_CUSTOM;*/
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
