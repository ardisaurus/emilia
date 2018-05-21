package com.example.user.emilia;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.emilia.adapter.SectionPageAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static MainActivity ma;

    private SectionPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;

    private Boolean adminLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ma=this;

        adminLevel = true;

        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container1);
        setupViewPager(mViewPager, adminLevel);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager, adminLevel);

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
