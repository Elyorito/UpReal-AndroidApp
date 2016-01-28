package com.upreal.miscellaneous;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.upreal.R;
import com.upreal.utils.Loyalty;
import com.upreal.utils.SoapGlobalManager;

/**
 * Created by Eric on 15/11/2015.
 */
public class LoyaltyCardActivity extends Activity {

    Toolbar toolbar;
    Loyalty loyalty;
    ImageView image;
    TextView body;
    Button delete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty_card);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        image = (ImageView) findViewById(R.id.image_loyalty);
        body = (TextView) findViewById(R.id.body_loyalty);
        loyalty = getIntent().getExtras().getParcelable("loyalty");
        delete = (Button) findViewById(R.id.button_delete);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.black));

        body.setText(loyalty.getName());
        image.setImageBitmap(loyalty.getBarcode());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new deletePossess().execute();
            }
        });
    }

    public class deletePossess extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            SoapGlobalManager sg = new SoapGlobalManager();
            return sg.deletePossess(loyalty.getId());
        }

        protected void onPostExecute(Boolean success) {
            finish();
        }
    }
}
