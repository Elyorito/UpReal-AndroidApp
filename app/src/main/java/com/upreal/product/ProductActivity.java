package com.upreal.product;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.geolocalisation.GeolocalisationActivity;
import com.upreal.login.LoginActivity;
import com.upreal.utils.BlurImages;
import com.upreal.utils.CircleTransform;
import com.upreal.utils.Product;
import com.upreal.utils.Refresh;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.SoapProductManager;
import com.upreal.utils.SoapProductUtilManager;
import com.upreal.utils.SoapUserUtilManager;
import com.upreal.utils.database.DatabaseHelper;
import com.upreal.utils.database.DatabaseQuery;
import com.upreal.view.SlidingTabLayout;


/**
 * Created by Elyo on 11/02/2015.
 */
public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageBlurred;
    private ImageView imageProduct;
    private TabLayout tabLayout;
    private Context context;
    private Activity activity;
    private Toolbar toolbar;

    private ViewPager mViewPager;
    //private ProductViewPagerAdapter adapter;
    private ProductNewViewPagerAdapter adapter;
    private SlidingTabLayout mSlidingTabLayout;
    private SessionManagerUser sessionManagerUser;

    private Product prod;
    private String listLike;
    private Boolean isLiked = false;
    private CharSequence title;

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private DatabaseQuery mDbQuery;

    private TextView prodName;
    private TextView prodBrand;
    private TextView prodShortDesc;
    private ImageView prodPicture;

    private Button geoloc;
    private FloatingActionButton menu;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        tabLayout = (TabLayout) findViewById(R.id.tabsproduct);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageBlurred = (ImageView) findViewById(R.id.imageproductblurred);
        imageProduct = (ImageView) findViewById(R.id.imageproduct);
        prod = getIntent().getExtras().getParcelable("prod");

        collapsingToolbarLayout.setTitle(prod.getName());
        Picasso.with(getApplicationContext()).load("http://163.5.84.202/Symfony/web/images/Product/" + prod.getPicture()).transform(new BlurImages(getApplicationContext(), 25)).into(imageBlurred);
        Picasso.with(getApplicationContext()).load("http://163.5.84.202/Symfony/web/images/Product/" + prod.getPicture()).transform(new CircleTransform()).into(imageProduct);
        CharSequence Tab[] = {"Info.", "Prix", "Avis"};
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ProductNewViewPagerAdapter(getSupportFragmentManager(), Tab, 3, prod, this);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        menu = (FloatingActionButton) findViewById(R.id.fab);
        menu.setOnClickListener(this);
        context = getApplicationContext();
        activity = this;
        sessionManagerUser = new SessionManagerUser(context);

        mDbHelper = new DatabaseHelper(context);
        mDbQuery = new DatabaseQuery(mDbHelper);

        final String[] option = new String[] { "J'aime", "Ajouter à une liste", "Partager", "Rafraichir" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quel action voulez-vous faire ?");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Like
                                if (!sessionManagerUser.isLogged()) {
                                    AlertDialog.Builder builderl = new  AlertDialog.Builder(context);
                                    builderl.setTitle("Vous aimez ce produit?").setMessage("Veuillez-vous connecter d'abord.")
                                            .setPositiveButton(context.getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(context, LoginActivity.class);
                                                    startActivity(intent);
                                                    dialog.dismiss();
                                                }
                                            }).setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).create().show();
                                } else {
                                    listLike = context.getString(R.string.liked_product);
                                    if (isLiked == true)
                                        new SendLike(0).execute();
                                    else
                                        new SendLike(1).execute();
                                }
                                break;
                            case 1: // Add to list
                                break;
                            case 2: // Share
                                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Intent.ACTION_SEND);

                                i.putExtra(Intent.EXTRA_TEXT, "Venez voir le produit : " + prod.getName() + " sur UpReal");
                                i.setType("text/plain");
                                try {
                                    startActivity(Intent.createChooser(i, "Partager ce produit avec ..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(context, context.getString(R.string.need_mail_app)
                                            , Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 3: // Refresh
                                new Refresh(activity, 2, prod.getId()).execute();
                                break;
                            default:
                                break;
                        }
                    }
                }
        );
        dialog = builder.create();
//        prodName = (TextView) findViewById(R.id.product_name);
//        prodBrand = (TextView) findViewById(R.id.product_brand);
//        prodShortDesc = (TextView) findViewById(R.id.product_desc);
//        prodPicture = (ImageView) findViewById(R.id.product_picture);
//
//        geoloc = (Button) findViewById(R.id.geoloc);
//        geoloc.setOnClickListener(this);
//
//        prod = getIntent().getExtras().getParcelable("listprod");
//
//
//        title = new String(prod.getName());
//        prodName.setText(prod.getName());
//        Picasso.with(getApplicationContext()).load("http://163.5.84.202/Symfony/web/images/Product/" + prod.getPicture()).into(prodPicture);
//        Picasso.with(getApplicationContext()).load("http://163.5.84.202/Symfony/web/images/Product/" + prod.getPicture()).into(prodPicture);
//
//        prodBrand.setText(prod.getBrand());
//
//        CharSequence Tab[] = {getString(R.string.commentary), getString(R.string.social), getString(R.string.options)};
//        toolbar = (Toolbar) findViewById(R.id.app_bar);
//        toolbar.setTitle(title);
//        setSupportActionBar(toolbar);
//
//        mViewPager = (ViewPager) findViewById(R.id.viewpager);
//        adapter = new ProductViewPagerAdapter(getSupportFragmentManager(), Tab, 3, prod);
//         mViewPager.setAdapter(adapter);
//
//        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab);
//        mSlidingTabLayout.setDistributeEvenly(true);
//        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
//          @Override
//          public int getIndicatorColor(int position) {
//              return getResources().getColor(R.color.ColorPrimaryDark);
//          }
//        });
//        mSlidingTabLayout.setViewPager(mViewPager);

/*        new RetrievePicture().execute(); */
//        new RetrieveDesc().execute();
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
            case R.id.fab:
                dialog.show();
                break;
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

    private class isProductLiked extends AsyncTask<Void, Void, Boolean> {
        Boolean isLike = false;

        @Override
        protected Boolean doInBackground(Void... params) {
            SoapUserUtilManager uum = new SoapUserUtilManager();
            isLike = uum.isProductLiked(Integer.toString(sessionManagerUser.getUserId()), Integer.toString(prod.getId()));
            return isLike;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isLiked = aBoolean;
        }
    }

    private class SendLike extends AsyncTask<Void, Void, Boolean> {
        Boolean isSuccess = false;
        Boolean isLike = false;
        private int type;

        public SendLike(int type) {
            this.type = type;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SoapUserUtilManager uum = new SoapUserUtilManager();
            SoapProductUtilManager pum = new SoapProductUtilManager();
            isLike = uum.isProductLiked(Integer.toString(sessionManagerUser.getUserId()), Integer.toString(prod.getId()));
            if (!isLike) {
                isSuccess = pum.rateProduct(sessionManagerUser.getUserId(), prod.getId(), 1);
                return isSuccess;
            } else if (isLike) {
                isSuccess = pum.rateProduct(sessionManagerUser.getUserId(), prod.getId(), -1);
                if (isSuccess == true)
                    return false;
                return isSuccess;
                    /*
                    isSuccess = pum.rateUser(sessionManagerUser.getUserId(), )
*/
            }
            return isSuccess;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if(b == true && sessionManagerUser.isLogged()){
                isLiked = true;
                mDatabase = mDbHelper.openDataBase();
                String getListId[][] = mDbQuery.QueryGetElements("lists", new String[]{"id", "public", "nb_items", "id_user", "name"}, "name=? AND type=?", new String[]{listLike, "3"}, null, null, null);
                String getProductElement[] = mDbQuery.QueryGetElement("product", new String[]{"name", "ean", "brand", "picture", "product_id"}, "product_id=?", new String[]{Integer.toString(prod.getId())}, null, null, null);
                if (getProductElement[0] == null) {
                    if (prod.getPicture() == null)
                        prod.setPicture("");
                    mDbQuery.InsertData("product", new String[]{"name", "ean", "picture", "brand", "product_id"}, new String[]{prod.getName(), prod.getEan(), prod.getPicture(), prod.getBrand(), Integer.toString(prod.getId())});
                }
                String getITEMS[] = mDbQuery.QueryGetElement("items", new String[]{"id_list", "id_product", "id_user"}, "id_product=? AND id_list=?", new String[]{Integer.toString(prod.getId()), getListId[0][0]}, null, null, null);
                if (getITEMS[0] == null)
                    mDbQuery.InsertData("items", new String[]{"id_list", "id_product", "id_user"}, new String[]{getListId[0][0], Integer.toString(prod.getId()), Integer.toString(sessionManagerUser.getUserId())});
            } else if (b == false) {
                isLiked = false;
                Log.v("HEOUAIS", "DISLIKE PRODUCT");
                mDatabase = mDbHelper.openDataBase();
                String getListId[][] = mDbQuery.QueryGetElements("lists", new String[]{"id", "public", "nb_items", "id_user", "name"}, "name=? AND type=?", new String[]{listLike, "3"}, null, null, null);
                String getProductElement[] = mDbQuery.QueryGetElement("product", new String[]{"name", "ean", "brand", "picture", "product_id"}, "product_id=?", new String[]{Integer.toString(prod.getId())}, null, null, null);
                mDbQuery.DeleteData("items", "id_list=? AND id_product=? AND id_user=?", new String[]{getListId[0][0], Integer.toString(prod.getId()), Integer.toString(sessionManagerUser.getUserId())});
            }
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}