package com.example.user.emilia.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.emilia.R;
import com.example.user.emilia.model.History;

import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {
    public List<History> mHistory;
    public AdapterHistory(List<History> mHistory) {
        this.mHistory=mHistory;
    }

    @Override
    public AdapterHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHistory.ViewHolder holder, int position) {
        History history = mHistory.get(position);
        holder.lblEmail.setText(history.getEmail());
        holder.lblName.setText(history.getName());
        holder.lblTime.setText(history.getDate()+" / "+history.getTime());
    }

    @Override
    public int getItemCount() {
        return mHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView lblEmail, lblName, lblTime;
        public ViewHolder(View itemView) {
            super(itemView);
            lblEmail = itemView.findViewById(R.id.lblEmailHistory_list);
            lblName = itemView.findViewById(R.id.lblNameHistory_list);
            lblTime = itemView.findViewById(R.id.lblTimeHistory_list);
        }
    }
}
