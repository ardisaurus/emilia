package com.example.user.emilia.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.emilia.R;
import com.example.user.emilia.model.RegisteredDevice;

import java.util.List;

public class AdapterRegisteredDevice extends RecyclerView.Adapter<AdapterRegisteredDevice.ViewHolder> {
    public List<RegisteredDevice> mRegisteredDevice;
    public AdapterRegisteredDevice(List<RegisteredDevice> mRegisteredDevice) {
        this.mRegisteredDevice=mRegisteredDevice;
    }

    @NonNull
    @Override
    public AdapterRegisteredDevice.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_registered_device, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRegisteredDevice.ViewHolder holder, int position) {
        RegisteredDevice registereddevice = mRegisteredDevice.get(position);
        holder.lbldvc_id.setText(registereddevice.getDvc_id());
        holder.lblemail.setText(registereddevice.getEmail());
    }

    @Override
    public int getItemCount() {
        return mRegisteredDevice.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView lbldvc_id, lblemail;
        public ViewHolder(View itemView) {
            super(itemView);
            lbldvc_id = itemView.findViewById(R.id.lblDeviceIdRegistered_list);
            lblemail = itemView.findViewById(R.id.lblEmailRegistered_list);
        }
    }
}
