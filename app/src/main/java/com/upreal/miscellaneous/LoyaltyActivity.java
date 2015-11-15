package com.upreal.miscellaneous;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.upreal.R;
import com.upreal.utils.Loyalty;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.SoapGlobalManager;

import java.util.List;

/**
 * Created by Eric on 15/11/2015.
 */
public class LoyaltyActivity extends Activity {
    private RecyclerView mRecyclerViewLoyalty;
    private AdapterLoyaltyCard mAdapterLoyalty;
    private RecyclerView.LayoutManager mLayoutManager;
    private SessionManagerUser sessionManagerUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty);
        sessionManagerUser = new SessionManagerUser(getApplicationContext());
        mRecyclerViewLoyalty = (RecyclerView) findViewById(R.id.recyclerlist);
//        mRecyclerViewLoyalty.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewLoyalty.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        new RetrieveLoyalty().execute();
    }

    private class RetrieveLoyalty extends AsyncTask<Void, Void, List<Loyalty>> {

        @Override
        protected List<Loyalty> doInBackground(Void... params) {
            SoapGlobalManager sg = new SoapGlobalManager();
            return sg.getUserPossess(sessionManagerUser.getUserId());
        }

        protected void onPostExecute(List<Loyalty> loyalties) {
            mAdapterLoyalty = new AdapterLoyaltyCard(loyalties, getApplicationContext());
            mRecyclerViewLoyalty.setAdapter(mAdapterLoyalty);
        }
    }
}
