package product;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.upreal.upreal.R;

import view.SlidingTabLayout;

/**
 * Created by Elyo on 16/02/2015.
 */
public class ProductSearchActivity extends ActionBarActivity {

    private CharSequence Title[];


    private Toolbar toolbar;

    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private ProductViewPagerSearchAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_ProductSearch);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        //Test
//        sProduct = new String[]{"Coca", "Coca", "Coca", "Coca", "Coca", "Coca", "Coca"};
//        mAdapter = new ProductSearchAdapter(sProduct);
//        mRecyclerView.setAdapter(mAdapter);

        Title = new CharSequence[]{getString(R.string.product), getString(R.string.user)};
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ProductViewPagerSearchAdapter(getSupportFragmentManager(), Title, 2);
        mViewPager.setAdapter(adapter);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab);
        mSlidingTabLayout.setViewPager(mViewPager);
    }
}
