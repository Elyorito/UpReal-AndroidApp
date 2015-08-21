package com.upreal.upreal.user;

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
import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.SoapProductUtilManager;
import com.upreal.upreal.utils.SoapUserUtilManager;
import com.upreal.upreal.utils.User;
import com.upreal.upreal.utils.database.DatabaseHelper;
import com.upreal.upreal.utils.database.DatabaseQuery;

/**
 * Created by Elyo on 21/08/15.
 */
public class UserAdapterSocial extends RecyclerView.Adapter<UserAdapterSocial.ViewHolder> implements View.OnClickListener{

    private String mSOCIALOPT[];
    private User mUser;
    private SessionManagerUser sessionManagerUser;

    private AlertDialog.Builder builder;
    private String listLike;
    private Boolean isLiked = false;
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private DatabaseQuery mDbQuery;

    private Product mProduct;

    public UserAdapterSocial(String SOCIALOPT[], User user, SessionManagerUser sessionManagerUser) {
        this.mSOCIALOPT = SOCIALOPT;
        this.mUser = user;
        this.sessionManagerUser = sessionManagerUser;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_social;
        CardView mCardview;
        public ViewHolder(View itemView) {
            super(itemView);
        text_social = (TextView) itemView.findViewById(R.id.text_social);
        mCardview = (CardView) itemView.findViewById(R.id.cardview_social);
        }
    }

    @Override
    public UserAdapterSocial.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_social, parent, false);

        ViewHolder vhUsocial = new ViewHolder(v);
        return vhUsocial;
    }

    @Override
    public void onBindViewHolder(UserAdapterSocial.ViewHolder holder, final int position) {
        holder.text_social.setText(mSOCIALOPT[position]);
        holder.mCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater;
                final View layout;
                switch (position) {
                    case 0: // Suivre
                        Toast.makeText(v.getContext(), "Like", Toast.LENGTH_SHORT).show();
                        if (!sessionManagerUser.isLogged()) {
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
                    case 2: // Troc
                        Toast.makeText(v.getContext(), "Troc", Toast.LENGTH_SHORT).show();
                        break;
                    case 3: // Partager
                        Toast.makeText(v.getContext(), "Share", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Intent.ACTION_SEND);

                        i.putExtra(Intent.EXTRA_TEXT, "Vient chercher ton bonheur chez " + mUser.getUsername() + " sur UpReal");
                        i.setType("text/plain");
                        try {
                            v.getContext().startActivity(Intent.createChooser(i, "Partager cet utilisateur avec ..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(v.getContext(), v.getContext().getString(R.string.need_mail_app)
                                    , Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 4: // Send Message by mail
                        Toast.makeText(v.getContext(),"Message", Toast.LENGTH_SHORT).show();
                        builder.setTitle("Contacter " + mUser.getUsername() + " ?");
                        inflater = LayoutInflater.from(v.getContext());
                        layout = inflater.inflate(R.layout.dialog_mail, null);
                        builder.setView(layout);

                        final EditText subject = (EditText) layout.findViewById(R.id.subjectEmail);
                        final EditText messageEmail = (EditText) layout.findViewById(R.id.messageEmail);
                        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("message/rfc822");

                                i.putExtra(Intent.EXTRA_EMAIL, new String[]{mUser.getEmail()});
                                if (subject.getText().toString().equals(""))
                                    Toast.makeText(v.getContext(), "Le sujet ne peut etre vide", Toast.LENGTH_SHORT).show();
                                i.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
                                if (messageEmail.getText().toString().equals(""))
                                    Toast.makeText(v.getContext(), "Le message ne peut etre vide", Toast.LENGTH_SHORT).show();
                                i.putExtra(Intent.EXTRA_TEXT, messageEmail.getText().toString());
                                try {
                                    v.getContext().startActivity(Intent.createChooser(i, "Send mail..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.need_mail_app)
                                            , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                        break;
                    case 5: // Signal compteur, CA PART TROP LOIN, il y a plus important
                        Toast.makeText(v.getContext(), "Signal", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
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

    @Override
    public int getItemCount() {
        return mSOCIALOPT.length;
    }

    @Override
    public void onClick(View v) {

    }
}
