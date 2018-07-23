package com.example.user.emilia.rest;
import com.example.user.emilia.model.GetAdmin;
import com.example.user.emilia.model.GetAdminDevice;
import com.example.user.emilia.model.GetHistory;
import com.example.user.emilia.model.GetNugen;
import com.example.user.emilia.model.GetPrimaryDevice;
import com.example.user.emilia.model.GetRegisteredDevice;
import com.example.user.emilia.model.GetSecondaryDevice;
import com.example.user.emilia.model.GetUser;
import com.example.user.emilia.model.PostAdminDevice;
import com.example.user.emilia.model.PostPrimaryDevice;
import com.example.user.emilia.model.PostSecondaryDevice;
import com.example.user.emilia.model.PostUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
//  ======= User management =======
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
//  ===========================
//  ======= Admin device ======
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
//    ===========================
//    ====== Member Device ======
    @GET("latestaccess")
    Call<GetHistory> getLatestAccess(@retrofit2.http.Query("email") String email);
    @GET("memberdeviceman")
    Call<GetPrimaryDevice> getPrimaryDevice(@retrofit2.http.Query("email") String email);
    @GET("memberdeviceman")
    Call<GetSecondaryDevice> getSecondaryDevice(@retrofit2.http.Query("email") String email,
                                                @retrofit2.http.Query("level") Integer level);
    @GET("accesshistory")
    Call<GetHistory> getHistory(@retrofit2.http.Query("dvc_id") String dvc_id);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostPrimaryDevice> postAddPrimaryDevice( @Field("own_dvc_id") String dvc_id,
                                                  @Field("own_email") String password,
                                                  @Field("action") String action);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostPrimaryDevice> postAuthPrimaryDevice( @Field("dvc_id") String dvc_id,
                                                   @Field("dvc_password") String password,
                                                   @Field("action") String action);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostPrimaryDevice> postIdcheckPrimaryDevice( @Field("dvc_id") String dvc_id,
                                                      @Field("action") String action);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostPrimaryDevice> postEditNamePrimaryDevice( @Field("dvc_id") String dvc_id,
                                                       @Field("dvc_name") String dvc_name,
                                                       @Field("action") String action,
                                                       @Field("part") String part);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostPrimaryDevice> postEditPasswordPrimaryDevice(  @Field("dvc_id") String dvc_id,
                                                            @Field("dvc_password") String dvc_password,
                                                            @Field("action") String action,
                                                            @Field("part") String part);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostPrimaryDevice> postAddScPasswordPrimaryDevice(  @Field("dvc_id") String dvc_id,
                                                            @Field("dvc_password_sc") String dvc_password,
                                                            @Field("action") String action);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostPrimaryDevice> postUnlockPrimaryDevice( @Field("email") String email,
                                                     @Field("dvc_id") String dvc_id,
                                                     @Field("dvc_password") String password,
                                                     @Field("action") String action);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostSecondaryDevice> postUnlockSecondaryDevice( @Field("email") String email,
                                                         @Field("dvc_id") String dvc_id,
                                                         @Field("dvc_password_sc") String password,
                                                         @Field("action") String action);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostPrimaryDevice> postLockPrimaryDevice( @Field("email") String email,
                                                     @Field("dvc_id") String dvc_id,
                                                     @Field("action") String action);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostSecondaryDevice> postLockSecondaryDevice( @Field("email") String email,
                                                         @Field("dvc_id") String dvc_id,
                                                         @Field("action") String action);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostPrimaryDevice> postDeletePrimaryDevice( @Field("dvc_id") String dvc_id,
                                                     @Field("action") String action);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostPrimaryDevice> postForgetPasswordPrimaryDevice( @Field("email") String email,
                                                             @Field("password") String password,
                                                             @Field("dvc_id") String dvc_id,
                                                             @Field("dvc_password") String dvc_password,
                                                             @Field("action") String action);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostSecondaryDevice> postAddSecondaryDevice(@Field("own_dvc_id") String dvc_id,
                                                     @Field("own_email") String email,
                                                     @Field("action") String action);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostSecondaryDevice> postAuthSecondaryDevice( @Field("dvc_id") String dvc_id,
                                                       @Field("dvc_password_sc") String password,
                                                       @Field("action") String action);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostSecondaryDevice> postIdcheckSecondaryDevice( @Field("dvc_id") String dvc_id,
                                                          @Field("action") String action);
    @FormUrlEncoded
    @POST("memberdeviceman")
    Call<PostSecondaryDevice> postDeleteSecondaryDevice( @Field("dvc_id") String dvc_id,
                                                         @Field("email") String email,
                                                         @Field("action") String action);
//    ===========================
}
