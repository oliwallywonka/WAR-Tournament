package com.example.wrsw.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wrsw.adapters.EmptyAdapter;
import com.example.wrsw.adapters.ReciclerViewAdaptador;
import com.example.wrsw.R;
import com.example.wrsw.api.RetrofitClient;
import com.example.wrsw.models.Team;
import com.example.wrsw.models.Tournamet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mJsonTxtView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mJsonTxtView = findViewById(R.id.jsonText);
        findViewById(R.id.teams).setOnClickListener(this);
        findViewById(R.id.tournament).setOnClickListener(this);
        findViewById(R.id.btnAddTournament).setOnClickListener(this);
        token = getIntent().getStringExtra("token");
        //getTeamFromApi(token);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerTournament);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new EmptyAdapter());

        getTournamentFromApi(token,this);
    }

    private  void getTournamentFromApi(final String token, final Context context){
        Call<List<Tournamet>> call = RetrofitClient.getInstance()
                .getApi()
                .getTournamet(token);
        call.enqueue(new Callback<List<Tournamet>>() {
            @Override
            public void onResponse(Call<List<Tournamet>> call, Response<List<Tournamet>> response) {
                if(!response.isSuccessful()){
                    mJsonTxtView.setText("Codigo: "+response.code());
                    return;
                }
                if(response.code()<209){
                    List<Tournamet> tournamentList = response.body();

                   adapter = new ReciclerViewAdaptador(context,tournamentList,token);
                   recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Tournamet>> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.toString()+"123",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createTournament(final String token,String name,String date){
        Call<Tournamet> call = RetrofitClient.getInstance()
                .getApi()
                .createTournament(token,name,date);
        call.enqueue(new Callback<Tournamet>() {
            @Override
            public void onResponse(Call<Tournamet> call, Response<Tournamet> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(response.code()<209){
                    //adapter.notifyItemRangeInserted();
                    Toast.makeText(MainActivity.this,"Torneo Creado Correctamente",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, MainActivity.class).putExtra("token",token));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Tournamet> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.toString()+"123",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.teams:
                break;
            case R.id.tournament:
                startActivity(new Intent(this, MainActivity.class).putExtra("token",token));
                finish();
                break;
            case R.id.btnAddTournament:
                System.out.println("click crearboton");
                createDialog();
                break;
        }
    }

    private void createDialog(){
        Button cancel , save;
        final EditText name;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View view = inflater.inflate(R.layout.layout_edit_tournament_dialog,null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final Tournamet tournamet = new Tournamet();
        name = view.findViewById(R.id.tournamentDialogName);
        cancel = view.findViewById(R.id.btnTournamentCancel);
        save = view.findViewById(R.id.btnTournamentSave);

        final DatePicker date = view.findViewById(R.id.datepickerTournament);
        Calendar calendar= Calendar.getInstance();
        date.init(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String yearMonthDay = "";
                if(date.getMonth()<10){
                    yearMonthDay = date.getYear()+"-0"+date.getMonth()+"-"+date.getDayOfMonth();
                }
                if(date.getDayOfMonth()<10){
                    yearMonthDay = date.getYear()+"-"+date.getMonth()+"-0"+date.getDayOfMonth();
                }

                tournamet.setCreated(yearMonthDay);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTournament(token,name.getText().toString().trim(),tournamet.getDate());
                dialog.dismiss();
            }
        });


    }
}
