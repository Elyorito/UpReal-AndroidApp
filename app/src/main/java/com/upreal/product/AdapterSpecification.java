package com.upreal.product;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upreal.R;
import com.upreal.utils.Characteristic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 05/11/2015.
 */
public class AdapterSpecification extends RecyclerView.Adapter<AdapterSpecification.ViewHolder> {

    List<Characteristic> listCharacteristics;
    List<String> types;

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

    public AdapterSpecification(Context context, List<Characteristic> listCharacteristics) {
        this.listCharacteristics = listCharacteristics;

        this.types = new ArrayList<String>();

        types.add(context.getString(R.string.description));
        types.add(context.getString(R.string.nutritional));
        types.add(context.getString(R.string.component));
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

        for (Characteristic c : listCharacteristics) {
            if (c.getType() == position && position != 0) {
                value += c.getName() + ": " + c.getValue() + " " + c.getHealthy() + "\n";
            }
            else if (c.getType() == position && position == 0) {
                value = c.getValue();
                break ;
            }
        }

        if (value != null || value != "") {
            holder.typeName.setText(types.get(position));
            holder.typeValue.setText(value);
        }
        else {
            holder.typeName.setText(null);
            holder.typeValue.setText(null);
        }
    }

    @Override
    public int getItemCount() {
        return types.size();
    }
}
