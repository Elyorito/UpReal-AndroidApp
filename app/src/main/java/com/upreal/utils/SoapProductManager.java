package com.upreal.utils;

import android.util.Base64;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Elyo on 16/03/2015.
 */
public class SoapProductManager extends SoapManager {

    public SoapProductManager() {
        super("ProductManager");
    }

/*
    private List<Product> getRequestToProduct(SoapObject result, int nbProd) {

        Product prod;
        List<Product> prodList = new ArrayList<Product>();
        Iterator it = prodList.iterator();
        for (int i = 0; nbProd > i; i++) {
            result.getProperty(i).toString();
        }
        return ;
    }
*/

    public int registerProduct(String name, String brand, String desc, String barcode, String noticedPrice, String shop) {
        int id = 0;
        String methodname = "registerProduct";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("name", name);
        request.addProperty("ean", barcode);
        request.addProperty("brand", brand);
        /*request.addProperty("picture", );*/

        try {
            SoapPrimitive results= (SoapPrimitive) callService(methodname, request);
            id = Integer.parseInt(results.toString());
/*
            Vector<SoapObject> results = (Vector<SoapObject>) envelope.getResponse();
*/
        } catch (Exception q) {
            q.printStackTrace();
        }
        return id;
    }

    public Boolean updateProduct(Product product) {
        Boolean result = false;
        String methodname = "updateProduct";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", product.getId());
        request.addProperty("name", product.getName());
        request.addProperty("ean", product.getEan());
        request.addProperty("brand", product.getBrand());

        try {
            SoapPrimitive results= (SoapPrimitive) callService(methodname, request);
            result = Boolean.valueOf(results.toString());
        } catch (Exception q) {
            q.printStackTrace();
        }
        return result;
    }

    public Product getProductInfo(int id) {

        String methodname = "getProductInfo";
        Product prod = null;
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);

        try {
            SoapObject results= (SoapObject) callService(methodname, request);
/*
            Vector<SoapObject> results = (Vector<SoapObject>) envelope.getResponse();
*/
            if (results == null)
                return null;
            prod = ConverterManager.convertToProduct(results);
/*
            nbProduct = results.getAttributeCount();
*/
/*
            data = results.getProperty("Product").toString();
*/
/*
            for (SoapObject res : results)
                listprod.add(this.convertToQuery(res, data));
*/

/*
            if (results instanceof SoapObject) {
                data = results.getProperty("ean").toString();
            }
*/
        } catch (Exception q) {
            q.printStackTrace();
        }
        return prod;
    }


    public Product getProductbyEAN(String ean) {
        String methodname = "getProductByEAN";
        Product prod = null;
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("ean", ean);

        try {
            SoapObject results= (SoapObject) callService(methodname, request);
/*
            Vector<SoapObject> results = (Vector<SoapObject>) envelope.getResponse();
*/
            if (results == null)
                return null;

            prod = ConverterManager.convertToProduct(results);

/*
            nbProduct = results.getAttributeCount();
*/
/*
            data = results.getProperty("Product").toString();
*/
/*
            for (SoapObject res : results)
                listprod.add(this.convertToQuery(res, data));
*/

/*
            if (results instanceof SoapObject) {
                data = results.getProperty("ean").toString();
            }
*/
        } catch (Exception q) {
            q.printStackTrace();
        }
        return prod;
    }

    public List<Product> getProduct(String searchName) {
        List<Product> listProds = new ArrayList<>();
        String methodname = "getProduct";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("keyword", searchName);

        try {
            Object response = callService(methodname, request);
/*
            nbProduct = results.getAttributeCount();
*/
/*
            data = results.getProperty("Product").toString();

*/            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    listProds.add(ConverterManager.convertToProduct(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listProds.add(ConverterManager.convertToProduct(result));
            }
/*
            if (results instanceof SoapObject) {
                data = results.getProperty("ean").toString();
            }
*/
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listProds;
    }

    public Product scanProduct(byte[] imageBytes) {
/*        Log.v("Test:", "coucou");
        for (byte b : imageBytes) {
            Log.v("Test:", "b: " + b);
        }*/

        String methodname = "scanProduct";
        Product prod = new Product();
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        //request.addProperty("imageBytes", imageBytes);
        request.addProperty("image", Base64.encodeToString(imageBytes, Base64.DEFAULT));

        try {
            SoapObject results = (SoapObject) callService(methodname, request);

            if (results == null)
                return null;
            prod = ConverterManager.convertToProduct(results);
        } catch (Exception q) {
            q.printStackTrace();
        }
        return prod;
    }

    public String getPicture(int id, int type) {
        String methodname;
        switch (type) {
            case 0:
                methodname = "getProductPicture";
                break ;
            case 1:
                methodname = "getUserPicture";
                break ;
            case 2:
                methodname = "getStorePicture";
                break ;
            case 3:
                methodname = "getCompanyPicture";
                break ;
            default:
                methodname = "getProductPicture";
                break ;
        }

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);

        try {

            SoapPrimitive results = (SoapPrimitive) callService(methodname, request);
            if (results == null)
                return null;

            final File dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath());
            final File file = new File(dir, type + "-" + id + ".jpg");
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            out.write(Base64.decode(results.toString(), Base64.DEFAULT));


        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + type + "-" + id + ".jpg";
    }
}
