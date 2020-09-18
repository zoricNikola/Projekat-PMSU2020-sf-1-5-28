package com.example.projekat_pmsu2020_sf_1_5_28.activities.folderActivities;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.projekat_pmsu2020_sf_1_5_28.model.Message;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FolderFragment extends Fragment {

    private Folder currentFolder;

    private boolean showFolders;
    private boolean showEmails;

    private LinearLayout mEmailsContainer;
    private LinearLayout mFoldersContainer;

    private Button mButtonEmails;
    private Button mButtonFolders;

    private EmailsAdapter mMessagesAdapter;
    private FoldersAdapter mFoldersAdapter;

    private EmailClientService mService;

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

        mService = ((MainActivity) getActivity()).getEmailClientService();

        Bundle folderData = this.getArguments();
        currentFolder = (Folder) folderData.getSerializable("folder");

        RecyclerView emailsRecyclerView = getActivity().findViewById(R.id.recyclerViewEmails);
        mMessagesAdapter = new EmailsAdapter(getContext(), new ArrayList<Message>(), (MainActivity) getContext());
        emailsRecyclerView.setAdapter(mMessagesAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        emailsRecyclerView.setLayoutManager(layoutManager);

        emailsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView foldersRecyclerView = getActivity().findViewById(R.id.recyclerViewFolders);
        mFoldersAdapter = new FoldersAdapter(getContext(), new ArrayList<Folder>(), (MainActivity) getContext());
        foldersRecyclerView.setAdapter(mFoldersAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        foldersRecyclerView.setLayoutManager(gridLayoutManager);
        rearangeContainers();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCurrentFragment(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(currentFolder.getName());

        Call<List<Message>> callM = mService.getFolderMessages(currentFolder.getId());
        callM.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.code() == 200) {
                    List<Message> messages = response.body();
                    mMessagesAdapter.updateItems(messages);
                }
                else {
                    Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });

        Call<List<Folder>> callF = mService.getFolderChildFolders(currentFolder.getId());
        callF.enqueue(new Callback<List<Folder>>() {
            @Override
            public void onResponse(Call<List<Folder>> call, Response<List<Folder>> response) {
                if (response.code() == 200) {
                    List<Folder> folders = response.body();
                    mFoldersAdapter.updateItems(folders);
                }
                else {
                    Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Folder>> call, Throwable t) {
                Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
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
                Toast.makeText(getContext(),"New folder", Toast.LENGTH_SHORT).show();
                startFolderDetailsActivity("newChildFolder");
                return true;
            case R.id.item_editFolder:
                Toast.makeText(getContext(),"Edit folder", Toast.LENGTH_SHORT).show();
                startFolderDetailsActivity("editCurrentFolder");
                return true;
            case R.id.item_doRules:
                Toast.makeText(getContext(),"Do rules", Toast.LENGTH_SHORT).show();
                doRules();
                return true;
            case R.id.item_deleteFolder:
                if(currentFolder.getName().equals("Inbox") || currentFolder.getName().equals("Drafts")
                        || currentFolder.getName().equals("Sent")){
                    Toast.makeText(getContext(),"You can't delete this folder!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(),"Delete folder", Toast.LENGTH_SHORT).show();
                    deleteFolder();
                }
                return true;
        }
        return false;
    }

    private void doRules() {
        Call<Void> call = mService.doRules(currentFolder.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    Toast.makeText(getContext(),"Rules applied", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void startFolderDetailsActivity(String mode){
        if (mode.equals("newChildFolder")) {
            Intent intent = new Intent(getActivity(), FolderDetailsActivity.class);
            intent.putExtra("parent", currentFolder);
            startActivity(intent);
        }
        else if (mode.equals("editCurrentFolder")) {
            Intent intent = new Intent(getActivity(), FolderDetailsActivity.class);
            intent.putExtra("folder", currentFolder);
            startActivity(intent);
        }
    }

    private void deleteFolder() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(R.string.delete_folder)
                .setMessage(R.string.delete_folder_alert_message)
                .setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Void> call = mService.removeFolder(currentFolder.getId());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(getContext(),"Folder deleted", Toast.LENGTH_SHORT).show();
                                    getActivity().onBackPressed();
                                }
                                else {
                                    Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).show();
    }
}
