package com.upreal.upreal.user;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upreal.upreal.R;
import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.User;

/**
 * Created by Elyo on 21/08/15.
 */
public class UserAdapterOption extends RecyclerView.Adapter<UserAdapterOption.ViewHolder>{

    private String mOPTION[];
    private User mUser;
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

    public UserAdapterOption(String OPTION[], User user, SessionManagerUser sessionManagerUser) {
        this.mOPTION = OPTION;
        this.mUser = user;
        this.mSessionManagerUser = sessionManagerUser;
    }
    public UserAdapterOption(String OPTION[]) {
        this.mOPTION = OPTION;
    }

    @Override
    public UserAdapterOption.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_option, parent, false);
        ViewHolder vhOption = new ViewHolder(v);
        return vhOption;

    }

    @Override
    public void onBindViewHolder(UserAdapterOption.ViewHolder holder, final int i) {
        holder.text_option.setText(this.mOPTION[i]);

        holder.mCardOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (i == 0) {
                    Log.v("Editer mon compte", "tralala");
                    Intent intent = new Intent(v.getContext(), UserUpdateActivity.class);
                    v.getContext().startActivity(intent);
                } else if (i == 1) {
                    Intent intent = new Intent(v.getContext(), UserChangePwd.class);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOPTION.length;
    }
}
