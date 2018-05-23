package com.example.user.emilia.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.emilia.DeviceUnlockSecondaryActivity;
import com.example.user.emilia.MainActivity;
import com.example.user.emilia.R;
import com.example.user.emilia.model.SecondaryDevice;

import java.util.List;

public class AdapterSecondaryDevice extends RecyclerView.Adapter<AdapterSecondaryDevice.ViewHolder> {
    public List<SecondaryDevice> mSecondaryDevice;
    public AdapterSecondaryDevice(List<SecondaryDevice> mSecondaryDevice) {
        this.mSecondaryDevice=mSecondaryDevice;
    }

    @NonNull
    @Override
    public AdapterSecondaryDevice.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_secondary_device, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSecondaryDevice.ViewHolder holder, int position) {
        SecondaryDevice secondarydevice = mSecondaryDevice.get(position);
        holder.dvc_id.setText(secondarydevice.getDvc_id());
        holder.dvc_name.setText(secondarydevice.getDvc_name());
        holder.dvc_status.setText(secondarydevice.getDvc_status());
        holder.btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.ma, DeviceUnlockSecondaryActivity.class);
                MainActivity.ma.startActivity(i);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mSecondaryDevice.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dvc_id, dvc_name, dvc_status;
        Button btnLock, btnDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            dvc_id = (TextView) itemView.findViewById(R.id.lblSecondaryDeviceid_list);
            dvc_name = (TextView) itemView.findViewById(R.id.lblSecondaryDeviceName_list);
            dvc_status = (TextView) itemView.findViewById(R.id.lblSecondaryDeviceStatus_list);
            btnLock = itemView.findViewById(R.id.btnLockSecondaryDevice_list);
            btnDelete = itemView.findViewById(R.id.btnDeleteSecondaryDevice_list);
        }
    }
}
