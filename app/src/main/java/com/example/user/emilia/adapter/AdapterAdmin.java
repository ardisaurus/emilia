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
import android.widget.Toast;

import com.example.user.emilia.FragmentAdminAdminList;
import com.example.user.emilia.MainActivity;
import com.example.user.emilia.R;
import com.example.user.emilia.model.Admin;
import com.example.user.emilia.model.PostUser;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        final ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        final Admin admindevice = mAdmin.get(position);
        holder.email.setText(admindevice.getEmail());
        holder.name.setText(admindevice.getName());
        holder.dob.setText(admindevice.getDob());
        if (admindevice.getActive().equals("1")){
            holder.active.setText("Confirmed");
        }else{
            holder.active.setText("Not confirmed");
        }
        holder.deleteAdmin.setVisibility(View.GONE);
        if (admindevice.getActive().equals("0")){
            holder.deleteAdmin.setVisibility(View.VISIBLE);
            holder.deleteAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.ma);
                    builder.setMessage("Are you sure to delete this account?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Call<PostUser> postSettingDeleteCall = mApiInterface.postSettingDelete(admindevice.getEmail(), "delete");
                                    postSettingDeleteCall.enqueue(new Callback<PostUser>() {
                                        @Override
                                        public void onResponse(Call<PostUser> call, Response<PostUser> response) {
                                            FragmentAdminAdminList.faal.refresh();
                                            Toast.makeText(MainActivity.ma, "Admin has been delete", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<PostUser> call, Throwable t) {
                                            Toast.makeText(MainActivity.ma, "Connection fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });
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
