package com.example.wrsw.api;

import com.example.wrsw.models.Team;
import com.example.wrsw.models.Tournamet;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface JsonApi {

    @Headers("contentType:application/json")

    //GET request
    @GET("tournaments")
    Call<List<Tournamet>> getTournamet();

    @GET("teams")
    Call<List<Team>> getTeam();

    @FormUrlEncoded
    @POST("organizers")
    Call<ResponseBody> createUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name
    );
}
