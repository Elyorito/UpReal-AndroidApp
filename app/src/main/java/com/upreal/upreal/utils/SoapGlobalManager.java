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
 * Created by Elyo on 04/04/2015.
 */
public class SoapGlobalManager {

    private static final boolean DEBUG_SOAP_REQUEST_RESPONSE = true;
    private static String MAIN_REQUEST_URL = "http://163.5.84.202/UpReal/services/GlobalManager/";
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

    public Boolean getRateLike(int idUser, int idProduct, int idOvrRate, int type, int idArticle) {

        Boolean isSuccess = false;
        String methodName = "getRate";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("id_user", idUser);
        request.addProperty("id_product", idProduct);
        request.addProperty("id_ovr_rate", idOvrRate);
        request.addProperty("type", type);
        request.addProperty("id_article", idArticle);

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

    public List<RateComment> getRateComment(int idUser, int idProduct, int idOvrRate, int type, int idArticle) {
        List<RateComment> listRate = new ArrayList<RateComment>();

        int nbProduct;
        String data = null;
        String methodname = "getRate";
        RateComment rate = new RateComment();
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        if (idUser != 0)
            request.addProperty("id_user", idUser);
        if (idProduct != 0)
            request.addProperty("id_product", idProduct);
        request.addProperty("type", type);

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
                    listRate.add(this.convertToQuery(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listRate.add(this.convertToQuery(result));
            }

/*
            nbProduct = results.getAttributeCount();
*/
/*
            data = results.getProperty("Product").toString();
*/
/*
            for (SoapObject res : results)
                listRate.add(this.convertToQuery(res, data));
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
        return listRate;
    }

    private RateComment convertToQuery(SoapObject soapObject) {
        RateComment rate = new RateComment();
        rate.setmTextComment(soapObject.getPropertyAsString("commentary").toString());
        rate.setmNameUser(soapObject.getPropertyAsString("id_user").toString());

/*        rate.setmDateTime(soapObject.getPropertyAsString("date"));*/

        return rate;
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
        HeaderProperty headerPropertyObj = new HeaderProperty("cookie", SoapGlobalManager.SESSION_ID);
        header.add(headerPropertyObj);
        return header;
    }
}
