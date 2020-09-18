package com.example.projekat_pmsu2020_sf_1_5_28.activities.folderActivities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.contactActivities.CreateContactActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.adapters.RulesAdapter;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Folder;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Rule;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.service.ServiceUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FolderDetailsActivity extends AppCompatActivity implements RulesAdapter.OnRuleItemListener {

    private Folder mCurrentFolder;
    private Folder mParentFolder;
    private TextInputLayout mNameLayout;
    private TextInputEditText mName;
    private RecyclerView mRecyclerView;
    private RulesAdapter mAdapter;
    private EmailClientService mService;
    private SharedPreferences mSharedPreferences;
    private RuleDialogFragment mRuleDialogFragment;

    private TextInputLayout mRuleValueLayout;
    private TextInputEditText mRuleValueInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_details);

        mNameLayout = findViewById(R.id.folderNameLayout);

        mName = findViewById(R.id.folderNameInput);
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mName.getText().toString().trim().equals(""))
                    mNameLayout.setError("Folder name can't be blank");
                else
                    mNameLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRecyclerView = findViewById(R.id.recyclerViewRules);
        mService = ServiceUtils.emailClientService(this);
        mSharedPreferences = getSharedPreferences(ServiceUtils.PREFERENCES_NAME, MODE_PRIVATE);

        try {
            Folder folder = (Folder) getIntent().getSerializableExtra("folder");
            if (folder != null)
                mCurrentFolder = folder;
            else
                mCurrentFolder = null;
        } catch (Exception e) {
            mCurrentFolder = null;
        }
        try {
            Folder parent = (Folder) getIntent().getSerializableExtra("parent");
            if (parent != null)
                mParentFolder = parent;
            else
                mParentFolder = null;
        } catch (Exception e) {
            mParentFolder = null;
        }
        mAdapter = new RulesAdapter(this, new ArrayList<Rule>(), this);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (mCurrentFolder != null) {
            setCurrentFolderData();
        }

    }

    private void setCurrentFolderData() {
        mName.setText(mCurrentFolder.getName());
        Call<List<Rule>> call = mService.getFolderRules(mCurrentFolder.getId());
        call.enqueue(new Callback<List<Rule>>() {
            @Override
            public void onResponse(Call<List<Rule>> call, Response<List<Rule>> response) {
                if (response.code() == 200) {
                    Toast.makeText(FolderDetailsActivity.this, "Rules loaded", Toast.LENGTH_SHORT).show();
                    List<Rule> rules = response.body();
                    mAdapter.updateItems(rules);
                } else {
                    Toast.makeText(FolderDetailsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Rule>> call, Throwable t) {
                Toast.makeText(FolderDetailsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onCancel(View v) {
        finish();
    }

    public void onAddRuleClick(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mRuleDialogFragment = new RuleDialogFragment();
        mRuleDialogFragment.show(fragmentManager, "Dialog");
    }

    public void onRuleDialogCancel(View v) {
        mRuleDialogFragment.getDialog().cancel();
    }

    public void onRuleDialogSave(View v) {
        mRuleValueLayout = mRuleDialogFragment.getDialog().findViewById(R.id.ruleValueLayout);
        mRuleValueInput = mRuleDialogFragment.getDialog().findViewById(R.id.ruleValueInput);
        String ruleValue = "";
        Rule.Condition condition = null;
        Rule.Operation operation = null;

        ruleValue = mRuleValueInput.getText().toString().trim();

        if (ruleValue.equals(""))
            mRuleValueLayout.setError("Value can't be blank");
        else
            mRuleValueLayout.setError(null);

        RadioGroup conditionRadioGroup = mRuleDialogFragment.getDialog().findViewById(R.id.conditionRadioGroup);
        switch (conditionRadioGroup.getCheckedRadioButtonId()) {
            case R.id.condition_button_FROM: {
                condition = Rule.Condition.FROM;
                break;
            }
            case R.id.condition_button_TO: {
                condition = Rule.Condition.TO;
                break;
            }
            case R.id.condition_button_CC: {
                condition = Rule.Condition.CC;
                break;
            }
            case R.id.condition_button_SUBJECT: {
                condition = Rule.Condition.SUBJECT;
                break;
            }
        }

        RadioGroup operationRadioGroup = mRuleDialogFragment.getDialog().findViewById(R.id.operationRadioGroup);
        switch (operationRadioGroup.getCheckedRadioButtonId()) {
            case R.id.operation_button_MOVE: {
                operation = Rule.Operation.MOVE;
                break;
            }
            case R.id.operation_button_COPY: {
                operation = Rule.Operation.COPY;
                break;
            }
            case R.id.operation_button_DELETE: {
                operation = Rule.Operation.DELETE;
                break;
            }
        }

        TextView conditionError = mRuleDialogFragment.getDialog().findViewById(R.id.conditionErrorLabel);
        if (condition == null) {
            conditionError.setVisibility(View.VISIBLE);
        } else
            conditionError.setVisibility(View.INVISIBLE);

        TextView operationError = mRuleDialogFragment.getDialog().findViewById(R.id.operationErrorLabel);
        if (operation == null) {
            operationError.setVisibility(View.VISIBLE);
        } else
            operationError.setVisibility(View.INVISIBLE);

        if (!ruleValue.equals("") && condition != null && operation != null) {
            Rule rule = new Rule();
            rule.setValue(ruleValue);
            rule.setCondition(condition);
            rule.setOperation(operation);

            mAdapter.addItem(rule);
            mRuleDialogFragment.getDialog().dismiss();
            Toast.makeText(this, "Add rule", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please fill all needed data", Toast.LENGTH_SHORT).show();
        }

    }

    public void onSave(View v) {
        if (validate()) {
            Toast.makeText(this, "Save folder", Toast.LENGTH_SHORT).show();
            if (mCurrentFolder == null)
                createNewFolder();
            else {
                updateCurrentFolder();
            }
        } else {
            Toast.makeText(this, "Enter name or add rule", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validate() {
        if (mNameLayout.getError() == null /*&& mAdapter.getItems().size() > 0*/)
            return true;
        return false;
    }

    @Override
    public void onRuleItemDelete(final Rule rule) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(R.string.delete_rule)
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
                        mAdapter.removeItem(rule);
                    }
                }).show();

    }

    private void createNewFolder() {
        Folder newFolder = new Folder();
        newFolder.setName(mName.getText().toString().trim());
        newFolder.setNumberOfMessages(0);

        if (mParentFolder == null) {
            Long currentAccountId = mSharedPreferences.getLong("currentAccountId", 0);
            Call<Folder> call = mService.createFolder(currentAccountId, newFolder);
            call.enqueue(new Callback<Folder>() {
                @Override
                public void onResponse(Call<Folder> call, Response<Folder> response) {
                    if (response.code() == 201) {
                        Toast.makeText(FolderDetailsActivity.this, "Folder created", Toast.LENGTH_SHORT).show();
                        Folder folder = response.body();
                        updateFolderRules(folder);
                    } else {
                        Toast.makeText(FolderDetailsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Folder> call, Throwable t) {
                    Toast.makeText(FolderDetailsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Call<Folder> call = mService.createFolderChildFolder(mParentFolder.getId(), newFolder);
            call.enqueue(new Callback<Folder>() {
                @Override
                public void onResponse(Call<Folder> call, Response<Folder> response) {
                    if (response.code() == 201) {
                        Toast.makeText(FolderDetailsActivity.this, "Folder created", Toast.LENGTH_SHORT).show();
                        Folder folder = response.body();
                        updateFolderRules(folder);
                    } else {
                        Toast.makeText(FolderDetailsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Folder> call, Throwable t) {
                    Toast.makeText(FolderDetailsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void updateFolderRules(Folder folder) {
        List<Rule> rules = mAdapter.getItems();
        Call<List<Rule>> call = mService.updateFolderRules(folder.getId(), rules);
        call.enqueue(new Callback<List<Rule>>() {
            @Override
            public void onResponse(Call<List<Rule>> call, Response<List<Rule>> response) {
                if (response.code() == 201) {
                    Toast.makeText(FolderDetailsActivity.this, "Rules added", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(FolderDetailsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Rule>> call, Throwable t) {
                Toast.makeText(FolderDetailsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateCurrentFolder() {
        mCurrentFolder.setName(mName.getText().toString().trim());
        Call<Folder> call = mService.updateFolder(mCurrentFolder.getId(), mCurrentFolder);
        call.enqueue(new Callback<Folder>() {
            @Override
            public void onResponse(Call<Folder> call, Response<Folder> response) {
                if (response.code() == 200) {
                    Toast.makeText(FolderDetailsActivity.this, "Folder name updated", Toast.LENGTH_SHORT).show();
                    Folder folder = response.body();
                    updateFolderRules(folder);
                } else {
                    Toast.makeText(FolderDetailsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Folder> call, Throwable t) {
                Toast.makeText(FolderDetailsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }

}