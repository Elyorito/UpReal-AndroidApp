package com.upreal.uprealwear.server;

import android.util.Log;

import com.upreal.uprealwear.utils.Article;
import com.upreal.uprealwear.utils.ConverterManager;
import com.upreal.uprealwear.utils.Items;
import com.upreal.uprealwear.utils.Lists;
import com.upreal.uprealwear.utils.Rate;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Kyosukke on 27/07/2015.
 */
public class GlobalManager extends SoapManager {

    public GlobalManager() {
        super("GlobalManager");
    }

    public List<Article> searchNews(String search) {
        List<Article> listArticle = new ArrayList<Article>();
        String methodname = "searchNews";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("keyword", search);

        try {
            Object res = callService(methodname, request);
            if (res instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) res;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject o = results.get(i);
                    listArticle.add(ConverterManager.convertToArticle(o));
                }
            } else if (res instanceof SoapObject) {
                SoapObject o = (SoapObject) res;
                listArticle.add(ConverterManager.convertToArticle(o));
            }
            return listArticle;
        } catch (Exception q) {
            q.printStackTrace();
        }

        return null;
    }

    public Article getNewsInfo(int id) {
        String methodname = "getNewsInfo";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);

        try {
            Object res = callService(methodname, request);
            SoapObject o = (SoapObject) res;
            Article a = ConverterManager.convertToArticle(o);

            return a;
        } catch (Exception q) {
            q.printStackTrace();
        }

        return null;
    }

    public List<Rate> getRate(int id, int targetType) {
        List<Rate> listRate = new ArrayList<Rate>();
        String methodname = "getRate";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_target", id);
        request.addProperty("id_target_type", targetType);

        try {
            Object res = callService(methodname, request);
            if (res instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) res;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject o = results.get(i);
                    listRate.add(ConverterManager.convertToRate(o));
                }
            } else if (res instanceof SoapObject) {
                SoapObject o = (SoapObject) res;
                listRate.add(ConverterManager.convertToRate(o));
            }
            return listRate;
        } catch (Exception q) {
            q.printStackTrace();
        }

        return null;
    }

    public int getRateStatus(int idTarget, int targetType, int idUser) {
        String methodname = "getRateStatus";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", idUser);
        request.addProperty("id_target", idTarget);
        request.addProperty("id_target_type", targetType);

        try {
            int res = Integer.parseInt(((SoapPrimitive) callService(methodname, request)).toString());
            Log.e(NAMESPACE, "'getRateStatus': " + res);
            return res;
        } catch (Exception q) {
            q.printStackTrace();
        }

        return 0;
    }

    public void likeSomething(int idTarget, int targetType, int idUser) {
        String methodname = "likeSomething";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", idUser);
        request.addProperty("id_target", idTarget);
        request.addProperty("id_target_type", targetType);

        try {
            callService(methodname, request);
        } catch (Exception q) {
            q.printStackTrace();
        }
    }

    public void dislikeSomething(int idTarget, int targetType, int idUser) {
        String methodname = "dislikeSomething";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", idUser);
        request.addProperty("id_target", idTarget);
        request.addProperty("id_target_type", targetType);

        try {
            callService(methodname, request);
        } catch (Exception q) {
            q.printStackTrace();
        }
    }

    public void unLikeSomething(int idTarget, int targetType, int idUser) {
        String methodname = "unLikeSomething";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", idUser);
        request.addProperty("id_target", idTarget);
        request.addProperty("id_target_type", targetType);

        try {
            callService(methodname, request);
        } catch (Exception q) {
            q.printStackTrace();
        }
    }

    public int countRate(int idTarget, int targetType, int type) {
        String methodname = "countRate";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_target", idTarget);
        request.addProperty("id_target_type", targetType);
        request.addProperty("type", type);

        try {
            return Integer.parseInt(((SoapPrimitive) callService(methodname, request)).toString());
        } catch (Exception q) {
            q.printStackTrace();
        }

        return 0;
    }

    public String getPicture(int idTarget, int targetType) {
        String methodname = "getPicture";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_target", idTarget);
        request.addProperty("id_target_type", targetType);

        try {
            return ((SoapPrimitive) callService(methodname, request)).toString();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return null;
    }

    public List<Lists> getUserList(int idUser) {
        List<Lists> listLists = new ArrayList<Lists>();
        String methodname = "getUserList";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", idUser);

        try {
            Object res = callService(methodname, request);
            if (res instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) res;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject o = results.get(i);
                    listLists.add(ConverterManager.convertToLists(o));
                }
            } else if (res instanceof SoapObject) {
                SoapObject o = (SoapObject) res;
                listLists.add(ConverterManager.convertToLists(o));
            }
            return listLists;
        } catch (Exception q) {
            q.printStackTrace();
        }

        return null;
    }

    public void createItem(int idList, int idProduct, int idUser) {
        String methodname = "createItem";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_list", idList);
        request.addProperty("id_product", idProduct);
        request.addProperty("id_user", idUser);

        try {
            int res = Integer.parseInt(((SoapPrimitive) callService(methodname, request)).toString());
        } catch (Exception q) {
            q.printStackTrace();
        }
    }

    public void deleteItem(int id) {
        String methodname = "deleteItem";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", id);

        try {
            boolean res = Boolean.parseBoolean(((SoapPrimitive) callService(methodname, request)).toString());
        } catch (Exception q) {
            q.printStackTrace();
        }
    }

    public List<Items> getItemsLists(int idList) {
        List<Items> listItems = new ArrayList<Items>();
        String methodname = "getItemsLists";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_list", idList);

        try {
            Object res = callService(methodname, request);
            if (res instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) res;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject o = results.get(i);
                    listItems.add(ConverterManager.convertToItems(o));
                }
            } else if (res instanceof SoapObject) {
                SoapObject o = (SoapObject) res;
                listItems.add(ConverterManager.convertToItems(o));
            }
            return listItems;
        } catch (Exception q) {
            q.printStackTrace();
        }

        return null;
    }
}
