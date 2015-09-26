package com.upreal.store;

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
import com.upreal.login.LoginActivity;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.SoapGlobalManager;
import com.upreal.utils.Store;
import com.upreal.utils.database.DatabaseHelper;
import com.upreal.utils.database.DatabaseQuery;

/**
 * Created by Elyo on 21/08/15.
 */
public class StoreAdapterSocial extends RecyclerView.Adapter<StoreAdapterSocial.ViewHolder> implements View.OnClickListener {

    private String mSOCIALOPT[];
    private Store mStore;
    private SessionManagerUser sessionManagerUser;

    private AlertDialog.Builder builder;
    private String listLike;
    private Boolean isLiked = false;
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private DatabaseQuery mDbQuery;

    public StoreAdapterSocial(String SOCIALOPT[], Store store, SessionManagerUser sessionManagerUser) {
        this.mSOCIALOPT = SOCIALOPT;
        this.mStore = store;
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
    public StoreAdapterSocial.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_social, parent, false);

        ViewHolder vhStoreSocial = new ViewHolder(v);
        return vhStoreSocial;
    }

    @Override
    public void onBindViewHolder(StoreAdapterSocial.ViewHolder holder, final int position) {
        holder.text_social.setText(mSOCIALOPT[position]);
        holder.mCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Put logic
                builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater;
                final View layout;
                switch (position) {
                    case 0: // Folloz
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
                            break;
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
                            builder.setTitle(mStore.getName());
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
                                        new sendComment(comment.getText().toString(), v.getContext()).execute();
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
                    case 3: // Partager
                        Toast.makeText(v.getContext(), "Share", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Intent.ACTION_SEND);

                        i.putExtra(Intent.EXTRA_TEXT, "Vient chercher ton bonheur chez " + mStore.getName() + " sur UpReal");
                        i.setType("text/plain");
                        try {
                            v.getContext().startActivity(Intent.createChooser(i, "Partager cet utilisateur avec ..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(v.getContext(), v.getContext().getString(R.string.need_mail_app)
                                    , Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 5: // Signal compteur, CA PART TROP LOIN, il y a plus important
                        Toast.makeText(v.getContext(), "Signal", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private class sendComment extends AsyncTask<Void, Void, Boolean> {
        private String comment;
        private Context mContext;

        public sendComment(String comment, Context context) {
            this.comment = comment;
            this.mContext = context;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SoapGlobalManager pum = new SoapGlobalManager();
            pum.createComment(sessionManagerUser.getUserId(), mStore.getId(), pum.getTarget_store(), comment);
            return true;
        }

        protected void onPostExecute(Boolean success) {
            Toast.makeText(mContext, "Votre commentaire a bien été envoyé", Toast.LENGTH_SHORT).show();
            Log.v("comment", comment);
            mContext = null;
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
