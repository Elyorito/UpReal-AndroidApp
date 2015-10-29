package com.upreal.utils;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Elyo on 01/03/2015.
 */
public class SoapUserManager extends SoapManager {

    public SoapUserManager() {
        super("UserManager");
    }

    public boolean updateAccount(int id, String firstName, String lastName, int phone, int id_address, String shortDesc) {
        String methodname = "updateAccount";
        boolean result = false;

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);
        request.addProperty("firstname", firstName);
        request.addProperty("lastname", lastName);
        request.addProperty("phone", phone);
        request.addProperty("id_address", id_address);
        request.addProperty("short_desc", shortDesc);

        try {
            SoapPrimitive results= (SoapPrimitive) callService(methodname, request);

            result = Boolean.getBoolean(results.toString());
        } catch (Exception q) {
            q.printStackTrace();
        }
        return result;
    }

    public Boolean updatePassword(int id, String old, String newP) {
        String methodname = "changePassword";
        boolean result = false;

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", id);
        request.addProperty("old_password", old);
        request.addProperty("new_password", newP);

        try {
            SoapPrimitive results= (SoapPrimitive) callService(methodname, request);

            result = Boolean.valueOf(results.toString());
        } catch (Exception q) {
            q.printStackTrace();
        }
        return result;
    }

    public int registerAccount(String id, String password, String email) {
        String data;
        String methodname = "registerAccount";
        int result = 0;

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("username", id);
        request.addProperty("email", email);
        request.addProperty("password", password);

        try {
            SoapPrimitive results= (SoapPrimitive) callService(methodname, request);

            data = results.toString();
            result = Integer.parseInt(data);
        } catch (Exception q) {
            q.printStackTrace();
        }
        return result;
    }

    public int registerAccount(String username, String firstname, String lastname, String password, String email) {
        String data;
        String methodname = "registerAccount";
        int result = 0;

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("username", username);
        request.addProperty("firstname", firstname);
        request.addProperty("lastname", lastname);
        request.addProperty("email", email);
        request.addProperty("password", password);

        try {
            SoapPrimitive results= (SoapPrimitive) callService(methodname, request);

            data = results.toString();
            result = Integer.parseInt(data);
        } catch (Exception q) {
            q.printStackTrace();
        }
        return result;
    }

    public Boolean connectAccount(String username, String password) {
        Boolean result = false;
        String data;
        String methodname = "connectAccount";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("username", username);
        request.addProperty("password", password);

        try {
            SoapPrimitive resultsBoolean= (SoapPrimitive) callService(methodname, request);

            data = resultsBoolean.toString();
            result = Boolean.valueOf(data);
        } catch (Exception q) {
            q.printStackTrace();
        }
        return result;
    }

    public Boolean isUsernameTaken(String username) {
        boolean result = true;
        String data = null;
        String methodname = "isUsernameTaken";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("username", username);

        try {
            SoapPrimitive resultsString = (SoapPrimitive) callService(methodname, request);

            data = resultsString.toString();
            result = Boolean.parseBoolean(data);
        } catch (Exception q) {
            q.printStackTrace();
        }
        return result;
    }

    public User getAccountInfoUsername(int id) {
        User user = new User();

        String methodname = "getAccountInfo";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);

        try {
            SoapObject res2 = (SoapObject) callService(methodname, request);

            user.setId(Integer.parseInt(res2.getPropertyAsString("id")));
            user.setUsername(res2.getPropertyAsString("username"));
        } catch (Exception q) {
            q.printStackTrace();
        }
        return user;
    }

    public User getUserByUsername(String username) {
        User user = null;
        String methodname = "getSingleUserByUsername";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("username", username);

        try {
            SoapObject res2 = (SoapObject) callService(methodname, request);

            user = ConverterManager.convertToUser(res2);
        } catch (Exception q) {
            q.printStackTrace();
        }
        return user;
    }

    public List<User> getListUser(String searchName) {

        List<User> listUsers = new ArrayList<>();
        String methodname = "getUserByUsername";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("username", searchName);

        try {
            Object response = callService(methodname, request);

            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    listUsers.add(ConverterManager.convertToUser(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listUsers.add(ConverterManager.convertToUser(result));
            }
/*
            nbProduct = results.getAttributeCount();
*/
/*
            data = results.getProperty("Product").toString();
                */
/*
                for (SoapObject res : results) {
                    listUsers.add(this.convertToQuery(res, data));
            }
*/
/*
            if (results instanceof SoapObject) {
                data = results.getProperty("ean").toString();
            }
*/
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listUsers;
    }
}
