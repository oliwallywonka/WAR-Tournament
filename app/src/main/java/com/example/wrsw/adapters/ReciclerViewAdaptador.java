package com.example.wrsw.adapters;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wrsw.R;
import com.example.wrsw.activities.MainActivity;
import com.example.wrsw.api.RetrofitClient;
import com.example.wrsw.models.Tournamet;

import java.util.List;

public class ReciclerViewAdaptador extends RecyclerView.Adapter<ReciclerViewAdaptador.ViewHolder> {

    Context context;
    List<Tournamet> tournamets;

    String token;

    public ReciclerViewAdaptador (Context context ,List<Tournamet> tournaments,String token){
        this.token = token;
        this.context = context;
        this.tournamets = tournaments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.tournament_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        TextView name = holder.name1;
        TextView date = holder.date1;
        TextView status = holder.status1;
        Button delete = holder.delete;
        Button edit = holder.edit;
        name.setText(tournamets.get(position).getName());
        date.setText(tournamets.get(position).getDate());
        status.setText(tournamets.get(position).getStatus());
        delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialog(tournamets.get(position).get_id(),position);

                    }
                }
        );
        edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog(position,tournamets.get(position));
                        System.out.println("EDITARRRRRR"+position);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return tournamets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView tournamentCard;
        TextView name1,date1,status1;
        Button delete,edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tournamentCard = itemView.findViewById(R.id.tournamentCard);
            name1 =itemView.findViewById(R.id.tournamentName);
            date1 = itemView.findViewById(R.id.tournamentDate);
            status1 = itemView.findViewById(R.id.tournamentStatus);
            delete = itemView.findViewById(R.id.btnDelete);
            edit = itemView.findViewById(R.id.btnEdit);
        }

    }
    private void deleteDialog(final String id,final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ELIMINAR");
        builder.setTitle("¿Quieres eliminar este torneo con todos sus datos asociados?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<Tournamet> call = RetrofitClient.getInstance()
                        .getApi()
                        .desactivateTournament(token,id);
                call.enqueue(new Callback<Tournamet>() {
                    @Override
                    public void onResponse(Call<Tournamet> call, Response<Tournamet> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(context,"Error en la accion",Toast.LENGTH_SHORT).show();
                        }

                        if(response.code()<209){
                            Toast.makeText(context,"Eliminado correctamente",Toast.LENGTH_SHORT).show();
                            removeAt(position);
                        }else{
                            Toast.makeText(context,"Error intente mas tarde"+response.body(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Tournamet> call, Throwable t) {
                        Toast.makeText(context,"Error en el servidor intente mas tarde",Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context,"Cancelando",Toast.LENGTH_SHORT).show();
            }
        }).show();
    }

    private void editDialog(final int position, final Tournamet tournamet){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_edit_tournament_dialog,null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText name = view.findViewById(R.id.tournamentDialogName);
        name.setText(tournamet.getName());
        Button cancel , save;

        cancel = view.findViewById(R.id.btnTournamentCancel);
        save = view.findViewById(R.id.btnTournamentSave);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Tournamet> call = RetrofitClient.getInstance()
                        .getApi()
                        .editTournament(token,tournamet.get_id(),name.getText().toString().trim());

                call.enqueue(new Callback<Tournamet>() {
                    @Override
                    public void onResponse(Call<Tournamet> call, Response<Tournamet> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(context,"Error en la accion",Toast.LENGTH_SHORT).show();
                        }

                        if(response.code()<209){
                            tournamet.setName(name.getText().toString().trim());
                            editAt(position,tournamet);
                            Toast.makeText(context,"Editado correctamente",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context,"Error intente mas tarde"+response.body(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Tournamet> call, Throwable t) {
                        Toast.makeText(context,"Error en el servidor intente mas tarde",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        });
    }

    public void removeAt(int position){
        tournamets.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,tournamets.size());
    }

    public void editAt(int position,Tournamet tournamet){
        tournamets.set(position,tournamet);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }
}
