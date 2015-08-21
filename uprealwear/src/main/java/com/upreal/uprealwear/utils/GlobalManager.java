package com.upreal.uprealwear.utils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by Kyosukke on 27/07/2015.
 */
public class GlobalManager extends SoapManager {

    public GlobalManager() {
        super("GlobalManager");
    }

    public void searchNews(String search) {
        String methodname = "searchNews";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("search", search);

        try {
            SoapObject result = (SoapObject) callService(methodname, request);

        } catch (Exception q) {
            q.printStackTrace();
        }
    }
}
