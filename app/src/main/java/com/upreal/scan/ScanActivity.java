package com.upreal.scan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.upreal.R;
import com.upreal.product.ProductActivity;
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.Product;
import com.upreal.utils.SoapProductManager;

public class ScanActivity extends Activity {

    private Button scanner;
    private TextView formatTxt, contentTxt;
    private AlertDialog.Builder builder;
    private Intent intent;
    private ConnectionDetector cd;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_camera);


        /*scanner = (Button) findViewById(R.id.scanner);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        //scanner.setOnClickListener(this);*/
        builder = new AlertDialog.Builder(ScanActivity.this);
        cd = new ConnectionDetector(getApplicationContext());
        intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "SCAN_MODE");
        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.d("xZing", "contents: "+contents+" format: "+format); // Handle successful scan
                //formatTxt.setText("FORMAT: " + format);
                //contentTxt.setText(contents);
                if (cd.isConnectedToInternet() && format.toString().equals("QR_CODE")) {
                    Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(contents));
                    startActivity(in);
                    finish();
                }
                else if (cd.isConnectedToInternet()) {
                    new RetrieveScannedProduct(contents).execute();
                }
                else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.retry_retrieve_connection), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            else if(resultCode == RESULT_CANCELED){ // Handle cancel
                Toast toast = Toast.makeText(this, "Le scan à été annulé", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
                Log.i("xZing", "Cancelled");
                this.finish();
            }
        }
    }

/*
    public void onClick(View v) {
        if (v.getId() == R.id.scanner) {

            intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "SCAN_MODE");
*/
/*
            intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
*//*

            startActivityForResult(intent, 0);
        }
    }
*/

    public class RetrieveScannedProduct extends AsyncTask<String, Void, Product> {

        private Product prod = new Product();
        private String productEAN;

        public RetrieveScannedProduct(String productEAN) {
            this.productEAN = productEAN;
        }

        @Override
        protected Product doInBackground(String... params) {

            SoapProductManager pm = new SoapProductManager();
            prod = pm.getProductbyEAN(productEAN);
            return prod;
        }

        @Override
        protected void onPostExecute(Product prod) {
            super.onPostExecute(prod);
            if (prod == null) {
                builder.setTitle("Produit non trouvé !")
                        .setMessage("Voulez-vous ajouter ce produit dans notre base de données ?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), AddProductFromScan.class);
                                intent.putExtra("ean", productEAN);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
                builder.show();
                return;
            }
            Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
            intent.putExtra("prod", prod);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
            finish();
        }
    }
}