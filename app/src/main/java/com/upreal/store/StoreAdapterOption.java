package com.upreal.store;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upreal.upreal.R;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.Store;

/**
 * Created by Elyo on 21/08/15.
 */
public class StoreAdapterOption extends RecyclerView.Adapter<StoreAdapterOption.ViewHolder> {

    private String mOPTION[];
    private Store mStore;
    private SessionManagerUser mSessionManagerUser;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_option;
        CardView mCardOption;
        public ViewHolder(View itemView) {
            super(itemView);
            text_option = (TextView) itemView.findViewById(R.id.text_option);
            mCardOption = (CardView) itemView.findViewById(R.id.cardview_option);
        }
    }
    public StoreAdapterOption(String OPTION[], Store store, SessionManagerUser sessionManagerUser) {
        this.mOPTION = OPTION;
        this.mStore = store;
        this.mSessionManagerUser = sessionManagerUser;
    }
    public StoreAdapterOption(String OPTION[]) {
        this.mOPTION = OPTION;
    }

    @Override
    public StoreAdapterOption.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(StoreAdapterOption.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
