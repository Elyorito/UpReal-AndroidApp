package com.upreal.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.upreal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 12/03/2015.
 */
public class AdapterCommentary extends RecyclerView.Adapter<AdapterCommentary.ViewHolder>{
    private List<RateComment> listComment = new ArrayList<RateComment>();
    private Context context;

    private ViewHolder[] holder;

    private int status = 1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int holderId;

        TextView text_username;
        TextView text_comment;
        ImageView like;
        ImageView dislike;
        TextView likeV;
        TextView dislikeV;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            text_username = (TextView) itemView.findViewById(R.id.text_username);
            text_comment = (TextView) itemView.findViewById(R.id.text_comment);
            like = (ImageView) itemView.findViewById(R.id.logo_like);
            dislike = (ImageView) itemView.findViewById(R.id.logo_dislike);
            likeV = (TextView) itemView.findViewById(R.id.like_value);
            dislikeV = (TextView) itemView.findViewById(R.id.dislike_value);
        }
    }

    public AdapterCommentary(Context context, List<RateComment> list) {
        this.context = context;
        this.listComment = list;
        this.holder = new ViewHolder[list.size()];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (getItemCount() == 0) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            ViewHolder vhCom = new ViewHolder(v, viewType);
            return vhCom;
        }
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_commentary, viewGroup, false);

        ViewHolder vhCom = new ViewHolder(v, viewType);

        return vhCom;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        this.holder[position] = holder;

        holder.text_username.setText(this.listComment.get(position).getmNameUser());
        holder.text_comment.setText(this.listComment.get(position).getmTextComment());
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = (holder.like.getAlpha() >= 1f) ? (1) : (2);
                new SendRateStatus().execute(position);
                new RetrieveRateStatus().execute(position);
            }
        });
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = (holder.dislike.getAlpha() >= 1f) ? (1) : (3);
                new SendRateStatus().execute(position);
                new RetrieveRateStatus().execute(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listComment.size();
    }

    private class RetrieveRateStatus extends AsyncTask<Integer, Void, Integer> {

        int likeV = 0;
        int dislikeV = 0;
        int position = 0;

        @Override
        protected Integer doInBackground(Integer... params) {

            SoapGlobalManager gm = new SoapGlobalManager();
            SessionManagerUser userSession = new SessionManagerUser(context);

            likeV = gm.countRate(listComment.get(params[0]).getId(), 5, 2);
            dislikeV = gm.countRate(listComment.get(params[0]).getId(), 5, 3);
            position = params[0];

            if (userSession != null && userSession.isLogged()) {
                return gm.getRateStatus(listComment.get(params[0]).getId(), 5, userSession.getUserId());
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer res) {
            super.onPostExecute(res);

            holder[position].likeV.setText(likeV + "");
            holder[position].dislikeV.setText(dislikeV + "");

            switch (res) {
                case 1:
                    holder[position].like.setAlpha(.5f);
                    holder[position].dislike.setAlpha(.5f);
                    break ;
                case 2:
                    holder[position].like.setAlpha(1f);
                    holder[position].dislike.setAlpha(.5f);
                    break ;
                case 3:
                    holder[position].like.setAlpha(.5f);
                    holder[position].dislike.setAlpha(1f);
                    break ;
                default:
                    holder[position].like.setAlpha(.5f);
                    holder[position].dislike.setAlpha(.5f);
                    break ;
            }
        }
    }

    private class SendRateStatus extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            SessionManagerUser userSession = new SessionManagerUser(context);

            if (userSession != null && userSession.isLogged()) {
                SoapGlobalManager gm = new SoapGlobalManager();

                switch (status) {
                    case 1:
                        gm.unLikeSomething(listComment.get(params[0]).getId(), 5, userSession.getUserId());
                        break ;
                    case 2:
                        gm.likeSomething(listComment.get(params[0]).getId(), 5, userSession.getUserId());
                        break ;
                    case 3:
                        gm.dislikeSomething(listComment.get(params[0]).getId(), 5, userSession.getUserId());
                        break ;
                    default:
                        break ;
                }

                SoapUserUtilManager uum = new SoapUserUtilManager();

                switch (status) {
                    case 1:
                        uum.createHistory(userSession.getUserId(), 2, 5, listComment.get(params[0]).getId());
                        break ;
                    case 2:
                        uum.createHistory(userSession.getUserId(), 4, 5, listComment.get(params[0]).getId());
                        break ;
                    case 3:
                        uum.createHistory(userSession.getUserId(), 3, 5, listComment.get(params[0]).getId());
                        break ;
                    default:
                        break ;
                }
            }

            return null;
        }
    }

}
