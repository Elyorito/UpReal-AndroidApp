package com.upreal.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.upreal.R;
import com.upreal.home.HomeActivity;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.SoapUserManager;
import com.upreal.utils.User;
import com.upreal.utils.WearManager;
import com.upreal.utils.database.DatabaseHelper;
import com.upreal.utils.database.DatabaseQuery;

/**
 * Created by Elyo on 01/03/2015.
 */
public class LoginFragmentConnect extends Fragment implements View.OnClickListener {

    private static final String TAG = "LoginFragmentConnect";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;
    private TextInputLayout textInputUserName;
    private TextInputLayout textInputPassword;

    private AlertDialog.Builder builder;
    private EditText login;
    private EditText password;
    private Button connect;
    public SessionManagerUser sessionManagerUser;

    private Context mContext;

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private DatabaseQuery mDbQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_connect, container, false);
        sessionManagerUser = new SessionManagerUser(getActivity());
        textInputUserName = (TextInputLayout) v.findViewById(R.id.input_edit_login);
        textInputPassword = (TextInputLayout) v.findViewById(R.id.input_edit_password);
        login = (EditText) v.findViewById(R.id.edittext_login_mail);
        password = (EditText) v.findViewById(R.id.edittext_login_password);
        connect = (Button) v.findViewById(R.id.button_login_connect);
        login.addTextChangedListener(new myTextWatcher(login));

        builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Erreur Compte")
                .setMessage("Email/Identifiant ou Mot de passe incorrect")
                .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        connect.setOnClickListener(this);
        // Google+
        mContext = getActivity().getApplicationContext();

        // Facebook
        FacebookSdk.sdkInitialize(mContext);
        return v;
    }

    public boolean validateLogin() {

        if (login.getText().toString().trim().isEmpty()) {
            textInputUserName.setError("L'identifiant ou email n'est pas renseigné");
            return false;
        } else
            textInputUserName.setErrorEnabled(false);
        return true;
    }

    public boolean validatePassword() {

        String password = this.password.getText().toString().trim();

        if (password.isEmpty() || password.length() < 4) {
            textInputPassword.setError("Le mot de passe doit faire plus de 4 caractère");
            return false;
        }
        else
            textInputPassword.setErrorEnabled(false);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login_connect:
                if (!validateLogin() || !validatePassword())
                    break;
                else {
                    InputMethodManager im = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(login.getWindowToken(), 0);
                    new RetrieveInfoFromServer().execute();
                }
                break;
            default:
                break;
        }
    }

    public class myTextWatcher implements TextWatcher {

        private View mView;

        myTextWatcher(View view) {
            this.mView = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (this.mView.getId()) {
                case R.id.edittext_login_mail:
                    validateLogin();
                    break;
                case R.id.edittext_login_password:
                    validatePassword();
                    break;
            }
        }
    }

    private class RetrieveUser extends AsyncTask<Void, Void, User> {

        User user = new User();
        @Override
            protected User doInBackground(Void... params) {

            SoapUserManager um = new SoapUserManager();
            user = um.getUserByUsername(sessionManagerUser.getRegisterLoginUser()[0]);
            Log.v("User info", sessionManagerUser.getRegisterLoginUser()[0]);
            Log.v("User FirstName", user.getFirstname());

            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            sessionManagerUser.setUser(user);
            getActivity().finish();
        }
    }

    private class RetrieveInfoFromServer extends AsyncTask<Void, Void, Boolean> {

        private Boolean info_serv;
        private String mUsername;
        private String mPassword;

        RetrieveInfoFromServer() {
            mUsername = login.getText().toString();
            mPassword = password.getText().toString();
        }

        RetrieveInfoFromServer(String username, String rPassword) {
            mUsername = username;
            mPassword = rPassword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            SoapUserManager ex = new SoapUserManager();
            info_serv = ex.connectAccount(mUsername, mPassword);
            return info_serv;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if (!s) {
                builder.create().show();
            } else {
                WearManager.notifyWear(getActivity().getApplicationContext(), "Connected successfully !");

                mDbHelper = new DatabaseHelper(getActivity().getApplicationContext());
                mDbQuery = new DatabaseQuery(mDbHelper);
                mDatabase = mDbHelper.openDataBase();
                sessionManagerUser.setRegisterLoginUser(login.getText().toString(), password.getText().toString());
                mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.liked_product), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "3"});
                mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.followed_user), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "2"});
                mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.product_seen_history), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "10"});
                mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.my_commentary), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "11"});
                mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.my_barter_product_list), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "6"});
                mDatabase.close();
                Toast.makeText(getActivity().getApplication(), "IDUSER=" + Integer.toString(sessionManagerUser.getUserId()), Toast.LENGTH_LONG).show();
                HomeActivity homeActivity = new HomeActivity();
                Intent close = new Intent(getActivity().getApplicationContext(), HomeActivity.ACTION_CLOSE_HOME.getClass());
                Intent intent = new Intent(getActivity().getApplicationContext(), homeActivity.getClass());
                getActivity().sendBroadcast(close);
                new RetrieveUser().execute();
                startActivity(intent);
            }
        }
    }

}
