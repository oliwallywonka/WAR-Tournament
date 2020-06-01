package com.example.wrsw;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface JsonApi {

    @Headers("contentType:application/json")

    //GET request
    @GET("tournaments")
    Call<List<Tournamet>> getTournamet();

    @GET("teams")
    Call<List<Team>> getTeam();
}
