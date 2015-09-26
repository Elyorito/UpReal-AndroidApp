package com.upreal.global;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.upreal.server.GlobalManager;
import com.upreal.R;
import com.upreal.utils.Item;
//import Item;

/**
 * Created by Kyosukke on 18/08/2015.
 */
public class AddListActivity extends Activity implements View.OnClickListener {

    private TextView name;
    private ImageButton ok;
    private ImageButton cancel;

    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_add);

        name = (TextView) findViewById(R.id.name);
        ok = (ImageButton) findViewById(R.id.ok);
        cancel = (ImageButton) findViewById(R.id.cancel);

        item = getIntent().getExtras().getParcelable("item");
        name.setText(item.getName());

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                Log.e("AddListActivity", "Ok clicked.");
                new SendItems().execute();
                finish();
                break ;
            case R.id.cancel:
                Log.e("AddListActivity", "Cancel clicked.");
                finish();
                break ;
            default:
                Log.e("AddListActivity", "DEFAULT");
                break ;
        }
    }

    private class SendItems extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            GlobalManager gm = new GlobalManager();

            gm.createItem(item.getId(), Integer.parseInt(item.getImagePath()), 0);
            return null;
        }
    }
}
