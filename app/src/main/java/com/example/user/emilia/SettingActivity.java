package com.example.user.emilia;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.emilia.model.GetUser;
import com.example.user.emilia.model.User;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {
    public static SettingActivity sa;
    SessionManager session;
    ApiInterface mApiInterface;
    private TextView lblEmail, lblName, lblDob;
    private Button btnLogout, btnEmail, btnName, btnDob, btnPassword, btnDelete, btnAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("Account Settings");
        sa=this;
        session = new SessionManager(getApplicationContext());
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManager.KEY_EMAIL);
        lblEmail = findViewById(R.id.lblEmail_setting);
        lblEmail.setText("Email : "+email);
        lblName  = findViewById(R.id.lblName_setting);
        lblDob = findViewById(R.id.lblDob_setting);

        Call<GetUser> userCall = mApiInterface.getUser(email);
        userCall.enqueue(new Callback<GetUser>() {
            @Override
            public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                List<User> UserList = response.body().getListDataUser();
                lblName.setText("Name : "+UserList.get(0).getName());
                lblDob.setText("Date of birth : "+UserList.get(0).getDob());
            }

            @Override
            public void onFailure(Call<GetUser> call, Throwable t) {
                Toast.makeText(SettingActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
            }
        });

        btnEmail = findViewById(R.id.btnEmail_setting);
        btnName = findViewById(R.id.btnName_setting);
        btnDob = findViewById(R.id.btnDob_setting);
        btnPassword = findViewById(R.id.btnPassword_setting);
        btnDelete = findViewById(R.id.btnDelete_setting);
        btnAddress = findViewById(R.id.btnAddress_Setting);
        btnLogout = findViewById(R.id.btnLogout_Setting);

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingEmailActivity.class);
                startActivity(i);
            }
        });

        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingNameActivity.class);
                startActivity(i);
            }
        });

        btnDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingDobActivity.class);
                startActivity(i);
            }
        });

        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingPasswordActivity.class);
                startActivity(i);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingDeleteActivity.class);
                startActivity(i);
            }
        });

        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddressActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setMessage("Are you sure to log out yor account?")
                        .setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                session.logoutUser();
                                MainActivity.ma.recreate();
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.create();
                builder.show();
            }
        });
    }
}
