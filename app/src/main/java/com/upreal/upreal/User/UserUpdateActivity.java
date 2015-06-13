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

import com.upreal.upreal.utils.Address;
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

    private Address userAddress;

    private LinearLayout address;
    private TextView homeAddress;
    private TextView homeAddress2;
    private TextView city;
    private TextView postalCode;
    private TextView country;

    private EditText short_desc;

    private Button update;

    private SessionManagerUser sessionManagerUser;
    User user;
/*    RecyclerView rv;*/

    AlertDialog.Builder builder;
    LayoutInflater inflater;
    View layout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);

        sessionManagerUser = new SessionManagerUser(getApplicationContext());
        user = sessionManagerUser.getUser();

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
            phoneNumber.setText(R.string.not_defined);
        else
            phoneNumber.setText(String.valueOf(user.getPhone()));
        phoneAllow = (ImageView) findViewById(R.id.allowNumber);

        address = (LinearLayout) findViewById(R.id.address);
        homeAddress = (TextView) findViewById(R.id.homeAddress);
        if (user.getId_address() != -1)
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
                builder.setTitle(R.string.change_name);
                layout = inflater.inflate(R.layout.alertbox_name, null);
                builder.setView(layout);
                final EditText eFirstName = (EditText) layout.findViewById(R.id.eFirstName);
                final EditText eLastName = (EditText) layout.findViewById(R.id.eLastName);
                builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
                builder.setTitle(R.string.change_phone_number);
                layout = inflater.inflate(R.layout.alertbox_phone, null);
                builder.setView(layout);
                final EditText ePhoneNumber = (EditText) layout.findViewById(R.id.ePhoneNumber);
                builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                builder.setTitle(R.string.change_address);
                layout = inflater.inflate(R.layout.alertbox_address, null);
                builder.setView(layout);
                final EditText eAddress = (EditText) layout.findViewById(R.id.eAddress);
                final EditText eAddress2 = (EditText) layout.findViewById(R.id.eAddress2);
                final EditText eCountry = (EditText) layout.findViewById(R.id.eCountry);
                final EditText eCity = (EditText) layout.findViewById(R.id.eCity);
                final EditText ePostalCode = (EditText) layout.findViewById(R.id.ePostalCode);
                builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    homeAddress.setText(eAddress.getText().toString());
                    homeAddress2.setText(eAddress2.getText().toString());
                    country.setText(eCountry.getText().toString());
                    city.setText(eCity.getText().toString());
                    postalCode.setText(ePostalCode.getText().toString());
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

                userAddress = new Address(user.getId_address(), homeAddress.getText().toString(), homeAddress2.getText().toString(),
                        country.getText().toString(), city.getText().toString(), Integer.parseInt(postalCode.getText().toString()));
                new updateUser(sessionManagerUser.getUserId(), firstName.getText().toString(), lastName.getText().toString(),
                        Integer.getInteger(phoneNumber.getText().toString()), userAddress).execute();
                break;
            default:
                break;
        }
    }

    private class getAddress extends AsyncTask<Integer, Void, Address> {
        SoapUserUtilManager pm = new SoapUserUtilManager();

        @Override
        protected Address doInBackground(Integer... params) {
            return pm.getAddressInfo(params[0]);
        }

        protected void onPostExecute(Address result) {
            homeAddress.setText(result.getAddress());
            homeAddress2.setText(result.getAddress2());
            country.setText(result.getCountry());
            city.setText(result.getCity());
            postalCode.setText((result.getPostalCode() == 0) ? String.valueOf(R.string.not_defined) : String.valueOf(result.getPostalCode()));
        }
    }

    private class updateUser extends AsyncTask<Void, Void, Boolean> {
        private int id;
        private String fn;
        private String ln;
        private int phone;
        private Address add;
        private int id_address;

        updateUser(int id, String firstName, String lastName, int phone, Address address) {
            this.id = id;
            this.fn = firstName;
            this.ln = lastName;
            this.phone = phone;
            this.add = address;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SoapUserManager pm = new SoapUserManager();
            SoapUserUtilManager pm2 = new SoapUserUtilManager();
            if (user.getId_address() == -1) {
                id_address = pm2.registerAddress(this.add);
                pm.updateAccount(this.id, this.fn, this.ln, this.phone, id_address);

            }
            else {
                pm2.updateAddress(this.add);
                pm.updateAccount(this.id, this.fn, this.ln, this.phone, user.getId_address());
            }
            return false;
        }

        protected void onPostExecute(Boolean success) {

        }
    }
}
