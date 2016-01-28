package com.upreal.login;

import android.content.Intent;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.upreal.R;
import com.upreal.home.HomeActivity;
import com.upreal.utils.GoogleConnection;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.State;
import com.upreal.utils.User;
import com.upreal.utils.WearManager;
import com.upreal.utils.database.DatabaseHelper;
import com.upreal.utils.database.DatabaseQuery;
import com.upreal.view.SlidingTabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Elyo on 01/03/2015.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ResultCallback<People.LoadPeopleResult>, Observer, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, FacebookCallback<LoginResult> {
    private static final int RC_SIGN_IN = 0;
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    private GoogleApiClient mGoogleApiClient;

    private SessionManagerUser sessionManagerUser;
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private DatabaseQuery mDbQuery;
    private static final String TAG = "LoginActivity";

    private CharSequence Tab[];

    private Toolbar toolbar;

    private ViewPager mViewPager;
    private LoginViewPagerAdapter mAdapter;
    private SlidingTabLayout mSlidingTabLayout;

    private GoogleConnection googleConnection;
    private SignInButton gConnect;
    private Button tConnect;

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        sessionManagerUser = new SessionManagerUser(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        loginButton = (LoginButton) findViewById(R.id.facebookconnect);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        loginButton.registerCallback(callbackManager, this);
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
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();
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
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
                googleConnection.connect();
                Toast.makeText(v.getContext(), "Google +", Toast.LENGTH_SHORT).show();
                onSignInClicked();
                break;
            case R.id.button_twitter:
//                if (googleConnection.getGoogleApiClient().isConnected())
//                    Plus.AccountApi.clearDefaultAccount(googleConnection.getGoogleApiClient());
//                googleConnection.disconnect();
//                googleConnection.revokeAccessAndDisconnect();


                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a message to the user that we are signing in.
        Toast.makeText(getApplicationContext(), "We are signing in", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected:" + bundle);
        User user = new User();
        mShouldResolve = false;
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            user.setUsername(currentPerson.getDisplayName());
            user.setPicture(currentPerson.getImage().getUrl());
            user.setFirstname(currentPerson.getName().getGivenName());
            user.setLastname(currentPerson.getName().getFamilyName());
            user.setPassword("google+");
            String personGooglePlusProfile = currentPerson.getUrl();
            sessionManagerUser.setUser(user);
            //Toast.makeText(getApplicationContext(), "Username "+ user.getUsername() + "| Firstname " + user.getFirstname() + "| Picture " + user.getPicture(), Toast.LENGTH_SHORT).show();
            /////////
            WearManager.notifyWear(getApplicationContext(), "Connected successfully !");

            mDbHelper = new DatabaseHelper(getApplicationContext());
            mDbQuery = new DatabaseQuery(mDbHelper);
            mDatabase = mDbHelper.openDataBase();
            sessionManagerUser.setRegisterLoginUser(user.getUsername(), "google+");
            mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.liked_product), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "3"});
            mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.followed_user), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "2"});
            mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.product_seen_history), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "10"});
            mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.my_commentary), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "11"});
            mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.my_barter_product_list), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "6"});
            mDatabase.close();
            HomeActivity homeActivity = new HomeActivity();
            Intent close = new Intent(getApplicationContext(), HomeActivity.ACTION_CLOSE_HOME.getClass());
            Intent intent = new Intent(LoginActivity.this, homeActivity.getClass());
            LoginActivity.this.sendBroadcast(close);
            startActivity(intent);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                Log.v("Cresult", connectionResult.toString());
            }
        } else {
            // Show the signed-out UI
            Log.v("Cresult", "Signed out");
        }



    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        final User user = new User();
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        if (response.getError() != null) {
                            // handle error
                            System.out.println("ERROR");
                        } else {
                            System.out.println("Success");
                           try {

                                String jsonresult = String.valueOf(object);
                                System.out.println("JSON Result" + jsonresult);

                                user.setEmail(object.getString("email"));
                                //user.setId(Integer.parseInt(object.getString("id")));
                                //user.setFirstname(object.getString("first_name"));
                                //user.setLastname(object.getString("last_name"));
                                user.setUsername(object.getString("name"));
                                user.setPassword("facebook");
                                sessionManagerUser.setUser(user);
                               mDbHelper = new DatabaseHelper(getApplicationContext());
                               mDbQuery = new DatabaseQuery(mDbHelper);
                               mDatabase = mDbHelper.openDataBase();
                               //Toast.makeText(this, "Success Facebook !Username="+user.getUsername(), Toast.LENGTH_SHORT).show();
                               sessionManagerUser.setRegisterLoginUser(user.getUsername(), "facebook");
                               mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.liked_product), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "3"});
                               mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.followed_user), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "2"});
                               mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.product_seen_history), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "10"});
                               mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.my_commentary), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "11"});
                               mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.my_barter_product_list), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "6"});
                               mDatabase.close();
                               WearManager.notifyWear(getApplicationContext(), "Connected successfully !");
                               HomeActivity homeActivity = new HomeActivity();
                               Intent close = new Intent(getApplicationContext(), HomeActivity.ACTION_CLOSE_HOME.getClass());
                               Intent intent = new Intent(LoginActivity.this, homeActivity.getClass());
                               LoginActivity.this.sendBroadcast(close);
                               startActivity(intent);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        Toast.makeText(this, "Success Facebook !UserId="+loginResult.getAccessToken().getUserId(), Toast.LENGTH_SHORT).show();
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

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