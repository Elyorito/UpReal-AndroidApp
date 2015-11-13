package com.upreal.utils;

import android.util.Log;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 18/07/2015.
 */
public abstract class SoapManager {

    static boolean DEBUG_SOAP_REQUEST_RESPONSE = true;
    static String MAIN_REQUEST_URL = "http://163.5.84.202/UpReal/services/";
    static String NAMESPACE = "http://manager.entity.upreal";
    static String SESSION_ID;

    public SoapManager(String service) {
        //Outside
        //MAIN_REQUEST_URL = "http://163.5.84.202/UpReal/services/" + service + "/";
        //Inside
        MAIN_REQUEST_URL = "http://10.224.9.202/UpReal/services/" + service + "/";
    }

    protected final void testHttpResponse(HttpTransportSE ht) {
        ht.debug = DEBUG_SOAP_REQUEST_RESPONSE;
        if (DEBUG_SOAP_REQUEST_RESPONSE) {
            Log.v("SOAP RETURN", "Request XML:\n" + ht.requestDump);
            Log.v("SOAP RETURN", "\n\n\nResponse XML:\n" + ht.responseDump);
        }
    }

    protected final SoapSerializationEnvelope getSoapSerializationEnvelope(SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);
        return envelope;
    }

    protected final HttpTransportSE getHttpTransportSE() {
        HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY, MAIN_REQUEST_URL, 60000);
        ht.debug = true;
        ht.setXmlVersionTag("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>");
        return ht;
    }

    protected final List<HeaderProperty> getHeader() {
        List<HeaderProperty> header = new ArrayList<>();
        HeaderProperty headerPropertyObj = new HeaderProperty("cookie", this.SESSION_ID);
        header.add(headerPropertyObj);
        return header;
    }

    protected final Object callService(String methodname, SoapObject request) {
        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {
            ht.call(methodname, envelope);
            testHttpResponse(ht);
            return envelope.getResponse();
        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }

        return null;
    }
}