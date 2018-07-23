package com.example.user.emilia;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.emilia.model.PostUser;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingDobActivity extends AppCompatActivity {
    ApiInterface mApiInterface;
    SessionManager session;
    Button btnSubmit, btnPick;
    TextView txtDob;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_dob);
        setTitle("Setting : Dob");
        session = new SessionManager(getApplicationContext());
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        btnSubmit = findViewById(R.id.btnSubmit_settingdob);
        txtDob = findViewById(R.id.txtDob_settingdob);
        btnPick = findViewById(R.id.btnPickdate_settingdob);
        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(SettingDobActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtDob.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtDob.getText().toString().isEmpty()){
                    Toast.makeText(SettingDobActivity.this, "Fill date of birth", Toast.LENGTH_SHORT).show();
                }else{
                    HashMap<String, String> user = session.getUserDetails();
                    final String email = user.get(SessionManager.KEY_EMAIL);
                    final String dob = txtDob.getText().toString();
                    Call<PostUser> postSettingDobCall = mApiInterface.postSettingDob(email, dob, "update", "dob");
                    postSettingDobCall.enqueue(new Callback<PostUser>() {
                        @Override
                        public void onResponse(Call<PostUser> call, Response<PostUser> response) {
                            finish();
                            SettingActivity.sa.recreate();
                            Toast.makeText(SettingActivity.sa, "Date of birth has been changed", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<PostUser> call, Throwable t) {
                            Toast.makeText(SettingDobActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
