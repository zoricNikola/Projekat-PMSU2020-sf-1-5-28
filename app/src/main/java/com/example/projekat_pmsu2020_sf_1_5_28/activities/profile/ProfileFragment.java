package com.example.projekat_pmsu2020_sf_1_5_28.activities.profile;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.service.ServiceUtils;

public class ProfileFragment extends Fragment {

    private TextView mUsername, mFirstName, mLastName, mDisplayName, mEmail;

    public static ProfileFragment newInstance() {return new ProfileFragment();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUsername = getActivity().findViewById(R.id.username);
        mFirstName = getActivity().findViewById(R.id.firstName);
        mLastName = getActivity().findViewById(R.id.lastName);
        mDisplayName = getActivity().findViewById(R.id.displayName);
        mEmail = getActivity().findViewById(R.id.contactEmail);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(ServiceUtils.PREFERENCES_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String firstName = sharedPreferences.getString("firstName", "");
        String lastName = sharedPreferences.getString("lastName", "");
        String currentAccountDisplayName = sharedPreferences.getString("currentAccountDisplayName", "");
        String currentAccountEmail = sharedPreferences.getString("currentAccountDisplayName", "");

        mUsername.setText(mUsername.getText().toString().trim() + ": " + username);
        mFirstName.setText(mFirstName.getText().toString().trim() + ": " + firstName);
        mLastName.setText(mLastName.getText().toString().trim() + ": " + lastName);
        mDisplayName.setText(mDisplayName.getText().toString().trim() + ": " + currentAccountDisplayName);
        mEmail.setText(mEmail.getText().toString().trim() + ": " + currentAccountEmail);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.profile_fragment_menu, menu);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_switch_account:
                Toast.makeText(getContext(),"Switch account", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCurrentFragment(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
    }

}
