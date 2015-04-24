package com.upreal.upreal.utils;

import android.util.Log;

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
import java.util.Vector;

/**
 * Created by Elyo on 01/03/2015.
 */
public class SoapUserManager {

    private static final boolean DEBUG_SOAP_REQUEST_RESPONSE = true;
    private static String MAIN_REQUEST_URL = "http://163.5.84.202/UpReal/services/UserManager/";
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

    public User getUserByUsername(String username) {
        User user = new User();
        String methodName = "getUserByUsername";
        int result = 0;

        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("username", username);
        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {
            ht.call(methodName, envelope);
            testHttpResponse(ht);
            SoapObject res2 = (SoapObject) envelope.getResponse();

            user.setId(Integer.parseInt(res2.getPropertyAsString("id").toString()));
            user.setUsername(res2.getPropertyAsString("username").toString());
            user.setFirstname(res2.getPropertyAsString("firstname").toString());
            user.setLastname(res2.getPropertyAsString("lastname").toString());
            user.setEmail(res2.getPropertyAsString("email").toString());
            user.setPassword(res2.getPropertyAsString("password").toString());
            user.setPhone(Integer.parseInt(res2.getPropertyAsString("phone").toString()));
            user.setId_address(Integer.parseInt(res2.getPropertyAsString("id_address").toString()));
            user.setShort_desc(res2.getPropertyAsString("short_desc").toString());

        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return user;
    }

    public int registerAccount(String id, String password, String email) {
        String data = null;
        String methodname = "registerAccount";
        int result = 0;

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("username", id);
        request.addProperty("email", email);
        request.addProperty("password", password);
        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {
            ht.call(methodname, envelope);
            testHttpResponse(ht);
            SoapPrimitive results= (SoapPrimitive)envelope.getResponse();

            data = results.toString();
            result = Integer.parseInt(data);
        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return result;
    }

    public Boolean connectAccount(String username, String password) {
        Boolean result = false;
        String data = null;
        String methodname = "connectAccount";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("username", username);
        request.addProperty("password", password);
        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {
            ht.call(methodname, envelope);
            testHttpResponse(ht);
            SoapPrimitive resultsBoolean= (SoapPrimitive)envelope.getResponse();

            data = resultsBoolean.toString();
            result = Boolean.valueOf(data);
        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
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
        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {
            ht.call(methodname, envelope);
            testHttpResponse(ht);
            SoapPrimitive resultsString = (SoapPrimitive)envelope.getResponse();

            data = resultsString.toString();
            result = Boolean.parseBoolean(data);
        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return result;
    }

    public User getAccountInfoUsername(int id) {
        User user = new User();

        String methodName = "getAccountInfo";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("id", id);
        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
        HttpTransportSE ht = getHttpTransportSE();
        try {
            ht.call(methodName, envelope);
            testHttpResponse(ht);
            SoapObject res2 = (SoapObject) envelope.getResponse();

            user.setId(Integer.parseInt(res2.getPropertyAsString("id").toString()));
            user.setUsername(res2.getPropertyAsString("username").toString());
        } catch (SocketTimeoutException t){
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return user;
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
        HeaderProperty headerPropertyObj = new HeaderProperty("cookie", SoapUserManager.SESSION_ID);
        header.add(headerPropertyObj);
        return header;
    }
}
