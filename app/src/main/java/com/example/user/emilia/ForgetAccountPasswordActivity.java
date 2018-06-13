package com.example.user.emilia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.emilia.model.GetUser;
import com.example.user.emilia.model.PostUser;
import com.example.user.emilia.model.User;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetAccountPasswordActivity extends AppCompatActivity {
    EditText txtEmail;
    Button btnSubit;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_account_password);
        setTitle("Forgot Password");
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        txtEmail = findViewById(R.id.txtEmail_forgetaccountpassword);
        btnSubit = findViewById(R.id.btnSubmit_forgetaccountpassword);
        btnSubit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtEmail.getText().toString().isEmpty()){
                    Toast.makeText(ForgetAccountPasswordActivity.this, "Fill email address", Toast.LENGTH_SHORT).show();
                }else{
                    final String email = txtEmail.getText().toString();
                    if (isValidEmail(email)==true){
                        Call<GetUser> userCall = mApiInterface.getUser(email);
                        userCall.enqueue(new Callback<GetUser>() {
                            @Override
                            public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                                List<User> UserList = response.body().getListDataUser();
                                if(UserList.size()>0){
                                    Call<PostUser> postResetCall = mApiInterface.postReset(email, "password");
                                    postResetCall.enqueue(new Callback<PostUser>() {
                                        @Override
                                        public void onResponse(Call<PostUser> call, Response<PostUser> response) {
                                            finish();
                                            Toast.makeText(ForgetAccountPasswordActivity.this, "A new password has been sent to your email", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<PostUser> call, Throwable t) {
                                            Log.d("Reset Retrofit", t.getMessage());
                                            Toast.makeText(ForgetAccountPasswordActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(ForgetAccountPasswordActivity.this, "Email address not registered", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<GetUser> call, Throwable t) {
                                Toast.makeText(ForgetAccountPasswordActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(ForgetAccountPasswordActivity.this, "Email address not valid", Toast.LENGTH_SHORT).show();
                    }
                }
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
