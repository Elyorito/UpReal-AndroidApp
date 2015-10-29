package com.upreal.utils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Elyo on 16/03/2015.
 */
public class SoapStoreManager extends SoapManager {

    public SoapStoreManager() {
        super("StoreManager");
    }

    public Store getStoreByAddress(int id_address) {
        String methodname = "getStoreByAddress";
        Store store = null;
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id_address);

        try {
            SoapObject results= (SoapObject) callService(methodname, request);

            if (results == null)
                return null;

            store = ConverterManager.convertToStore(results);
        } catch (Exception q) {
            q.printStackTrace();
        }
        return store;
    }

    public Address getAddressByStore(int id) {
        String methodname = "getAddressByStore";
        Address address = null;
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);

        try {
            SoapObject results= (SoapObject) callService(methodname, request);

            if (results == null)
                return null;

            address = ConverterManager.convertToAddress(results);
        } catch (Exception q) {
            q.printStackTrace();
        }
        return address;
    }

    public List<Store> getListStore(String searchName) {

        List<Store> listStore = new ArrayList<>();
        String methodname = "getStoreByName";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("keyword", searchName);

        try {
            Object response = callService(methodname, request);

            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    listStore.add(ConverterManager.convertToStore(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listStore.add(ConverterManager.convertToStore(result));
            }
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listStore;
    }
}
