package com.upreal.uprealwear.server;

import com.upreal.uprealwear.utils.Achievement;
import com.upreal.uprealwear.utils.ConverterManager;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Kyosukke on 18/08/2015.
 */
public class UserUtilManager extends SoapManager {


    public UserUtilManager() {
        super("UserUtilManager");
    }

    public List<Achievement> getAchievement() {
        List<Achievement> listAchievement = new ArrayList<Achievement>();
        String methodname = "getAchievement";

        SoapObject request = new SoapObject(NAMESPACE, methodname);

        try {
            Object res = callService(methodname, request);
            if (res instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) res;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject o = results.get(i);
                    listAchievement.add(ConverterManager.convertToAchievement(o));
                }
            } else if (res instanceof SoapObject) {
                SoapObject o = (SoapObject) res;
                listAchievement.add(ConverterManager.convertToAchievement(o));
            }
            return listAchievement;
        } catch (Exception q) {
            q.printStackTrace();
        }
        return null;
    }

    public List<Achievement> getUserAchievement(int idUser) {
        List<Achievement> listAchievement = new ArrayList<Achievement>();
        String methodname = "getUserAchievement";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", idUser);

        try {
            Object res = callService(methodname, request);
            if (res instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) res;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject o = results.get(i);
                    listAchievement.add(ConverterManager.convertToAchievement(o));
                }
            } else if (res instanceof SoapObject) {
                SoapObject o = (SoapObject) res;
                listAchievement.add(ConverterManager.convertToAchievement(o));
            }
            return listAchievement;
        } catch (Exception q) {
            q.printStackTrace();
        }
        return null;
    }

    public boolean hasAchievement(int idUser, int idAchievement) {
        Boolean res;
        String methodname = "hasAchievement";

        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", idUser);
        request.addProperty("id_achievement", idAchievement);

        try {
            res = Boolean.valueOf(((SoapPrimitive) callService(methodname, request)).toString());
            return res;
        } catch (Exception q) {
            q.printStackTrace();
        }
        return false;
    }
}
