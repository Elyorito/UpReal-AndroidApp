package product;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.upreal.upreal.R;

import view.SlidingTabLayout;


/**
 * Created by Elyo on 11/02/2015.
 */
public class ProductActivity extends ActionBarActivity {

    CharSequence Tiab[] = {"Tab1", "Tab2"};
    private Toolbar toolbar;

    private ViewPager mViewPager;
    private ProductViewPagerAdapter adapter;
    private SlidingTabLayout mSlidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ProductViewPagerAdapter(getSupportFragmentManager(), Tiab, 2);
        mViewPager.setAdapter(adapter);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.ColorTabs);
            }

            @Override
            public int getDividerColor(int position) {
                return 0;
            }
        });

        mSlidingTabLayout.setViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }

}
