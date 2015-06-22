package com.upreal.upreal.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
                builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater;
                final View layout;
                if (getItemCount() == 4) {
                    switch (i) {
                        case 0: //Like
                            Toast.makeText(v.getContext(), "Like", Toast.LENGTH_SHORT).show();
                            if (sessionManagerUser.getUserId() <= 0) {
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
                            if (sessionManagerUser.getUserId() <= 0) {
                                builder.setTitle("Suivre cet utilisateur ?").setMessage("Connectez vous pour pouvoir le suivre.")
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
                                new SendLike(1).execute();
                            }
                            break;
                        case 1: //Commenter
                            Toast.makeText(v.getContext(), "Comment", Toast.LENGTH_SHORT).show();
                            if (sessionManagerUser.getUserId() <= 0) {
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
                                builder.setTitle(mUser.getUsername());
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
                                            new sendComment(1, comment.getText().toString(), v.getContext()).execute();
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
            }
            return true;
        }

        protected void onPostExecute(Boolean success) {
            Toast.makeText(mContext, "Votre commentaire a bien été envoyé", Toast.LENGTH_SHORT).show();
            Log.v("comment", comment);
            mContext = null;
        }
    }

        private class SendLike extends AsyncTask<Void, Void, Boolean> {
            Boolean isSuccess = false;
            private int type;

            public SendLike(int type) {
                this.type = type;
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                if (this.type == 0) {
                    SoapProductUtilManager pum = new SoapProductUtilManager();
                    isSuccess = pum.rateProduct(sessionManagerUser.getUserId(), mProduct.getId(), 2);
                } else if (this.type == 1) {
                    SoapUserUtilManager pum = new SoapUserUtilManager();
/*
                    isSuccess = pum.rateUser(sessionManagerUser.getUserId(), )
*/
                }
                return isSuccess;
            }

            @Override
            protected void onPostExecute(Boolean b) {
                super.onPostExecute(b);
                if(sessionManagerUser.getUserId() > 0){
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
                } else {

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