package com.example.wrsw.api;

import com.example.wrsw.models.Team;
import com.example.wrsw.models.Tournamet;
import com.example.wrsw.models.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonApi {

    @Headers("contentType:application/json")

    //TORNEOS
    @GET("tournaments")
    Call<List<Tournamet>> getTournamet(
            @Header("x-auth-token") String token
    );

    @FormUrlEncoded
    @POST("tournaments")
    Call<Tournamet> createTournament(
            @Header("x-auth-token") String token,
            @Field("name") String name
    );

    @FormUrlEncoded
    @PUT("tournaments/{id}")
    Call<Tournamet> editTournament(
            @Header("x-auth-token") String token,
            @Path("id") String idTournament,
            @Field("name") String name
    );

    @PUT("tournaments/desactivate/{id}")
    Call<Tournamet> desactivateTournament(
            @Header("x-auth-token") String token,
            @Path("id") String id
    );

    //EQUIPOS
    @GET("teams/{id}")
    Call<List<Team>> getTeam(
            @Header("x-auth-token") String token,
            @Path("id") String idTeam
    );

    @FormUrlEncoded
    @POST("teams")
    Call<Team> createTeam(
            @Header("x-auth-token") String token,
            @Field("name") String name,
            @Field("tournament") String tournament
    );

    @FormUrlEncoded
    @PUT("teams/{id}")
    Call<Team> editTeam(
            @Header("x-auth-token") String token,
            @Path("id") String idTeam,
            @Field("name") String name
    );

    @PUT("temas/desactivate/{id}")
    Call<Team> descativateTeam(
            @Header("x-auth-token") String token,
            @Path("id") String idTeam
    );

    //JUGADORES



    //LOGIN USUARIO
    @FormUrlEncoded
    @POST("auth")
    Call<User> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );


    //CREAR NUEVO USUARIO
    @FormUrlEncoded
    @POST("organizers")
    Call<ResponseBody> createUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name
    );
}
