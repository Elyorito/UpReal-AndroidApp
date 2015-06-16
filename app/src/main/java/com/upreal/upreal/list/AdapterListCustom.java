package com.upreal.upreal.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.dragsortadapter.DragSortAdapter;
import com.upreal.upreal.R;
import com.upreal.upreal.utils.Product;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 12/06/2015.
 */
public class AdapterListCustom extends UltimateViewAdapter {
        private ArrayList<Product> products;

        public AdapterListCustom(ArrayList<Product> prod) {
            this.products = prod;
        }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    class ViewHolder extends UltimateRecyclerviewViewHolder {

            TextView nameList;

            public ViewHolder(View itemView) {
                super(itemView);
                nameList = (TextView) itemView.findViewById(
                        R.id.text_listcut);
            }
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((ViewHolder) holder).nameList.setText(products.get(position).getName());
        }

        @Override
        public int getAdapterItemCount() {
            return products.size();
        }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
        public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_custom_drag, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

    @Override
    public <T> void insert(List<T> list, T object, int position) {
        super.insert(list, object, position);
    }

    @Override
    public void remove(List<?> list, int position) {
        super.remove(list, position);
    }

    @Override
    public void clear(List<?> list) {
        super.clear(list);
    }

    @Override
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }

    @Override
    public void swapPositions(List<?> list, int from, int to) {
        super.swapPositions(list, from, to);
    }

    /*
        public void insert(String string, int position) {
            insert(stringList, string, position);
        }

        public void remove(int position) {
            remove(stringList, position);
        }

        public void clear() {
            clear(stringList);
        }

        @Override
        public void toggleSelection(int pos) {
            super.toggleSelection(pos);
        }

        @Override
        public void setSelected(int pos) {
            super.setSelected(pos);
        }

        @Override
        public void clearSelection(int pos) {
            super.clearSelection(pos);
        }


        public void swapPositions(int from, int to) {
            swapPositions(stringList, from, to);
        }*/

}