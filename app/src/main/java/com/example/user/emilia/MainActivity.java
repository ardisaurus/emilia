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
import android.widget.Toast;

import com.example.user.emilia.adapter.SectionPageAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static MainActivity ma;

    private SectionPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton btnAdd, btnRegistered;

    private Boolean adminLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ma=this;

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
