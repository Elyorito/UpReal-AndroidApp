package com.upreal.server;

import android.util.Base64;
import android.util.Log;

import com.upreal.utils.ConverterManager;
import com.upreal.utils.User;

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

    public User getAccountInfo(int id) {
        String methodname = "getAccountInfo";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);

        try {
            Object res = callService(methodname, request);
            SoapObject o = (SoapObject) res;
            User u = ConverterManager.convertToUser(o);

            return u;
        } catch (Exception q) {
            q.printStackTrace();
        }

        return null;
    }

    public List<User> getUserByUsername(String search) {
        List<User> listUser = new ArrayList<>();
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
                    listUser.add(ConverterManager.convertToUser(o));
                }
            } else if (res instanceof SoapObject) {
                SoapObject o = (SoapObject) res;
                listUser.add(ConverterManager.convertToUser(o));
            }
            return listUser;
        } catch (Exception q) {
            q.printStackTrace();
        }

        return null;
    }

    public String getUserPicture(int id) {
        final File dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath());
        final File file = new File(dir, "1-" + id + ".jpg"); // 1 = ID User

        String methodname = "getUserPicture";
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
                else
                    return null;
            } catch (Exception q) {
                q.printStackTrace();
            }
        }
        return android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "1-" + id + ".jpg";
    }
}
