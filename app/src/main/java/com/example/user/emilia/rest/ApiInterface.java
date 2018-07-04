package com.example.user.emilia.rest;
import com.example.user.emilia.model.GetAdmin;
import com.example.user.emilia.model.GetAdminDevice;
import com.example.user.emilia.model.GetNugen;
import com.example.user.emilia.model.GetRegisteredDevice;
import com.example.user.emilia.model.GetUser;
import com.example.user.emilia.model.Nugen;
import com.example.user.emilia.model.PostAdminDevice;
import com.example.user.emilia.model.PostUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
//    User management
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
    @FormUrlEncoded
    @POST("user")
    Call<PostUser> postSettingEmail(@Field("email") String email,
                                    @Field("new_email") String new_email,
                                    @Field("action") String action,
                                    @Field("part") String part);
    @FormUrlEncoded
    @POST("user")
    Call<PostUser> postSettingDob(@Field("email") String email,
                                  @Field("dob") String dob,
                                  @Field("action") String action,
                                  @Field("part") String part);
    @FormUrlEncoded
    @POST("user")
    Call<PostUser> postSettingName(@Field("email") String email,
                                   @Field("name") String dob,
                                   @Field("action") String action,
                                   @Field("part") String part);
    @FormUrlEncoded
    @POST("user")
    Call<PostUser> postSettingDelete(@Field("email") String email,
                                     @Field("action") String action);
    @FormUrlEncoded
    @POST("user")
    Call<PostUser> postSettingPassword( @Field("email") String email,
                                        @Field("password") String password,
                                        @Field("action") String action,
                                        @Field("part") String part);
    @GET("user")
    Call<GetAdmin> getAdmin(@retrofit2.http.Query("email") String email);
//    ===========

    @GET("admindeviceman")
    Call<GetRegisteredDevice> getRegisteredDevice(@retrofit2.http.Query("ownership") Integer ownership);
    @GET("admindeviceman")
    Call<GetAdminDevice> getAdminDevice();
    @GET("nugen")
    Call<GetNugen> getNugenId();
    @FormUrlEncoded
    @POST("admindeviceman")
    Call<PostAdminDevice> postAddAdminDevice(@Field("dvc_id") String dvc_id,
                                             @Field("dvc_password") String password,
                                             @Field("action") String action);
}
