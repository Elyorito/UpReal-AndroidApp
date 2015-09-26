package com.upreal.scan;

import android.app.Activity;
import android.os.Bundle;

import com.upreal.R;

/**
 * Created by Elyo on 17/05/2015.
 */
public class Camera2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2Fragment.newInstance(getIntent().getExtras()))
                    .commit();
        }
    }


}
