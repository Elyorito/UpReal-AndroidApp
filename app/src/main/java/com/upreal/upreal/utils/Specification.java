package com.upreal.upreal.utils;

/**
 * Created by Eric on 18/09/2015.
 */
public class Specification {

    private int idProduct;
    private String fieldName;
    private String fielDesc;

    Specification() {}

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFielDesc() {
        return fielDesc;
    }

    public void setFielDesc(String fielDesc) {
        this.fielDesc = fielDesc;
    }
}
