package com.upreal.upreal.utils;

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

    public int createProductComment(int idUser, int idProduct, String commentary) {

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
                    listAddress.add(this.convertToQuery(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listAddress.add(this.convertToQuery(result));
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

    private Address convertToQuery(SoapObject soapObject) {
        Address address = new Address();
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            address.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
        if (soapObject.hasProperty("address") && soapObject.getProperty("address") != null)
            address.setAddress(soapObject.getProperty("address").toString());
        if (soapObject.hasProperty("address_2") && soapObject.getProperty("address_2") != null)
            address.setAddress2(soapObject.getProperty("address_2").toString());
        if (soapObject.hasProperty("city") && soapObject.getProperty("city") != null)
            address.setCity(soapObject.getProperty("city").toString());
        if (soapObject.hasProperty("country") && soapObject.getProperty("country") != null)
            address.setCountry(soapObject.getProperty("country").toString());
        if (soapObject.hasProperty("postal_code") && soapObject.getProperty("postal_code") != null)
            address.setPostalCode(Integer.parseInt(soapObject.getProperty("postal_code").toString()));

        return address;
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
