package com.upreal.upreal.utils;

/**
 * Created by Elyo on 11/04/2015.
 */
public class User {
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    /*private java.sql.Timestamp create_time;*/
    private int phone;
    private int id_address;
    private String roles;
    private String short_desc;
    /*private int active;*/

    public User() {}

    public User(int id, String username, String firstname, String lastname, String email, int phone, int id_address, String roles, String short_desc){
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.id_address = id_address;
        this.roles = roles;
        this.short_desc = short_desc;
    }

    public User(int id, String username) {
        this.username = username;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getId_address() {
        return id_address;
    }

    public void setId_address(int id_address) {
        this.id_address = id_address;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

}