package com.upreal.upreal.utils;

import android.util.Log;

import com.upreal.upreal.R;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 07/04/2015.
 */
public class SoapUserUtilManager {
    private static final boolean DEBUG_SOAP_REQUEST_RESPONSE = true;
    private static String MAIN_REQUEST_URL = "http://163.5.84.202/UpReal/services/UserUtilManager/";
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

    public Address getAddressInfo(int id) {
        String methodname = "getAddressInfo";
        Address address = new Address();

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);
        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {
            ht.call(methodname, envelope);
            testHttpResponse(ht);
            SoapObject result= (SoapObject)envelope.getResponse();
            if (result.hasProperty("id"))
                address.setId(Integer.parseInt(result.getPropertyAsString("id")));
            if (result.hasProperty("address"))
                address.setAddress(result.getPropertyAsString("address"));
            else
                address.setAddress(String.valueOf(R.string.not_defined));
            if (result.hasProperty("address_2"))
                address.setAddress2(result.getPropertyAsString("address_2"));
            else
                address.setAddress2(String.valueOf(R.string.not_defined));
            if (result.hasProperty("city"))
                address.setCity(result.getPropertyAsString("city"));
            else
                address.setCity(String.valueOf(R.string.not_defined));
            if (result.hasProperty("postal_code"))
                address.setPostalCode(Integer.parseInt(result.getPropertyAsString("postal_code")));
            else
                address.setPostalCode(0);

        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
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
        request.addProperty("address2", address.getAddress2());
        request.addProperty("country", address.getCountry());
        request.addProperty("city", address.getCity());
        request.addProperty("postal_code", address.getPostalCode());

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {
            ht.call(methodname, envelope);
            testHttpResponse(ht);
            SoapPrimitive results= (SoapPrimitive)envelope.getResponse();

            result = Boolean.getBoolean(results.toString());
        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return result;
    }

    public int registerAddress(Address address) {
        String methodname = "register";
        int result = -1;

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("address", address.getAddress());
        request.addProperty("address2", address.getAddress2());
        request.addProperty("country", address.getCountry());
        request.addProperty("city", address.getCity());
        request.addProperty("postal_code", address.getPostalCode());

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {
            ht.call(methodname, envelope);
            testHttpResponse(ht);
            SoapPrimitive results= (SoapPrimitive)envelope.getResponse();

            result = Integer.getInteger(results.toString());
        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return result;
    }

    private SoapSerializationEnvelope getSoapSerializationEnvelope(SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);
        return envelope;
    }

    private HttpTransportSE getHttpTransportSE() {
        HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY,MAIN_REQUEST_URL,60000);
        ht.debug = true;
        ht.setXmlVersionTag("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>");
        return ht;
    }

    private final List<HeaderProperty> getHeader() {
        List<HeaderProperty> header = new ArrayList<HeaderProperty>();
        HeaderProperty headerPropertyObj = new HeaderProperty("cookie", SoapUserUtilManager.SESSION_ID);
        header.add(headerPropertyObj);
        return header;
    }
}
