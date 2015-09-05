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

    private int target_user;
    private int target_product;
    private int target_store;
    private int target_article;
    private int target_ovr_rate;

    public SoapGlobalManager() {
        target_user = 1;
        target_product = 2;
        target_store = 3;
        target_article = 4;
        target_ovr_rate = 5;
    }

    private final void testHttpResponse(HttpTransportSE ht) {
        ht.debug = DEBUG_SOAP_REQUEST_RESPONSE;
        if (DEBUG_SOAP_REQUEST_RESPONSE) {
            Log.v("SOAP RETURN", "Request XML:\n" + ht.requestDump);
            Log.v("SOAP RETURN", "\n\n\nResponse XML:\n" + ht.responseDump);
        }
    }

    public int createLists(String name, int publics, int type, int nb_items, int id_user) {
        int responseComment = 0;

        String methodname = "createLists";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("name", name);
        request.addProperty("publics", publics);
        request.addProperty("type", type);
        request.addProperty("nb_items", nb_items);
        request.addProperty("id_user", id_user);

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

    public int createComment(int id_user, int id_target, int id_target_type, String commentary) {
        int responseComment = 0;
        String methodname = "createComment";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", id_user);
        request.addProperty("id_target", id_target);
        request.addProperty("id_target_type", id_target_type);
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

    public List<Lists> getUserList(int userId) {
        List<Lists> listUserLists = new ArrayList<Lists>();

        String methodname = "getUserList";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);

        HttpTransportSE ht = getHttpTransportSE();
        try {

            ht.call(methodname, envelope);
            testHttpResponse(ht);

            Object response = envelope.getResponse();
            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    listUserLists.add(this.convertToQueryLists(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listUserLists.add(this.convertToQueryLists(result));
            }


        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listUserLists;
    }

    public List<Article> getNews(){
        List<Article> listNews = new ArrayList<Article>();

        int nbProduct;
        String data = null;
        String methodname = "getNews";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
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
                    listNews.add(this.convertToQueryNews(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listNews.add(this.convertToQueryNews(result));
            }


        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listNews;
    }

    public Boolean getLikeOnProduct(int idUser, int idProduct, int idOvrRate, int type, int idArticle) {

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

    public List<Rate> getRate(int id_target, int id_target_type) {
        List<Rate> listRate = new ArrayList<Rate>();

        String methodname = "getRate";
        Rate rate = new Rate();
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        if (id_target_type == 1) {
            request.addProperty("id_target", id_target);
            request.addProperty("id_target_type", id_target_type);
        }
        if (id_target_type == 2){
            request.addProperty("id_target", id_target);
            request.addProperty("id_target_type", id_target_type);
        }

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
                    listRate.add(this.convertToQueryRate(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listRate.add(this.convertToQueryRate(result));
            }

        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listRate;
    }

    public List<RateComment> getRateCommentOLD(int idUser, int idProduct, int idOvrRate, int type, int idArticle) {
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
                    //listRate.add(this.convertToQueryRate(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                //listRate.add(this.convertToQueryRate(result));
            }

        } catch (SocketTimeoutException t) {
            t.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listRate;
    }

    private Lists convertToQueryLists(SoapObject soapObject) {
        Lists lists = new Lists();
        lists.setId(Integer.parseInt(soapObject.getPropertyAsString("id").toString()));
        lists.setName(soapObject.getPropertyAsString("name").toString());
        lists.setL_public(Integer.parseInt(soapObject.getPropertyAsString("public").toString()));
        lists.setNb_items(Integer.parseInt(soapObject.getPropertyAsString("nb_items").toString()));
        lists.setId_user(Integer.parseInt(soapObject.getPropertyAsString("id_user").toString()));
        lists.setType(Integer.parseInt(soapObject.getPropertyAsString("type").toString()));
        lists.setDate(Integer.parseInt(soapObject.getPropertyAsString("date").toString()));

        return lists;
    }

    private Article convertToQueryNews(SoapObject soapObject) {
        Article news = new Article();
        news.setTitle(soapObject.getPropertyAsString("title").toString());
        news.setBody(soapObject.getPropertyAsString("body").toString());
        news.setCreation(soapObject.getPropertyAsString("creation").toString());
        news.setType(Integer.parseInt(soapObject.getPropertyAsString("type").toString()));
        news.setPicture(soapObject.getPropertyAsString("picture").toString());

        return news;
    }

    private Rate convertToQueryRate(SoapObject soapObject) {
        Rate rate = new Rate();
        rate.setmId_user(Integer.parseInt(soapObject.getPropertyAsString("id_user").toString()));
        rate.setmMark(Integer.parseInt(soapObject.getPropertyAsString("mark").toString()));
        rate.setmCommentary(soapObject.getPropertyAsString("commentary").toString());
        rate.setmDate(soapObject.getPropertyAsString("date").toString());
        rate.setmActive(Integer.parseInt(soapObject.getPropertyAsString("active").toString()));
        rate.setmUp(Integer.parseInt(soapObject.getPropertyAsString("up").toString()));
        rate.setmDown(Integer.parseInt(soapObject.getPropertyAsString("down").toString()));
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

    public int getTarget_user() {
        return target_user;
    }

    public int getTarget_product() {
        return target_product;
    }

    public int getTarget_store() {
        return target_store;
    }

    public int getTarget_article() {
        return target_article;
    }

    public int getTarget_ovr_rate() {
        return target_ovr_rate;
    }

    public void setTarget_user(int target_user) {
        this.target_user = target_user;
    }

    public void setTarget_product(int target_product) {
        this.target_product = target_product;
    }

    public void setTarget_store(int target_store) {
        this.target_store = target_store;
    }

    public void setTarget_article(int target_article) {
        this.target_article = target_article;
    }

    public void setTarget_ovr_rate(int target_ovr_rate) {
        this.target_ovr_rate = target_ovr_rate;
    }
}
