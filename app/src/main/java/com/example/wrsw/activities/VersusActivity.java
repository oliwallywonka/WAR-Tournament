package com.example.wrsw.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Toast;

import com.example.wrsw.R;
import com.example.wrsw.adapters.EmptyAdapter;
import com.example.wrsw.adapters.VersusAdapter;
import com.example.wrsw.api.RetrofitClient;
import com.example.wrsw.models.Team;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VersusActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    List<Team> teams;
    String token;
    String idTournament;

    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);
        recyclerView = (RecyclerView) findViewById(R.id.versusRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new EmptyAdapter());
        token = getIntent().getStringExtra("token");
        idTournament = getIntent().getStringExtra("idTournament");
        getTeam(token,idTournament,this);

    }

    private void getTeam(final String token, String idTournament, final Context context){
        Call<List<Team>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getTeam(token,idTournament);
        call.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
                }
                if (response.code()<209){
                    List<Team> teams = response.body();
                    RecyclerView.Adapter adapter;
                    adapter = new VersusAdapter(token,teams,context);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {
                Toast.makeText(context,t.toString()+"123",Toast.LENGTH_SHORT).show();
            }
        });
    };

    @Override
    public void onClick(View v) {

    }
}
