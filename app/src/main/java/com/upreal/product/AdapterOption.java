package com.upreal.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.upreal.login.LoginActivity;
import com.upreal.R;
import com.upreal.utils.Product;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.database.DatabaseHelper;
import com.upreal.utils.database.DatabaseQuery;

import java.util.ArrayList;

/**
 * Created by Elyo on 16/03/2015.
 */
public class AdapterOption extends RecyclerView.Adapter<AdapterOption.ViewHolder> implements View.OnClickListener{

    private String mOPTION[];
    private Product mProduct;
    private SessionManagerUser mSessionManagerUser;

    private AlertDialog.Builder builder;
    private AlertDialog.Builder builderCustom;
    private View dialogView;

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private DatabaseQuery mDbQuery;

    private RecyclerView recyclerViewCustomList;

    private ArrayList<Integer> checkedList = new ArrayList<>();


    public static class ViewHolder extends RecyclerView.ViewHolder {
        int holderId;

        //Change Textview to Button
        TextView text_option;
        CardView mCardOption;
        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            text_option = (TextView) itemView.findViewById(R.id.text_option);
            mCardOption = (CardView) itemView.findViewById(R.id.cardview_option);
        }
    }

    public AdapterOption(String OPTION[], Product product, SessionManagerUser sessionManagerUser) {
        this.mOPTION = OPTION;
        this.mProduct = product;
        this.mSessionManagerUser = sessionManagerUser;
    }
    public AdapterOption(String OPTION[]) {
        this.mOPTION = OPTION;
    }

    @Override
    public AdapterOption.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product_option, viewGroup, false);
        ViewHolder vhOption = new ViewHolder(v, i);
        return vhOption;
    }

    @Override
    public void onBindViewHolder(AdapterOption.ViewHolder viewHolder, final int i) {
        viewHolder.text_option.setText(this.mOPTION[i]);

        viewHolder.mCardOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                builder = new AlertDialog.Builder(v.getContext());

                switch (i) {
                    case 0://Edit
                        if (!mSessionManagerUser.isLogged()) {
                            builder.setTitle("Vous voulez changer ce produit ?").setMessage("Connectez-vous pour continuer.")
                                    .setPositiveButton(v.getContext().getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(v.getContext(), LoginActivity.class);
                                            v.getContext().startActivity(intent);
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton(v.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).create().show();
                        } else {
                            Intent intent = new Intent(v.getContext(), ProductUpdateActivity.class);
                            intent.putExtra("prod", mProduct);
                            v.getContext().startActivity(intent);
                        }
                        break;
                    case 1://Add list

                        mDbHelper = new DatabaseHelper(v.getContext());
                        mDbQuery = new DatabaseQuery(mDbHelper);
                        mDatabase = mDbHelper.openDataBase();

                        final String[][] listsElements = mDbQuery.QueryGetElements("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, "type=?", new String[]{"8"}, null, null, null);
                        mDatabase.close();
                        final String[] lists = new String[listsElements.length];


                        for (int i = 0; i < listsElements.length; i++) {
                            lists[i] = listsElements[i][0];
                        }
                        LayoutInflater layoutInflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        dialogView = layoutInflater.inflate(R.layout.dialog_addproduct_list, null);
                        TextView addCustom = (TextView) dialogView.findViewById(R.id.addcustom_list);
                        addCustom.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                builderCustom = new AlertDialog.Builder(v.getContext());
                                LayoutInflater layoutInflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View view = layoutInflater.inflate(R.layout.dialog_addlist, null);
                                final EditText editList = (EditText) view.findViewById(R.id.namelist);
                                builderCustom.setCancelable(false).setTitle(R.string.add_list).setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (editList.getText().length() <= 0) {
                                            dialog.cancel();
                                        }
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builderCustom.setView(view).create().show();
                            }
                        });

                        builder.setTitle(v.getContext().getString(R.string.add_product_in_which_list))
                                .setMultiChoiceItems(lists, null, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        if (isChecked) {
                                            checkedList.add(which);
                                        } else if (checkedList.contains(which)) {
                                            checkedList.remove(Integer.valueOf(which));
                                        }
                                        mDbHelper = new DatabaseHelper(dialogView.getContext());
                                        mDbQuery = new DatabaseQuery(mDbHelper);
                                        mDatabase = mDbHelper.openDataBase();
                                    }
                                }).setPositiveButton(v.getContext().getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDbHelper = new DatabaseHelper(dialogView.getContext());
                                mDbQuery = new DatabaseQuery(mDbHelper);
                                mDatabase = mDbHelper.openDataBase();

                                String getProductElement[] = mDbQuery.QueryGetElement("product", new String[]{"name", "ean", "brand", "picture", "product_id"}, "product_id=?", new String[]{Integer.toString(mProduct.getId())}, null, null, null);
                                String getProduct[] = mDbQuery.QueryGetProduct("product", new String[]{"name", "ean", "brand", "picture", "product_id"}, "product_id=?", new String[]{Integer.toString(mProduct.getId())}, null, null, null);
                                if (getProductElement[0] != null) {
                                    Toast.makeText(dialogView.getContext(), "|Name=" + getProductElement[0] + "|Ean=" + getProductElement[1] + "|Brand=" + getProductElement[2] + "|Picture=" + getProductElement[3] + "|idProduct=" + getProductElement[4], Toast.LENGTH_SHORT).show();
                                    Toast.makeText(dialogView.getContext(), "|Name=" + getProduct[0] + "|Ean=" + getProduct[1] + "|Brand=" + getProduct[2] + "|Picture=" + getProduct[3] + "|idProduct=" + getProduct[4], Toast.LENGTH_SHORT).show();
                                } else if (getProductElement[0] == null) {
                                    if (mProduct.getPicture() == null)
                                        mProduct.setPicture("");
                                    mDbQuery.InsertData("product", new String[]{"name", "ean", "picture", "brand", "product_id"}, new String[]{mProduct.getName(), mProduct.getEan(), mProduct.getPicture(), mProduct.getBrand(), Integer.toString(mProduct.getId())});
                                }
                                for (int i = 0; i < checkedList.size(); i++) {
                                    String getListId[][] = mDbQuery.QueryGetElements("lists", new String[]{"id", "public", "nb_items", "id_user", "name"}, "name=? AND type=?", new String[]{lists[checkedList.get(i)], "8"}, null, null, null);

                                    String getITEMS[] = mDbQuery.QueryGetElement("items", new String[]{"id_list", "id_product", "id_user"}, "id_product=? AND id_list=?", new String[]{Integer.toString(mProduct.getId()), getListId[0][0]}, null, null, null);
                                     if (getITEMS[0] == null)
                                        mDbQuery.InsertData("items", new String[]{"id_list", "id_product", "id_user"}, new String[]{getListId[0][0], Integer.toString(mProduct.getId()), Integer.toString(mSessionManagerUser.getUserId())});
                                }
                                mDatabase.close();
                            }
                        }).setNegativeButton(v.getContext().getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }
                        ).setView(dialogView).create().show();
                        break;
                    case 2://Troc

                        break;
                    case 3://More details

                        break;
                    default:
                        break;
                }
            }
        });
    }

 

    @Override
    public int getItemCount () {
        return mOPTION.length;
    }

    @Override
    public void onClick (View v){
    }
}
