package com.example.wrsw.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wrsw.R;
import com.example.wrsw.api.RetrofitClient;
import com.example.wrsw.models.Player;
import com.example.wrsw.models.Team;
import com.example.wrsw.models.Tournamet;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.EmptyHolder> {

    Context context;
    Tournamet tournamet;
    List<Team> teams;
    String token;

    public TeamAdapter (Context context , List<Team> teams, Tournamet tournament, String token){
        this.token = token;
        this.context = context;
        this.tournamet = tournament;
        this.teams = teams;
    }

    @Override
    public EmptyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.teams_card, parent, false);
        return new TeamAdapter.EmptyHolder(view);
    }

    @Override
    public void onBindViewHolder(EmptyHolder holder, final int position) {
        TextView name = holder.name;
        TextView status = holder.status;
        Button delete = holder.delete;
        Button edit = holder.edit;
        Button players = holder.showPlayers;
        Button addPlayer = holder.addPlayer;
        final ConstraintLayout dropdownPlayer = holder.dropdownPlayer;
        final RecyclerView recyclerView = holder.recyclerView;
        final RecyclerView.LayoutManager layoutManager;

        name.setText(teams.get(position).getName());
        status.setText(teams.get(position).getStatus().toString());
        layoutManager = new LinearLayoutManager(context);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTeamDialog(position,teams.get(position));
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog(teams.get(position).getId(),position);
            }
        });

        players.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dropdownPlayer.getVisibility() == View.GONE){
                    dropdownPlayer.setVisibility(View.VISIBLE);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    Call<List<Player>> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .getPlayers(token,teams.get(position).getId());
                    call.enqueue(new Callback<List<Player>>() {
                        @Override
                        public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                            if(!response.isSuccessful()){
                                Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
                            }
                            if (response.code()<209){
                                List<Player> players = response.body();
                                RecyclerView.Adapter adapter;
                                adapter = new PlayerAdapter(context,players,token);
                                recyclerView.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Player>> call, Throwable t) {
                            Toast.makeText(context,t.toString()+"123",Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    dropdownPlayer.setVisibility(View.GONE);

                }
            }
        });

        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPlayerDialog(teams.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
            return teams.size();
    }

    class EmptyHolder extends RecyclerView.ViewHolder {
        TextView name,status;
        Button edit,delete ,addPlayer,showPlayers;
        ConstraintLayout dropdownPlayer;
        RecyclerView recyclerView;
        public EmptyHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.teamName);
            status = itemView.findViewById(R.id.teamStatus);
            edit = itemView.findViewById(R.id.btnTeamEdit);
            delete = itemView.findViewById(R.id.btnTeamDelete);
            addPlayer = itemView.findViewById(R.id.btnAddPlayer);
            showPlayers = itemView.findViewById(R.id.btnPlayers);
            dropdownPlayer = itemView.findViewById(R.id.dropdownPlayer);
            recyclerView = itemView.findViewById(R.id.playersReciclerView);

        }
    }

    private void editTeamDialog(final int position, final Team team){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_edit_team_dialog,null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final EditText name = view.findViewById(R.id.teamDialogName);
        name.setText(team.getName());
        Button cancel = view.findViewById(R.id.btnTeamCancel);
        Button save = view.findViewById(R.id.btnTeamSave);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Team> call = RetrofitClient.getInstance()
                        .getApi()
                        .editTeam(token,team.getId(),name.getText().toString().trim());

                call.enqueue(new Callback<Team>() {
                    @Override
                    public void onResponse(Call<Team> call, Response<Team> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(context,"Error en la accion",Toast.LENGTH_SHORT).show();
                        }

                        if(response.code()<209){
                            team.setName(name.getText().toString().trim());
                            Toast.makeText(context,"Equipo editado correctamente",Toast.LENGTH_SHORT).show();
                            editAt(position,team);
                        }else{
                            Toast.makeText(context,"Error intente mas tarde"+response,Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Team> call, Throwable t) {
                        Toast.makeText(context,"Error de conexion intente mas tarde",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        });
    }

    private void deleteDialog(final String id,final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ELIMINAR");
        builder.setTitle("¿Quieres eliminar este equipo con todos sus datos asociados?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<Team> call = RetrofitClient.getInstance()
                        .getApi()
                        .desactivateTeam(token,id);
                call.enqueue(new Callback<Team>() {
                    @Override
                    public void onResponse(Call<Team> call, Response<Team> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(context,"Error en la accion",Toast.LENGTH_SHORT).show();
                        }

                        if(response.code()<209){
                            Toast.makeText(context,"Eliminado correctamente",Toast.LENGTH_SHORT).show();
                            removeAt(position);
                        }
                        if(response.code()>299){
                            Toast.makeText(context,"Error intente mas tarde"+response,Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Team> call, Throwable t) {
                        Toast.makeText(context,"Error en el servidor intente mas tarde",Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    private void createPlayerDialog(final Team team){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_edit_player_dialog,null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText nick = view.findViewById(R.id.playerDialogNick);
        final EditText age = view.findViewById(R.id.playerDialogAge);
        Button cancel = view.findViewById(R.id.btnPlayerCancel);
        Button save = view.findViewById(R.id.btnPlayerSave);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Player> call = RetrofitClient.getInstance()
                        .getApi()
                        .createPlayer(token,team.getId(),nick.getText().toString().trim(),age.getText().toString().trim());

                call.enqueue(new Callback<Player>() {
                    @Override
                    public void onResponse(Call<Player> call, Response<Player> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(context,"Error en la accion",Toast.LENGTH_SHORT).show();
                        }

                        if(response.code()<209){
                            Toast.makeText(context,"Equipo editado correctamente",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context,"Error intente mas tarde"+response,Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Player> call, Throwable t) {
                        Toast.makeText(context,"Error de conexion intente mas tarde",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        });
    }

    public void removeAt(int position){
        teams.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,teams.size());
    }

    public void editAt(int position,Team team){
        teams.set(position,team);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }
}