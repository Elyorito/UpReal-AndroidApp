package com.upreal.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;
import com.upreal.R;
import com.upreal.home.HomeActivity;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.SoapUserManager;
import com.upreal.utils.User;
import com.upreal.utils.database.DatabaseHelper;
import com.upreal.utils.database.DatabaseQuery;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Elyo on 01/03/2015.
 */
public class LoginFragmentRegister extends Fragment
        implements View.OnClickListener, TextView.OnEditorActionListener {

    private static final String TAG = "LoginFragmentRegister";

    private TextInputLayout textInputId;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputPassword2;
    private TextInputLayout textInputEmail;

    private AlertDialog.Builder builder;
    private EditText edit_id;
    private EditText edit_password;
    private EditText edit_confirmpassword;
    private EditText edit_email;
    private TextView e_id;
    private TextView e_password;
    private TextView e_confirm;
    private TextView e_email;
    private boolean complete;
    private boolean uIsTaken;

    private Button but_register;
    private CheckedTextView checkedTextView;
    private TextView e_cgu;
    public SessionManagerUser sessionManagerUser;

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private DatabaseQuery mDbQuery;

    private Context mContext;
    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_register, container, false);
        sessionManagerUser = new SessionManagerUser(getActivity());

        textInputId = (TextInputLayout) v.findViewById(R.id.input_register_id);
        textInputPassword = (TextInputLayout) v.findViewById(R.id.input_register_password);
        textInputPassword2 = (TextInputLayout) v.findViewById(R.id.input_register_password2);
        textInputEmail = (TextInputLayout) v.findViewById(R.id.input_register_email);
        edit_id = (EditText) v.findViewById(R.id.edittext_register_id);
        edit_password = (EditText) v.findViewById(R.id.edittext_register_password);
        edit_confirmpassword = (EditText) v.findViewById(R.id.edittext_register_password2);
        edit_email = (EditText) v.findViewById(R.id.edittext_register_email);
        but_register = (Button) v.findViewById(R.id.button_login_connect);
        checkedTextView = (CheckedTextView) v.findViewById(R.id.checktext_cgu);

        checkedTextView.setOnClickListener(this);
        edit_id.setOnEditorActionListener(this);
        but_register.setOnClickListener(this);
        builder = new AlertDialog.Builder(v.getContext());
        complete = true;
        uIsTaken = false;

        mContext = getActivity().getApplicationContext();

        return v;
    }

    //Like this product
    //Sign to make your opinion count

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            new RetrieveIsUsernameTaken().execute(v.getText().toString());
        }
        return false;
    }

    public boolean validateId() {

        String id = edit_id.getText().toString().trim();
        if (id.isEmpty() || id.length() < 4) {
            textInputId.setError("Username must be four caracter long");
            return false;
        } else
            textInputId.setErrorEnabled(false);
        return true;
    }
    public boolean validatePassword() {

        String password = edit_password.getText().toString().trim();
        String password2 = edit_confirmpassword.getText().toString().trim();

        if (password.isEmpty() || (password.length() < 6 && password2.length() < 6)|| password.equals(password2)) {
            textInputPassword.setError("Assurer vous d'avoir entre au moins 6 caractere(contre " + password.length());
            textInputPassword2.setError("Assurer vous d'avoir entre au moins 6 caractere(contre " + password2.length());
            return false;
        } else {
            textInputPassword.setErrorEnabled(false);
            textInputPassword2.setErrorEnabled(false);
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean validateEmail() {

        String email = edit_email.getText().toString().trim();
        if (email.isEmpty() || isValidEmail(email)) {
            textInputEmail.setError("Please include '@'");
            return false;
        }
        return true;
    }

    public class myTextWatcher implements TextWatcher {

        private View view;

        myTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edittext_register_id:
                    validateId();
                    break;
                case R.id.edittext_register_password:
                    validatePassword();
                    break;
                case R.id.edittext_register_password2:
                    validatePassword();
                    break;
                case R.id.edittext_register_email:
                    validateEmail();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_login_connect:
                if (!validateId() || !validatePassword() || !validateEmail())
                    Toast.makeText(v.getContext(), "Error formulaire", Toast.LENGTH_SHORT).show();
//                if (!checkedTextView.isChecked()) {
//                    complete = false;
//                }
//                if (edit_password.length() < 6) {
//                    if (edit_password.length() == 0) {
//                        e_password.setText("Un mot de passe est requis");
//                        e_password.setVisibility(View.VISIBLE);
//                        Toast.makeText(v.getContext(), "Password Empty", Toast.LENGTH_SHORT).show();
//                        complete = false;
//                    }
//                    else {
//                        e_password.setText("Votre mot de passe doit contenir au moins 6 caractères");
//                        e_password.setVisibility(View.VISIBLE);
//                        Toast.makeText(v.getContext(), "Password too short (Length > 6 caracters)", Toast.LENGTH_SHORT).show();
//                        complete = false;
//                    }
//                }
//                if (edit_password.toString() == edit_confirmpassword.toString()/*!edit_password.toString().equals(edit_confirmpassword.toString())*/) {
//                    e_confirm.setText("Vous n'avez pas entré le même mot de passe");
//                    e_confirm.setVisibility(View.VISIBLE);
//                    Toast.makeText(v.getContext(), "Password doesnt match", Toast.LENGTH_SHORT).show();
//                    complete = false;
//                }
//                if (complete && !uIsTaken) {
//                    InputMethodManager im = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
//                    im.hideSoftInputFromWindow(edit_email.getWindowToken(), 0);
//                    new RetrieveRegisterAccount().execute();
//                    complete = true;
//                }
                break;
            case R.id.checktext_cgu:
                if (!checkedTextView.isChecked())
                    checkedTextView.setChecked(true);
                else
                    checkedTextView.setChecked(false);
                break;
            default:
                break;
        }
    }

    /**
     * Fetching user's information name, email, profile pic
     * */


    private class RetrieveIsUsernameTaken extends AsyncTask<String, Void, Boolean> {

        boolean isTaken;

        @Override
        protected Boolean doInBackground(String... params) {
            SoapUserManager ex = new SoapUserManager();
            isTaken = ex.isUsernameTaken(params[0]);
            return isTaken;
        }

        @Override
        protected void onPostExecute(Boolean isTaken) {
            super.onPostExecute(isTaken);
            if (isTaken) {
                Toast.makeText(getActivity().getApplicationContext(), "Username Already Taken", Toast.LENGTH_SHORT).show();
                e_id.setText("Ce nom de compte existe déjà");
                e_id.setVisibility(View.VISIBLE);
                edit_id.setBackgroundColor(Color.RED);
                uIsTaken = true;
                complete = false;
            }
            else {
                edit_id.setBackgroundColor(Color.GREEN);
                e_id.setVisibility(View.GONE);
                uIsTaken = false;
                complete = true;
            }
        }
    }

    private class RetreiveRegisterAccount extends AsyncTask<Void, Void, Integer> {

        private int info_serv;
        private String username;
        private String firstname;
        private String lastname;
        private String email;
        private String password;
        private boolean social;

        RetreiveRegisterAccount() {
            social = false;
            username = edit_id.getText().toString();
            password = edit_password.getText().toString();
            email = edit_email.getText().toString();
        }

        RetreiveRegisterAccount(String username, Person currentPerson, String email) {
            social = true;
            this.username = username;
            firstname = null;
            if (currentPerson.getName().hasGivenName())
                firstname = currentPerson.getName().getGivenName();
            lastname = null;
            if (currentPerson.getName().hasFamilyName())
                this.lastname = currentPerson.getName().getFamilyName();
            this.email = email;
            SecretKeySpec sks = null;
            try {
                SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                sr.setSeed("any data used as random seed".getBytes());
                KeyGenerator kg = KeyGenerator.getInstance("AES");
                kg.init(128, sr);
                sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
            } catch (Exception e) {
                Log.e(TAG, "AES secret key spec error");
            }

            // Encode the original data with AES
            byte[] encodedBytes = null;
            try {
                Cipher c = Cipher.getInstance("AES");
                c.init(Cipher.ENCRYPT_MODE, sks);
                encodedBytes = c.doFinal(this.email.getBytes());
            } catch (Exception e) {
                Log.e(TAG, "AES encryption error");
            }
            this.password = Base64.encodeToString(encodedBytes, Base64.DEFAULT);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            SoapUserManager ex = new SoapUserManager();
            if (social)
                info_serv = ex.registerAccount(username, firstname, lastname, password, email);
            else
                info_serv = ex.registerAccount(username, password, email);
            return info_serv;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity().getApplicationContext(), username +" |" + password +" |" + email +" |", Toast.LENGTH_SHORT).show();
            if (s <= 0) {
                Toast.makeText(getActivity().getApplicationContext(), Integer.toString(s), Toast.LENGTH_SHORT).show();
                builder.setTitle("Erreur Creation Compte")
                        .setMessage("Probleme lors de la creation du compte. Veuillez ressayer ulterieurement")
                        .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();
            } else {
                mDbHelper = new DatabaseHelper(getActivity().getApplicationContext());
                mDbQuery = new DatabaseQuery(mDbHelper);
                mDatabase = mDbHelper.openDataBase();
                sessionManagerUser.setRegisterLoginUser(username, password);
                new RetrieveUser().execute();
                mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.liked_product), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "3"});
                mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.followed_user), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "2"});
                mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.product_seen_history), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "10"});
                mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.my_commentary), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "11"});
                mDbQuery.InsertData("lists", new String[]{"name", "public", "nb_items", "id_user", "type"}, new String[]{getString(R.string.my_barter_product_list), Integer.toString(1), Integer.toString(0), Integer.toString(sessionManagerUser.getUserId()), "6"});
                mDatabase.close();
                HomeActivity homeActivity = new HomeActivity();
                Intent close = new Intent(getActivity().getApplicationContext(), homeActivity.ACTION_CLOSE_HOME.getClass());
                Intent intent = new Intent(getActivity().getApplicationContext(), homeActivity.getClass());
                getActivity().sendBroadcast(close);
                startActivity(intent);
                getActivity().finish();
                //Toast.makeText(getActivity().getApplicationContext(), "Response:" + s, Toast.LENGTH_SHORT).show();
            }
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getFirstname() {
            return firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isSocial() {
            return social;
        }

        public void setSocial(boolean social) {
            this.social = social;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    private class RetrieveUser extends AsyncTask<Void, Void, User> {

        User user = new User();
        @Override
        protected User doInBackground(Void... params) {

            SoapUserManager um = new SoapUserManager();
            user = um.getUserByUsername(sessionManagerUser.getRegisterLoginUser()[0]);
/*
            Log.v("User info", sessionManagerUser.getRegisterLoginUser()[0]);
            Log.v("User FirstName", user.getFirstname());
*/

            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            sessionManagerUser.setUser(user);
        }
    }
}