package com.upreal.uprealwear.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.upreal.uprealwear.utils.User;

/**
 * Created by Elyo on 05/03/2015.
 */
public class SessionManagerUser {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context mContext;

    public SessionManagerUser(Context context) {
        this.mContext = context;
        sharedPreferences = this.mContext.getApplicationContext().getSharedPreferences("UpRealPref", 0);
        editor = sharedPreferences.edit();
    }

    public boolean isLogged() {
        boolean isL = sharedPreferences.getBoolean("isLogged", false);
        if (isL)
            return true;
        return false;
    }

    public void setRegisterLoginUser(String login, String password) {
        editor.putString("username", login);
        editor.putString("password", password);
        editor.putBoolean("isLogged", true);
        editor.commit();
    }

    public void login() {
        editor.putBoolean("isLogged", true);
        editor.commit();
    }

    public void setUser(User user) {
        editor.putInt("id", user.getId());
        editor.putString("username", user.getUsername());
        editor.putString("firstName", user.getFirstname());
        editor.putString("lastName", user.getLastname());
        editor.putString("email", user.getEmail());
        editor.putInt("phone", user.getPhone());
        editor.putInt("id_address", user.getIdAddress());
        editor.putString("short_desc", user.getShortDesc());
        editor.commit();
    }

    public int getUserId() {
        return sharedPreferences.getInt("id", -1);
    }

    public String[] getRegisterLoginUser() {
        String tab[] = new String[2];

        tab[0] = sharedPreferences.getString("username", null);
        tab[1] = sharedPreferences.getString("password", null);

        return tab;
    }

    public User getUser() {
        User user = new User();

        user.setId(sharedPreferences.getInt("id", -1));
        user.setUsername(sharedPreferences.getString("username", null));
        user.setFirstname(sharedPreferences.getString("firstName", null));
        user.setLastname(sharedPreferences.getString("lastName", null));
        user.setEmail(sharedPreferences.getString("email", null));
        user.setPhone(sharedPreferences.getInt("phone", -1));
        user.setIdAddress(sharedPreferences.getInt("id_address", -1));
        user.setShortDesc(sharedPreferences.getString("short_desc", null));
        return user;
    }

    public void deleteCurrentUser() {
        editor.clear();
        editor.putBoolean("isLogged", false);
        editor.commit();
    }

}
