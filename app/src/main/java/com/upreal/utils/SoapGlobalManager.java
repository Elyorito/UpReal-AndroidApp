package com.upreal.utils;

import android.util.Base64;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Elyo on 04/04/2015.
 */
public class SoapGlobalManager extends SoapManager {

    public SoapGlobalManager() {
        super("GlobalManager");
    }

    public ArrayList<Lists> getDiffListServer(ArrayList<Lists> lists) {
        ArrayList<Lists> serverList = new ArrayList<>();

        String methodname = "getDiffListServer";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("mobile", lists);
        try {
            Object response = callService(methodname, request);
            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    serverList.add(ConverterManager.convertToLists(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                serverList.add(ConverterManager.convertToLists(result));
            }
        } catch (Exception q) {
            q.printStackTrace();
        }
        return serverList;
    }

    public int createLists(String name, int publics, int type, int nb_items, int id_user) {
        int responseLists = 0;

        String methodname = "createLists";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("name", name);
        request.addProperty("publics", publics);
        request.addProperty("type", type);
        request.addProperty("nb_items", nb_items);
        request.addProperty("id_user", id_user);

        try {
            SoapPrimitive result = (SoapPrimitive) callService(methodname, request);
            responseLists = Integer.parseInt(result.toString());
        } catch (Exception q) {
            q.printStackTrace();
        }
        return responseLists;
    }

    public int createComment(int id_user, int id_target, int id_target_type, String commentary) {
        int responseComment = 0;
        String methodname = "createComment";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", id_user);
        request.addProperty("id_target", id_target);
        request.addProperty("id_target_type", id_target_type);
        request.addProperty("commentary", commentary);

        try {
            SoapPrimitive result = (SoapPrimitive) callService(methodname, request);
            responseComment = Integer.parseInt(result.toString());
        } catch (Exception q) {
            q.printStackTrace();
        }
        return responseComment;
    }

    public void uploadPicture(byte[] image, String name) {
        String methodname = "uploadPicture";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("name", name);
        request.addProperty("image", Base64.encodeToString(image, Base64.DEFAULT));

        try {
            callService(methodname, request);
        } catch (Exception q) {
            q.printStackTrace();
        }

    }

    public List<Lists> getUserList(int userId) {
        List<Lists> listUserLists = new ArrayList<Lists>();

        String methodname = "getUserList";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        try {
            Object response = callService(methodname, request);
            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    listUserLists.add(ConverterManager.convertToLists(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listUserLists.add(ConverterManager.convertToLists(result));
            }
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listUserLists;
    }

    public List<Article> getNews(){
        List<Article> listNews = new ArrayList<Article>();
        String methodname = "getNews";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        try {
            Object response = callService(methodname, request);
            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    listNews.add(ConverterManager.convertToArticle(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listNews.add(ConverterManager.convertToArticle(result));
            }
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listNews;
    }

    public Boolean getLikeOnProduct(int idUser, int idProduct, int idOvrRate, int type, int idArticle) {

        Boolean isSuccess = false;
        String methodname = "getRate";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", idUser);
        request.addProperty("id_product", idProduct);
        request.addProperty("id_ovr_rate", idOvrRate);
        request.addProperty("type", type);
        request.addProperty("id_article", idArticle);

        try {
                SoapPrimitive res = (SoapPrimitive) callService(methodname, request);
                isSuccess = Boolean.valueOf(res.toString());
            } catch (Exception q) {
            q.printStackTrace();
        }
        return isSuccess;
    }

    public List<Rate> getRate(int id_target, int id_target_type) {
        List<Rate> listRate = new ArrayList<Rate>();

        String methodname = "getRate";
        Rate rate = new Rate();
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_target", id_target);
        request.addProperty("id_target_type", id_target_type);

        try {
            Object response = callService(methodname, request);
            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    listRate.add(ConverterManager.convertToRate(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listRate.add(ConverterManager.convertToRate(result));
            }
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listRate;
    }

    public List<RateComment> getRateCommentOLD(int idUser, int idProduct, int idOvrRate, int type, int idArticle) {
        List<RateComment> listRate = new ArrayList<RateComment>();

        String methodname = "getRate";
        RateComment rate = new RateComment();
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        if (idUser != 0)
            request.addProperty("id_user", idUser);
        if (idProduct != 0)
            request.addProperty("id_product", idProduct);
        request.addProperty("type", type);

        try {
            Object response = callService(methodname, request);
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
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listRate;
    }
}
