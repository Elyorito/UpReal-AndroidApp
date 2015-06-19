package com.upreal.upreal.utils;

import android.util.Base64;
import android.util.Log;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
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
public class SoapProductManager {

    private static final boolean DEBUG_SOAP_REQUEST_RESPONSE = true;
    private static String MAIN_REQUEST_URL = "http://163.5.84.202/UpReal/services/ProductManager/";
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

/*
    private List<Product> getRequestToProduct(SoapObject result, int nbProd) {

        Product prod;
        List<Product> prodList = new ArrayList<Product>();
        Iterator it = prodList.iterator();
        for (int i = 0; nbProd > i; i++) {
            result.getProperty(i).toString();
        }
        return ;
    }
*/

    public int registerProduct(String name, String brand, String desc, String barcode, String noticedPrice, String shop) {
        int id = 0;
        String methodname = "registerProduct";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("name", name);
        request.addProperty("ean", barcode);
        request.addProperty("brand", brand);
        /*request.addProperty("picture", );*/

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {

            ht.call(methodname, envelope);
            testHttpResponse(ht);

            SoapObject res0 = (SoapObject) envelope.bodyIn;
            SoapPrimitive results= (SoapPrimitive)envelope.getResponse();
            id = Integer.parseInt(results.toString());
/*
            Vector<SoapObject> results = (Vector<SoapObject>) envelope.getResponse();
*/

        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return id;
    }

    public Product getProductInfo(int id) {

        String methodname = "getProductInfo";
        Product prod = null;
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {

            ht.call(methodname, envelope);
            testHttpResponse(ht);

            SoapObject res0 = (SoapObject) envelope.bodyIn;
            SoapObject results= (SoapObject)envelope.getResponse();
/*
            Vector<SoapObject> results = (Vector<SoapObject>) envelope.getResponse();
*/
            if (results == null)
                return null;

            prod = convertToQuery(results);
/*
            nbProduct = results.getAttributeCount();
*/
/*
            data = results.getProperty("Product").toString();
*/
/*
            for (SoapObject res : results)
                listprod.add(this.convertToQuery(res, data));
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
        return prod;
    }


    public Product getProductbyEAN(String ean) {
        String methodname = "getProductByEAN";
        Product prod = null;
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("ean", ean);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {

            ht.call(methodname, envelope);
            testHttpResponse(ht);

            SoapObject res0 = (SoapObject) envelope.bodyIn;
            SoapObject results= (SoapObject)envelope.getResponse();
/*
            Vector<SoapObject> results = (Vector<SoapObject>) envelope.getResponse();
*/
            if (results == null)
                return null;

            prod = convertToQuery(results);

/*
            nbProduct = results.getAttributeCount();
*/
/*
            data = results.getProperty("Product").toString();
*/
/*
            for (SoapObject res : results)
                listprod.add(this.convertToQuery(res, data));
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
        return prod;
    }

    public List<Product> getProduct(String searchName) {
        List<Product> listProds = new ArrayList<>();
        String methodname = "getProduct";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("keyword", searchName);

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {

            ht.call(methodname, envelope);
            testHttpResponse(ht);

            SoapObject res0 = (SoapObject) envelope.bodyIn;
            /*SoapObject results= (SoapObject)envelope.getResponse();*/
            Object response = envelope.getResponse();
/*
            nbProduct = results.getAttributeCount();
*/
/*
            data = results.getProperty("Product").toString();

*/            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    listProds.add(this.convertToQuery(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listProds.add(this.convertToQuery(result));
            }
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
        return listProds;
    }

    public Product scanProduct(byte[] imageBytes) {
/*        Log.v("Test:", "coucou");
        for (byte b : imageBytes) {
            Log.v("Test:", "b: " + b);
        }*/

        String methodname = "scanProduct";
        Product prod = new Product();
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        //request.addProperty("imageBytes", imageBytes);
        request.addProperty("image", Base64.encodeToString(imageBytes, Base64.DEFAULT));

        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
        HttpTransportSE ht = getHttpTransportSE();
        try {

            ht.call(methodname, envelope);
            testHttpResponse(ht);

            SoapObject res0 = (SoapObject) envelope.bodyIn;
            SoapObject results = (SoapObject) envelope.getResponse();

            if (results == null)
                return null;
            prod = convertToQuery(results);
        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return prod;
    }

    public String getPicture(int id, int type) {
        String methodname;
        switch (type) {
            case 0:
                methodname = "getProductPicture";
                break ;
            case 1:
                methodname = "getUserPicture";
                break ;
            case 2:
                methodname = "getStorePicture";
                break ;
            case 3:
                methodname = "getCompanyPicture";
                break ;
            default:
                methodname = "getProductPicture";
                break ;
        }

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);
        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
        HttpTransportSE ht = getHttpTransportSE();

        try {

            ht.call(methodname, envelope);
            testHttpResponse(ht);

            SoapObject res0 = (SoapObject) envelope.bodyIn;
            SoapObject results = (SoapObject) envelope.getResponse();

            OutputStream out = new BufferedOutputStream(new FileOutputStream(type + "-" + id + ".jpg"));
            out.write(Base64.decode(results.toString(), Base64.DEFAULT));

        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return type + "-" + id + ".jpg";
    }

    private Product convertToQuery(SoapObject soapObject) {
        Product prod = new Product();
        if (soapObject.hasProperty("name") && soapObject.getProperty("name") != null)
        prod.setName(soapObject.getProperty("name").toString());
        if (soapObject.hasProperty("ean") && soapObject.getProperty("ean") != null)
        prod.setEan(soapObject.getProperty("ean").toString());
        if (soapObject.hasProperty("brand") && soapObject.getProperty("brand") != null)
        prod.setBrand(soapObject.getProperty("brand").toString());
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
        prod.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
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
        return prod;
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
        HeaderProperty headerPropertyObj = new HeaderProperty("cookie", SoapProductManager.SESSION_ID);
        header.add(headerPropertyObj);
        return header;
    }
}
