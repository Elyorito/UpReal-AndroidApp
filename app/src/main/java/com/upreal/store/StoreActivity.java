package com.upreal.store;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.home.NavigationBar;
import com.upreal.login.LoginActivity;
import com.upreal.utils.Address;
import com.upreal.utils.CircleTransform;
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.History;
import com.upreal.utils.IPDefiner;
import com.upreal.utils.Refresh;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.SoapGlobalManager;
import com.upreal.utils.SoapStoreManager;
import com.upreal.utils.SoapUserUtilManager;
import com.upreal.utils.Store;

/**
 * Created by Kyosukke on 01/11/2015.
 */
public class StoreActivity extends AppCompatActivity implements View.OnClickListener {
    private ConnectionDetector cd;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private SupportMapFragment map;
    private MapFragment map_store;
    private ImageView imageStore;
    private TabLayout tabLayout;
    private Activity activity;
    private Context context;

    private ViewPager mViewPager;

    private StoreViewPagerAdapter adapter;

    private Store store;

    private FloatingActionButton menu;
    private FloatingActionButton like;
    private AlertDialog dialog;
    private Address storeAddress;

    private int status = 0;
    private int idType = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        context = getApplicationContext();
        cd = new ConnectionDetector(context);
        tabLayout = (TabLayout) findViewById(R.id.tabsproduct);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageStore = (ImageView) findViewById(R.id.imageproduct);
        store = getIntent().getExtras().getParcelable("store");
        new History.createHistory(context, 1, 3 , store.getId()).execute();

        new RetrieveRateStatus().execute();
        new RetrieveAddressStore().execute();

        new NavigationBar(this);

        createMapView();

        collapsingToolbarLayout.setTitle(store.getName());
        if (cd.isConnectedToInternet()) {
            Picasso.with(getApplicationContext()).load(new IPDefiner().getIP() + "Symfony/web/images/Store/" + store.getPicture()).transform(new CircleTransform()).into(imageStore);
        } else
            Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.please_reload), Toast.LENGTH_SHORT).show();
        CharSequence tab[] = {"Info", "Produits", "Avis"};

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new StoreViewPagerAdapter(getSupportFragmentManager(), tab, 3, store);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);

        menu = (FloatingActionButton) findViewById(R.id.fab);
        menu.setOnClickListener(this);
        like = (FloatingActionButton) findViewById(R.id.like);
        like.setOnClickListener(this);
        activity = this;

        final String[] option = new String[] { "Partager", "Rafraichir", "Suggestion" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quel action voulez-vous faire ?");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Share
                                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Intent.ACTION_SEND);

                                i.putExtra(Intent.EXTRA_TEXT, "Venez voir le magasin : " + store.getName() + " sur UpReal");
                                i.setType("text/plain");
                                try {
                                    startActivity(Intent.createChooser(i, "Partager ce produit avec ..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(context, context.getString(R.string.need_mail_app)
                                            , Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1: // Refresh
                                if (cd.isConnectedToInternet()) {
                                    new Refresh(activity, 3, store.getId()).execute();
                                }
                                else
                                    Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.retry_retrieve_connection), Toast.LENGTH_SHORT).show();
                                break;
                            case 2: // Suggest
                                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View dialogView = layoutInflater.inflate(R.layout.dialog_suggestion, null);
                                Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);
                                final EditText text = (EditText) dialogView.findViewById(R.id.text);

                                String[] array = {"Suggérer", "Signaler"};

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                                        (activity, android.R.layout.simple_spinner_item, array);

                                dataAdapter.setDropDownViewResource
                                        (android.R.layout.simple_spinner_dropdown_item);

                                spinner.setAdapter(dataAdapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (parent.getItemAtPosition(position).equals("Suggérer"))
                                            idType = 1;
                                        else if (parent.getItemAtPosition(position).equals("Signaler"))
                                            idType = 2;
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        idType = 0;
                                    }
                                });

                                AlertDialog.Builder builderCustom = new AlertDialog.Builder(StoreActivity.this);
                                builderCustom.setView(dialogView)
                                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                new AddSuggestion().execute(text.getText().toString());
                                            }
                                        })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                builderCustom.create().show();
                                break;
                            default:
                                break;
                        }
                    }
                }
        );
        dialog = builder.create();
    }

    private void createMapView() {
        try {
            if (map_store == null) {
                map_store = (MapFragment) getFragmentManager()
                        .findFragmentById(R.id.map_store);
                map_store.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                    }
                });
                if (map == null) {
                    Toast.makeText(getApplicationContext(), "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException e) {
            Log.e("mapApp", e.toString());
        }
    }

    private void addMarker(String label, double latitude, double longitude) {
        if (map != null) {
            Log.e("StoreActivity", "Adding new marker " + label + ".");
            map.getMap().addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(label).draggable(false));
        } else {
            Log.e("StoreActivity", "Map is null.");
        }
    }

    @Override
    public void onClick(final View v) {
        SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

        switch (v.getId()) {
            case R.id.fab:
                if (!userSession.isLogged()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.error)
                            .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                }
                else
                    dialog.show();
                break ;
            case R.id.like:
                if (cd.isConnectedToInternet()) {
                    switch (status) {
                        case 1:
                            new SendRateStatus().execute(2);
                            break ;
                        case 2:
                            new SendRateStatus().execute(1);
                            break ;
                        default:
                            new SendRateStatus().execute(2);
                            break ;
                    }
                } else
                    Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.retry_retrieve_connection), Toast.LENGTH_SHORT).show();
                new RetrieveRateStatus().execute();
                break ;
            default:
                break ;
        }
    }

    private class RetrieveAddressStore extends AsyncTask<Void, Void, Address> {

        @Override
        protected Address doInBackground(Void... params) {
            SoapStoreManager sm = new SoapStoreManager();
            storeAddress = sm.getAddressByStore(store.getId());
            return storeAddress;
        }

        @Override
        protected void onPostExecute(Address address) {
            super.onPostExecute(address);
            addMarker(store.getName(), storeAddress.getLatitude(), storeAddress.getLongitude());
        }
    }

    private class RetrieveRateStatus extends AsyncTask<Void, Void, Integer> {

        int likeV = 0;

        @Override
        protected Integer doInBackground(Void... params) {

            SoapGlobalManager gm = new SoapGlobalManager();
            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            likeV = gm.countRate(store.getId(), 3, 2);
            if (userSession != null && userSession.isLogged()) {
                return gm.getRateStatus(store.getId(), 3, userSession.getUserId());
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer res) {
            super.onPostExecute(res);

            status = res;
            switch (status) {
                case 1:
                    like.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.result_minor_text)));
                    break;
                case 2:
                    like.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    break;
            }
        }
    }

    private class SendRateStatus extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            Log.e("ProductActivity", "SendRateStatus called :" + params[0]);

            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            if (userSession != null && userSession.isLogged()) {
                SoapGlobalManager gm = new SoapGlobalManager();

                switch (params[0]) {
                    case 1:
                        gm.unLikeSomething(store.getId(), 3, userSession.getUserId());
                        break ;
                    case 2:
                        gm.likeSomething(store.getId(), 3, userSession.getUserId());
                        break ;
                    default:
                        break ;
                }

                SoapUserUtilManager uum = new SoapUserUtilManager();

                switch (params[0]) {
                    case 1:
                        uum.createHistory(userSession.getUserId(), 2, 3, store.getId());
                        break ;
                    case 2:
                        uum.createHistory(userSession.getUserId(), 4, 3, store.getId());
                        break ;
                    default:
                        break ;
                }
            }

            return null;
        }
    }

    private class AddSuggestion extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            SoapGlobalManager gm = new SoapGlobalManager();

            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            if (userSession != null && userSession.isLogged())
                gm.createSuggestion(userSession.getUserId(), idType, 2, store.getId(), params[0]);

            return null;
        }
    }
}
