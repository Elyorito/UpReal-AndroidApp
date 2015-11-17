package com.upreal.utils;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

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

    public Integer registerStore(String name) {
        Integer id_store = -1;
        String methodName = "registerStore";

        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("name", name);
        try {
            SoapPrimitive response = (SoapPrimitive) callService(methodName, request);
            id_store = Integer.parseInt(response.toString());
        } catch (Exception q) {
            q.printStackTrace();
        }
        return id_store;
    }

    public List<List<String>> getProductByLocation(Double latitude, Double longitude, int radius, int idProduct) {
        List<String> listStoreProduct = new ArrayList<>();
        List<List<String>> listStoreReturn = new ArrayList<>();
        String methodname = "getProductByLocation";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("latitude", latitude.toString());
        request.addProperty("longitude", longitude.toString());
        request.addProperty("radius", radius);
        request.addProperty("id_product", idProduct);

        try {
            Object response = callService(methodname, request);

            Vector<SoapObject> res = (Vector<SoapObject>) response;

                for (SoapObject obj : res) {
                    String price = obj.getProperty(0).toString();
                    String shop = obj.getProperty(1).toString();
                    String address = obj.getProperty(2).toString();
                    String latitude_ = obj.getProperty(3).toString();
                    String longitude_ = obj.getProperty(4).toString();
                    String distance = obj.getProperty(5).toString();

                    listStoreProduct.add(price);
                    listStoreProduct.add(shop);
                    listStoreProduct.add(address);
                    listStoreProduct.add(latitude_);
                    listStoreProduct.add(longitude_);
                    listStoreProduct.add(distance);

                    listStoreReturn.add(listStoreProduct);
                    listStoreProduct = new ArrayList<>();
                }

        } catch (Exception q) {
            q.printStackTrace();
        }
        return listStoreReturn;
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

    public Store getStoreInfo(int id) {
        Store store = new Store();
        String methodName = "getStoreInfo";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("id", id);

        try {
            SoapObject results= (SoapObject) callService(methodName, request);

            if (results == null)
                return null;

            store = ConverterManager.convertToStore(results);
        } catch (Exception q) {
            q.printStackTrace();
        }
        return store;
    }
}
