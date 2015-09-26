package com.upreal.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.upreal.R;

/**
 * Created by Kyosukke on 16/08/2015.
 */
public class SearchActivity extends Activity implements View.OnClickListener {

    private ImageButton user;
    private ImageButton product;
    private ImageButton store;

    private EditText searchText;
    private ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        user = (ImageButton) findViewById(R.id.user);
        product = (ImageButton) findViewById(R.id.product);
        store = (ImageButton) findViewById(R.id.store);
        searchText = (EditText) findViewById(R.id.search_text);
        searchButton = (ImageButton) findViewById(R.id.search_button);

        user.setOnClickListener(this);
        product.setOnClickListener(this);
        store.setOnClickListener(this);
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user:
                Log.e("SearchActivity", "User clicked.");
                user.setAlpha((user.getAlpha() >= 1f) ? (.5f) : (1f));
                break ;
            case R.id.product:
                Log.e("SearchActivity", "Product clicked.");
                product.setAlpha((product.getAlpha() >= 1f) ? (.5f) : (1f));
                break ;
            case R.id.store:
                Log.e("SearchActivity", "Store clicked.");
                store.setAlpha((store.getAlpha() >= 1f) ? (.5f) : (1f));
                break ;
            case R.id.search_button:
                Log.e("SearchActivity", "Search button clicked.");
                Intent intent = new Intent(this, SearchResultActivity.class);
                intent.putExtra("search_text", searchText.getText().toString());
                intent.putExtra("user", (user.getAlpha() >= 1f) ? (true) : (false));
                intent.putExtra("product", (product.getAlpha() >= 1f) ? (true) : (false));
                intent.putExtra("store", (store.getAlpha() >= 1f) ? (true) : (false));
                startActivity(intent);
                break ;
            default:
                Log.e("SearchActivity", "DEFAULT");
                break ;
        }
    }
}
