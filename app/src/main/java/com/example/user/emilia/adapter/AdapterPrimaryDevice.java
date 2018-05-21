package com.example.user.emilia.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.emilia.R;
import com.example.user.emilia.model.PrimaryDevice;

import java.util.List;

public class AdapterPrimaryDevice extends android.support.v7.widget.RecyclerView.Adapter<AdapterPrimaryDevice.ViewHolder> {
    public List<PrimaryDevice> mPrimaryDevice;
    public AdapterPrimaryDevice(List<PrimaryDevice> mPrimaryDevice) {
        this.mPrimaryDevice=mPrimaryDevice;
    }

    @NonNull
    @Override
    public AdapterPrimaryDevice.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_primary_device, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPrimaryDevice.ViewHolder holder, int position) {
        PrimaryDevice primarydevice = mPrimaryDevice.get(position);
        holder.dvc_id.setText(primarydevice.getDvc_id());
        holder.dvc_name.setText(primarydevice.getDvc_name());
        holder.dvc_status.setText(primarydevice.getDvc_status());
    }

    @Override
    public int getItemCount() {
        return mPrimaryDevice.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dvc_id, dvc_name, dvc_status;
        public ViewHolder(View itemView) {
            super(itemView);
            dvc_id = (TextView) itemView.findViewById(R.id.lblPrimaryDeviceid_list);
            dvc_name = (TextView) itemView.findViewById(R.id.lblPrimaryDeviceName_list);
            dvc_status = (TextView) itemView.findViewById(R.id.lblPrimaryDeviceStatus_list);
        }
    }
}
