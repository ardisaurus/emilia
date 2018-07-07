package com.example.user.emilia.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.emilia.DeviceAddSecondaryActivity;
import com.example.user.emilia.DeviceUnlockSecondaryActivity;
import com.example.user.emilia.FragmentDeviceSecondary;
import com.example.user.emilia.MainActivity;
import com.example.user.emilia.R;
import com.example.user.emilia.SessionManager;
import com.example.user.emilia.model.PostSecondaryDevice;
import com.example.user.emilia.model.SecondaryDevice;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        final SecondaryDevice secondarydevice = mSecondaryDevice.get(position);
        String lockStatus;
        SessionManager session;
        final ApiInterface mApiInterface;
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        session = new SessionManager(MainActivity.ma);
        HashMap<String, String> user = session.getUserDetails();
        final String email = user.get(SessionManager.KEY_EMAIL);
        holder.dvc_id.setText(secondarydevice.getDvc_id());
        holder.dvc_name.setText(secondarydevice.getDvc_name());
        if (secondarydevice.getDvc_status().equals("1")){
            lockStatus = "Open";
        }else{
            lockStatus = "Close";
        }
        holder.dvc_status.setText(lockStatus);
        holder.btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.ma, DeviceUnlockSecondaryActivity.class);
                i.putExtra("dvc_id",secondarydevice.getDvc_id());
                MainActivity.ma.startActivity(i);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.ma);
                builder.setMessage("Are you sure to delete this device?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Call<PostSecondaryDevice> postSecondaryDeviceCall = mApiInterface.postDeleteSecondaryDevice(secondarydevice.getDvc_id(), email ,"delete_sc");
                                postSecondaryDeviceCall.enqueue(new Callback<PostSecondaryDevice>() {
                                    @Override
                                    public void onResponse(Call<PostSecondaryDevice> call, Response<PostSecondaryDevice> response) {
                                        FragmentDeviceSecondary.fds.refresh();
                                        Toast.makeText(MainActivity.ma, "Device has been deleted", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<PostSecondaryDevice> call, Throwable t) {
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
