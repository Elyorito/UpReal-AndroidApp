package com.upreal.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Elyo on 11/04/2015.
 */
public class User implements Parcelable {
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
    private String picture;

    /*private int active;*/

    public User() {
        this.id = -1;
        this.username = "";
        this.firstname = "";
        this.lastname = "";
        this.email = "";
        this.phone = -1;
        this.id_address = -1;
        this.roles = "";
        this.short_desc = "";
        this.picture = "";
    }

    public User(int id, String username, String firstname, String lastname, String email, int phone, int id_address, String roles, String short_desc, String picture){
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.id_address = id_address;
        this.roles = roles;
        this.short_desc = short_desc;
        this.picture = picture;
    }

    public User(int id, String username) {
        this.username = username;
        this.id = id;
    }

    public void setUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.id_address = user.getId_address();
        this.roles = user.getRoles();
        this.short_desc = user.getShort_desc();
        this.picture = user.getPicture();
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.username);
        dest.writeString(this.firstname);
        dest.writeString(this.lastname);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeInt(this.phone);
        dest.writeInt(this.id_address);
        dest.writeString(this.short_desc);
        dest.writeString(this.picture);
    }
    public static final Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(Parcel in) {
        this.id = in.readInt();
        this.username = in.readString();
        this.firstname = in.readString();
        this.lastname = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.phone = in.readInt();
        this.id_address = in.readInt();
        this.short_desc = in.readString();
        this.picture = in.readString();
    }
}