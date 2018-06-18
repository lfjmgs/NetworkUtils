package com.lfjmgs.networkutils;

import com.lfjmgs.networkutils.net.Response;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiService {

    String BASE_URL = "http://45.63.61.144:10000";

    @GET("/ipinfo")
    Observable<IPInfo> getIPInfo();

    @GET("/apierr")
    Observable<Response> apiErr();
}
