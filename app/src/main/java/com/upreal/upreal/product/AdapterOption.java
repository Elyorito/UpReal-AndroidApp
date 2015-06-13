package com.upreal.upreal.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.upreal.upreal.R;
import com.upreal.upreal.list.AdapterListHomeCustom;
import com.upreal.upreal.user.UserUpdateActivity;
import com.upreal.upreal.utils.Product;
import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.database.DatabaseHelper;
import com.upreal.upreal.utils.database.DatabaseQuery;

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
            public void onClick(View v) {
                if (getItemCount() != 1) {
                    switch (i) {
                        case 0://Edit

                            break;
                        case 1://Add list
/*
                        mDbHelper = new DatabaseHelper(v.getContext());
                        mDbQuery = new DatabaseQuery(mDbHelper);
                        mDatabase = mDbHelper.openDataBase();

                        final String[] lists = mDbQuery.QueryGetElement("lists", new String[]{"name", "public", "nb_items", "id_user"}, null, null, null, null, null);
                        mDatabase.close();
                        */
/*Builder MultiChoice List*//*

                        builder = new AlertDialog.Builder(v.getContext());
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
                                builderCustom.setCancelable(false).setTitle(R.string.add_list).setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (editList.getText().length() <= 0) {
                                            dialog.cancel();
                                        }
*/
/*
                                        mDbQuery.InsertData(new String("lists"), new String[]{"name", "public", "nb_items", "id_user"}, new String[]{editList.getText().toString(), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId())});
                                        lists = mDbQuery.QueryGetElements("lists", new String[]{"name", "public", "nb_items", "id_user"}, null, null, null, null, null);
                                        // TODO Auto-generated method stub
                         *//*

                            */
/*Refresh list item [BUG]*//*
*/
/*

                                        mAdapterListCust = new AdapterListHomeCustom(lists, delimiter);
                                        mAdapterListCust.notifyDataSetChanged();
                                        mRecyclerViewListCust.getAdapter().notifyDataSetChanged();
*//*

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
                        builder.setView(dialogView).setTitle(v.getContext().getString(R.string.add_product_in_which_list))
                                .setMultiChoiceItems(lists, null, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        if (isChecked) {
                                            checkedList.add(which);
                                        } else if (checkedList.contains(which)) {
                                            checkedList.remove(Integer.valueOf(which));
                                        }
                                    }
                                }).setPositiveButton(v.getContext().getString(R.string.ok_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < checkedList.size(); i++) {
*/
/*
                                    String getid[] = mDbQuery.MyRawQuery("SELECT id FROM LISTS WHERE NAME=" + "'" + lists[checkedList.get(i)] + "'") ;
*//*


*/
/*
                                    Toast.makeText(dialogView.getContext(), "LIST CHECKED= " + lists[checkedList.get(i)]+ "USERID=" + Integer.toString(mSessionManagerUser.getUserId())+ "ListID=" + getid[0], Toast.LENGTH_SHORT).show();*//*

                                }
                                Snackbar.make(dialogView, "Produit a bien ete ajouter", Snackbar.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton(v.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();
                        Toast.makeText(v.getContext(), "Add List", Toast.LENGTH_SHORT).show();
                        break;
                    case 2://Troc

*/
                            break;
                        case 3://More details

                            break;
                        default:
                            break;
                    }
                } else {
                    Log.v("User Option", "On est bien arriver");
                    if (i == 0) {
                        Log.v("Editer mon compte", "tralala");
                        Intent intent = new Intent(v.getContext(), UserUpdateActivity.class);
                        v.getContext().startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOPTION.length;
    }

    @Override
    public void onClick(View v) {
    }
}
