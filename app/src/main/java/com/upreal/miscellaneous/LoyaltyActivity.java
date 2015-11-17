package com.upreal.miscellaneous;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.upreal.R;
import com.upreal.utils.Loyalty;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.SoapGlobalManager;
import com.upreal.utils.SoapStoreManager;
import com.upreal.utils.Store;

import java.util.List;

/**
 * Created by Eric on 15/11/2015.
 */
public class LoyaltyActivity extends Activity {
    private RecyclerView mRecyclerViewLoyalty;
    private AdapterLoyaltyCard mAdapterLoyalty;
    private RecyclerView.LayoutManager mLayoutManager;
    private SessionManagerUser sessionManagerUser;
    private AlertDialog.Builder builder;
    private FloatingActionButton addButton;
    private Store store;
    private Activity activity;

    private Spinner spinner;
    private EditText storeName;
    private EditText ean;
    private CheckBox checkBox;
    private View layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty);
        sessionManagerUser = new SessionManagerUser(getApplicationContext());
        mRecyclerViewLoyalty = (RecyclerView) findViewById(R.id.recyclerlist);
        mRecyclerViewLoyalty.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerViewLoyalty.setLayoutManager(mLayoutManager);
        builder = new AlertDialog.Builder(this);
        activity = this;
        addButton = (FloatingActionButton) findViewById(R.id.fabaddlist);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Ajout d'une carte de fidélité");
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                layout = inflater.inflate(R.layout.dialog_loyalty_card, null);
                builder.setView(layout);
                spinner = (Spinner) layout.findViewById(R.id.spinner);
                new RetrieveStore().execute();
                storeName = (EditText) layout.findViewById(R.id.shop_name);
                checkBox = (CheckBox) layout.findViewById(R.id.checkBox);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox)v).isChecked())
                            storeName.setVisibility(View.VISIBLE);
                        else if (!((CheckBox)v).isChecked())
                            storeName.setVisibility(View.GONE);
                    }
                });
                ean = (EditText) layout.findViewById(R.id.ean);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        store = (Store) parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                builder.setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ean.getText().toString().equals(""))
                            Toast.makeText(getApplicationContext(), "le code barre ne peut être vide", Toast.LENGTH_SHORT).show();
                        else if (store == null && !checkBox.isChecked())
                            Toast.makeText(getApplicationContext(), "Vous devez choisir un magasin", Toast.LENGTH_SHORT).show();
                        else if (checkBox.isChecked() && storeName.getText().toString().equals(""))
                            Toast.makeText(getApplicationContext(), "le nom du magasin ne peut être vide", Toast.LENGTH_SHORT).show();
                        else if (checkBox.isChecked() && !storeName.getText().toString().equals(""))
                            new CreateStore().execute();
                        else
                            new CreateLoyaltyCard(store.getId(), ean.getText().toString()).execute();
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
        });
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

    private class RetrieveStore extends AsyncTask<Void, Void, List<Store>> {

        @Override
        protected List<Store> doInBackground(Void... params) {
            SoapStoreManager ss = new SoapStoreManager();
            return ss.getListStore("");
        }

        protected void onPostExecute(List<Store> stores) {
            super.onPostExecute(stores);

            ArrayAdapter<Store> dataAdapter = new ArrayAdapter<>
                    (activity, android.R.layout.simple_spinner_item, stores);

            dataAdapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(dataAdapter);
        }
    }

    private class CreateStore extends AsyncTask<Void, Void, Integer> {
        private String name;

        CreateStore() {
            name = storeName.getText().toString();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            SoapStoreManager ss = new SoapStoreManager();
            return ss.registerStore(name);
        }

        protected void onPostExecute(Integer id_store) {
            new CreateLoyaltyCard(id_store, ean.getText().toString()).execute();
        }
    }

    private class CreateLoyaltyCard extends AsyncTask<Void, Void, Boolean> {
        private int id;
        private String eanCopy;

        CreateLoyaltyCard(int id_store, String ean) {
            id = id_store;
            eanCopy = ean;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SoapGlobalManager sg = new SoapGlobalManager();
            return sg.createPossess(sessionManagerUser.getUserId(), id, eanCopy);
        }

        protected void onPostExecute(Boolean b) {
            recreate();
        }
    }
}
