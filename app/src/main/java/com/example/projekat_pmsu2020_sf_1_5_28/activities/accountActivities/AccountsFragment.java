package com.example.projekat_pmsu2020_sf_1_5_28.activities.accountActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.adapters.AccountsAdapter;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Account;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountsFragment extends Fragment {

    public static AccountsFragment newInstance() { return new AccountsFragment(); }

    private EmailClientService mService;
    private AccountsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_accounts, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mService = ((MainActivity) getActivity()).getEmailClientService();
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerViewAccounts);
        mAdapter = new AccountsAdapter(getContext(), new ArrayList<Account>(), (MainActivity) getContext());
        recyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = getActivity().findViewById(R.id.fabAccount);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateContactActivity();
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.Accounts));

        SharedPreferences sharedPreferences = ((MainActivity) getActivity()).getSharedPreferences();
        Long userId = sharedPreferences.getLong("userId", 0);

        Call<List<Account>> call = mService.getUserAccounts(userId);
        call.enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.code() == 200) {
                    List<Account> accounts = response.body();
                    mAdapter.updateItems(accounts);
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {

            }
        });
    }

    private void startCreateContactActivity() {
        Toast.makeText(getActivity(), "Create account", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), CreateAccountActivity.class);
        startActivity(intent);
    }


}
