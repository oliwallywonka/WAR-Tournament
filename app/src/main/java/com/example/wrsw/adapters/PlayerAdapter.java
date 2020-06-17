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

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.EmptyHolder> {

    List<Player> players;
    String token;
    Context context;

    public PlayerAdapter(Context context, List<Player> players,String token){
        this.context = context;
        this.token = token;
        this.players = players;
    }

    @Override
    public EmptyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.player_card, parent, false);
        return new PlayerAdapter.EmptyHolder(view);
    }

    @Override
    public void onBindViewHolder(EmptyHolder holder, final int position) {
        TextView nick = holder.nick;
        TextView age = holder.age;
        Button delete = holder.delete;
        Button edit = holder.edit;
        nick.setText(players.get(position).getNick());
        age.setText(players.get(position).getAge());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePlayerDialog(players.get(position).get_id(),position);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPlayerDialog(position,players.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
            return players.size();
            }

    class EmptyHolder extends RecyclerView.ViewHolder {
        TextView nick,age;
        Button edit,delete;
        public EmptyHolder(View itemView) {
            super(itemView);
            nick = itemView.findViewById(R.id.playerNick);
            age = itemView.findViewById(R.id.playerAge);
            edit = itemView.findViewById(R.id.btnPlayerEdit);
            delete = itemView.findViewById(R.id.btnPlayerDelete);
        }
    }

    private void editPlayerDialog(final int position , final Player player){
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

        nick.setText(player.getNick());
        age.setText(player.getAge());

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
                        .editPlayer(token,player.get_id(),nick.getText().toString().trim(),age.getText().toString().trim());

                call.enqueue(new Callback<Player>() {
                    @Override
                    public void onResponse(Call<Player> call, Response<Player> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(context,"Error en la accion",Toast.LENGTH_SHORT).show();
                        }
                        if(response.code()<209){
                            player.setNick(nick.getText().toString().trim());
                            player.setAge(age.getText().toString().trim());
                            editAt(position,player);
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

    private void deletePlayerDialog(final String id,final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ELIMINAR");
        builder.setTitle("¿Quieres eliminar a este jugador?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<Player> call = RetrofitClient.getInstance()
                        .getApi()
                        .desactivatePlayer(token,id);
                call.enqueue(new Callback<Player>() {
                    @Override
                    public void onResponse(Call<Player> call, Response<Player> response) {
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
                    public void onFailure(Call<Player> call, Throwable t) {
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

    public void removeAt(int position){
        players.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,players.size());
    }

    public void editAt(int position,Player player){
        players.set(position,player);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }
}
