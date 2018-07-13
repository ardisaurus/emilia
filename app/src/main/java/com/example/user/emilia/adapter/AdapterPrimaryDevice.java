package com.example.user.emilia.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.emilia.DeviceEditActivity;
import com.example.user.emilia.DeviceHistoryActivity;
import com.example.user.emilia.DeviceUnlockActivity;
import com.example.user.emilia.FragmentDevicePrimary;
import com.example.user.emilia.MainActivity;
import com.example.user.emilia.R;
import com.example.user.emilia.SessionManager;
import com.example.user.emilia.model.PostPrimaryDevice;
import com.example.user.emilia.model.PrimaryDevice;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        final PrimaryDevice primarydevice = mPrimaryDevice.get(position);
        String lockStatus;
        SessionManager session;session = new SessionManager(MainActivity.ma);
        HashMap<String, String> user = session.getUserDetails();
        final String email = user.get(SessionManager.KEY_EMAIL);
        holder.dvc_id.setText(primarydevice.getDvc_id());
        holder.dvc_name.setText(primarydevice.getDvc_name());
        final ApiInterface mApiInterface;
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        if (primarydevice.getDvc_status().equals("1")){
            lockStatus = "Open";
            holder.btnUnlock.setText("Lock");
            holder.btnUnlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Call<PostPrimaryDevice> postSecondaryDeviceCall = mApiInterface.postLockPrimaryDevice(email, primarydevice.getDvc_id(), "lock");
                    postSecondaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                        @Override
                        public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                            FragmentDevicePrimary.fdp.refresh();
                            Toast.makeText(MainActivity.ma, "Device Locked", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                            Toast.makeText(MainActivity.ma, "Connection fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }else{
            lockStatus = "Close";
            holder.btnUnlock.setText("Unlock");
            holder.btnUnlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.ma, DeviceUnlockActivity.class);
                    i.putExtra("dvc_id",primarydevice.getDvc_id());
                    MainActivity.ma.startActivity(i);
                }
            });
        }
        holder.dvc_status.setText(lockStatus);
        holder.btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.ma, DeviceHistoryActivity.class);
                i.putExtra("dvc_id",primarydevice.getDvc_id());
                MainActivity.ma.startActivity(i);
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.ma, DeviceEditActivity.class);
                i.putExtra("dvc_id",primarydevice.getDvc_id());
                MainActivity.ma.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPrimaryDevice.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dvc_id, dvc_name, dvc_status;
        Button btnUnlock, btnHistory, btnEdit;
        public ViewHolder(View itemView) {
            super(itemView);
            dvc_id = (TextView) itemView.findViewById(R.id.lblPrimaryDeviceid_list);
            dvc_name = (TextView) itemView.findViewById(R.id.lblPrimaryDeviceName_list);
            dvc_status = (TextView) itemView.findViewById(R.id.lblPrimaryDeviceStatus_list);
            btnUnlock = itemView.findViewById(R.id.btnLockPrimaryDevice_list);
            btnHistory = itemView.findViewById(R.id.btnHistoryPrimaryDevice_list);
            btnEdit = itemView.findViewById(R.id.btnEditPrimaryDevice_list);
        }
    }
}
