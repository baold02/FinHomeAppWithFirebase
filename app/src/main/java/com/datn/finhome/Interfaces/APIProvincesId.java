package com.datn.finhome.Interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIProvincesId {
    String URL = "https://provinces.open-api.vn/api/";
    @GET("?depth=3")
    Call<String> getState();

    @GET("p/{code}?depth=2")
    Call<String> getDistrict(@Path("code") int sCode);


}
