package com.example.user.emilia.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.emilia.MainActivity;
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
        holder.deleteAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.ma);
                builder.setMessage("Are you sure to delete this account?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create();
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAdmin.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView email, name, dob, active ;
        Button deleteAdmin;
        public ViewHolder(View itemView) {
            super(itemView);
            email = (TextView) itemView.findViewById(R.id.lblEmailAdmin_list);
            name = (TextView) itemView.findViewById(R.id.lblNameAdmin_list);
            dob = (TextView) itemView.findViewById(R.id.lblDobAdmin_list);
            active = (TextView) itemView.findViewById(R.id.lblStatusAdmin_list);
            deleteAdmin = itemView.findViewById(R.id.btnDeleteAdmin_list);
        }
    }
}
