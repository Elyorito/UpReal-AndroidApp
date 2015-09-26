package com.upreal.server;

import com.upreal.utils.ConverterManager;
import com.upreal.utils.Product;

import org.ksoap2.serialization.SoapObject;

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

    public Product getProductInfo(int id) {
        String methodname = "getProductInfo";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);

        try {
            Object res = callService(methodname, request);
            SoapObject o = (SoapObject) res;
            Product p = ConverterManager.convertToProduct(o);

            return p;
        } catch (Exception q) {
            q.printStackTrace();
        }

        return null;
    }
}