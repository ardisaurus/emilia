package com.example.user.emilia;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class AddressSessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "EmiliaAddress";
    private static final String IS_SET = "IsSetIn";
    public static final String KEY_ADDRESS = "address";

    public AddressSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setAddress(String address){
        editor.putBoolean(IS_SET, true);
        editor.putString(KEY_ADDRESS, address);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));
        return user;
    }

    public void unsetAddress(){
        editor.putBoolean(IS_SET, false);
        editor.clear();
        editor.commit();
    }

    public boolean isSetIn(){
        return pref.getBoolean(IS_SET, false);
    }
}
