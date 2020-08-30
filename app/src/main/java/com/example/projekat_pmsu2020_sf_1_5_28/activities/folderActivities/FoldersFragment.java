package com.example.projekat_pmsu2020_sf_1_5_28.activities.folderActivities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.adapters.FoldersAdapter;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Email;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Folder;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.Mokap;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoldersFragment extends Fragment {


    public static FoldersFragment newInstance() { return new FoldersFragment(); }
    private EmailClientService mService;
    private FoldersAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_folders, container, false);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mService = ((MainActivity) getActivity()).getEmailClientService();
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerViewFolders);
        mAdapter = new FoldersAdapter(getContext(), new ArrayList<Folder>(), (MainActivity) getContext());
        recyclerView.setAdapter(mAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);

        FloatingActionButton fab = getActivity().findViewById(R.id.fabFolder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "New folder clicked", Toast.LENGTH_SHORT).show();
                openCreateFolderDialog();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_menu,menu);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCurrentFragment(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.Folders));

        SharedPreferences sharedPreferences = ((MainActivity) getActivity()).getSharedPreferences();
        Long currentAccountId = sharedPreferences.getLong("currentAccountId", 0);

        Call<List<Folder>> call = mService.getAccountFolders(currentAccountId);
        call.enqueue(new Callback<List<Folder>>() {
            @Override
            public void onResponse(Call<List<Folder>> call, Response<List<Folder>> response) {
                if (response.code() == 200) {
                    List<Folder> folders = response.body();
                    mAdapter.updateItems(folders);
                }
            }

            @Override
            public void onFailure(Call<List<Folder>> call, Throwable t) {

            }
        });
    }

    private void openCreateFolderDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(getString(R.string.new_folder));

        final EditText input = new EditText(getContext());
        input.setHint(getString(R.string.folder_name));

        builder.setView(input);

        builder.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newFolderName = input.getText().toString();
                Toast.makeText(getContext(),newFolderName, Toast.LENGTH_SHORT).show();

                Folder newFolder = new Folder();
                newFolder.setName(newFolderName);
//                newFolder.setEmailsList(new ArrayList<Email>());
//                newFolder.setFoldersList(new ArrayList<Folder>());
//                newFolder.setParentFolder(null);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
