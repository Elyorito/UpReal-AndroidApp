package com.upreal.server;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Kyosukke on 09/10/2015.
 */
public class ProductUtilManager extends SoapManager {

    public ProductUtilManager() {
        super("ProductUtilManager");
    }

    public List<String> getCategory() {
        List<String> listCategory = new ArrayList<String>();
        String methodname = "getCategory";
        SoapObject request = new SoapObject(NAMESPACE, methodname);

        try {
            Object res = callService(methodname, request);
            if (res instanceof Vector) {
                Vector<SoapPrimitive> results = (Vector<SoapPrimitive>) res;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapPrimitive p = results.get(i);
                    listCategory.add(p.toString());
                }
            } else if (res instanceof SoapPrimitive) {
                SoapPrimitive p = (SoapPrimitive) res;
                listCategory.add(p.toString());
            }
            return listCategory;
        } catch (Exception q) {
            q.printStackTrace();
        }

        return null;
    }
}
