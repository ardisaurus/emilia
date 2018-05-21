package com.example.user.emilia.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.emilia.R;
import com.example.user.emilia.model.AdminDevice;

import java.util.List;

public class AdapterAdminDevice extends RecyclerView.Adapter<AdapterAdminDevice.ViewHolder> {
    public List<AdminDevice> mAdminDevice;
    public AdapterAdminDevice(List<AdminDevice> mAdminDevice) {
        this.mAdminDevice=mAdminDevice;
    }

    @NonNull
    @Override
    public AdapterAdminDevice.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_admin_device, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAdminDevice.ViewHolder holder, int position) {
        AdminDevice admindevice = mAdminDevice.get(position);
        holder.dvc_id.setText(admindevice.getDvc_id());
    }

    @Override
    public int getItemCount() {
        return mAdminDevice.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dvc_id;
        public ViewHolder(View itemView) {
            super(itemView);
            dvc_id = (TextView) itemView.findViewById(R.id.lblAdminDeviceid_list);
        }
    }
}
