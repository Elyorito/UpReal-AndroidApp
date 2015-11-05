package com.upreal.utils;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Elyo on 07/04/2015.
 */
public class SoapProductUtilManager extends SoapManager {

    public SoapProductUtilManager() {
        super("ProductUtilManager");
    }

    public Boolean rateProduct(int idUser, int idTarget, int mark) {
        Boolean isSuccess = false;
        String methodname = "rateProduct";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", idUser);
        request.addProperty("id_target", idTarget);
        request.addProperty("mark", mark);

        try {
            SoapPrimitive res = (SoapPrimitive) callService(methodname, request);
            isSuccess = Boolean.valueOf(res.toString());
        } catch (Exception q) {
            q.printStackTrace();
        }
        return isSuccess;
    }

/*    public int createProductComment(int idUser, int idProduct, String commentary) {

        int responseComment = 0;
        String methodname = "createProductComment";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_user", idUser);
        request.addProperty("id_target", idProduct);
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
    }*/

    public void createSpecification(int idProduct, String name, String value, int type) {
        String methodname = "createSpecification";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_product", idProduct);
        request.addProperty("type", type);
        request.addProperty("name", name);
        request.addProperty("value", value);

        try {
            callService(methodname, request);
        } catch (Exception q) {
            q.printStackTrace();
        }
    }

    public List<Characteristic> getSpecificationByType(int idProduct, int type) {
        List<Characteristic> list = new ArrayList<>();
        String methodname = "getSpecificationByType";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_product", idProduct);
        request.addProperty("type", type);

        try {
            Object response = callService(methodname, request);

            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    list.add(ConverterManager.convertToCharacteristic(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                list.add(ConverterManager.convertToCharacteristic(result));
            }
        } catch (Exception q) {
            q.printStackTrace();
        }
        return list;
    }

    public List<Characteristic> getSpecification(int idProduct) {
        String methodname = "getSpecification";
        List<Characteristic> list = new ArrayList<>();
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_product", idProduct);

        try {
            Object response = callService(methodname, request);

            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    list.add(ConverterManager.convertToCharacteristic(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                list.add(ConverterManager.convertToCharacteristic(result));
            }
        } catch (Exception q) {
            q.printStackTrace();
        }
        return list;
    }


    public List<Address> getAddressByProduct(int idProduct) {
        List<Address> listAddress = new ArrayList<>();
        String methodname = "getStoreAddressByProduct";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", idProduct);

        try {
            Object response = callService(methodname, request);

            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    listAddress.add(ConverterManager.convertToAddress(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listAddress.add(ConverterManager.convertToAddress(result));
            }
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listAddress;
    }

    public ArrayList<String> getCategory() {
        ArrayList<String> list = new ArrayList<>();
        String methodname = "getCategory";
        SoapObject request = new SoapObject(NAMESPACE, methodname);

        try {
            Object response = callService(methodname, request);

            Vector<SoapPrimitive> res = (Vector<SoapPrimitive>) response;

            for (int i = 0; i < res.size(); i++) {
                list.add(res.get(i).getValue().toString());
            }
        } catch (Exception q) {
            q.printStackTrace();
        }
        return list;
    }

    public void setProductCategory(int idProduct, String keyword) {
        String methodname = "setProductCategory";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_product", idProduct);
        request.addProperty("keyword", keyword);

        try {
            SoapPrimitive res = (SoapPrimitive) callService(methodname, request);
        } catch (Exception q) {
            q.printStackTrace();
        }
    }

    public String getProductCategory(int idProduct) {
        String cat = "Aucun";
        String methodname = "getProductCategory";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id_product", idProduct);

        try {
            SoapPrimitive results = (SoapPrimitive) callService(methodname, request);

            if (results == null)
                return null;

            cat = results.getValue().toString();
        } catch (Exception q) {
            q.printStackTrace();
        }
        return cat;
    }

    public List<Double> getPriceByProduct(int idProduct) {
        List<Double> listPrices = new ArrayList<>();
        String methodname = "getPriceByProduct";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", idProduct);

        try {
            Object response = callService(methodname, request);

            if (response instanceof Vector) {
                Vector<SoapPrimitive> results = (Vector<SoapPrimitive>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapPrimitive res = results.get(i);
                    listPrices.add(Double.parseDouble(res.toString()));
                }
            } else if (response instanceof SoapPrimitive) {
                SoapPrimitive result = (SoapPrimitive) response;
                listPrices.add(Double.parseDouble(result.toString()));
            }
        } catch (Exception q) {
            q.printStackTrace();
        }
        return listPrices;
    }

    public List<StoreSell> getProductByStore(int idStore) {
    List<StoreSell> listStoreSells = new ArrayList<>();
        String methodname = "getProductByStore";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", idStore);

        try {
            Object response = callService(methodname, request);

            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    listStoreSells.add(ConverterManager.convertToStoreSell(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listStoreSells.add(ConverterManager.convertToStoreSell(result));
            }
        } catch (Exception q) {
            q.printStackTrace();
        }

        return listStoreSells;
    }

    public List<UserSell> getProductByUser(int idUser) {
        List<UserSell> listUserSells = new ArrayList<>();
        String methodname = "getProductByUser";
        SoapObject request = new SoapObject(NAMESPACE, methodname);
        request.addProperty("id", idUser);

        try {
            Object response = callService(methodname, request);

            if (response instanceof Vector) {
                Vector<SoapObject> results = (Vector<SoapObject>) response;
                int length = results.size();
                for (int i = 0; i < length; ++i) {
                    SoapObject res = results.get(i);
                    listUserSells.add(ConverterManager.convertToUserSell(res));
                }
            } else if (response instanceof SoapObject) {
                SoapObject result = (SoapObject) response;
                listUserSells.add(ConverterManager.convertToUserSell(result));
            }
        } catch (Exception q) {
            q.printStackTrace();
        }

        return listUserSells;
    }
}
