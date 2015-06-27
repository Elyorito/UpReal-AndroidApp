package com.upreal.upreal.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.upreal.upreal.R;
import com.upreal.upreal.home.HomeActivity;
import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.SoapUserManager;
import com.upreal.upreal.utils.User;
import com.upreal.upreal.utils.database.DatabaseHelper;
import com.upreal.upreal.utils.database.DatabaseQuery;

/**
 * Created by Elyo on 01/03/2015.
 */
public class LoginFragmentRegister extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

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

    private Button but_register;
    private CheckedTextView checkedTextView;
    private TextView e_cgu;
    public SessionManagerUser sessionManagerUser;

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private DatabaseQuery mDbQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_register, container, false);
        sessionManagerUser = new SessionManagerUser(getActivity());

        edit_id = (EditText) v.findViewById(R.id.edittext_register_id);
        edit_password = (EditText) v.findViewById(R.id.edittext_register_password);
        edit_confirmpassword = (EditText) v.findViewById(R.id.edittext_register_password2);
        edit_email = (EditText) v.findViewById(R.id.edittext_register_email);
        but_register = (Button) v.findViewById(R.id.button_login_connect);
        checkedTextView = (CheckedTextView) v.findViewById(R.id.checktext_cgu);

        e_id = (TextView) v.findViewById(R.id.user_error);
        e_password = (TextView) v.findViewById(R.id.password_error);
        e_confirm = (TextView) v.findViewById(R.id.password_error2);
        e_email = (TextView) v.findViewById(R.id.email_error);
        e_cgu = (TextView) v.findViewById(R.id.cgu_error);

        checkedTextView.setOnClickListener(this);
        edit_id.setOnEditorActionListener(this);
        but_register.setOnClickListener(this);
        builder = new AlertDialog.Builder(v.getContext());
        complete = true;
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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_login_connect:
                e_confirm.setVisibility(View.GONE);
                e_password.setVisibility(View.GONE);
                e_email.setVisibility(View.GONE);
                e_cgu.setVisibility(View.GONE);
                if (!checkedTextView.isChecked()) {
                    e_cgu.setVisibility(View.VISIBLE);
                    complete = false;
                }
                if (edit_password.length() < 6) {
                    if (edit_password.length() == 0) {
                        e_password.setText("Un mot de passe est requis");
                        e_password.setVisibility(View.VISIBLE);
                        Toast.makeText(v.getContext(), "Password Empty", Toast.LENGTH_SHORT).show();
                        complete = false;
                    }
                    else {
                        e_password.setText("Votre mot de passe doit contenir au moins 6 caractères");
                        e_password.setVisibility(View.VISIBLE);
                        Toast.makeText(v.getContext(), "Password too short (Length > 6 caracters)", Toast.LENGTH_SHORT).show();
                        complete = false;
                    }
                }
                if (edit_password.toString() == edit_confirmpassword.toString()/*!edit_password.toString().equals(edit_confirmpassword.toString())*/) {
                    e_confirm.setText("Vous n'avez pas entré le même mot de passe");
                    e_confirm.setVisibility(View.VISIBLE);
                    Toast.makeText(v.getContext(), "Password doesnt match", Toast.LENGTH_SHORT).show();
                    complete = false;
                }
                if (complete) {
                    InputMethodManager im = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(edit_email.getWindowToken(), 0);
                    new RetreiveRegisterAccount().execute();
                    complete = true;
                }
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
                complete = false;
            }
            else {
                edit_id.setBackgroundColor(Color.GREEN);
                e_id.setVisibility(View.GONE);
                complete = true;
            }
        }
    }

    private class RetreiveRegisterAccount extends AsyncTask<Void, Void, Integer> {

        private int info_serv;

        @Override
        protected Integer doInBackground(Void... params) {

            SoapUserManager ex = new SoapUserManager();
            info_serv = ex.registerAccount(edit_id.getText().toString(), edit_password.getText().toString(), edit_email.getText().toString());
            return info_serv;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity().getApplicationContext(), edit_id.getText().toString() +" |" + edit_password.getText().toString() +" |" + edit_email.getText().toString()+" |", Toast.LENGTH_SHORT).show();
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
                sessionManagerUser.setRegisterLoginUser(edit_id.getText().toString(), edit_password.getText().toString());
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
        }
    }
}