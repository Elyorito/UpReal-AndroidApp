package com.upreal.upreal.scan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.upreal.upreal.R;
import com.upreal.upreal.product.ProductActivity;
import com.upreal.upreal.utils.Product;
import com.upreal.upreal.utils.SoapGlobalManager;
import com.upreal.upreal.utils.SoapProductManager;
import com.upreal.upreal.utils.SoapProductUtilManager;

/**
 * Created by Elyo on 24/04/2015.
 */
public class AddProductFromScan extends Activity implements View.OnClickListener{

    private static final int GET_IMAGE_REQUEST = 1;

    private EditText productName;
    private EditText brand;
    private EditText desc;
    private EditText barcode;
    private EditText noticedPrice;
    private EditText shopNearby;
    private ImageView imageProduct;

    private String barcodeEAN;
    private Bitmap bitmap;
    private byte[] image;



    private AlertDialog.Builder builder;
    private Button cancel;
    private Button sendProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_add_product);

        bitmap = null;
        image = null;

        builder = new AlertDialog.Builder(AddProductFromScan.this);
        barcodeEAN = getIntent().getExtras().getString("ean");
        productName = (EditText) findViewById(R.id.product_name);
        brand = (EditText) findViewById(R.id.brand);
        desc = (EditText) findViewById(R.id.description);
        barcode = (EditText) findViewById(R.id.barcode);
        noticedPrice = (EditText) findViewById(R.id.noticedprice);
        shopNearby = (EditText) findViewById(R.id.attheshop);
        imageProduct = (ImageView) findViewById(R.id.image_product);

        cancel = (Button) findViewById(R.id.addprodcancel);
        sendProduct = (Button) findViewById(R.id.addprodok);

        cancel.setOnClickListener(this);
        sendProduct.setOnClickListener(this);
        if (barcodeEAN != null)
            barcode.setText(barcodeEAN.toString());
        imageProduct.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addprodcancel:
                finish();
                break;
            case R.id.image_product:
                Intent intent = new Intent(v.getContext(), Camera2Activity.class);
                intent.putExtra("type", "addImage");
                startActivityForResult(intent, GET_IMAGE_REQUEST);
                break;
            case R.id.addprodok:
                new CreateProductFromScan().execute();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_IMAGE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                image = data.getByteArrayExtra("result");
                bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                if (bitmap != null)
                imageProduct.setImageBitmap(bitmap);
            }
        }
    }

    public class CreateProductFromScan extends AsyncTask<Void, Void, Integer> {
        Product product;
        int id;

        @Override
        protected Integer doInBackground(Void... params) {
            SoapProductManager pm = new SoapProductManager();
            id = pm.registerProduct(productName.getText().toString(), brand.getText().toString(), desc.getText().toString(), barcodeEAN, noticedPrice.getText().toString(), shopNearby.getText().toString());
            product = pm.getProductInfo(id);
            return id;
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);

            new CreateSpec().execute(i);
            if (bitmap != null)
                new SendImage().execute(i);
            builder.setTitle("Produit Ajouté")
                    .setMessage("Votre produit a été ajouté avec succès !" +
                            "Voulez-vous y accéder?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                            intent.putExtra("listprod", product);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create().show();
        }
    }

    public class CreateSpec extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            SoapProductUtilManager pm = new SoapProductUtilManager();
            pm.createSpecification(params[0], "Description", desc.getText().toString());
            return null;
        }
    }

    public class SendImage extends AsyncTask<Integer, Void, Void> {
        String name;

        @Override
        protected Void doInBackground(Integer... params) {
            SoapGlobalManager gm = new SoapGlobalManager();
            name = "2_" + params[0] + ".jpg";
            gm.uploadPicture(image, name);
            return null;
        }
    }
}
