package com.upreal.upreal.product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.upreal.upreal.R;
import com.upreal.upreal.geolocalisation.GeolocalisationActivity;
import com.upreal.upreal.utils.Product;
import com.upreal.upreal.utils.SoapProductManager;
import com.upreal.upreal.view.SlidingTabLayout;


/**
 * Created by Elyo on 11/02/2015.
 */
public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private ViewPager mViewPager;
    private ProductViewPagerAdapter adapter;
    private SlidingTabLayout mSlidingTabLayout;
    private Product prod;
    private CharSequence title;

    private TextView prodName;
    private TextView prodBrand;
    private TextView prodShortDesc;
    private ImageView prodPicture;

    private Button geoloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        prodName = (TextView) findViewById(R.id.product_name);
        prodBrand = (TextView) findViewById(R.id.product_brand);
        prodShortDesc = (TextView) findViewById(R.id.product_desc);
        prodPicture = (ImageView) findViewById(R.id.product_picture);

        geoloc = (Button) findViewById(R.id.geoloc);
        geoloc.setOnClickListener(this);

        prod = getIntent().getExtras().getParcelable("listprod");

        title = new String(prod.getName());
        prodName.setText(prod.getName());
        Picasso.with(getApplicationContext()).load("http://163.5.84.202/Symfony/web/images/Product/" + prod.getPicture()).into(prodPicture);

        prodBrand.setText(prod.getBrand());
        prodShortDesc.setText(prod.getShortDesc());

        CharSequence Tab[] = {getString(R.string.commentary), getString(R.string.social), getString(R.string.options)};
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ProductViewPagerAdapter(getSupportFragmentManager(), Tab, 3, prod);
        mViewPager.setAdapter(adapter);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab);
        mSlidingTabLayout.setDistributeEvenly(true);
      mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
          @Override
          public int getIndicatorColor(int position) {
              return getResources().getColor(R.color.ColorPrimaryDark);
          }
        });
        mSlidingTabLayout.setViewPager(mViewPager);

        //new RetrievePicture().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                //Refresh Fiche produit
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.geoloc:
                Intent intent = new Intent(v.getContext(), GeolocalisationActivity.class);
                intent.putExtra("id_product", prod.getId());
                v.getContext().startActivity(intent);
                break ;
            default:
                break ;
        }
    }


    class RetrievePicture extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            SoapProductManager pm = new SoapProductManager();

            String path = pm.getPicture(prod.getId(), 0);
            return path;
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            if (path != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                prodPicture.setImageBitmap(BitmapFactory.decodeFile(path, options));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        recreate();
    }
}