package com.upreal.user;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.utils.BlurImages;
import com.upreal.utils.CircleTransform;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.User;

/**
 * Created by Eric on 26/05/2015.
 */
public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageBlurred;
    private ImageView imageUser;
    private TabLayout tabLayout;

    private ViewPager mViewPager;

    private UserViewPagerAdapter adapter;

    private User user;

    private SessionManagerUser sessionManagerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        tabLayout = (TabLayout) findViewById(R.id.tabsproduct);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageBlurred = (ImageView) findViewById(R.id.imageproductblurred);
        imageUser = (ImageView) findViewById(R.id.imageproduct);

        sessionManagerUser = new SessionManagerUser(getApplicationContext());
        user = getIntent().getExtras().getParcelable("user");

        boolean toggleAccount = sessionManagerUser.isLogged();

        CharSequence tab[] = null;

        if (toggleAccount && sessionManagerUser.getUserId() == user.getId())
            tab = new CharSequence[]{"Info", "Produits", "Avis"};
        else
            tab = new CharSequence[]{"Info", "Produits", "Avis"};

        collapsingToolbarLayout.setTitle(user.getUsername());
        Log.e("TEST", "\"http://163.5.84.202/Symfony/web/images/User/1_" + user.getId() + ".jpg");
        Picasso.with(getApplicationContext()).load("http://163.5.84.202/Symfony/web/images/User/1_" + user.getId() + ".jpg").transform(new BlurImages(getApplicationContext(), 25)).into(imageBlurred);
        Picasso.with(getApplicationContext()).load("http://163.5.84.202/Symfony/web/images/User/1_" + user.getId() + ".jpg").transform(new CircleTransform()).into(imageUser);


        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new UserViewPagerAdapter(getSupportFragmentManager(), tab, 3, user, getApplicationContext());
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
    }
}
