package com.upreal.miscellaneous;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.upreal.R;
import com.upreal.utils.Loyalty;

/**
 * Created by Eric on 15/11/2015.
 */
public class LoyaltyCardActivity extends Activity {

    Toolbar toolbar;
    Loyalty loyalty;
    ImageView image;
    TextView body;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_news);
        setContentView(R.layout.activity_loyalty_card);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        image = (ImageView) findViewById(R.id.image_loyalty);
        body = (TextView) findViewById(R.id.body_loyalty);
        loyalty = getIntent().getExtras().getParcelable("loyalty");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setTitle(loyalty.getName());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.black));

//        toolbar.setTitle(article.getTitle());
        body.setText(loyalty.getName());
        image.setImageBitmap(loyalty.getBarcode());
//        Toast.makeText(NewsActivity.this, article.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
