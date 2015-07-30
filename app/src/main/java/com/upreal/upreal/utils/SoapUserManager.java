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

    //change int phone into String

    public boolean updateAccount(int id, String firstName, String lastName, int phone, int id_address, String shortDesc) {
        String methodname = "updateAccount";
        boolean result = false;

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);
        request.addProperty("firstname", firstName);
        request.addProperty("lastname", lastName);
        request.addProperty("phone", phone);
        request.addProperty("id_address", id_address);
        request.addProperty("short_desc", shortDesc);
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

    public Boolean updatePassword(int id, String old, String newP) {
        String methodname = "changePassword";
        boolean result = false;

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", id);
        request.addProperty("old_password", old);
        request.addProperty("new_password", newP);
        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {
            ht.call(methodname, envelope);
            testHttpResponse(ht);
            SoapPrimitive results= (SoapPrimitive)envelope.getResponse();

            result = Boolean.valueOf(results.toString());
        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return result;
    }

    public int registerAccount(String id, String password, String email) {
        String data;
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
        String data;
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

            user.setId(Integer.parseInt(res2.getPropertyAsString("id")));
            user.setUsername(res2.getPropertyAsString("username"));
        } catch (SocketTimeoutException t){
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return user;
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
        List<HeaderProperty> header = new ArrayList<>();
        HeaderProperty headerPropertyObj = new HeaderProperty("cookie", SoapUserManager.SESSION_ID);
        header.add(headerPropertyObj);
        return header;
    }

    public User getUserByUsername(String username) {
        User user = null;
        String methodName = "getSingleUserByUsername";

        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("username", username);
        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {
            ht.call(methodName, envelope);
            testHttpResponse(ht);
            SoapObject res2 = (SoapObject) envelope.getResponse();

            user = convertToQuery(res2);
        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return user;
    }

    private User convertToQuery(SoapObject soapObject) {
        User user = new User();
        user.setEmail(soapObject.getProperty("email").toString());
        if (soapObject.hasProperty("firstname") && soapObject.getProperty("firstname") != null)
            user.setFirstname(soapObject.getProperty("firstname").toString());
        user.setId(Integer.parseInt(soapObject.getPropertyAsString("id")));
        if (soapObject.hasProperty("id_address") && soapObject.getProperty("id_address") != null)
            user.setId_address(Integer.parseInt(soapObject.getPropertyAsString("id_address")));
        user.setUsername(soapObject.getPropertyAsString("username"));
        if (soapObject.hasProperty("lastname") && soapObject.getProperty("lastname") != null)
            user.setLastname(soapObject.getPropertyAsString("lastname"));
        if (soapObject.hasProperty("phone") && soapObject.getProperty("phone") != null)
            user.setPhone(Integer.parseInt(soapObject.getPropertyAsString("phone")));
        if (soapObject.hasProperty("short_desc") && soapObject.getProperty("short_desc") != null)
         user.setShort_desc(soapObject.getPropertyAsString("short_desc"));
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            user.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
/*
        prod.setPicture(soapObject.getPropertyAsString("picture").toString());
*/

/*
        prod.setName("Fanta");
        prod.setEan(50235823);
        prod.setBrand("The coca cola COMPANY");
        prod.setId(4);
        prod.setPicture("link to picture");
*/
        return user;
    }

    public List<User> getListUser(String searchName) {

        List<User> listUsers = new ArrayList<>();
        String methodname = "getUserByUsername";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("username", searchName);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
        HttpTransportSE ht = getHttpTransportSE();
        try {

            ht.call(methodname, envelope);
            testHttpResponse(ht);

            SoapObject res0 = (SoapObject) envelope.bodyIn;
            /*SoapObject results= (SoapObject)envelope.getResponse();*/
            Object response = envelope.getResponse();

            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    listUsers.add(this.convertToQuery(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listUsers.add(this.convertToQuery(result));
            }
/*
            nbProduct = results.getAttributeCount();
*/
/*
            data = results.getProperty("Product").toString();
                */
/*
                for (SoapObject res : results) {
                    listUsers.add(this.convertToQuery(res, data));
            }
*/
/*
            if (results instanceof SoapObject) {
                data = results.getProperty("ean").toString();
            }
*/

        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listUsers;
    }
}
