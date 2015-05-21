package com.upreal.upreal.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.upreal.upreal.R;
import com.upreal.upreal.home.HomeActivity;
import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.SoapUserManager;
import com.upreal.upreal.utils.User;

/**
 * Created by Elyo on 01/03/2015.
 */
public class LoginFragmentConnect extends Fragment implements View.OnClickListener{

    private AlertDialog.Builder builder;
    private EditText login;
    private EditText password;
    private Button connect;
    public SessionManagerUser sessionManagerUser;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_connect, container, false);
        sessionManagerUser = new SessionManagerUser(getActivity());
        login = (EditText) v.findViewById(R.id.edittext_login_mail);
        password = (EditText) v.findViewById(R.id.edittext_login_password);
        connect = (Button) v.findViewById(R.id.button_login_connect);
        builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Erreur Compte")
                .setMessage("Email/Identifiant ou Mot de passe incorrect")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        connect.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login_connect:
                if (login.getText().length() == 0 || password.getText().length() == 0)
                    Toast.makeText(v.getContext(), "Login or password empty", Toast.LENGTH_SHORT).show();
                else
                    new RetrieveInfoFromServer().execute();
                break;
            default:
                break;
        }
    }

    private class RetrieveUser extends AsyncTask<Void, Void, User> {

        User user = new User();
        @Override
            protected User doInBackground(Void... params) {

            SoapUserManager um = new SoapUserManager();
            user = um.getUserByUsername(sessionManagerUser.getRegisterLoginUser()[0]);
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            sessionManagerUser.setUser(user);
        }
    }

    private class RetrieveInfoFromServer extends AsyncTask<Void, Void, Boolean> {

        private Boolean info_serv;

        @Override
        protected Boolean doInBackground(Void... params) {

            SoapUserManager ex = new SoapUserManager();
            info_serv = ex.connectAccount(login.getText().toString(), password.getText().toString());
            return info_serv;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if (!s) {
                builder.create().show();
            } else {
                sessionManagerUser.setRegisterLoginUser(login.getText().toString(), password.getText().toString());
                new RetrieveUser().execute();
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
}
