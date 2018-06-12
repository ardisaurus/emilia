package com.example.user.emilia.rest;
import com.example.user.emilia.model.GetUser;
import com.example.user.emilia.model.PostUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @GET("user")
    Call<GetUser> getUser(@retrofit2.http.Query("email") String email);
    @FormUrlEncoded
    @POST("user")
    Call<PostUser> postUser(@Field("email") String email,
                            @Field("name") String name,
                            @Field("password") String password,
                            @Field("dob") String dob,
                            @Field("level") String level,
                            @Field("action") String action);
    @FormUrlEncoded
    @POST("reset")
    Call<PostUser> postReset(@Field("email") String email,
                             @Field("action") String action);
    @FormUrlEncoded
    @POST("user")
    Call<PostUser> postLogin(@Field("email") String email,
                             @Field("password") String password,
                             @Field("action") String action);
}
