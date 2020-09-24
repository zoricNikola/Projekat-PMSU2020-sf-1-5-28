package com.example.projekat_pmsu2020_sf_1_5_28.activities.emailActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.accountActivities.AccountsFragment;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.contactActivities.ContactsFragment;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.folderActivities.FoldersFragment;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.profile.ProfileFragment;
import com.example.projekat_pmsu2020_sf_1_5_28.adapters.EmailsAdapter;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Email;
import com.example.projekat_pmsu2020_sf_1_5_28.model.FilterDTO;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Message;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Tag;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.service.ServiceUtils;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.Mokap;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailsFragment extends Fragment {

    public static EmailsFragment newInstance() { return new EmailsFragment(); }
    private EmailClientService mService;
    private EmailsAdapter mAdapter;
    private List<Tag> selectedTags = new ArrayList<>();
    private String searchText = "";
    private String[] sortOptions = {"Subject ascending", "Subject descending",
            "Sender ascending", "Sender descending",
            "Date ascending", "Date descending"};

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
        inflater.inflate(R.menu.emails_fragment_menu, menu);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.item_search:
                Toast.makeText(getActivity(), "Search clicked", Toast.LENGTH_SHORT).show();
                openSearchDialog();
                return true;
            case R.id.item_filter:
                Toast.makeText(getActivity(), "Filter clicked", Toast.LENGTH_SHORT).show();
                opeanFilterDialog();
                return true;
            case R.id.item_sort:
                Toast.makeText(getActivity(), "Sort clicked", Toast.LENGTH_SHORT).show();
                openSortDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCurrentFragment(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.Emails));

        if (!searchText.isEmpty() || selectedTags.size() > 0)
            showSearchedMessages(searchText);
        else
            loadAccountMessages();
    }

    private void loadAccountMessages() {
        SharedPreferences sharedPreferences = ((MainActivity) getActivity()).getSharedPreferences();
        Long currentAccountId = sharedPreferences.getLong("currentAccountId", 0);

        if (currentAccountId != 0) {
            Call<List<Message>> call = mService.getAccountMessages(currentAccountId);
            call.enqueue(new Callback<List<Message>>() {
                @Override
                public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                    if (response.code() == 200) {
                        List<Message> messages = response.body();
                        mAdapter.updateItems(messages);
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<List<Message>> call, Throwable t) {

                }
            });
        }
    }

    public void startCreateEmailActivity() {
        Toast.makeText(getActivity(), "Create email", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), CreateEmailActivity.class);
        startActivity(intent);
    }

    private void opeanFilterDialog() {
        SharedPreferences sharedPreferences = ((MainActivity) getActivity()).getSharedPreferences();
        Long userId = sharedPreferences.getLong("userId", 0);
        if (userId != 0) {
            Call<List<Tag>> call = mService.getUserTags(userId);
            call.enqueue(new Callback<List<Tag>>() {
                @Override
                public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                    if (response.code() == 200) {
                        final List<Tag> allTags = response.body();
                        final String[] items = new String[allTags.size()];
                        final boolean[] checkedItems = new boolean[allTags.size()];
                        for (int i = 0; i < allTags.size(); i++) {
                            items[i] = allTags.get(i).getName();
                            checkedItems[i] = false;
                            for (Tag tag : selectedTags) {
                                if (tag.getName().equalsIgnoreCase(allTags.get(i).getName())) {
                                    checkedItems[i] = true;
                                }
                            }
                        }

                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                        builder.setTitle("Filter by tags")
                                .setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        if (isChecked) {
                                            selectedTags.add(allTags.get(which));
                                        }
                                        else {
                                            for (Tag tag : selectedTags) {
                                                if (tag.getName().equalsIgnoreCase(allTags.get(which).getName())) {
                                                    selectedTags.remove(tag);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                })
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (selectedTags.size() > 0)
                                            showFilteredMessages(selectedTags);
                                        else
                                            loadAccountMessages();
                                    }
                                })
                                .setNeutralButton("Reset", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        selectedTags.removeAll(selectedTags);
                                        dialog.dismiss();
                                        loadAccountMessages();
                                    }
                                }).create().show();
                    }
                }

                @Override
                public void onFailure(Call<List<Tag>> call, Throwable t) {

                }
            });
        }
    }

    private void showFilteredMessages(List<Tag> selectedTags) {
        SharedPreferences sharedPreferences = ((MainActivity) getActivity()).getSharedPreferences();
        Long currentAccountId = sharedPreferences.getLong("currentAccountId", 0);

        if (currentAccountId != 0) {
            FilterDTO filter = new FilterDTO();
            filter.setSearchText(searchText);
            filter.getFilteringTags().addAll(selectedTags);
            Call<List<Message>> call = mService.filterMessages(currentAccountId, filter);
            call.enqueue(new Callback<List<Message>>() {
                @Override
                public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                    if (response.code() == 200) {
                        mAdapter.updateItems(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<Message>> call, Throwable t) {

                }
            });
        }
    }

    private void openSearchDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

        final EditText input = new EditText(getContext());
        input.setText(searchText, TextView.BufferType.NORMAL);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Search text");
        builder.setTitle("Search").setView(input)
                .setNeutralButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        searchText = "";
                        loadAccountMessages();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                searchText = input.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    showSearchedMessages(searchText);
                }
                else {
                    loadAccountMessages();
                }
            }
        });
        builder.create().show();
    }

    private void showSearchedMessages(String searchText) {
        SharedPreferences sharedPreferences = ((MainActivity) getActivity()).getSharedPreferences();
        Long currentAccountId = sharedPreferences.getLong("currentAccountId", 0);

        if (currentAccountId != 0) {
            FilterDTO filter = new FilterDTO();
            filter.setSearchText(searchText);
            filter.getFilteringTags().addAll(selectedTags);
            Call<List<Message>> call = mService.filterMessages(currentAccountId, filter);
            call.enqueue(new Callback<List<Message>>() {
                @Override
                public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                    if (response.code() == 200) {
                        mAdapter.updateItems(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<Message>> call, Throwable t) {

                }
            });
        }
    }

    private void openSortDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

        builder.setTitle("Sort")
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sortMessages(sortOptions[which]);
                    }
                }).create().show();
    }

    private void sortMessages(String sortOption) {
        List<Message> messages = new ArrayList<>();
        messages.addAll(mAdapter.getItems());
        switch (sortOption) {
            case "Date descending":
                Collections.sort(messages, new Comparator<Message>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public int compare(Message o1, Message o2) {
                        int result = Instant.parse(o1.getDateTime() + "Z").isAfter(Instant.parse(o2.getDateTime() + "Z")) ? -1 : 1;
                        if (result != 1 && result != -1)
                            result = 0;
                        return result;
                    }
                });
                mAdapter.updateItems(messages);
                return;
            case "Date ascending":
                Collections.sort(messages, new Comparator<Message>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public int compare(Message o1, Message o2) {
                        int result = Instant.parse(o1.getDateTime() + "Z").isBefore(Instant.parse(o2.getDateTime() + "Z")) ? -1 : 1;
                        if (result != 1 && result != -1)
                            result = 0;
                        return result;
                    }
                });
                mAdapter.updateItems(messages);
                return;
            case "Subject ascending":
                Collections.sort(messages, new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        return o1.getSubject().compareToIgnoreCase(o2.getSubject());
                    }
                });
                mAdapter.updateItems(messages);
                return;
            case "Subject descending":
                Collections.sort(messages, new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        return o2.getSubject().compareToIgnoreCase(o1.getSubject());
                    }
                });
                mAdapter.updateItems(messages);
                return;
            case "Sender ascending":
                Collections.sort(messages, new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        return o1.getFrom().compareToIgnoreCase(o2.getFrom());
                    }
                });
                mAdapter.updateItems(messages);
                return;
            case "Sender descending":
                Collections.sort(messages, new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        return o2.getFrom().compareToIgnoreCase(o1.getFrom());
                    }
                });
                mAdapter.updateItems(messages);
                return;
        }
    }
}
