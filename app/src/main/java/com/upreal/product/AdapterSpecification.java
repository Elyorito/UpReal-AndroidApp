package com.upreal.product;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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

    Context context;
    List<Characteristic> listCharacteristics;
    List<String> types;
    int size = 0;

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
        this.context = context;
        this.listCharacteristics = listCharacteristics;
        boolean hasNutritional = false;
        boolean hasComponent = false;
        boolean hasAdditive = false;

        this.types = new ArrayList<>();

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
        SpannableStringBuilder builder = null;

        if (types.get(position).equals(context.getString(R.string.description))) {
            builder = getCharacteristicFromType(0);
        }
        else if (types.get(position).equals(context.getString(R.string.nutritional))) {
            builder = getCharacteristicFromType(1);
        }
        else if (types.get(position).equals(context.getString(R.string.component))) {
            builder = getCharacteristicFromType(2);
        }
        else if (types.get(position).equals(context.getString(R.string.additive))) {
            builder = getCharacteristicFromType(3);
        }
        if (builder != null) {
            holder.typeName.setTypeface(null, Typeface.BOLD);
            holder.typeName.setText(types.get(position));
            holder.typeValue.setText(builder, TextView.BufferType.SPANNABLE);
        }
        else {
            holder.typeName.setText("");
            holder.typeValue.setText("");
        }
    }

    public int getColor(Characteristic c) {
        int color;
        switch (c.getHealthy()) {
            case 0:
                color = Color.rgb(76, 175, 80);
                break;
            case 1:
                color = Color.rgb(139,195,74);
                break;
            case 2:
                color = Color.rgb(205, 220, 57);
                break;
            case 3:
                color = Color.rgb(255,193,7);
                break;
            case 4:
                color = Color.rgb(243, 190, 82);
                break;
            default:
                color = Color.RED;
                break;
        }
        return color;
    }


    public SpannableStringBuilder getCharacteristicFromType(int type) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        int num = 0;
        for (Characteristic c : listCharacteristics) {
            if (type == c.getType()) {
                if (type == 0) {
                    SpannableString value = new SpannableString(c.getValue());
                    value.setSpan(new ForegroundColorSpan(Color.BLACK), 0, value.length(), 0);
                    builder.append(value);
                }
                else if (type == 1) {
                    String[] array = c.getValue().split(",");
                    StringBuffer s = new StringBuffer();
                    for (int i = 0; i < array.length; i += 2) {
                        if (i + 1 < array.length)
                            s.append(array[i] + " : " +  array[i + 1] + "\n");
                    }
                    SpannableString value = new SpannableString(s);
                    value.setSpan(new ForegroundColorSpan(Color.BLACK), 0, value.length(), 0);
                    builder.append(value);
                }
                else if (type == 3) {
                    int color;
                    color = getColor(c);
                    SpannableString healthy = new SpannableString(c.getName() + "\n");
                    healthy.setSpan(new ForegroundColorSpan(color), 0, healthy.length(), 0);
                    builder.append(healthy);
                }
                else {
                    SpannableString value = new SpannableString(c.getName() + ": " + c.getValue() + "\n");
                    value.setSpan(new ForegroundColorSpan(Color.BLACK), 0, value.length(), 0);
                    builder.append(value);
                }
            }
        }
        if (builder != null)
            return builder;
        else if (type == 0) {
            SpannableString value = new SpannableString(context.getString(R.string.not_defined));
            value.setSpan(new ForegroundColorSpan(Color.BLACK), 0, value.length(), 0);
            builder.append(value);
            return builder;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return size;
    }
}
