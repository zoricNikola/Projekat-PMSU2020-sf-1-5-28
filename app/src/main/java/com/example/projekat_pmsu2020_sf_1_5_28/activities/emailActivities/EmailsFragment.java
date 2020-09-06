package com.example.projekat_pmsu2020_sf_1_5_28.activities.emailActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.adapters.EmailsAdapter;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Email;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Message;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.service.ServiceUtils;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.Mokap;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailsFragment extends Fragment {

    public static EmailsFragment newInstance() { return new EmailsFragment(); }
    private EmailClientService mService;
    private EmailsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_emails, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mService = ((MainActivity) getActivity()).getEmailClientService();
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerViewEmails);
        mAdapter = new EmailsAdapter(getContext(), new ArrayList<Message>(), (MainActivity) getContext());
        recyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton fab = getActivity().findViewById(R.id.fabEmail);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startCreateEmailActivity();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_menu, menu);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCurrentFragment(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.Emails));

        SharedPreferences sharedPreferences = ((MainActivity) getActivity()).getSharedPreferences();
        Long currentAccountId = sharedPreferences.getLong("currentAccountId", 0);

        Call<List<Message>> call = mService.getAccountMessages(currentAccountId);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.code() == 200) {
                    List<Message> messages = response.body();
                    mAdapter.updateItems(messages);
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {

            }
        });
    }

    public void startCreateEmailActivity() {
        Toast.makeText(getActivity(), "Create email", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), CreateEmailActivity.class);
        startActivity(intent);
    }
}
