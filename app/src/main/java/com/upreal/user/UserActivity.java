package com.upreal.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
            option = new String[] { "Editer son profil", "Partager", "Rafraichir" };
        else
            option = new String[] { "Suivre", "Partager", "Rafraichir" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quel action voulez-vous faire ?");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        switch (which) {
                            case 0: // Edit - Follow
                                if (toggleAccount && sessionManagerUser.getUserId() == user.getId()) {
                                    intent = new Intent(context, UserUpdateActivity.class);
                                    startActivity(intent);
                                }
                                break;
                            case 1: // Share
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
                            case 2: // Refresh
                                if (cd.isConnectedToInternet())
                                    new Refresh(activity, 1, user.getId()).execute();
                                else
                                    Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.retry_retrieve_connection), Toast.LENGTH_SHORT).show();
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
}
