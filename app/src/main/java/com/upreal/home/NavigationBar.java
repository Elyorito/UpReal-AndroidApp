package com.upreal.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.upreal.R;
import com.upreal.list.ListActivity;
import com.upreal.login.LoginActivity;
import com.upreal.scan.CameraActivity;
import com.upreal.scan.GetProductActivity;
import com.upreal.user.UserActivity;
import com.upreal.utils.DividerItemDecoration;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.database.DatabaseHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kyosukke on 13/11/2015.
 */
public class NavigationBar extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    Activity activity;
    Toolbar toolbar;
    final GestureDetector mGestureDetector;
    private SessionManagerUser sessionManagerUser;

    private String ACCOUNT[];
    private String ITEM_WACCOUNT[];

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    private Intent intent;

    // Camera
    private static final int ACTIVITY_START_CAMERA = 0;
    private String mImageFileLocation = "";

    // Storage
    private static final int PERMISSIONS_REQUEST = 1;
    private File photoFile = null;

    public NavigationBar(Activity activity) {

        toolbar = (Toolbar) activity.findViewById(R.id.app_bar);
        mRecyclerView = (RecyclerView) activity.findViewById(R.id.RecyclerView_NavigationDrawer);
        mDrawer = (DrawerLayout) activity.findViewById(R.id.DrawerLayout);

        mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawer, toolbar,  R.string.connexion/*"OpenDr"*/, R.string.connexion/*"CloseDr"*/) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /*
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.ColorTabs));
        toolbar.setTitleTextColor(activity.getResources().getColor(R.color.ColorTitle));
        */

        this.activity = activity;
        sessionManagerUser = new SessionManagerUser(activity.getApplicationContext());

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(activity, null));
        mRecyclerView.setHasFixedSize(true);

        if (sessionManagerUser.isLogged()) {
            ITEM_WACCOUNT = new String[]{activity.getString(R.string.news),
                    activity.getString(R.string.scan),
                    activity.getString(R.string.list),
                    activity.getString(R.string.settings),
                    activity.getString(R.string.deconnexion)};
            mAdapter = new AdapterNavDrawerHome(sessionManagerUser.getRegisterLoginUser()[0], ITEM_WACCOUNT, activity.getApplicationContext(), sessionManagerUser.getUser());
        }
        else {
            ITEM_WACCOUNT = new String[]{activity.getString(R.string.news),
                    activity.getString(R.string.scan),
                    activity.getString(R.string.settings)};
            ACCOUNT = new String[]{activity.getString(R.string.connexion)};
            mAdapter = new AdapterNavDrawerConnectHome(ACCOUNT, ITEM_WACCOUNT);
        }

        mRecyclerView.addOnItemTouchListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mGestureDetector = new GestureDetector(activity, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());

        if (child != null && mGestureDetector.onTouchEvent(e)) {
            mDrawer.closeDrawers();
            //                Toast.makeText(HomeActivity.this, "Item :" + rv.getChildPosition(child), Toast.LENGTH_SHORT).show();
            if (sessionManagerUser.isLogged())
                switch (rv.getChildAdapterPosition(child)) {
                case 0://Connect
                    intent = new Intent(rv.getContext(), UserActivity.class);
                    rv.getContext().startActivity(intent);
                    return true;
                case 1://Home
                    intent = new Intent(rv.getContext(), HomeActivity.class);
                    rv.getContext().startActivity(intent);
                    activity.finish();
                    return true;
                case 2://Scan
                    startScan(rv.getContext());
                    return true;
                case 3://List
                    intent = new Intent(rv.getContext(), ListActivity.class);
                    rv.getContext().startActivity(intent);
                    return true;
                case 4://Settings
                    intent = new Intent(rv.getContext(), ParameterActivity.class);
                    rv.getContext().startActivity(intent);
                    return true;
                case 5://Disconnect
                    sessionManagerUser.deleteCurrentUser();
                    new DatabaseHelper(activity).deleteDataBase();
                    intent = new Intent(rv.getContext(), HomeActivity.class);
                    rv.getContext().startActivity(intent);
                    activity.finish();
                    return true;
                default:
                    return false;
            }
            else {
                switch (rv.getChildAdapterPosition(child)) {
                    case 0://Connect
                        intent = new Intent(rv.getContext(), LoginActivity.class);
                        rv.getContext().startActivity(intent);
                        return true;
                    case 1://Home
                        intent = new Intent(rv.getContext(), HomeActivity.class);
                        rv.getContext().startActivity(intent);
                        activity.finish();
                        return true;
                    case 2://Scan
                        startScan(rv.getContext());
                        return true;
                    case 3://Parameter
                        intent = new Intent(rv.getContext(), ParameterActivity.class);
                        rv.getContext().startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        }

        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    private void startScan(Context context) {
        View scanview = activity.getLayoutInflater().inflate(R.layout.dialog_scan_choice, null);
        ImageButton butQRcode = (ImageButton) scanview.findViewById(R.id.but_qrcode);
        ImageButton butScan = (ImageButton) scanview.findViewById(R.id.but_scanir);
        butQRcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), CameraActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        butScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                if (ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    try {
                        photoFile = createImageFile();
                        mImageFileLocation = photoFile.getAbsolutePath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    activity.startActivityForResult(intent, ACTIVITY_START_CAMERA);
                } else {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.CAMERA)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        Toast.makeText(activity, R.string.permission_camera_storage, Toast.LENGTH_SHORT).show();
                    }
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            PERMISSIONS_REQUEST);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.

                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.scan).setMessage(R.string.choose_scan_mode).setView(scanview).create().show();
    }

    static public File createImageFile() throws IOException {
        String FOLDER_LOCATION = "Upreal Pictures";
        File imageFolder;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";

        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        imageFolder = new File(storageDirectory, FOLDER_LOCATION);
        if (!imageFolder.exists())
            imageFolder.mkdirs();

        File myImage = File.createTempFile(imageFileName, ".jpg", imageFolder);
        return myImage;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_START_CAMERA && resultCode == RESULT_OK) {
/*          Thumbnail
            Bundle extras = data.getExtras();
            Bitmap photoCapturedBitmap = (Bitmap) extras.get("data");
*/
            // Get Bitmap from File
/*
            Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoCapturedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
*/
            intent = new Intent(this, GetProductActivity.class);
//            intent.putExtra("bytes", byteArray);
            intent.putExtra("imageLocation", mImageFileLocation);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // storage-related task you need to do.

                    try {
                        photoFile = createImageFile();
                        mImageFileLocation = photoFile.getAbsolutePath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    activity.startActivityForResult(intent, ACTIVITY_START_CAMERA);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle(R.string.scan).setMessage(R.string.no_permission).create().show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
