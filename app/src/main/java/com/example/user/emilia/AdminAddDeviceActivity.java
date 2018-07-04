package com.example.user.emilia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.emilia.model.AdminDevice;
import com.example.user.emilia.model.GetAdminDevice;
import com.example.user.emilia.model.GetNugen;
import com.example.user.emilia.model.Nugen;
import com.example.user.emilia.model.PostAdminDevice;
import com.example.user.emilia.model.PostUser;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAddDeviceActivity extends AppCompatActivity {

    TextView lblDvc_id;
    Button btnSubmit;
    EditText txtPassword1, txtPassword2;
    private List<Nugen> mNugen = new ArrayList<>();
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_device);
        setTitle("Add Device");
        lblDvc_id = findViewById(R.id.lblDeviceid_adminadddevice);
        txtPassword1 = findViewById(R.id.txtPassword1_adminadddevice);
        txtPassword2 = findViewById(R.id.txtPassword2_adminadddevice);
        btnSubmit = findViewById(R.id.btnSubmit_adminadddevice);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        final List<Nugen> mNugen = new ArrayList<>();
        Call<GetNugen> nugenDevice = mApiInterface.getNugenId();
        nugenDevice.enqueue(new Callback<GetNugen>() {
            @Override
            public void onResponse(Call<GetNugen> call, Response<GetNugen> response) {
                List<Nugen> NugenList = response.body().getListNugen();
                lblDvc_id.setText(NugenList.get(0).getDvc_id().toString());
            }

            @Override
            public void onFailure(Call<GetNugen> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toast.makeText(AdminAddDeviceActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtPassword1.getText().toString().isEmpty() || txtPassword2.getText().toString().isEmpty()){
                    Toast.makeText(AdminAddDeviceActivity.this, "Fill every available form", Toast.LENGTH_SHORT).show();
                }else{
                    String dvc_id = lblDvc_id.getText().toString();
                    final String password1 = txtPassword1.getText().toString();
                    String password2 = txtPassword2.getText().toString();
                    if (password1.length()>=8 && password1.length()<=12) {
                        if (password1.equals(password2)) {
                            Call<PostAdminDevice> postAdminDeviceCall = mApiInterface.postAddAdminDevice(dvc_id, md5(password1), "insert");
                            postAdminDeviceCall.enqueue(new Callback<PostAdminDevice>() {
                                @Override
                                public void onResponse(Call<PostAdminDevice> call, Response<PostAdminDevice> response) {
                                    FragmentAdminDevice.fad.refresh();
                                    finish();
                                    Toast.makeText(MainActivity.ma, "Device been added", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<PostAdminDevice> call, Throwable t) {
                                    Toast.makeText(AdminAddDeviceActivity.this, "New passwords don't match", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(AdminAddDeviceActivity.this, "New passwords don't match", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(AdminAddDeviceActivity.this, "Password need to be between 8 to 12 character", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private static String md5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }
}
