package home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.upreal.upreal.R;

import static android.widget.TextView.OnEditorActionListener;

/**
 * Created by Elyo on 15/02/2015.
 */
public class AdapterNavDrawerSearchHome extends RecyclerView.Adapter<AdapterNavDrawerSearchHome.ViewHolder> implements OnEditorActionListener{

    private static final int TYPE_HEADER = 0;

    private Context context;



    public class ViewHolder extends  RecyclerView.ViewHolder{
        int HolderId;

        EditText search;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            if (ViewType == TYPE_HEADER) {
                search = (EditText) itemView.findViewById(R.id.edittext_search);
                HolderId = 0;
            }
        }
    }

    AdapterNavDrawerSearchHome(Context mContext) {
        context = mContext;
    }

    @Override
    public AdapterNavDrawerSearchHome.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_search_home, parent, false);

            ViewHolder vhHeader = new ViewHolder(v, viewType);
            vhHeader.search.setOnEditorActionListener(AdapterNavDrawerSearchHome.this);

            vhHeader.search.setTag(vhHeader);
            return vhHeader;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final AdapterNavDrawerSearchHome.ViewHolder holder, int position) {
        //holder.search.setText("Search !");


    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if(actionId == EditorInfo.IME_ACTION_DONE) {
            ViewHolder holder = (ViewHolder) v.getTag();
            holder.search.setText("Searching!...");
            Intent intent = new Intent(v.getContext(), product.ProductSearchActivity.class);
            v.getContext().startActivity(intent);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    
    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return super.getItemViewType(position);
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
