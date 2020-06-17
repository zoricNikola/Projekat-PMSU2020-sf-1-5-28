package com.example.projekat_pmsu2020_sf_1_5_28.activities.settingActivities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.example.projekat_pmsu2020_sf_1_5_28.R;

public class SettingsActivity extends PreferenceActivity {

    private Toolbar mToolbar;
    private PreferenceFragment mSettingsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setToolbar();

        mSettingsFragment = SettingsFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, mSettingsFragment).commit();
    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setTitle(R.string.Settings);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
