package com.upreal.upreal.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.upreal.upreal.R;
import com.upreal.upreal.bridge.BridgeDeviceActivity;
import com.upreal.upreal.login.LoginActivity;
import com.upreal.upreal.login.LoginViewPagerAdapter;
import com.upreal.upreal.user.UserChangePwd;
import com.upreal.upreal.user.UserUpdateActivity;
import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.database.DatabaseHelper;
import com.upreal.upreal.view.SlidingTabLayout;

/**
 * Created by Eric on 22/06/2015.
 */
public class ParameterActivity extends ActionBarActivity implements View.OnClickListener {
    private CharSequence Tab[];

    private Toolbar toolbar;

    private ViewPager mViewPager;
    private LoginViewPagerAdapter mAdapter;
    private SlidingTabLayout mSlidingTabLayout;

    private Button editProfile;
    private Button editPassword;
    private Button clearCache;
    private Button likeFacebook;
    private Button followTwitter;
    private Button bridge;
    private Button contactUs;
    private Button about;
    private Button deconnexion;

    private SessionManagerUser sessionManagerUser;
    private AlertDialog.Builder builder;
    private DatabaseHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

        editProfile = (Button) findViewById(R.id.edit_profil);
        editPassword = (Button) findViewById(R.id.change_password);
        clearCache = (Button) findViewById(R.id.clear_cache);
        likeFacebook = (Button) findViewById(R.id.like_facebook);
        followTwitter = (Button) findViewById(R.id.follow_twitter);
        bridge = (Button) findViewById(R.id.bridge);
        contactUs = (Button) findViewById(R.id.contact_us);
        about = (Button) findViewById(R.id.about);
        deconnexion = (Button) findViewById(R.id.deconnexion);

        editProfile.setOnClickListener(this);
        editPassword.setOnClickListener(this);
        clearCache.setOnClickListener(this);
        likeFacebook.setOnClickListener(this);
        followTwitter.setOnClickListener(this);
        bridge.setOnClickListener(this);
        contactUs.setOnClickListener(this);
        about.setOnClickListener(this);
        deconnexion.setOnClickListener(this);

        sessionManagerUser = new SessionManagerUser(getApplicationContext());
        mDbHelper = new DatabaseHelper(this);

        builder = new AlertDialog.Builder(this);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.edit_profil:
                if (sessionManagerUser.getUserId() <= 0) {
                    setDialogContent(false, 0, v);
                    builder.create().show();
                } else {
                    Intent intent = new Intent(v.getContext(), UserUpdateActivity.class);
                    v.getContext().startActivity(intent);
                }
                break;
            case R.id.change_password:
                if (sessionManagerUser.getUserId() <= 0) {
                    setDialogContent(false, 1, v);
                    builder.create().show();
                } else {
                    Intent intent = new Intent(v.getContext(), UserChangePwd.class);
                    v.getContext().startActivity(intent);
                }
                break;
            case R.id.clear_cache:
                mDbHelper.deleteDataBase();
                break;
            case R.id.like_facebook:
                break;
            case R.id.follow_twitter:
                break;
            case R.id.bridge:
                if (sessionManagerUser.isLogged()) {
                    Intent i = new Intent(this.getApplicationContext(), BridgeDeviceActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.contact_us:
                builder.setTitle(R.string.send_us_mail);
                LayoutInflater inflater = this.getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog_mail, null);
                builder.setView(layout);

                final EditText subject = (EditText) layout.findViewById(R.id.subjectEmail);
                final EditText messageEmail = (EditText) layout.findViewById(R.id.messageEmail);
                builder.setMessage("");
                builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");

                        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"UpReal@gmail.com"});
                        if (subject.getText().toString().equals(""))
                            Toast.makeText(v.getContext(), "Le sujet ne peut etre vide", Toast.LENGTH_SHORT).show();
                        i.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
                        if (messageEmail.getText().toString().equals(""))
                            Toast.makeText(v.getContext(), "Le message ne peut etre vide", Toast.LENGTH_SHORT).show();
                        i.putExtra(Intent.EXTRA_TEXT, messageEmail.getText().toString());
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(v.getContext(), getString(R.string.need_mail_app)
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

                break;
            case R.id.about:
                break;
            case R.id.deconnexion:
                if (sessionManagerUser.getUserId() <= 0) {
                    setDialogContent(false, 2, v);
                    builder.create().show();
                } else {
                    sessionManagerUser.deleteCurrentUser();
                    HomeActivity homeActivity = new HomeActivity();
                    Intent close = new Intent(this.getApplicationContext(), homeActivity.ACTION_CLOSE_HOME.getClass());
                    Intent intent = new Intent(this.getApplicationContext(), homeActivity.getClass());
                    this.sendBroadcast(close);
                    startActivity(intent);
                    this.finish();
                }
                break;
        }
    }

    private void setDialogContent(Boolean connect ,int type, final View v) {
        if (connect == false) {
            builder.setTitle(getString(R.string.need_connected))
                    .setPositiveButton(v.getContext().getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(v.getContext(), LoginActivity.class);
                            v.getContext().startActivity(intent);
                            dialog.dismiss();
                        }
                    }).setNegativeButton(v.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        switch (type) {
            case 0:
                builder.setMessage(R.string.connect_to_change_profil);
                break;
            case 1:
                builder.setMessage(R.string.connect_to_change_pwd);
                break;
            case 2:
                builder.setMessage(R.string.connect_question);
                break;
        }
    }
}
