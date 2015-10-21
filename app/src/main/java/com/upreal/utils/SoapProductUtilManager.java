package com.upreal.utils;

import android.util.Log;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Elyo on 07/04/2015.
 */
public class SoapProductUtilManager {

    private static final boolean DEBUG_SOAP_REQUEST_RESPONSE = true;
    private static String MAIN_REQUEST_URL = "http://163.5.84.202/UpReal/services/ProductUtilManager/";
    private static String NAMESPACE = "http://manager.entity.upreal";
    private static final String SOAP_ACTION = "http://163.5.84.202/UpReal/services";
    private static String SESSION_ID;

    private final void testHttpResponse(HttpTransportSE ht) {
        ht.debug = DEBUG_SOAP_REQUEST_RESPONSE;
        if (DEBUG_SOAP_REQUEST_RESPONSE) {
            Log.v("SOAP RETURN", "Request XML:\n" + ht.requestDump);
            Log.v("SOAP RETURN", "\n\n\nResponse XML:\n" + ht.responseDump);
        }
    }

    public Boolean rateProduct(int idUser, int idTarget, int mark) {
        Boolean isSuccess = false;
        String methodName = "rateProduct";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("id_user", idUser);
        request.addProperty("id_target", idTarget);
        request.addProperty("mark", mark);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
        HttpTransportSE ht = getHttpTransportSE();

        try {
            ht.call(methodName, envelope);
            testHttpResponse(ht);
            SoapPrimitive res = (SoapPrimitive) envelope.getResponse();
            isSuccess = Boolean.valueOf(res.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

/*    public int createProductComment(int idUser, int idProduct, String commentary) {

        int responseComment = 0;
        String methodname = "createProductComment";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", idUser);
        request.addProperty("id_target", idProduct);
        request.addProperty("commentary", commentary);


        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {

            ht.call(methodname, envelope);
            testHttpResponse(ht);
            SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
            responseComment = Integer.parseInt(result.toString());

        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return responseComment;
    }*/

    public void createSpecification(int idProduct, String fieldName, String fieldDesc) {
        String methodName = "createSpecification";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("id_product", idProduct);
        request.addProperty("field_name", fieldName);
        request.addProperty("field_desc", fieldDesc);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
        HttpTransportSE ht = getHttpTransportSE();

        try {
            ht.call(methodName, envelope);
            testHttpResponse(ht);
            SoapPrimitive res = (SoapPrimitive) envelope.getResponse();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public Specification getDescription(int idProduct) {
        Specification spec = null;
        String methodName = "getDescription";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("id_product", idProduct);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {

            ht.call(methodName, envelope);
            testHttpResponse(ht);

            SoapObject res0 = (SoapObject) envelope.bodyIn;
            SoapObject results= (SoapObject)envelope.getResponse();

            if (results == null)
                return null;

            spec = ConverterManager.convertToSpecification(results);

        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return spec;
    }

    public Specification getSpecification(int idProduct, String fieldName) {
        Specification spec = null;
        String methodName = "getSpecification";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("id_product", idProduct);
        request.addProperty("field_name", fieldName);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {

            ht.call(methodName, envelope);
            testHttpResponse(ht);

            SoapObject res0 = (SoapObject) envelope.bodyIn;
            SoapObject results= (SoapObject)envelope.getResponse();

            if (results == null)
                return null;

            spec = ConverterManager.convertToSpecification(results);

        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return spec;
    }

    public List<Specification> getAllSpecification(int idProduct) {
        List<Specification> listSpec = null;
        String methodName = "getAllSpecification";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("id_product", idProduct);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {

            ht.call(methodName, envelope);
            testHttpResponse(ht);

            SoapObject res0 = (SoapObject) envelope.bodyIn;
            Object response = envelope.getResponse();

            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    listSpec.add(ConverterManager.convertToSpecification(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listSpec.add(ConverterManager.convertToSpecification(result));
            }

        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listSpec;
    }


    public List<Address> getAddressByProduct(int idProduct) {
        List<Address> listAddress = new ArrayList<>();
        String methodname = "getStoreAddressByProduct";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", idProduct);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {

            ht.call(methodname, envelope);
            testHttpResponse(ht);

            SoapObject res0 = (SoapObject) envelope.bodyIn;
            Object response = envelope.getResponse();

            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    listAddress.add(ConverterManager.convertToAddress(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listAddress.add(ConverterManager.convertToAddress(result));
            }

        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listAddress;
    }

    public ArrayList<String> getCategory() {
        ArrayList<String> list = new ArrayList<>();
        String methodName = "getCategory";
        SoapObject request = new SoapObject(NAMESPACE, methodName);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {

            ht.call(methodName, envelope);
            testHttpResponse(ht);

            SoapObject res0 = (SoapObject) envelope.bodyIn;
            Object response = envelope.getResponse();

            Vector<SoapPrimitive> res = (Vector<SoapPrimitive>) response;

            for (int i = 0; i < res.size(); i++) {
                list.add(res.get(i).getValue().toString());
            }
        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return list;
    }

    public void setProductCategory(int idProduct, String keyword) {
        String methodName = "setProductCategory";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("id_product", idProduct);
        request.addProperty("keyword", keyword);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
        HttpTransportSE ht = getHttpTransportSE();

        try {
            ht.call(methodName, envelope);
            testHttpResponse(ht);
            SoapPrimitive res = (SoapPrimitive) envelope.getResponse();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public String getProductCategory(int idProduct) {
        String cat = "Aucun";
        String methodName = "getProductCategory";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("id_product", idProduct);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
        HttpTransportSE ht = getHttpTransportSE();

        try {
            ht.call(methodName, envelope);
            testHttpResponse(ht);
            SoapObject res0 = (SoapObject) envelope.bodyIn;
            SoapPrimitive results = (SoapPrimitive)envelope.getResponse();

            if (results == null)
                return null;

            cat = results.getValue().toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return cat;
    }

    public List<Double> getPriceByProduct(int idProduct) {
        List<Double> listPrices = new ArrayList<>();
        String methodname = "getPriceByProduct";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", idProduct);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {

            ht.call(methodname, envelope);
            testHttpResponse(ht);

            SoapObject res0 = (SoapObject) envelope.bodyIn;
            Object response = envelope.getResponse();

            if (response instanceof Vector) {
                Vector<SoapPrimitive> results = (Vector<SoapPrimitive>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapPrimitive res = results.get(i);
                    listPrices.add(Double.parseDouble(res.toString()));
                }
            } else if (response instanceof SoapPrimitive) {
                SoapPrimitive result = (SoapPrimitive) response;
                listPrices.add(Double.parseDouble(result.toString()));
            }

        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listPrices;
    }

    private final SoapSerializationEnvelope getSoapSerializationEnvelope(SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);
        return envelope;
    }

    private final HttpTransportSE getHttpTransportSE() {
        HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY,MAIN_REQUEST_URL,60000);
        ht.debug = true;
        ht.setXmlVersionTag("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>");
        return ht;
    }

    private final List<HeaderProperty> getHeader() {
        List<HeaderProperty> header = new ArrayList<HeaderProperty>();
        HeaderProperty headerPropertyObj = new HeaderProperty("cookie", SoapProductUtilManager.SESSION_ID);
        header.add(headerPropertyObj);
        return header;
    }
}
