package com.upreal.uprealwear.server;

import android.util.Base64;

import com.upreal.uprealwear.utils.ConverterManager;
import com.upreal.uprealwear.utils.Product;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Kyosukke on 27/07/2015.
 */
public class ProductManager extends SoapManager {

    public ProductManager() {
        super("ProductManager");
    }

    public List<Product> getProduct(String search) {
        List<Product> listProduct = new ArrayList<>();
        String methodname = "getProduct";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("keyword", search);
        try {
//            SoapObject res0 = (SoapObject) envelope.bodyIn;

            Object res = callService(methodname, request);
            if (res instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) res;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject o = results.get(i);
                    listProduct.add(ConverterManager.convertToProduct(o));
                }
            } else if (res instanceof SoapObject) {
                SoapObject o = (SoapObject) res;
                listProduct.add(ConverterManager.convertToProduct(o));
            }
            return listProduct;
        } catch (Exception q) {
            q.printStackTrace();
        }

        return null;
    }

    public String getProductPicture(int id) {
        final File dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath());
        final File file = new File(dir, "0-" + id + ".jpg"); // 0 = ID Product

        String methodname = "getProductPicture";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);

        // If the file don't exist, we retrieve it
        if (!file.exists()) {
            try {
                SoapPrimitive res = (SoapPrimitive) callService(methodname, request);
                if (res != null) {
                    OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                    out.write(Base64.decode(res.toString(), Base64.DEFAULT));
                }
            } catch (Exception q) {
                q.printStackTrace();
            }
        }
        return android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "0-" + id + ".jpg";
    }
}