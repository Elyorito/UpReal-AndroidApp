package com.upreal.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.home.NavigationBar;
import com.upreal.utils.BlurImages;
import com.upreal.utils.CircleTransform;
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.History;
import com.upreal.utils.IPDefiner;
import com.upreal.utils.Refresh;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.SoapGlobalManager;
import com.upreal.utils.SoapUserUtilManager;
import com.upreal.utils.User;

/**
 * Created by Eric on 26/05/2015.
 */
public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private ConnectionDetector cd;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageBlurred;
    private ImageView imageUser;
    private TabLayout tabLayout;
    private Activity activity;
    private Context context;

    private ViewPager mViewPager;

    private UserViewPagerAdapter adapter;

    private User user;
    private SessionManagerUser sessionManagerUser;

    private FloatingActionButton menu;
    private AlertDialog dialog;

    private int status = 0;
    private int idType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        context = getApplicationContext();
        cd = new ConnectionDetector(context);
        tabLayout = (TabLayout) findViewById(R.id.tabsproduct);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageBlurred = (ImageView) findViewById(R.id.imageproductblurred);
        imageUser = (ImageView) findViewById(R.id.imageproduct);

        sessionManagerUser = new SessionManagerUser(context);
        final boolean toggleAccount = sessionManagerUser.isLogged();

        if (getIntent().getExtras() == null)
            user = sessionManagerUser.getUser();
        else
            user = getIntent().getExtras().getParcelable("user");
        new History.createHistory(context, 1, 1 , user.getId()).execute();

        new NavigationBar(this);
        new RetrieveRateStatus().execute();

        CharSequence tab[] = new CharSequence[]{"Info", "Produits", "Avis"};

        collapsingToolbarLayout.setTitle(user.getUsername());
        if (cd.isConnectedToInternet()) {
            Log.e("TEST", "Symfony/web/images/User/1_" + user.getId() + ".jpg");
            Picasso.with(getApplicationContext()).load(new IPDefiner().getIP() + "Symfony/web/images/User/1_" + user.getId() + ".jpg").transform(new BlurImages(getApplicationContext(), 25)).into(imageBlurred);
            Picasso.with(getApplicationContext()).load(new IPDefiner().getIP() + "Symfony/web/images/User/1_" + user.getId() + ".jpg").transform(new CircleTransform()).into(imageUser);
        }
        else
            Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.please_reload), Toast.LENGTH_SHORT).show();

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new UserViewPagerAdapter(getSupportFragmentManager(), tab, 3, user, getApplicationContext());
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);

        menu = (FloatingActionButton) findViewById(R.id.fab);
        menu.setOnClickListener(this);
        activity = this;
        String[] option = null;
        if (toggleAccount && sessionManagerUser.getUserId() == user.getId())
            option = new String[] { "J'aime", "Editer son profil", "Partager", "Rafraichir", "Suggestion" };
        else
            option = new String[] { "J'aime", "Suivre", "Partager", "Rafraichir", "Suggestion" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quel action voulez-vous faire ?");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        switch (which) {
                            case 0: // Like
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
                                break ;
                            case 1: // Edit - Follow
                                if (toggleAccount && sessionManagerUser.getUserId() == user.getId()) {
                                    intent = new Intent(context, UserUpdateActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    new FollowUser().execute();
                                }
                                break;
                            case 2: // Share
                                intent = new Intent(Intent.ACTION_SEND);

                                intent.putExtra(Intent.EXTRA_TEXT, "Venez voir l'utilisateur : " + user.getUsername() + " sur UpReal");
                                intent.setType("text/plain");
                                try {
                                    startActivity(Intent.createChooser(intent, "Partager ce produit avec ..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(context, context.getString(R.string.need_mail_app)
                                            , Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 3: // Refresh
                                if (cd.isConnectedToInternet())
                                    new Refresh(activity, 1, user.getId()).execute();
                                else
                                    Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.retry_retrieve_connection), Toast.LENGTH_SHORT).show();
                                break;
                            case 4: // Suggest
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

                                AlertDialog.Builder builderCustom = new AlertDialog.Builder(UserActivity.this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                dialog.show();
                break;
        }
    }

    private class RetrieveRateStatus extends AsyncTask<Void, Void, Integer> {

        int likeV = 0;

        @Override
        protected Integer doInBackground(Void... params) {

            SoapGlobalManager gm = new SoapGlobalManager();
            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            likeV = gm.countRate(user.getId(), 1, 2);
            if (userSession.isLogged()) {
                return gm.getRateStatus(user.getId(), 1, userSession.getUserId());
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer res) {
            super.onPostExecute(res);

            status = res;
            Log.e("TEST", "stat: " + res);
        }
    }

    private class SendRateStatus extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            Log.e("ProductActivity", "SendRateStatus called :" + params[0]);

            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            if (userSession.isLogged()) {
                SoapGlobalManager gm = new SoapGlobalManager();

                switch (params[0]) {
                    case 1:
                        gm.unLikeSomething(user.getId(), 1, userSession.getUserId());
                        break ;
                    case 2:
                        gm.likeSomething(user.getId(), 1, userSession.getUserId());
                        break ;
                    default:
                        break ;
                }

                SoapUserUtilManager uum = new SoapUserUtilManager();

                switch (params[0]) {
                    case 1:
                        uum.createHistory(userSession.getUserId(), 2, 1, user.getId());
                        break ;
                    case 2:
                        uum.createHistory(userSession.getUserId(), 4, 1, user.getId());
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

            if (userSession.isLogged())
                gm.createSuggestion(userSession.getUserId(), idType, 2, user.getId(), params[0]);

            return null;
        }
    }

    private class FollowUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SoapUserUtilManager uum = new SoapUserUtilManager();

            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            if (userSession.isLogged())
                uum.followUser(user.getId(), userSession.getUserId());

            return null;
        }
    }
}
