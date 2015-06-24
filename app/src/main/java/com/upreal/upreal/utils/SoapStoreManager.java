package com.upreal.upreal.utils;

import android.util.Base64;
import android.util.Log;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Elyo on 16/03/2015.
 */
public class SoapStoreManager {

    private static final boolean DEBUG_SOAP_REQUEST_RESPONSE = true;
    private static String MAIN_REQUEST_URL = "http://163.5.84.202/UpReal/services/StoreManager/";
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

    public Store getStoreByAddress(int id_address) {
        String methodname = "getStoreByAddress";
        Store store = null;
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id_address);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {

            ht.call(methodname, envelope);
            testHttpResponse(ht);

            SoapObject res0 = (SoapObject) envelope.bodyIn;
            SoapObject results= (SoapObject)envelope.getResponse();

            if (results == null)
                return null;

            store = convertToQuery(results);

        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return store;
    }

    private Store convertToQuery(SoapObject soapObject) {
        Store store = new Store();
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            store.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
        if (soapObject.hasProperty("name") && soapObject.getProperty("name") != null)
            store.setName(soapObject.getProperty("name").toString());
        if (soapObject.hasProperty("website") && soapObject.getProperty("website") != null)
            store.setWebsite(soapObject.getProperty("website").toString());
        if (soapObject.hasProperty("id_address") && soapObject.getProperty("id_address") != null)
            store.setId_address(Integer.parseInt(soapObject.getProperty("id_address").toString()));
        if (soapObject.hasProperty("id_company") && soapObject.getProperty("id_company") != null)
            store.setId_company(Integer.parseInt(soapObject.getProperty("id_company").toString()));

        return store;
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

    private List<HeaderProperty> getHeader() {
        List<HeaderProperty> header = new ArrayList<HeaderProperty>();
        HeaderProperty headerPropertyObj = new HeaderProperty("cookie", SoapStoreManager.SESSION_ID);
        header.add(headerPropertyObj);
        return header;
    }
}
