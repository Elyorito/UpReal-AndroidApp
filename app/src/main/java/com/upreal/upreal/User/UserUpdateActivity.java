package com.upreal.upreal.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.upreal.upreal.R;

import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.SoapUserManager;
import com.upreal.upreal.utils.SoapUserUtilManager;
import com.upreal.upreal.utils.User;


/**
 * Created by Eric on 11/06/2015.
 */
public class UserUpdateActivity extends Activity implements View.OnClickListener {
    private LinearLayout name;
    private TextView firstName;
    private TextView lastName;

    private LinearLayout phone;
    private TextView phoneNumber;
    private ImageView phoneAllow;

    private LinearLayout address;
    private TextView homeAddress;

    private EditText short_desc;

    private Button update;

    private SessionManagerUser sessionManagerUser;
/*    RecyclerView rv;*/

    AlertDialog.Builder builder;
    LayoutInflater inflater;
    View layout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);

        sessionManagerUser = new SessionManagerUser(getApplicationContext());
        User user = sessionManagerUser.getUser();

   /*     rv = (RecyclerView)findViewById(R.id.RecyclerView_userUpdate);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);*/
        name = (LinearLayout) findViewById(R.id.name);
        firstName = (TextView) findViewById(R.id.firstName);
        firstName.setText(user.getFirstname());
        lastName = (TextView) findViewById(R.id.lastName);
        lastName.setText(user.getLastname());

        phone = (LinearLayout) findViewById(R.id.phone);
        phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        if (user.getPhone() == -1)
            phoneNumber.setText(R.string.notDefined);
        else
            phoneNumber.setText(String.valueOf(user.getPhone()));
        phoneAllow = (ImageView) findViewById(R.id.allowNumber);

        address = (LinearLayout) findViewById(R.id.address);
        homeAddress = (TextView) findViewById(R.id.homeAddress);
        if (user.getId_address() == -1)
            homeAddress.setText(R.string.notDefined);
        else
            new getAddress().execute(user.getId_address());

        short_desc = (EditText) findViewById(R.id.shortDesc);
        short_desc.setText(user.getShort_desc());

        update = (Button) findViewById(R.id.update);

        name.setOnClickListener(this);
        phone.setOnClickListener(this);
        address.setOnClickListener(this);
        update.setOnClickListener(this);

        builder = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();


    }

    @Override
    public void onClick(View v) {

        Log.v("onClick", "click here");
        switch (v.getId()) {
            case R.id.name:
                Log.v("Name", "Name");
                builder.setTitle(R.string.changeName);
                builder.setView(inflater.inflate(R.layout.alertbox_name, null))
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText eFirstName = (EditText) findViewById(R.id.eFirstName);
                        EditText eLastName = (EditText) findViewById(R.id.eLastName);

                        firstName.setText(eFirstName.getText().toString());
                        lastName.setText(eLastName.getText().toString());
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                break;
            case R.id.phone:
                Log.v("phone","Phone");
                builder.setTitle(R.string.changePhoneNumber); // change to String
                builder.setView(inflater.inflate(R.layout.alertbox_phone, null))
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText ePhoneNumber = (EditText) findViewById(R.id.ePhoneNumber);
                                phoneNumber.setText(ePhoneNumber.getText().toString());
                            }
                        })
                        .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
                break;
            case R.id.address:
                Log.v("address", "Address");

                builder.setTitle(R.string.changeAddress);
                layout = inflater.inflate(R.layout.alertbox_address, null);
                builder.setView(layout);
                final EditText eAddress = (EditText) this.findViewById(R.id.eAddress);
                builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    homeAddress.setText(eAddress.getText().toString());
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
            case R.id.update:
                Log.v("update","update");

                new updateUser(sessionManagerUser.getUserId(), firstName.getText().toString(), lastName.getText().toString(),
                        Integer.getInteger(phoneNumber.getText().toString()), homeAddress.getText().toString()).execute();
                break;
            default:
                break;
        }
    }

    private class getAddress extends AsyncTask<Integer, Void, String> {
        SoapUserUtilManager pm = new SoapUserUtilManager();

        @Override
        protected String doInBackground(Integer... params) {
            return pm.getAddressInfo(params[0]);
        }

        protected void onPostExecute(String result) {
            homeAddress.setText(result);
        }
    }

    private class updateUser extends AsyncTask<Void, Void, Boolean> {
        private int id;
        private String fn;
        private String ln;
        private int phone;
        private String add;

        updateUser(int id, String firstName, String lastName, int phone, String address) {
            this.id = id;
            this.fn = firstName;
            this.ln = lastName;
            this.phone = phone;
            this.add = address;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SoapUserManager pm = new SoapUserManager();

            return pm.updateAccount(this.id, this.fn, this.ln, this.phone, this.add);
        }

        protected void onPostExecute(Boolean success) {

        }
    }
}
