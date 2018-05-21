package com.example.user.emilia.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.emilia.R;
import com.example.user.emilia.model.Admin;

import java.util.List;

public class AdapterAdmin extends RecyclerView.Adapter<AdapterAdmin.ViewHolder> {
    public List<Admin> mAdmin;
    public AdapterAdmin(List<Admin> mAdmin) {
        this.mAdmin=mAdmin;
    }

    @NonNull
    @Override
    public AdapterAdmin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_admin, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAdmin.ViewHolder holder, int position) {
        Admin admindevice = mAdmin.get(position);
        holder.email.setText(admindevice.getEmail());
        holder.name.setText(admindevice.getName());
        holder.dob.setText(admindevice.getDob());
        holder.active.setText(admindevice.getActive());
    }

    @Override
    public int getItemCount() {
        return mAdmin.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView email, name, dob, active ;
        public ViewHolder(View itemView) {
            super(itemView);
            email = (TextView) itemView.findViewById(R.id.lblEmailAdmin_list);
            name = (TextView) itemView.findViewById(R.id.lblNameAdmin_list);
            dob = (TextView) itemView.findViewById(R.id.lblDobAdmin_list);
            active = (TextView) itemView.findViewById(R.id.lblStatusAdmin_list);
        }
    }
}
