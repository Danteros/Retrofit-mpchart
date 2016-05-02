package com.danteros.retropostmixera;


import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Login {

    @Multipart
    @POST("/en/auth/login/")
        Call<Object> login (@Part("login") RequestBody login,@Part("password") RequestBody password);

}
