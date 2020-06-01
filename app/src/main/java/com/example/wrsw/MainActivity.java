package com.example.wrsw;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String BASE_URL = "https://aqueous-dusk-09996.herokuapp.com/api/";
    private TextView mJsonTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mJsonTxtView = findViewById(R.id.jsonText);
        //getTournamentFromApi();
        getTeamFromApi();
    }

    private void getTeamFromApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApi jsonPlaceHolderApi = retrofit.create(JsonApi.class);

        Call<List<Team>> call = jsonPlaceHolderApi.getTeam();

        call.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {

                if(!response.isSuccessful()){
                    mJsonTxtView.setText("Codigo: "+response.code());
                    return;
                }

                List<Team> teamList = response.body();

                for(Team team: teamList){
                    String content = "";
                    content += "userId:"+ team.getId() + "\n";
                    content += "name:"+ team.getName() + "\n";
                    content += "logp:"+ team.getLogo() + "\n";
                    content += "status:"+ team.getStatus() + "\n\n";
                    mJsonTxtView.append(content);

                }


            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {
                mJsonTxtView.setText(t.getMessage());

            }
        });
    }
   /* private void getTournamentFromApi(){
        Call<List<Tournamet>> call = jsonapi.getTournamet();
        call.enqueue(new Callback<List<Tournamet>>() {
            @Override
            public void onResponse(Call<List<Tournamet>> call, Response<List<Tournamet>> response) {
                if(response == null){
                    Log.e("MaintActivit",response.code()+"");
                    return;
                }


                List<Tournamet> tournamentList = response.body();
                for(Tournamet tournamet : tournamentList){
                    String id = tournamet.get_id();
                    String organizer = tournamet.getOrganizer();
                    String name = tournamet.getName();
                    String banner = tournamet.getBanner();

                    TextView ptv = findViewById(R.id.res);
                    ptv.setText(id+"\n"+name+"\n"+banner+"\n");
                }
            }

            @Override
            public void onFailure(Call<List<Tournamet>> call, Throwable t) {
                Log.e("MainActivity","failo intento de obtener torneos"+t.getMessage());
            }
        });
    }*/
}
