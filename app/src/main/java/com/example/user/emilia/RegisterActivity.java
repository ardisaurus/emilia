package com.example.user.emilia;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.emilia.model.GetUser;
import com.example.user.emilia.model.PostUser;
import com.example.user.emilia.model.User;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    Button btnPick, btnRegister;
    EditText txtEmail, txtDob, txtName, txtPassword1, txtPassword2;
    private int mYear, mMonth, mDay;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        txtEmail = findViewById(R.id.txtEmail_register);
        txtName = findViewById(R.id.txtName_register);
        txtPassword1 = findViewById(R.id.txtPassword1_register);
        txtPassword2 = findViewById(R.id.txtPassword2_register);
        txtDob = findViewById(R.id.txtDob_register);
        btnPick = findViewById(R.id.btnPickDate_register);
        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                txtDob.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnRegister = findViewById(R.id.btnRegister_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtName.getText().toString().isEmpty()==true || txtEmail.getText().toString().isEmpty()==true || txtPassword1.getText().toString().isEmpty()==true || txtPassword2.getText().toString().isEmpty()==true || txtDob.getText().toString().isEmpty()==true){
                    Toast.makeText(RegisterActivity.this, "Make sure to fill every form", Toast.LENGTH_SHORT).show();
                }else{
                    final String name = txtName.getText().toString();
                    final String email = txtEmail.getText().toString();
                    final String password1 = txtPassword1.getText().toString();
                    final String password2 = txtPassword2.getText().toString();
                    final String dob = txtDob.getText().toString();
                    if (isValidEmail(email)==true){
                        Call<GetUser> userCall = mApiInterface.getUser(email);
                        userCall.enqueue(new Callback<GetUser>() {
                            @Override
                            public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                                List<User> UserList = response.body().getListDataUser();
                                if(UserList.size()>0){
                                    Toast.makeText(RegisterActivity.this, "Email address has been used", Toast.LENGTH_SHORT).show();
                                }else{
                                    if (password1.length()>=8 && password1.length()<=12) {
                                        if (password1.equals(password2) == true) {
                                            if (name.length() <= 4) {
                                                Toast.makeText(RegisterActivity.this, "Name need to be 5 or more character", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Call<PostUser> postKontakCall = mApiInterface.postUser(email, name, md5(password1), dob, "0", "insert");
                                                postKontakCall.enqueue(new Callback<PostUser>() {
                                                    @Override
                                                    public void onResponse(Call<PostUser> call, Response<PostUser> response) {
                                                        finish();
                                                        Toast.makeText(MainActivity.ma, "Check your email to confirm registration", Toast.LENGTH_SHORT).show();
                                                    }
                                                    @Override
                                                    public void onFailure(Call<PostUser> call, Throwable t) {
                                                        Toast.makeText(RegisterActivity.this, "Internet connection fail", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }else{
                                            Toast.makeText(RegisterActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Password need to be between 8 to 12 character", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<GetUser> call, Throwable t) {
                                Log.e("Retrofit Get", t.toString());
                            }
                        });
                    }else{
                        Toast.makeText(RegisterActivity.this, "Insert valid email address", Toast.LENGTH_SHORT).show();
                    }
                }

           }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public String md5(String s) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(),0,s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }
}
