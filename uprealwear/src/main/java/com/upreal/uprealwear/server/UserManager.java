package com.upreal.uprealwear.server;

import android.util.Log;

import com.upreal.uprealwear.utils.ConverterManager;
import com.upreal.uprealwear.utils.User;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Kyosukke on 18/07/2015.
 */
public class UserManager extends SoapManager {

    public UserManager() {
        super("UserManager");
    }

    public Boolean connectAccount(String username, String password) {
        Boolean res;
        String methodname = "connectAccount";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("username", username);
        request.addProperty("password", password);

        try {
            res = Boolean.valueOf(((SoapPrimitive) callService(methodname, request)).toString());
            Log.e(NAMESPACE, "'connectAccount': " + res);
            return res;
        } catch (Exception q) {
            q.printStackTrace();
        }
        return false;
    }

    public List<User> getUserByUsername(String search) {
        List<User> listProduct = new ArrayList<>();
        String methodname = "getUserByUsername";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("username", search);
        try {
//            SoapObject res0 = (SoapObject) envelope.bodyIn;

            Object res = callService(methodname, request);
            if (res instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) res;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject o = results.get(i);
                    listProduct.add(ConverterManager.convertToUser(o));
                }
            } else if (res instanceof SoapObject) {
                SoapObject o = (SoapObject) res;
                listProduct.add(ConverterManager.convertToUser(o));
            }
            return listProduct;
        } catch (Exception q) {
            q.printStackTrace();
        }

        return null;
    }
}
