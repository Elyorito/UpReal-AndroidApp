package com.upreal.upreal.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.upreal.upreal.utils.SoapUserUtilManager;
import com.upreal.upreal.utils.Store;
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
    private Store mStore;
    private SessionManagerUser sessionManagerUser;

    private String listLike;
    private Boolean isLiked = false;

    public User getmUser() {
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

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
        new isProductLiked().execute();
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
        if (i == 0) {
            if (isLiked == false)
                viewHolder.text_social.setTextColor(Color.RED);
            else
                viewHolder.text_social.setTextColor(Color.BLUE);
        }
        viewHolder.mCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater;
                final View layout;
                    switch (i) {
                        case 0: //Like
                            Toast.makeText(v.getContext(), "Like", Toast.LENGTH_SHORT).show();
                            if (!sessionManagerUser.isLogged()) {
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
                                new SendLike(0).execute();
                                /*if (isLiked == true)
                                    new SendLike(0).execute();
                                else
                                    new SendLike(1).execute();*/
                            }
                            break;
                        case 1: //Commenter
                            Toast.makeText(v.getContext(), "Comment", Toast.LENGTH_SHORT).show();
                            if (!sessionManagerUser.isLogged()) {
                                builder.setTitle("Vous voulez commenter cet utilisateur ?").setMessage("Connectez vous pour partager votre opinion")
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
                                builder.setTitle(mProduct.getName());
                                inflater = LayoutInflater.from(v.getContext());
                                layout = inflater.inflate(R.layout.dialog_comment, null);
                                builder.setView(layout);
                                final EditText comment = (EditText) layout.findViewById(R.id.comment);
                                final TextView limit = (TextView) layout.findViewById(R.id.limit);
                                comment.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }
                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }
                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        limit.setText(s.length() + " / " + String.valueOf(250));
                                        if (s.length() > 250)
                                            comment.setText(s.subSequence(0, 250));
                                    }
                                });
                                builder.setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (comment.getText().toString().equals(""))
                                            Toast.makeText(v.getContext(), "Le commentaire ne peut etre vide", Toast.LENGTH_SHORT).show();
                                        else
                                            new sendComment(0, comment.getText().toString(), v.getContext()).execute();
                                    }
                                });
                                builder.setNegativeButton(v.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.create().show();
                            }

                            break;
                        case 2: //Similar product
                            Toast.makeText(v.getContext(), "Similar Product", Toast.LENGTH_SHORT).show();
                            break;
                        case 3: //Share
                            Toast.makeText(v.getContext(), "Share", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Intent.ACTION_SEND);

                            i.putExtra(Intent.EXTRA_TEXT, "Venez voir le produit : " + mProduct.getName() + " sur UpReal");
                            i.setType("text/plain");
                            try {
                                v.getContext().startActivity(Intent.createChooser(i, "Partager ce produit avec ..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(v.getContext(), v.getContext().getString(R.string.need_mail_app)
                                        , Toast.LENGTH_SHORT).show();
                            }                            break;
                    }
            }

        });
    }

    @Override
    public int getItemCount() {
        return mSOCIALOPT.length;
    }

    @Override
    public void onClick(View v) {
    }

    private class sendComment extends AsyncTask<Void, Void, Boolean> {
        private int type;
        private String comment;
        private Context mContext;

        public sendComment(int type, String comment, Context context) {
            this.type = type;
            this.comment = comment;
            this.mContext = context;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (this.type == 1) {
                SoapUserUtilManager pum = new SoapUserUtilManager();
                pum.createUserComment(sessionManagerUser.getUserId(), mUser.getId(), comment);
            } else if (this.type == 0) {
                SoapProductUtilManager pum = new SoapProductUtilManager();
                pum.createProductComment(sessionManagerUser.getUserId(), mProduct.getId(), comment);
            }
            return true;
        }

        protected void onPostExecute(Boolean success) {
            Toast.makeText(mContext, "Votre commentaire a bien été envoyé", Toast.LENGTH_SHORT).show();
            Log.v("comment", comment);
            mContext = null;
        }
    }

    private class isProductLiked extends AsyncTask<Void, Void, Boolean> {
        Boolean isLike = false;

        @Override
        protected Boolean doInBackground(Void... params) {
            SoapUserUtilManager uum = new SoapUserUtilManager();
            isLike = uum.isProductLiked(Integer.toString(sessionManagerUser.getUserId()), Integer.toString(mProduct.getId()));
            return isLike;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isLiked = aBoolean;
        }
    }

        private class SendLike extends AsyncTask<Void, Void, Boolean> {
            Boolean isSuccess = false;
            Boolean isLike = false;
            private int type;

            public SendLike(int type) {
                this.type = type;
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                SoapUserUtilManager uum = new SoapUserUtilManager();
                SoapProductUtilManager pum = new SoapProductUtilManager();
                isLike = uum.isProductLiked(Integer.toString(sessionManagerUser.getUserId()), Integer.toString(mProduct.getId()));
                if (!isLike) {
                    isSuccess = pum.rateProduct(sessionManagerUser.getUserId(), mProduct.getId(), 1);
                    return isSuccess;
                } else if (isLike) {
                    isSuccess = pum.rateProduct(sessionManagerUser.getUserId(), mProduct.getId(), -1);
                    if (isSuccess == true)
                        return false;
                    return isSuccess;
                    /*
                    isSuccess = pum.rateUser(sessionManagerUser.getUserId(), )
*/
                }
                return isSuccess;
            }

            @Override
            protected void onPostExecute(Boolean b) {
                super.onPostExecute(b);
                if(b == true && sessionManagerUser.isLogged()){
                    isLiked = true;
                    mDatabase = mDbHelper.openDataBase();
                    String getListId[][] = mDbQuery.QueryGetElements("lists", new String[]{"id", "public", "nb_items", "id_user", "name"}, "name=? AND type=?", new String[]{listLike, "3"}, null, null, null);
                    String getProductElement[] = mDbQuery.QueryGetElement("product", new String[]{"name", "ean", "brand", "picture", "product_id"}, "product_id=?", new String[]{Integer.toString(mProduct.getId())}, null, null, null);
                    if (getProductElement[0] == null) {
                        if (mProduct.getPicture() == null)
                            mProduct.setPicture("");
                        mDbQuery.InsertData("product", new String[]{"name", "ean", "picture", "brand", "product_id"}, new String[]{mProduct.getName(), mProduct.getEan(), mProduct.getPicture(), mProduct.getBrand(), Integer.toString(mProduct.getId())});
                    }
                    String getITEMS[] = mDbQuery.QueryGetElement("items", new String[]{"id_list", "id_product", "id_user"}, "id_product=? AND id_list=?", new String[]{Integer.toString(mProduct.getId()), getListId[0][0]}, null, null, null);
                    if (getITEMS[0] == null)
                        mDbQuery.InsertData("items", new String[]{"id_list", "id_product", "id_user"}, new String[]{getListId[0][0], Integer.toString(mProduct.getId()), Integer.toString(sessionManagerUser.getUserId())});
                } else if (b == false) {
                    isLiked = false;
                    Log.v("HEOUAIS", "DISLIKE PRODUCT");
                    mDatabase = mDbHelper.openDataBase();
                    String getListId[][] = mDbQuery.QueryGetElements("lists", new String[]{"id", "public", "nb_items", "id_user", "name"}, "name=? AND type=?", new String[]{listLike, "3"}, null, null, null);
                    String getProductElement[] = mDbQuery.QueryGetElement("product", new String[]{"name", "ean", "brand", "picture", "product_id"}, "product_id=?", new String[]{Integer.toString(mProduct.getId())}, null, null, null);
                    mDbQuery.DeleteData("items", "id_list=? AND id_product=? AND id_user=?", new String[]{getListId[0][0], Integer.toString(mProduct.getId()), Integer.toString(sessionManagerUser.getUserId())});
                }
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
}