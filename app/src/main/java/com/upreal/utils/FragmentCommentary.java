package com.upreal.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

import com.upreal.R;
import com.upreal.login.LoginActivity;
import com.upreal.product.AdapterCommentary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 01/11/2015.
 */
public class FragmentCommentary extends Fragment {

    private AlertDialog.Builder builder;
    private View layout;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Rate> listRate = new ArrayList<Rate>();
    private List<RateComment> listComment = new ArrayList<>();
    private FloatingActionButton addButton;
    private SessionManagerUser sessionManagerUser;
    private Context context;

    /*
        private int idProduct = 0;
        private int idUser = 0;
        private int idStore = 0;
    */
    private int id = 0;
    private int type = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_product_commentary, container, false);
        Bundle b = getArguments();

        id = b.getInt("id");
        type = b.getInt("type");
/*
        idProduct = b.getInt("idProduct");
        idUser = b.getInt("idUser");
        idStore = b.getInt("idStore");
*/
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_Product_commentary);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        addButton = (FloatingActionButton) v.findViewById(R.id.fab);

        sessionManagerUser = new SessionManagerUser(getContext());
        builder = new AlertDialog.Builder(v.getContext());
        context = getContext();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Comment", Toast.LENGTH_SHORT).show();
                if (!sessionManagerUser.isLogged()) {
                    builder.setTitle("Vous voulez commenter cet utilisateur ?").setMessage("Connectez vous pour partager votre opinion")
                            .setPositiveButton(v.getContext().getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton(v.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create().show();
                } else {
                    builder.setTitle("Votre commentaire");
                    LayoutInflater inflater = LayoutInflater.from(context);
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
                                Toast.makeText(context, "Le commentaire ne peut etre vide", Toast.LENGTH_SHORT).show();
                            else
                                new SendComment(comment.getText().toString(), context).execute();
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
            }
        });
        new RetrieveComment().execute();

        return v;
    }

    private class RetrieveComment extends AsyncTask<Void, Void, List<RateComment>> {

        User user = new User();
        RateComment rateComment;

        @Override
        protected List<RateComment> doInBackground(Void... params) {
            SoapGlobalManager gm = new SoapGlobalManager();

            //        Log.e("FC", idUser + " " + idProduct + " " + idStore + " ");
/*
            if (idUser != 0)
                listRate = gm.getRate(idUser, 1);
            else if (idProduct != 0)
                listRate = gm.getRate(idProduct, 2);
            else if (idStore != 0)
                listRate = gm.getRate(idStore, 3);
            else
                return null;
*/
            listRate = gm.getRate(id, type);

            SoapUserManager um = new SoapUserManager();
            for (int i = 0; i < listRate.size(); i++) {
                Log.e("FC", "test:" + listRate.get(i).getmCommentary());
                user = um.getAccountInfoUsername(listRate.get(i).getmId_user());
                rateComment = new RateComment();
                rateComment.setmNameUser(user.getUsername());
                rateComment.setmTextComment(listRate.get(i).getmCommentary());
                listComment.add(rateComment);
            }
            return listComment;
        }

        @Override
        protected void onPostExecute(List<RateComment> rateComments) {
            super.onPostExecute(rateComments);
            if (rateComments != null) {
                mAdapter = new AdapterCommentary(rateComments);
                recyclerView.setAdapter(mAdapter);
            }
        }
    }

    private class SendComment extends AsyncTask<Void, Void, Boolean> {
        private String comment;
        private Context mContext;

        public SendComment(String comment, Context context) {
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
            SoapGlobalManager sm = new SoapGlobalManager();
            sm.createComment(sessionManagerUser.getUserId(), id, type, comment);
            return true;
        }

        protected void onPostExecute(Boolean success) {
            Toast.makeText(mContext, "Votre commentaire a bien été envoyé", Toast.LENGTH_SHORT).show();
            Log.v("comment", comment);
            mContext = null;
        }
    }
}
