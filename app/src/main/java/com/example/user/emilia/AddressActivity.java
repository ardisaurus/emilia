package com.example.user.emilia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.util.HashMap;

public class AddressActivity extends AppCompatActivity {
    Button btnSubmit;
    EditText txtAddress;
    AddressSessionManager addressSession;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        setTitle("Server Address");
        addressSession = new AddressSessionManager(getApplicationContext());
        txtAddress =findViewById(R.id.txtAddress_address);
        btnSubmit = findViewById(R.id.btnSubmit_address);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtAddress.getText().toString().isEmpty()){
                    Toast.makeText(AddressActivity.this, "Enter server address", Toast.LENGTH_SHORT).show();
                }else{
                    String serverAddress = txtAddress.getText().toString();
                    addressSession.setAddress(serverAddress);
                    Toast.makeText(AddressActivity.this, "Please restart the application", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
