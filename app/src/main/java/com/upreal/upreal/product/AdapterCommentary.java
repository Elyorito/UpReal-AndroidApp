package com.upreal.upreal.product;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.upreal.upreal.R;
import com.upreal.upreal.utils.RateComment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 12/03/2015.
 */
public class AdapterCommentary extends RecyclerView.Adapter<AdapterCommentary.ViewHolder>{

    private String mUser[];
    private String mComment[];
    private String mRating[];
    private AlertDialog.Builder builder;
    private List<RateComment> listComment = new ArrayList<RateComment>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int holderId;

        TextView text_username;
        TextView text_comment;
        Button but_isuseful;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            text_username = (TextView) itemView.findViewById(R.id.text_username);
            text_comment = (TextView) itemView.findViewById(R.id.text_comment);
            but_isuseful = (Button) itemView.findViewById(R.id.but_opinion);
        }
    }

    public AdapterCommentary(List<RateComment> list) {
        this.listComment = list;
    }

    public AdapterCommentary(String User[], String Comment[], String Rating[]){
        this.mUser = User;
        this.mComment = Comment;
        this.mRating = Rating;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
/*
        holder.text_username.setText(this.mUser[position]);
        holder.text_comment.setText(this.mComment[position]);
*/

        holder.text_username.setText(this.listComment.get(position).getmNameUser());
        holder.text_comment.setText(this.listComment.get(position).getmTextComment());
        holder.but_isuseful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (v.getId()) {
                    case R.id.but_opinion:
                        builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Utile?").setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setSingleChoiceItems(R.array.UsefulArray, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Toast.makeText(v.getContext(), "YES USEFUUUUUL", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        Toast.makeText(v.getContext(), "-_-", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        Toast.makeText(v.getContext(), "REPORT SPPAAAAMM", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                        builder.create().show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listComment.size();
/*
        return mUser.length;
*/
    }

}
