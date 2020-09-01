package com.example.projekat_pmsu2020_sf_1_5_28.activities.folderActivities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.projekat_pmsu2020_sf_1_5_28.R;

public class RuleDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_rule, container, false);
    }

}
