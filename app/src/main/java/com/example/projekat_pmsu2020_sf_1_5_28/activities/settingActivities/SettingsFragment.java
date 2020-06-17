package com.example.projekat_pmsu2020_sf_1_5_28.activities.settingActivities;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;

import com.example.projekat_pmsu2020_sf_1_5_28.R;

public class SettingsFragment extends PreferenceFragment {

    public static SettingsFragment newInstance() { return new SettingsFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }
}
