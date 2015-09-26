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
 * Created by Kyosukke on 17/08/2015.
 */
public class DeleteListActivity extends Activity implements View.OnClickListener {

    private TextView name;
    private ImageButton ok;
    private ImageButton cancel;

    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_delete);

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
                Log.e("DeleteListActivity", "Ok clicked.");
                new SendItems().execute();
                finish();
                break ;
            case R.id.cancel:
                Log.e("DeleteListActivity", "Cancel clicked.");
                finish();
                break ;
            default:
                Log.e("DeleteListActivity", "DEFAULT");
                break ;
        }
    }

    private class SendItems extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            GlobalManager gm = new GlobalManager();

            gm.deleteItem(item.getId());
            return null;
        }
    }
}
