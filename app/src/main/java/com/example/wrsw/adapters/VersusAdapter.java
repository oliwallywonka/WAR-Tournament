package com.example.wrsw.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wrsw.R;
import com.example.wrsw.models.Team;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class VersusAdapter extends  RecyclerView.Adapter<VersusAdapter.EmptyHolder> {

    private Context context;
    private List<Team> teams;
    private String token;

    public VersusAdapter(String token,List<Team> teams, Context context){
        this.token = token;
        this.teams = teams;
        this.context = context;
    }


    @Override
    public EmptyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.team_grid_card, parent, false);
        return new VersusAdapter.EmptyHolder(view);
    }

    @Override
    public void onBindViewHolder(EmptyHolder holder, int position) {
        TextView team1 = holder.team1;
        TextView team2 = holder.team2;
        team1.setText(teams.get(position).getName());
        team2.setText(teams.get(position+1).getName());
    }

    @Override
    public int getItemCount() {
        return teams.size()/2;
    }

    class EmptyHolder extends RecyclerView.ViewHolder {
        TextView team1,team2;
        public EmptyHolder(View itemView) {
            super(itemView);
            team1 = itemView.findViewById(R.id.team1Name);
            team2 = itemView.findViewById(R.id.team2Name);
        }
    }
}