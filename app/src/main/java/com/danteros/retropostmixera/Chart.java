package com.danteros.retropostmixera;


import retrofit2.Call;
import retrofit2.http.GET;

public interface Chart {
    @GET("/en/test/trust_graphic/")
    Call<ContentItem> getContentItems();
}
