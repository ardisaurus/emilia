package com.example.user.emilia;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.emilia.adapter.SectionPageAdapter;
import com.example.user.emilia.model.GetUser;
import com.example.user.emilia.model.PostUser;
import com.example.user.emilia.model.User;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static MainActivity ma;
    SessionManager session;
    private SectionPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton btnAdd, btnRegistered;
    private EditText txtEmail_login, txtPassword_login;
    private TextView lblForgotPassword_login, lblSignUp_login;
    private Button btnLogin;
    private TabLayout tabLayout;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());
        ma=this;
        Boolean status = session.isLoggedIn();
        if(status){
            mApiInterface = ApiClient.getClient().create(ApiInterface.class);
            HashMap<String, String> user = session.getUserDetails();
            String level = user.get(SessionManager.KEY_LEVEL);
            if (level.equals("admin")) {
                setContentView(R.layout.activity_main_admin);
                btnAdd = findViewById(R.id.btnAdd_mainadmin);
                btnRegistered = findViewById(R.id.btnRegistered_mainadmin);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), AdminAddDeviceActivity.class);
                        startActivity(i);
                    }
                });
                btnRegistered.setVisibility(View.VISIBLE);
                btnRegistered.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), AdminDeviceRegisteredActivity.class);
                        startActivity(i);
                    }
                });

                mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());

                mViewPager = findViewById(R.id.container2);
                setupViewPager(mViewPager, true);

                tabLayout = findViewById(R.id.tabsadmin);
                tabLayout.setupWithViewPager(mViewPager, true);
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        int tabPosition = tabLayout.getSelectedTabPosition();
                        if (tabPosition == 0) {
                            btnAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getApplicationContext(), AdminAddDeviceActivity.class);
                                    startActivity(i);
                                }
                            });
                            btnRegistered.setVisibility(View.VISIBLE);
                            btnRegistered.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getApplicationContext(), AdminDeviceRegisteredActivity.class);
                                    startActivity(i);
                                }
                            });
                        }
                        if (tabPosition == 1) {
                            btnAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getApplicationContext(), AdminAddAdminActivity.class);
                                    startActivity(i);
                                }
                            });
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        int tabPosition = tabLayout.getSelectedTabPosition();
                        if (tabPosition == 0) {
                            btnRegistered.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }else if (level.equals("member")){
                setContentView(R.layout.activity_main);
                btnAdd = findViewById(R.id.btnAdd_main);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), DeviceAddActivity.class);
                        startActivity(i);
                    }
                });
                mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());

                mViewPager = findViewById(R.id.container1);
                setupViewPager(mViewPager, false);

                tabLayout = findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(mViewPager, false);
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        int tabPosition = tabLayout.getSelectedTabPosition();
                            if (tabPosition==0){
                                btnAdd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(getApplicationContext(), DeviceAddActivity.class);
                                        startActivity(i);
                                    }
                                });
                            }
                            if (tabPosition==1){
                                btnAdd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(getApplicationContext(), DeviceAddSecondaryActivity.class);
                                        startActivity(i);
                                    }
                                });
                            }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        }else{
            setContentView(R.layout.activity_login);
            mApiInterface = ApiClient.getClient().create(ApiInterface.class);
            txtEmail_login = findViewById(R.id.txtEmail_login);
            txtPassword_login = findViewById(R.id.txtPassword_login);
            btnLogin = findViewById(R.id.btnLogin_login);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (txtEmail_login.getText().toString().isEmpty() || txtPassword_login.getText().toString().isEmpty()){
                        Toast.makeText(MainActivity.this, "Make sure to fill every form", Toast.LENGTH_SHORT).show();
                    }else {
                        final String email = txtEmail_login.getText().toString();
                        final String password = txtPassword_login.getText().toString();
                        if (isValidEmail(email)){
                            if (password.length()>=8 && password.length()<=12) {
                                Call<GetUser> userCall = mApiInterface.getUser(email);
                                userCall.enqueue(new Callback<GetUser>() {
                                    @Override
                                    public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                                        List<User> UserList = response.body().getListDataUser();
                                        if(UserList.size()>0){
                                            Call<PostUser> postLoginCall = mApiInterface.postLogin(email, md5(password), "auth");
                                            postLoginCall.enqueue(new Callback<PostUser>() {
                                                @Override
                                                public void onResponse(Call<PostUser> call, Response<PostUser> response) {
                                                    if (response.body().getmUser().getStatus().equals("success")){
                                                        Call<GetUser> userCall = mApiInterface.getUser(email);
                                                        userCall.enqueue(new Callback<GetUser>() {
                                                            @Override
                                                            public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                                                                List<User> listUser = response.body().getListDataUser();
                                                                if (listUser.get(0).getActive().equals("1")){
                                                                    String uLevel;
                                                                    if (listUser.get(0).getLevel().equals("1")){
                                                                        uLevel="admin";
                                                                    }else{
                                                                        uLevel="member";
                                                                    }
                                                                    session.createLoginSession(uLevel, email);
                                                                    ma.recreate();
                                                                }else{
                                                                    Toast.makeText(MainActivity.this, "User suspended",Toast.LENGTH_LONG).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<GetUser> call, Throwable t) {
                                                                Toast.makeText(MainActivity.this, "Connection fail",Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                    }else{
                                                        Toast.makeText(MainActivity.this, "Email And Password don't match ", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<PostUser> call, Throwable t) {
                                                    Toast.makeText(MainActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else{
                                            Toast.makeText(MainActivity.this, "Email address not registered", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<GetUser> call, Throwable t) {
                                        Toast.makeText(MainActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                Toast.makeText(MainActivity.this, "Password need to be between 8 to 12 character", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "Insert valid email", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            lblSignUp_login = findViewById(R.id.lblSignup_login);
            lblSignUp_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(i);
                }
            });
            lblForgotPassword_login = findViewById(R.id.lblForget_login);
            lblForgotPassword_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), ForgetAccountPasswordActivity.class);
                    startActivity(i);
                }
            });
        }
    }

    private void setupViewPager(ViewPager viewPager, Boolean adminLevel){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        if (adminLevel){
            adapter.addFragment(new FragmentAdminDevice(), "Device");
            adapter.addFragment(new FragmentAdminAdminList(), "Admins");
        }else{
            adapter.addFragment(new FragmentDevicePrimary(), "Primary");
            adapter.addFragment(new FragmentDeviceSecondary(), "Secondary");
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        Boolean status = session.isLoggedIn();
        if(status) {
            item.setVisible(true);
        }else{
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(i);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
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
