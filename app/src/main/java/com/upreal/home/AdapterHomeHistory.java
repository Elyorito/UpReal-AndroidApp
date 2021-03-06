package com.upreal.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.History;
import com.upreal.utils.IPDefiner;
import com.upreal.utils.SessionManagerUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kyosukke on 27/10/2015.
 */
public class AdapterHomeHistory extends RecyclerView.Adapter<AdapterHomeHistory.ViewHolder> {

    private List<History> hList;
    private Context context;
    private ConnectionDetector cd;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        Button type;
        TextView title;
        ImageView image;
        LinearLayout news;
        public ViewHolder(View itemView) {
            super(itemView);
            type = (Button) itemView.findViewById(R.id.fab);
            title = (TextView) itemView.findViewById(R.id.titlenews);
            image = (ImageView) itemView.findViewById(R.id.imagenews);
            news = (LinearLayout) itemView.findViewById(R.id.newsarticle);
        }
    }

    public AdapterHomeHistory(List<History> hList, Context context) {
/*
        ArrayList<History> tmp = new ArrayList<>(hList);
        Collections.reverse(tmp);
*/
        this.hList = hList;
        this.context = context;
        cd = new ConnectionDetector(context);
    }

    @Override
    public AdapterHomeHistory.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_news, viewGroup, false);
        ViewHolder vhnews = new ViewHolder(v);
        return vhnews;
    }

    @Override
    public void onBindViewHolder(AdapterHomeHistory.ViewHolder viewHolder, final int i) {
        if (cd.isConnectedToInternet()) {
            Picasso.with(context).load(new IPDefiner().getIP() + "Symfony/web/images/User/1_" + this.hList.get(i).getIdUser() + ".jpg").placeholder(R.drawable.connection_img).into(viewHolder.image);
        } else
            Toast.makeText(context, R.string.no_internet_connection + R.string.please_reload, Toast.LENGTH_SHORT).show();
        if (this.hList.get(i).getDate() != null)
            viewHolder.type.setText(this.hList.get(i).getDate().toString());

        SessionManagerUser userSession = new SessionManagerUser(context);

        String state = userSession.getUser().getUsername() + " ";

        state += getActionType().get(this.hList.get(i).getActionType());

        switch (this.hList.get(i).getIdType()) {
            case 1:
                if (this.hList.get(i).getActionType() != 7) {
                    state += " " + context.getString(R.string.user) + " " + this.hList.get(i).getNameTarget();
                }
                break ;
            case 2:
                state += " " + context.getString(R.string.product) + " " + this.hList.get(i).getNameTarget();
                break ;
            case 3:
                state += " " + context.getString(R.string.store) + " " + this.hList.get(i).getNameTarget();
                break ;
            case 4:
                state += " " + context.getString(R.string.article) + " " + this.hList.get(i).getNameTarget();
                break ;
            default:
                break ;
        }
        viewHolder.title.setText(state);
    }

    @Override
    public int getItemCount() {
        return hList.size();
    }

    protected Map<Integer, String> getActionType() {
        Map<Integer, String> map = new HashMap<>();

        map.put(1, context.getString(R.string.has_consulted));
        map.put(2, context.getString(R.string.liked));
        map.put(3, context.getString(R.string.disliked));
        map.put(4, context.getString(R.string.unliked));
        map.put(5, context.getString(R.string.shared));
        map.put(6, context.getString(R.string.commented));
        map.put(7, context.getString(R.string.modified_profile));
        map.put(8, context.getString(R.string.added_product));

        return map;
    }
}