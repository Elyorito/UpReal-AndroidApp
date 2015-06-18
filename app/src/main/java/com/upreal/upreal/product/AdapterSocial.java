package com.upreal.upreal.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.upreal.upreal.R;
import com.upreal.upreal.login.LoginActivity;
import com.upreal.upreal.utils.Product;
import com.upreal.upreal.utils.RateComment;
import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.SoapGlobalManager;
import com.upreal.upreal.utils.SoapProductUtilManager;
import com.upreal.upreal.utils.SoapUserManager;
import com.upreal.upreal.utils.User;
import com.upreal.upreal.utils.database.DatabaseHelper;
import com.upreal.upreal.utils.database.DatabaseQuery;

import java.util.List;

/**
 * Created by Elyo on 16/03/2015.
 */
public class AdapterSocial extends RecyclerView.Adapter<AdapterSocial.ViewHolder> implements View.OnClickListener {

    private AlertDialog.Builder builder;

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private DatabaseQuery mDbQuery;

    private String mSOCIALOPT[];
    private Product mProduct;
    private User mUser;
    private SessionManagerUser sessionManagerUser;

    private String listLike;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int holderId;

        TextView text_social;
        CardView mCardview;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            text_social = (TextView) itemView.findViewById(R.id.text_social);
            mCardview = (CardView) itemView.findViewById(R.id.cardview_social);
        }
    }

    AdapterSocial(String SOCIALOPT[], Product product, SessionManagerUser sessionManagerUser) {
        this.mSOCIALOPT = SOCIALOPT;
        this.mProduct = product;
        this.sessionManagerUser = sessionManagerUser;
    }

    public AdapterSocial(String SOCIALOPT[], User user, SessionManagerUser sessionManagerUser) {
        this.mSOCIALOPT = SOCIALOPT;
        this.mUser = user;
        this.sessionManagerUser = sessionManagerUser;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product_social, viewGroup, false);

        ViewHolder vhSocial = new ViewHolder(v, i);
        return vhSocial;
    }

    @Override
    public void onBindViewHolder(AdapterSocial.ViewHolder viewHolder, final int i) {
        viewHolder.text_social.setText(mSOCIALOPT[i]);
        viewHolder.mCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (getItemCount() != 3) {
                    switch (i) {
                        case 0: //Like
                            Toast.makeText(v.getContext(), "Like", Toast.LENGTH_SHORT).show();
                            if (sessionManagerUser.getUserId() <= 0) {
                                builder = new AlertDialog.Builder(v.getContext());
                                builder.setTitle("Like this product?").setMessage("Sign in to make your opinion count")
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
                                listLike = v.getContext().getString(R.string.liked_product);
                                mDbHelper = new DatabaseHelper(v.getContext());
                                mDbQuery = new DatabaseQuery(mDbHelper);
                                new SendLike().execute();
                            }
                            break;
                        case 1: //Commenter
                            Toast.makeText(v.getContext(), "Comment", Toast.LENGTH_SHORT).show();
                            break;
                        case 2: //Similar product
                            Toast.makeText(v.getContext(), "Similar Product", Toast.LENGTH_SHORT).show();
                            break;
                        case 3: //Share
                            Toast.makeText(v.getContext(), "Share", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    // User
                    switch (i) {
                        case 0: // Suivre
                            Toast.makeText(v.getContext(), "Like", Toast.LENGTH_SHORT).show();
                            new SendLike().execute();
                            break;
                        case 1: //Commenter
                            Toast.makeText(v.getContext(), "Comment", Toast.LENGTH_SHORT).show();
                            break;
                        case 3: // Troc
                            Toast.makeText(v.getContext(), "Troc", Toast.LENGTH_SHORT).show();
                            break;
                        case 4: // Send Message
                            Toast.makeText(v.getContext(),"Message", Toast.LENGTH_SHORT).show();
                            break;
                        case 5: //Share
                            Toast.makeText(v.getContext(), "Share", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            ;
        });
    }

    @Override
    public int getItemCount() {
        return mSOCIALOPT.length;
    }

    @Override
    public void onClick(View v) {
    }

        private class SendLike extends AsyncTask<Void, Void, Boolean> {
            Boolean isSuccess = false;

            @Override
            protected Boolean doInBackground(Void... params) {
                SoapProductUtilManager pum = new SoapProductUtilManager();
                isSuccess = pum.rateProduct(sessionManagerUser.getUserId(), mProduct.getId(), 1);
                return isSuccess;
            }

            @Override
            protected void onPostExecute(Boolean b) {
                super.onPostExecute(b);
                if(sessionManagerUser.getUserId() > 0){
       /*             mDatabase = mDbHelper.openDataBase();
                    String getListId[][] = mDbQuery.QueryGetElements("lists", new String[]{"id", "public", "nb_items", "id_user", "name"}, "name=?", new String[]{listLike}, null, null, null);

                    String getITEMS[] = mDbQuery.QueryGetElement("items", new String[]{"id_list", "id_product", "id_user"}, "id_product=? AND id_list=?", new String[]{Integer.toString(mProduct.getId()), getListId[0][0]}, null, null, null);
                    if (getITEMS[0] != null)
                        //Toast.makeText(dialogView.getContext(), "id_list=" + getITEMS[0] + "|id_product=" + getITEMS[1] + "|id_user=" + getITEMS[2], Toast.LENGTH_SHORT).show();
                    else if (getITEMS[0] == null)
                        mDbQuery.InsertData("items", new String[]{"id_list", "id_product", "id_user"}, new String[]{getListId[0][0], Integer.toString(mProduct.getId()), Integer.toString(mSessionManagerUser.getUserId())});
*/
                } else {

                }
            }
        }
}