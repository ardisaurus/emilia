package com.example.user.emilia;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.emilia.model.GetUser;
import com.example.user.emilia.model.PostUser;
import com.example.user.emilia.model.User;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingEmailActivity extends AppCompatActivity {
    EditText txtEmail;
    Button btnSubmit;
    ApiInterface mApiInterface;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_email);
        setTitle("Setting : Email");
        session = new SessionManager(getApplicationContext());
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        txtEmail = findViewById(R.id.txtEmail_settingemail);
        btnSubmit = findViewById(R.id.btnSubmit_settingemail);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingEmailActivity.this);
                builder.setMessage("Are you sure to change your email?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (txtEmail.getText().toString().isEmpty()){
                                    Toast.makeText(SettingEmailActivity.this, "Insert new email address", Toast.LENGTH_SHORT).show();
                                }else {
                                    HashMap<String, String> user = session.getUserDetails();
                                    final String email = user.get(SessionManager.KEY_EMAIL);
                                    final String uLevel = user.get(SessionManager.KEY_LEVEL);
                                    final String new_email = txtEmail.getText().toString();
                                    if (isValidEmail(email)==true){
                                        Call<GetUser> userCall = mApiInterface.getUser(new_email);
                                        userCall.enqueue(new Callback<GetUser>() {
                                            @Override
                                            public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                                                List<User> UserList = response.body().getListDataUser();
                                                if(UserList.size()>0){
                                                    Toast.makeText(SettingEmailActivity.this, "Email already used", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Call<PostUser> postSettingEmailCall = mApiInterface.postSettingEmail(email, new_email, "update", "email");
                                                    postSettingEmailCall.enqueue(new Callback<PostUser>() {
                                                        @Override
                                                        public void onResponse(Call<PostUser> call, Response<PostUser> response) {
                                                            session.logoutUser();
                                                            session.createLoginSession(uLevel, new_email);
                                                            finish();
                                                            Toast.makeText(SettingActivity.sa, "Email has been changed", Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<PostUser> call, Throwable t) {
                                                            Toast.makeText(SettingEmailActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<GetUser> call, Throwable t) {
                                                Toast.makeText(SettingEmailActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{
                                        Toast.makeText(SettingEmailActivity.this, "Insert a valid email address", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
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

    private final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
