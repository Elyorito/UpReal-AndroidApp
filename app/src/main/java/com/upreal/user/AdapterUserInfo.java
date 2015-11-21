package com.upreal.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upreal.R;
import com.upreal.utils.User;

/**
 * Created by Kyosukke on 21/11/2015.
 */
public class AdapterUserInfo extends RecyclerView.Adapter<AdapterUserInfo.ViewHolder> {

    Context context;
    User user;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int HolderId;

        TextView typeName;
        TextView typeValue;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            typeName = (TextView) itemView.findViewById(R.id.type_name);
            typeValue = (TextView) itemView.findViewById(R.id.type_value);

            HolderId = 0;
        }
    }

    public AdapterUserInfo(Context context, User user) {
        this.context = context;
        this.user = user;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (getItemCount() == 0) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            ViewHolder vhCom = new ViewHolder(v, viewType);
            return vhCom;
        }
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_specification, viewGroup, false);

        ViewHolder vhCom = new ViewHolder(v, viewType);

        return vhCom;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String value = "";

        holder.typeName.setText("User Information");

        if (user.getUsername() != "")
            value += user.getUsername() + "\n";
        if (user.getFirstname() != "" && user.getLastname() != "")
            value += user.getFirstname() + " " + user.getLastname() + "\n";
        if (user.getEmail() != "")
            value += user.getEmail() + "\n";
        if (user.getPhone() > 0)
            value += user.getPhone();

        holder.typeValue.setText((value == "") ? (context.getString(R.string.not_defined)) : (value));
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
