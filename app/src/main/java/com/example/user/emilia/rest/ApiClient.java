package com.example.user.emilia.rest;
import com.example.user.emilia.AddressSessionManager;
import com.example.user.emilia.MainActivity;

import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    static AddressSessionManager addressSession = new AddressSessionManager(MainActivity.ma);
    static HashMap<String, String> address = addressSession.getUserDetails();
    static String saved = address.get(AddressSessionManager.KEY_ADDRESS);
    public static String BASE_URL = "http://"+saved+"/emilia-server/index.php/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
