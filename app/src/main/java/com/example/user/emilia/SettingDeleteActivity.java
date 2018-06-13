package com.example.user.emilia;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

public class SettingDeleteActivity extends AppCompatActivity {
    ApiInterface mApiInterface;
    SessionManager session;
    Button btnSubmit;
    EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_delete);
        setTitle("Delete Account");
        session = new SessionManager(getApplicationContext());
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        txtPassword = findViewById(R.id.txtPassword_settingdelete);
        btnSubmit = findViewById(R.id.btnSubmit_settingdelete);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingDeleteActivity.this);
                builder.setMessage("Are you sure to delete your account?")
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
}
