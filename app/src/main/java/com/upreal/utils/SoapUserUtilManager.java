package com.upreal.utils;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Elyo on 07/04/2015.
 */
public class SoapUserUtilManager extends SoapManager {

    public SoapUserUtilManager() {
        super("UserUtilManager");
    }

    public Boolean isProductLiked(String id_user, String id_product) {
        String methodname = "isProductLiked";
        boolean result = false;

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", id_user);
        request.addProperty("id_product", id_product);

        try {
            SoapPrimitive results= (SoapPrimitive) callService(methodname, request);

            result = Boolean.parseBoolean(results.toString());
        } catch (Exception q) {
            q.printStackTrace();
        }
        return result;
    }

    public Address getAddressInfo(int id) {
        String methodname = "getAddressInfo";
        Address address = new Address();

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);

        try {
            SoapObject result= (SoapObject) callService(methodname, request);
            address = ConverterManager.convertToAddress(result);
        } catch (Exception q) {
            q.printStackTrace();
        }
        return address;
    }

    public boolean updateAddress(Address address) {
        String methodname = "updateAddress";
        boolean result = false;

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", address.getId());
        request.addProperty("address", address.getAddress());
        request.addProperty("address_2", address.getAddress2());
        request.addProperty("city", address.getCity());
        request.addProperty("country", address.getCountry());
        request.addProperty("postal_code", address.getPostalCode());

        try {
            SoapPrimitive results= (SoapPrimitive) callService(methodname, request);

            result = Boolean.getBoolean(results.toString());
        } catch (Exception q) {
            q.printStackTrace();
        }
        return result;
    }

    public int registerAddress(Address address) {
        String methodname = "registerAddress";
        int result = -1;

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("address", (address.getAddress() == null) ? "" : address.getAddress());
        request.addProperty("address_2", (address.getAddress2() == null) ? "" : address.getAddress2());
        request.addProperty("city", (address.getCity() == null) ? "" : address.getCity());
        request.addProperty("country", (address.getCountry() == null) ? "" : address.getCountry());
        request.addProperty("postal_code", address.getPostalCode());

        try {
            SoapPrimitive results= (SoapPrimitive) callService(methodname, request);

            result = Integer.parseInt(results.toString());
        } catch (Exception q) {
            q.printStackTrace();
        }
        return result;
    }

    public void createUserComment(int id_user, int id_target, String commentary) {
        String methodname = "createUserComment";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", id_user);
        request.addProperty("id_target", id_target);
        request.addProperty("commentary", commentary);

        try {
            SoapPrimitive results= (SoapPrimitive) callService(methodname, request);

            results.toString();
        } catch (Exception q) {
            q.printStackTrace();
        }
    }

    public List<History> getUserHistory(int idUser) {
        List<History> listHistory = new ArrayList<History>();
        String methodname = "getUserHistory";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", idUser);

        try {
            Object res = callService(methodname, request);
            if (res instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) res;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject o = results.get(i);
                    listHistory.add(ConverterManager.convertToHistory(o));
                }
            } else if (res instanceof SoapObject) {
                SoapObject o = (SoapObject) res;
                listHistory.add(ConverterManager.convertToHistory(o));
            }
            return listHistory;
        } catch (Exception q) {
            q.printStackTrace();
        }

        return null;
    }

    public int createHistory(int idUser, int actionType, int idType, int idTarget) {
        String methodname = "createHistory";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", idUser);
        request.addProperty("action_type", actionType);
        request.addProperty("id_type", idType);
        request.addProperty("id_target", idTarget);

        try {
            return Integer.parseInt(callService(methodname, request).toString());
        } catch (Exception q) {
            q.printStackTrace();
        }
        return 0;
    }

    public void followUser(int idUser, int idFollower) {
        String methodname = "followUser";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", idUser);
        request.addProperty("id_follower", idFollower);

        try {
            callService(methodname, request);
        } catch (Exception q) {
            q.printStackTrace();
        }
    }
}
