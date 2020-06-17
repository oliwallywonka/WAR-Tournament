package com.example.wrsw.api;

import com.example.wrsw.models.Player;
import com.example.wrsw.models.Team;
import com.example.wrsw.models.Tournamet;
import com.example.wrsw.models.User;

import java.util.Date;
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
            @Field("name") String name,
            @Field("date") String date
    );

    @FormUrlEncoded
    @PUT("tournaments/{id}")
    Call<Tournamet> editTournament(
            @Header("x-auth-token") String token,
            @Path("id") String idTournament,
            @Field("name") String name,
            @Field("date") String date
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
            @Path("id") String idTournament
    );

    @FormUrlEncoded
    @POST("teams")
    Call<Team> createTeam(
            @Header("x-auth-token") String token,
            @Field("name") String name,
            @Field("tournament") String tournamentId
    );

    @FormUrlEncoded
    @PUT("teams/{id}")
    Call<Team> editTeam(
            @Header("x-auth-token") String token,
            @Path("id") String idTeam,
            @Field("name") String name
    );

    @PUT("teams/desactivate/{id}")
    Call<Team> desactivateTeam(
            @Header("x-auth-token") String token,
            @Path("id") String idTeam
    );

    //JUGADORES
    @GET("players/{id}")
    Call<List<Player>> getPlayers(
            @Header("x-auth-token") String token,
            @Path("id") String idTeam
    );

    @FormUrlEncoded
    @POST("players/team")
    Call<Player> createPlayer(
            @Header("x-auth-token") String token,
            @Field("team") String idTeam,
            @Field("nick") String nick,
            @Field("age") String age
    );

    @FormUrlEncoded
    @PUT("players/{id}")
    Call<Player> editPlayer(
            @Header("x-auth-token") String token,
            @Path("id") String idPlayer,
            @Field("nick") String nick,
            @Field("age") String age
    );

    @PUT("players/desactivate/{id}")
    Call<Player> desactivatePlayer(
            @Header("x-auth-token") String token,
            @Path("id") String id
    );

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
