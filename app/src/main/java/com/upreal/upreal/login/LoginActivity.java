package com.upreal.upreal.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.upreal.upreal.R;
import com.upreal.upreal.utils.GoogleConnection;
import com.upreal.upreal.utils.State;
import com.upreal.upreal.view.SlidingTabLayout;

import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Elyo on 01/03/2015.
 */
public class LoginActivity extends ActionBarActivity implements View.OnClickListener, ResultCallback<People.LoadPeopleResult>, Observer {

    private static final String TAG = "LoginActivity";

    private CharSequence Tab[];

    private Toolbar toolbar;

    private ViewPager mViewPager;
    private LoginViewPagerAdapter mAdapter;
    private SlidingTabLayout mSlidingTabLayout;

    private GoogleConnection googleConnection;
    private SignInButton gConnect;
    private Button tConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.connexion);
        setSupportActionBar(toolbar);
        Tab = new CharSequence[]{getString(R.string.select_connexion), getString(R.string.select_register)};

        mViewPager = (ViewPager) findViewById(R.id.login_viewpager);
        mAdapter = new LoginViewPagerAdapter(getSupportFragmentManager(), Tab, 2);
        mViewPager.setAdapter(mAdapter);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.login_sliding_tab);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.ColorTabs);
            }
        });
        mSlidingTabLayout.setViewPager(mViewPager);

        gConnect = (SignInButton) findViewById(R.id.button_google);
        gConnect.setOnClickListener(this);

        googleConnection = GoogleConnection.getInstance(this);
        googleConnection.addObserver(this);

        tConnect = (Button) findViewById(R.id.button_twitter);
        tConnect.setOnClickListener(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (GoogleConnection.REQUEST_CODE == requestCode) {
            googleConnection.onActivityResult(resultCode);
        }
    }

    private void getProfileInformation() {
        GoogleApiClient mGoogleApiClient = googleConnection.getGoogleApiClient();
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) == null)
                Log.e(TAG, "dead");
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String username = null;
                if (currentPerson.hasNickname())
                    username = currentPerson.getNickname();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);



                // Check email available

                // Check Username available
                //new RetrieveIsUsernameTaken().execute(v.getText().toString());

/*
                new RetrieveRegisterAccount(username != null ? username : personName, currentPerson, email).execute();
*/
/*
                txtName.setText(personName);
                txtEmail.setText(email);
*/

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
/*
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                new LoadProfileImage().execute(personPhotoUrl);
*/

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable != googleConnection)
            return;
        switch ((State) data) {
            case OPENING:
                break;
            case OPENED:
                try {
                    Plus.PeopleApi.loadVisible(googleConnection.getGoogleApiClient(), null).setResultCallback(this);
                    String emailAddress = googleConnection.getAccountName();
                    Log.d(TAG, "Opened");
                } catch (Exception ex) {
                    String exception = ex.getLocalizedMessage();
                    String exceptionString = ex.toString();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        Log.d(TAG, "result.getStatus():" + peopleData.getStatus());
        getProfileInformation();
        Log.i(TAG, "hallo");

/*
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            try {
                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {
                    Log.d(TAG, "Display name: " + personBuffer.get(i).getDisplayName());
111                }
            } finally {
                personBuffer.release();
            }
*/
/*
        } else {
            Log.e(TAG, "Error requesting visible circles: " + peopleData.getStatus());
        }
*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_google:
                googleConnection.connect();
                break;
            case R.id.button_twitter:
                if (googleConnection.getGoogleApiClient().isConnected())
                    Plus.AccountApi.clearDefaultAccount(googleConnection.getGoogleApiClient());
                googleConnection.disconnect();
                googleConnection.revokeAccessAndDisconnect();
                break;
        }
    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            // TO DO : Save in db
        }
    }

}