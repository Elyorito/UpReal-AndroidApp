package com.upreal.upreal.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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

/**
 * Created by Elyo on 01/03/2015.
 */
public class LoginFragmentRegister extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    private AlertDialog.Builder builder;
    private EditText edit_id;
    private EditText edit_password;
    private EditText edit_confirmpassword;
    private EditText edit_email;
    private Button but_register;
    private CheckedTextView checkedTextView;
    public SessionManagerUser sessionManagerUser;

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

        checkedTextView.setOnClickListener(this);
        edit_id.setOnEditorActionListener(this);
        but_register.setOnClickListener(this);
        builder = new AlertDialog.Builder(v.getContext());
        return v;
    }

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
                if (!checkedTextView.isChecked()) {
                    Toast.makeText(v.getContext(), "Please accept CGU", Toast.LENGTH_SHORT).show();
                } else if (edit_password.length() < 6) {
                    if (edit_password.length() == 0)
                        Toast.makeText(v.getContext(), "Password Empty", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(v.getContext(), "Password too short (Length > 6 caracters)", Toast.LENGTH_SHORT).show();
                } else if (edit_password.toString() == edit_confirmpassword.toString()/*!edit_password.toString().equals(edit_confirmpassword.toString())*/) {
                    Toast.makeText(v.getContext(), "Password doesnt match", Toast.LENGTH_SHORT).show();
                } else
                    new RetreiveRegisterAccount().execute();
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
                edit_id.setBackgroundColor(Color.RED);
            }
            else {
                edit_id.setBackgroundColor(Color.GREEN);
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
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();
            } else {
                sessionManagerUser.setRegisterLoginUser(edit_id.getText().toString(), edit_password.getText().toString());
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