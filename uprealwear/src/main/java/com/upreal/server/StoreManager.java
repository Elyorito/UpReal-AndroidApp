package com.upreal.server;

import com.upreal.utils.ConverterManager;
import com.upreal.utils.Store;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Kyosukke on 16/08/2015.
 */
public class StoreManager extends SoapManager {

    public StoreManager() {
        super("StoreManager");
    }

    public List<Store> getStoreByName(String search) {
        List<Store> listStore = new ArrayList<>();
        String methodname = "getStoreByName";
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
                    listStore.add(ConverterManager.convertToStore(o));
                }
            } else if (res instanceof SoapObject) {
                SoapObject o = (SoapObject) res;
                listStore.add(ConverterManager.convertToStore(o));
            }
            return listStore;
        } catch (Exception q) {
            q.printStackTrace();
        }

        return null;
    }

    public Store getStoreInfo(int id) {
        String methodname = "getStoreInfo";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);

        try {
            Object res = callService(methodname, request);
            SoapObject o = (SoapObject) res;
            Store s = ConverterManager.convertToStore(o);

            return s;
        } catch (Exception q) {
            q.printStackTrace();
        }

        return null;
    }
}
