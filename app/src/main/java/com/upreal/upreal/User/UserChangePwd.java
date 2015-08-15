package com.upreal.upreal.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.upreal.upreal.R;
import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.SoapUserManager;
import com.upreal.upreal.utils.User;

/**
 * Created by Eric on 17/06/2015.
 */
public class UserChangePwd extends Activity implements View.OnClickListener {

    private EditText oldPassword;
    private EditText nPassword;
    private EditText cPassword;
    private TextView confOld;
    private TextView confNew;

    private User user;

    AlertDialog.Builder builder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_password);

        SessionManagerUser sessionManagerUser = new SessionManagerUser(getApplicationContext());
        user = sessionManagerUser.getUser();

   /*     rv = (RecyclerView)findViewById(R.id.RecyclerView_userUpdate);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);*/
        oldPassword = (EditText) findViewById(R.id.current);
        nPassword = (EditText) findViewById(R.id.newp);
        cPassword = (EditText) findViewById(R.id.conf);
        confOld = (TextView) findViewById(R.id.errorC);
        confNew = (TextView) findViewById(R.id.errorN);
        Button cancel = (Button) findViewById(R.id.cancel);
        Button update = (Button) findViewById(R.id.update);

        cancel.setOnClickListener(this);
        update.setOnClickListener(this);

        builder = new AlertDialog.Builder(this);
    }

    @Override
    public void onClick(View v) {
        builder.setTitle(getString(R.string.error));
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        switch (v.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.update:
                Log.v("update", "update");
                if (updatePassword())
                    new changePassword().execute();
                break;
        }
    }

    private Boolean updatePassword() {
        if (!nPassword.getText().toString().equals(cPassword.getText().toString())) {
            confNew.setText(getString(R.string.password_not_same));
            return false;
        }
        return true;
    }

    private class changePassword extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            SoapUserManager pm = new SoapUserManager();
            return pm.updatePassword(user.getId(), oldPassword.getText().toString(), nPassword.getText().toString());
        }

        protected void onPostExecute(Boolean success) {
            if (success)
                finish();
            else
                confOld.setText(getString(R.string.wrong_password));
        }
    }
}
