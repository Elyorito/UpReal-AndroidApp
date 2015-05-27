package com.upreal.upreal.product;

import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.upreal.upreal.R;
import com.upreal.upreal.utils.Product;
import com.upreal.upreal.utils.RateComment;
import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.SoapGlobalManager;
import com.upreal.upreal.utils.SoapProductUtilManager;
import com.upreal.upreal.utils.SoapUserManager;
import com.upreal.upreal.utils.User;

import java.util.List;

/**
 * Created by Elyo on 16/03/2015.
 */
public class AdapterSocial extends RecyclerView.Adapter<AdapterSocial.ViewHolder> implements View.OnClickListener {

    private String mSOCIALOPT[];
    private Product mProduct;
    private User mUser;
    private SessionManagerUser sessionManagerUser;

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
            public void onClick(View v) {
                if (getItemCount() != 3) {
                    switch (i) {
                        case 0: //Like
                            Toast.makeText(v.getContext(), "Like", Toast.LENGTH_SHORT).show();
                            new SendLike().execute();
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
                    switch (i) {
                        case 0: //Like
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

            }
        }
}