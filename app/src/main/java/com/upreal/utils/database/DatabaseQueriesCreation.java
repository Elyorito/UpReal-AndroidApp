package com.upreal.utils.database;

/**
 * Created by Nunkh on 21/05/15.
 */
public class DatabaseQueriesCreation {

    public static final String ProductCreate = new String (
            "CREATE TABLE Product (name TEXT, ean TEXT, picture TEXT, brand TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT);"
    );

    public static final String AddressCreate = new String (
            "CREATE TABLE address (id INTEGER PRIMARY KEY AUTOINCREMENT, address TEXT, address2 TEXT, city TEXT, country TEXT, postal_code INTEGER)"
    );

    public static final String UserCreate = new String (
            "CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, firstame TEXT, lastname TEXT, email TEXT, password TEXT, id_address INTEGER, roles TEXT, short_desc TEXT, picture TEXT, FOREIGN KEY(id_address) REFERENCES address)"
    );

    public static final String ListsCreate = new String (
            "CREATE TABLE lists (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, public INTEGER, nb_items INTEGER, id_user INTEGER, FOREIGN KEY(id_user) REFERENCES user)"
    );

    public static final String ItemsCreate = new String (
            "CREATE TABLE items (id INTEGER PRIMARY KEY AUTOINCREMENT, id_list INTEGER, id_product INTEGER, id_user INTEGER, FOREIGN KEY(id_list) REFERENCES lists, FOREIGN KEY(id_product) REFERENCES product, FOREIGN KEY(id_user) REFERENCES user)"
    );
}
