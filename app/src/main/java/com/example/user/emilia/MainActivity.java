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

public class MainActivity extends AppCompatActivity {
    public static MainActivity ma;
    SessionManager session;
    private SectionPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton btnAdd, btnRegistered;
    private EditText txtEmail_login, txtPassword_login;
    private TextView lblForgotPassword_login, lblSignUp_login;
    private Button btnLogin;
    private Boolean adminLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());
        ma=this;
        Boolean status = session.isLoggedIn();
        if(status==true){
            setContentView(R.layout.activity_main);

            adminLevel = false;

            btnAdd = findViewById(R.id.btnAdd_main);
            btnRegistered = findViewById(R.id.btnRegistered_main);
            if(adminLevel==false){
                btnRegistered.setVisibility(View.GONE);
            }

            mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());

            mViewPager = (ViewPager) findViewById(R.id.container1);
            setupViewPager(mViewPager, adminLevel);

            final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager, adminLevel);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int tabPosition = tabLayout.getSelectedTabPosition();
                    if (adminLevel==true){
                        if (tabPosition==0){
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
                        if (tabPosition==1){
                            btnAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getApplicationContext(), AdminAddAdminActivity.class);
                                    startActivity(i);
                                }
                            });
                        }
                    }else{
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
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    int tabPosition = tabLayout.getSelectedTabPosition();
                    if (adminLevel==true && tabPosition==0){
                        btnRegistered.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }else{
            setContentView(R.layout.activity_login);
            txtEmail_login = (EditText) findViewById(R.id.txtEmail_login);
            txtPassword_login = (EditText) findViewById(R.id.txtPassword_login);
            btnLogin = (Button) findViewById(R.id.btnLogin_login);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    String username = txtEmail_login.getText().toString();
                    String password = txtPassword_login.getText().toString();
                    if(username.trim().length() > 0 && password.trim().length() > 0){
                        if(username.equals("test") && password.equals("test")){
                            session.createLoginSession("Android Hive", "anroidhive@gmail.com");
                            recreate();
                        }else{
                            Toast.makeText(getApplicationContext(), "Login fail", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Insert username and password", Toast.LENGTH_LONG).show();
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
        if (adminLevel==true){
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
        if(status==true) {
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
}
