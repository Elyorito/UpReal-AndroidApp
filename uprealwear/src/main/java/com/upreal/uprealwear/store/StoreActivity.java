package com.upreal.uprealwear.store;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.upreal.uprealwear.R;
import com.upreal.uprealwear.utils.Item;

/**
 * Created by Kyosukke on 08/08/2015.
 */
public class StoreActivity extends Activity implements View.OnClickListener {

    private int id;
    private TextView name;
    private ImageView image;
    private ImageButton comment;
    private ImageButton options;
    private ImageButton social;

    @Override
    protected void onCreate(Bundle savecInstanceState) {
        super.onCreate(savecInstanceState);
        setContentView(R.layout.util_details);

        name = (TextView) findViewById(R.id.name);
        image = (ImageView) findViewById(R.id.image);
        comment = (ImageButton) findViewById(R.id.comment);
        options = (ImageButton) findViewById(R.id.options);
        social = (ImageButton) findViewById(R.id.social);

        Item item = getIntent().getExtras().getParcelable("item");

        id = item.getId();
        name.setText(item.getName());
        if (item.getImagePath() != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            image.setImageBitmap(BitmapFactory.decodeFile(item.getImagePath(), options));
        }
        else
            image.setImageDrawable(getResources().getDrawable(R.drawable.picture_unavailable));

        comment.setOnClickListener(this);
        options.setOnClickListener(this);
        social.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment:
                Log.e("StoreActivity", "Comment clicked.");
                break ;
            case R.id.options:
                Log.e("StoreActivity", "Options clicked.");
                break ;
            case R.id.social:
                Log.e("StoreActivity", "Social clicked.");
/*                Intent intent = new Intent(this, SearchResultActivity.class);
                startActivity(intent);*/
                break ;
            default:
                Log.e("StoreActivity", "DEFAULT");
                break ;
        }
    }
}
