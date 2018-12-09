package com.example.user.emilia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.emilia.model.Crypto;
import com.example.user.emilia.model.GetCrypto;
import com.example.user.emilia.model.PostCrypto;
import com.example.user.emilia.model.PostPrimaryDevice;
import com.example.user.emilia.model.PostSecondaryDevice;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceEditSecondaryAccessActivity extends AppCompatActivity {
    TextView lblDelete;
    EditText txtOldPassword, txtNewPassword1, txtNewPassword2;
    Button btnSubmit;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Edit Device : Secondary Access");
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        final String dvc_id = i.getStringExtra("dvc_id");
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postIdcheckPrimaryDevice(dvc_id, "sc_check");
        postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
            @Override
            public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                if (response.body().getmPrimaryDevice().getStatus().equals("success")){
                    setContentView(R.layout.activity_device_edit_secondary_access);
                    txtOldPassword = findViewById(R.id.txtOldpassword_deviceeditsecondaryaccess);
                    txtNewPassword1 = findViewById(R.id.txtNewpassword1_deviceeditsecondaryaccess);
                    txtNewPassword2 = findViewById(R.id.txtNewpassword2_deviceeditsecondaryaccess);
                    btnSubmit = findViewById(R.id.btnSubmit_deviceeditsecondaryaccess);
                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (txtOldPassword.getText().toString().isEmpty() || txtNewPassword1.getText().toString().isEmpty() || txtNewPassword2.getText().toString().isEmpty()){
                                Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Fill every available form", Toast.LENGTH_SHORT).show();
                            }else{
                                final String OldPassword = txtOldPassword.getText().toString();
                                final String NewPassword1 = txtNewPassword1.getText().toString();
                                String NewPassword2 = txtNewPassword2.getText().toString();
                                if (OldPassword.length()==16 && NewPassword1.length()==16){
                                    if (NewPassword1.equals(NewPassword2)){
                                        Call<PostCrypto> postCryptoCall = mApiInterface.postRequest(dvc_id, "create");
                                        postCryptoCall.enqueue(new Callback<PostCrypto>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                String public_key= response.body().getmCrypto().getPublic_key();
                                                String modulo = response.body().getmCrypto().getModulo();
                                                final String session_id = response.body().getmCrypto().getSession_id();
                                                final String aes_key=Aes.getSaltString();
                                                Call<PostPrimaryDevice> postSecondaryDeviceCall = mApiInterface.postEncryptionCheck(dvc_id, "encryption_check");
                                                postSecondaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                                    @Override
                                                    public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                                        final String dvc_encryption = response.body().getmPrimaryDevice().getDvc_encryption();
                                                        final String cipheraes = Aes.encrypt(OldPassword, aes_key, dvc_encryption);
                                                        Call<GetCrypto> postCryptoCall = mApiInterface.getCrypto(session_id, aes_key);
                                                        postCryptoCall.enqueue(new Callback<GetCrypto>() {
                                                            @Override
                                                            public void onResponse(Call<GetCrypto> call, Response<GetCrypto> response) {
                                                                final List<Crypto> CryptoList = response.body().getListDataCrypto();
                                                                final String cipherrsa = CryptoList.get(0).getCipher();
                                                                Call<PostSecondaryDevice> postPrimaryDeviceCall = mApiInterface.postAuthSecondaryDevice(dvc_id, session_id, cipheraes, cipherrsa, "auth_sc");
                                                                postPrimaryDeviceCall.enqueue(new Callback<PostSecondaryDevice>() {
                                                                    @Override
                                                                    public void onResponse(Call<PostSecondaryDevice> call, Response<PostSecondaryDevice> response) {
                                                                        if(response.body().getmSecondaryDevice().getStatus().equals("success")){
                                                                            Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postAddScPasswordPrimaryDevice(dvc_id, NewPassword1 ,"insert_sc_key");
                                                                            postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                                                                @Override
                                                                                public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                                                                    Call<PostCrypto> postDeleteSessionCall = mApiInterface.postDeleteSession(session_id, "delete_session");
                                                                                    postDeleteSessionCall.enqueue(new Callback<PostCrypto>() {
                                                                                        @Override
                                                                                        public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                                                            finish();
                                                                                            Toast.makeText(DeviceEditActivity.dea, "Secondary password has been changed", Toast.LENGTH_SHORT).show();
                                                                                        }

                                                                                        @Override
                                                                                        public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                                                            Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Session not removed", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                                                    Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                        }else{
                                                                            Call<PostCrypto> postDeleteSessionCall = mApiInterface.postDeleteSession(session_id, "delete_session");
                                                                            postDeleteSessionCall.enqueue(new Callback<PostCrypto>() {
                                                                                @Override
                                                                                public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                                                    Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                                                    Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Session not removed", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<PostSecondaryDevice> call, Throwable t) {
                                                                        Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                            }

                                                            @Override
                                                            public void onFailure(Call<GetCrypto> call, Throwable t) {
                                                                Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{
                                        Toast.makeText(DeviceEditSecondaryAccessActivity.this, "New passwords don't match", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Password need to be between 8 to 12 character", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                    lblDelete = findViewById(R.id.lblDelete_deviceeditsecondaryaccess);
                    lblDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DeviceEditSecondaryAccessActivity.this);
                            builder.setMessage("Are you sure to delete secondary access?")
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postDeletePrimaryDevice(dvc_id, "delete_sc_key");
                                            postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                                @Override
                                                public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                                    finish();
                                                    Toast.makeText(DeviceEditActivity.dea, "Secondary access has been deleted", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                    Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
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
                }else{
                    setContentView(R.layout.activity_device_edit_secondary_access_add);
                    txtOldPassword = findViewById(R.id.txtOldpassword_deviceeditsecondaryaccessadd);
                    txtNewPassword1 = findViewById(R.id.txtNewpassword1_deviceeditsecondaryaccessadd);
                    txtNewPassword2 = findViewById(R.id.txtNewpassword2_deviceeditsecondaryaccessadd);
                    btnSubmit = findViewById(R.id.btnSubmit_deviceeditsecondaryaccessadd);
                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (txtOldPassword.getText().toString().isEmpty() || txtNewPassword1.getText().toString().isEmpty() || txtNewPassword2.getText().toString().isEmpty()){
                                Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Fill every available form", Toast.LENGTH_SHORT).show();
                            }else{
                                final String OldPassword = txtOldPassword.getText().toString();
                                final String NewPassword1 = txtNewPassword1.getText().toString();
                                String NewPassword2 = txtNewPassword2.getText().toString();
                                if (OldPassword.length()==16 && NewPassword1.length()==16){
                                    if (NewPassword1.equals(NewPassword2)){
                                        Call<PostCrypto> postCryptoCall = mApiInterface.postRequest(dvc_id, "create");
                                        postCryptoCall.enqueue(new Callback<PostCrypto>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                String public_key= response.body().getmCrypto().getPublic_key();
                                                String modulo = response.body().getmCrypto().getModulo();
                                                final String session_id = response.body().getmCrypto().getSession_id();
                                                final String aes_key=Aes.getSaltString();
                                                Call<PostPrimaryDevice> postSecondaryDeviceCall = mApiInterface.postEncryptionCheck(dvc_id, "encryption_check");
                                                postSecondaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                                    @Override
                                                    public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                                        final String dvc_encryption = response.body().getmPrimaryDevice().getDvc_encryption();
                                                        final String cipheraes = Aes.encrypt(OldPassword, aes_key, dvc_encryption);
                                                        Call<GetCrypto> postCryptoCall = mApiInterface.getCrypto(session_id, aes_key);
                                                        postCryptoCall.enqueue(new Callback<GetCrypto>() {
                                                            @Override
                                                            public void onResponse(Call<GetCrypto> call, Response<GetCrypto> response) {
                                                                final List<Crypto> CryptoList = response.body().getListDataCrypto();
                                                                final String cipherrsa = CryptoList.get(0).getCipher();
                                                                Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postAuthPrimaryDevice( dvc_id, session_id, cipheraes, cipherrsa, "auth");
                                                                postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                                                    @Override
                                                                    public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                                                        if(response.body().getmPrimaryDevice().getStatus().equals("success")){
                                                                            Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postAddScPasswordPrimaryDevice(dvc_id, NewPassword1 ,"insert_sc_key");
                                                                            postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                                                                @Override
                                                                                public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                                                                    Call<PostCrypto> postDeleteSessionCall = mApiInterface.postDeleteSession(session_id, "delete_session");
                                                                                    postDeleteSessionCall.enqueue(new Callback<PostCrypto>() {
                                                                                        @Override
                                                                                        public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                                                            finish();
                                                                                            Toast.makeText(DeviceEditActivity.dea, "Secondary password has been added", Toast.LENGTH_SHORT).show();
                                                                                        }

                                                                                        @Override
                                                                                        public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                                                            Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Session not removed", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                                                    Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                        }else{
                                                                            Call<PostCrypto> postDeleteSessionCall = mApiInterface.postDeleteSession(session_id, "delete_session");
                                                                            postDeleteSessionCall.enqueue(new Callback<PostCrypto>() {
                                                                                @Override
                                                                                public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                                                    Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Wrong primary password", Toast.LENGTH_SHORT).show();
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                                                    Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Session not removed", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                                        Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                            }

                                                            @Override
                                                            public void onFailure(Call<GetCrypto> call, Throwable t) {
                                                                Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                        Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{
                                        Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Secondary passwords don't match", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Password need to be 16 character", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                Toast.makeText(DeviceEditSecondaryAccessActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
