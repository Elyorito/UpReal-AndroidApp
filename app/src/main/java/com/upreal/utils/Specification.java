package com.upreal.utils;

/**
 * Created by Eric on 18/09/2015.
 */
public class Specification {

    private int id;
    private int idProduct;
    private int idProperty;
    private String value;

    Specification() {}

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(int idProperty) {
        this.idProperty = idProperty;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
