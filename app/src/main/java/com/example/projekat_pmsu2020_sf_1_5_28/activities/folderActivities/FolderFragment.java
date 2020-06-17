package com.example.projekat_pmsu2020_sf_1_5_28.activities.folderActivities;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.adapters.EmailsAdapter;
import com.example.projekat_pmsu2020_sf_1_5_28.adapters.FoldersAdapter;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Email;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Folder;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FolderFragment extends Fragment {

    private Folder currentFolder;

    private boolean showFolders;
    private boolean showEmails;

    private LinearLayout mEmailsContainer;
    private LinearLayout mFoldersContainer;

    private Button mButtonEmails;
    private Button mButtonFolders;

    public static FolderFragment newInstance() { return new FolderFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.showEmails = true;
        this.showFolders = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_folder, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEmailsContainer = getActivity().findViewById(R.id.emailsContainer);
        mFoldersContainer = getActivity().findViewById(R.id.foldersContainer);

        mButtonEmails = getActivity().findViewById(R.id.buttonEmails);
        mButtonFolders = getActivity().findViewById(R.id.buttonFolders);

        mButtonEmails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmails = !showEmails;
                rearangeContainers();
            }
        });

        mButtonFolders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFolders = !showFolders;
                rearangeContainers();
            }
        });

        Bundle folderData = this.getArguments();
        currentFolder = (Folder) folderData.getSerializable("folder");

        RecyclerView emailsRecyclerView = getActivity().findViewById(R.id.recyclerViewEmails);
        EmailsAdapter adapter = new EmailsAdapter(getContext(), currentFolder.getEmailsList(), (MainActivity) getContext());
        emailsRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        emailsRecyclerView.setLayoutManager(layoutManager);

        emailsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView foldersRecyclerView = getActivity().findViewById(R.id.recyclerViewFolders);
        FoldersAdapter foldersAdapter = new FoldersAdapter(getContext(), currentFolder.getFoldersList(), (MainActivity) getContext());
        foldersRecyclerView.setAdapter(foldersAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        foldersRecyclerView.setLayoutManager(gridLayoutManager);
        rearangeContainers();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCurrentFragment(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(currentFolder.getName());
    }

    public void rearangeContainers() {

        if (showEmails) {
            mEmailsContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f));
            ((MaterialButton) mButtonEmails).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_collapse, null));
        }
        else {
            mEmailsContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.0f));
            ((MaterialButton) mButtonEmails).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand, null));
        }

        if (showFolders) {
            mFoldersContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f));
            ((MaterialButton) mButtonFolders).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_collapse, null));
        }
        else {
            mFoldersContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.0f));
            ((MaterialButton) mButtonFolders).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand, null));
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.folder_fragment_menu, menu);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_newFolder:
                Toast.makeText(getContext(),"New folder", Toast.LENGTH_LONG).show();
                openCreateFolderDialog();
                return true;
            case R.id.item_editFolder:
                Toast.makeText(getContext(),"Edit folder", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item_deleteFolder:
                Toast.makeText(getContext(),"Delete folder", Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
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
                Toast.makeText(getContext(),newFolderName, Toast.LENGTH_LONG).show();

                Folder newFolder = new Folder();
                newFolder.setName(newFolderName);
                newFolder.setEmailsList(new ArrayList<Email>());
                newFolder.setFoldersList(new ArrayList<Folder>());
                newFolder.setParentFolder(currentFolder);
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
