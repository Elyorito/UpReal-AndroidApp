package com.upreal.product;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.upreal.R;
import com.upreal.utils.Characteristic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 05/11/2015.
 */
public class AdapterSpecification extends RecyclerView.Adapter<AdapterSpecification.ViewHolder> {

    Context context;
    List<Characteristic> listCharacteristics;
    List<String> types;
    int size = 0;
    int health = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int HolderId;

        TextView typeName;
        TextView typeValue;
        ImageView health;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            typeName = (TextView) itemView.findViewById(R.id.type_name);
            typeValue = (TextView) itemView.findViewById(R.id.type_value);
            health = (ImageView) itemView.findViewById(R.id.health);

            HolderId = 0;
        }
    }

    public AdapterSpecification(Context context, List<Characteristic> listCharacteristics) {
        this.context = context;
        this.listCharacteristics = listCharacteristics;
        boolean hasNutritional = false;
        boolean hasComponent = false;
        boolean hasAdditive = false;

        this.types = new ArrayList<String>();

        types.add(context.getString(R.string.description));

        for (Characteristic c : listCharacteristics) {
            if (c.getType() == 1 && !hasNutritional) {
                types.add(context.getString(R.string.nutritional));
                hasNutritional = true;
            }
            if (c.getType() == 2 && !hasComponent) {
                types.add(context.getString(R.string.component));
                hasComponent = true;
            }
            if (c.getType() == 3 && !hasAdditive) {
                types.add(context.getString(R.string.additive));
                hasAdditive = true;
            }
        }

        size = types.size();
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

        if (types.get(position).equals(context.getString(R.string.description))) {
            value = getCharacteristicFromType(0);
        }
        else if (types.get(position).equals(context.getString(R.string.nutritional))) {
            value = getCharacteristicFromType(1);
        }
        else if (types.get(position).equals(context.getString(R.string.component))) {
            value = getCharacteristicFromType(2);
        }
        else if (types.get(position).equals(context.getString(R.string.additive))) {
            value = getCharacteristicFromType(3);
            switch (health) {
                case 1:
                    holder.health.setImageResource(R.drawable.very_good_health);
                    break ;
                case 0:
                    holder.health.setImageResource(R.drawable.ok_health);
                    break ;
                case -1:
                    holder.health.setImageResource(R.drawable.bad_health);
                    break ;
            }
        }

        if (value != null && value != "") {
            holder.typeName.setText(types.get(position));
            holder.typeValue.setText(value);
        }
        else {
            holder.typeName.setText("");
            holder.typeValue.setText("");
        }
    }

    public String getCharacteristicFromType(int type) {
        String value = "";
        int num = 0;

        for (Characteristic c : listCharacteristics) {
            if (type == c.getType()) {
                if (type == 0)
                    value = c.getValue();
                else
                    value += c.getName() + ": " + c.getValue() + "\n";

                if (c.getType() == 3) {
                    num++;
                    if (c.getHealthy() == 1)
                        health++;
                    else if (c.getHealthy() == 0)
                        health--;
                }
            }
        }

        if (health < 0)
            health = -1;
        else if (health == 0 || num - health > num / 2)
            health = 0;
        else
            health = 1;

        if (value != null && value != "")
            return value;
        else if (type == 0)
            return context.getString(R.string.not_defined);

        return null;
    }

    @Override
    public int getItemCount() {
        return size;
    }
}
