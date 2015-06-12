package com.upreal.upreal.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.upreal.upreal.R;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);

        name = (LinearLayout) findViewById(R.id.name);
        firstName = (TextView) findViewById(R.id.firstName);
        lastName = (TextView) findViewById(R.id.lastName);

        phone = (LinearLayout) findViewById(R.id.phone);
        phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        phoneAllow = (ImageView) findViewById(R.id.allowNumber);

        address = (LinearLayout) findViewById(R.id.address);
        homeAddress = (TextView) findViewById(R.id.homeAddress);

        short_desc = (EditText) findViewById(R.id.shortDesc);
    }
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        switch (v.getId()) {
            case R.id.name:
                builder.setTitle(R.string.changeName);
                builder.setView(inflater.inflate(R.layout.alertbox_name, null))
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText eFirstName = (EditText) findViewById(R.id.eFirstName);
                        EditText eLastName = (EditText) findViewById(R.id.eLastName);

                        firstName.setText(eFirstName.getText());
                        lastName.setText(eLastName.getText());
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
                builder.setTitle("Vous voulez configurer vos données téléphoniques ?"); // change to String
                builder.setView(inflater.inflate(R.layout.alertbox_phone, null))
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText ePhoneNumber = (EditText) findViewById(R.id.ePhoneNumber);
                                phoneNumber.setText(ePhoneNumber.getText());
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
                builder.setTitle("Vous voulez modifier votre addresse ?"); // change to String
                builder.setView(inflater.inflate(R.layout.alertbox_address, null))
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText eAddress = (EditText) findViewById(R.id.eAddress);
                                homeAddress.setText(eAddress.getText());
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
            default:
                break;
        }
    }


}
