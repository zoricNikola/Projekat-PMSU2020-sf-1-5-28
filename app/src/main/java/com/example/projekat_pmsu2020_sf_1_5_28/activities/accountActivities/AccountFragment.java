package com.example.projekat_pmsu2020_sf_1_5_28.activities.accountActivities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Account;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    private Account mAccount;
    private TextInputEditText mSmtpAddress, mSmtpPort, mInServerAddress, mInServerPort, mEmail, mPassword, mDsiplayName;
    private TextInputLayout mSmtpAddressLayout, mSmtpPortLayout, mInServerAddressLayout, mInServerPortLayout, mEmailLayout, mPasswordLayout, mDsiplayNameLayout;
    private RadioGroup mRadioGroup;
    private EmailClientService mService;
    private SharedPreferences mSharedPreferences;

    public static AccountFragment newInstance() { return new AccountFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.account_fragment_menu, menu);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSmtpAddress = getActivity().findViewById(R.id.smtpAddressInput);
        mSmtpPort = getActivity().findViewById(R.id.smtpPortInput);
        mInServerAddress = getActivity().findViewById(R.id.inServerAddressInput);
        mInServerPort = getActivity().findViewById(R.id.inServerPortInput);
        mEmail = getActivity().findViewById(R.id.emailInput);
        mPassword = getActivity().findViewById(R.id.passwordInput);
        mDsiplayName = getActivity().findViewById(R.id.displayNameInput);

        mSmtpAddressLayout = getActivity().findViewById(R.id.smtpAddressInputLayout);
        mSmtpPortLayout = getActivity().findViewById(R.id.smtpPortInputLayout);
        mInServerAddressLayout = getActivity().findViewById(R.id.inServerAddressInputLayout);
        mInServerPortLayout = getActivity().findViewById(R.id.inServerPortInputLayout);
        mEmailLayout = getActivity().findViewById(R.id.emailInputLayout);
        mPasswordLayout = getActivity().findViewById(R.id.passwordInputLayout);
        mDsiplayNameLayout = getActivity().findViewById(R.id.displayNameInputLayout);

        mRadioGroup = getActivity().findViewById(R.id.inServerTypeRadioGroup);

        mService = ((MainActivity) getActivity()).getEmailClientService();
        mSharedPreferences = ((MainActivity) getActivity()).getSharedPreferences();

        Bundle accountData = this.getArguments();
        Account account = (Account) accountData.getSerializable("account");

        mAccount = account;
        mSmtpAddress.setText(account.getSmtpAddress());
        mSmtpPort.setText(account.getSmtpPort().toString());
        mInServerAddress.setText(account.getInServerAddress());
        mInServerPort.setText(account.getInServerPort().toString());
        mEmail.setText(account.getUsername());
        mPassword.setText(account.getPassword());
        mDsiplayName.setText(account.getDisplayName());

        if (account.getInServerType() == Account.InServerType.POP3)
            mRadioGroup.check(R.id.inServerType_POP3);
        else if (account.getInServerType() == Account.InServerType.IMAP)
            mRadioGroup.check(R.id.inServerType_IMAP);

        setTextChangeListeners();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_saveAccount:
                Toast.makeText(getContext(),"Save account", Toast.LENGTH_SHORT).show();
                saveAccount();
                return true;
            case R.id.item_deleteAccount:
                Toast.makeText(getContext(),"Delete account ", Toast.LENGTH_SHORT).show();
                deleteAccount();
                return true;
        }
        return false;
    }

    private void saveAccount() {
        if (validate()) {
            Account.InServerType inServerType = null;
            switch (mRadioGroup.getCheckedRadioButtonId()) {
                case R.id.inServerType_POP3: {
                    inServerType = Account.InServerType.POP3;
                    break;
                }
                case R.id.inServerType_IMAP: {
                    inServerType = Account.InServerType.IMAP;
                    break;
                }
            }

            mAccount.setSmtpAddress(mSmtpAddress.getText().toString().trim());
            mAccount.setSmtpPort(Integer.parseInt(mSmtpPort.getText().toString().trim()));
            mAccount.setInServerType(inServerType);
            mAccount.setInServerAddress(mInServerAddress.getText().toString().trim());
            mAccount.setInServerPort(Integer.parseInt(mInServerPort.getText().toString().trim()));
            mAccount.setUsername(mEmail.getText().toString().trim());
            mAccount.setPassword(mPassword.getText().toString().trim());
            mAccount.setDisplayName(mDsiplayName.getText().toString().trim());

            Call<Account> call = mService.updateAccount(mAccount.getId(), mAccount);
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.code() == 201) {
                        Toast.makeText(getContext(),"Account saved",Toast.LENGTH_SHORT).show();
                        Account account = response.body();
                        Long currentAccountId = mSharedPreferences.getLong("currentAccountId", 0);
                        if (currentAccountId == account.getId()) {
                            mSharedPreferences.edit().putString("currentAccountEmail", account.getUsername())
                                    .putString("currentAccountDisplayName", account.getDisplayName()).apply();
                            ((MainActivity)getActivity()).mCurrentAccountDisplayName().setText(account.getDisplayName());
                            ((MainActivity)getActivity()).mCurrentAccountEmail().setText(account.getUsername());

                        }
                    }
                    else {
                        Toast.makeText(getContext(),"Something went wrong...",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    Toast.makeText(getContext(),"Something went wrong...",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void setTextChangeListeners() {
        mSmtpAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                createErrorMessages("smtpAddress");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSmtpPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                createErrorMessages("smtpPort");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mInServerAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                createErrorMessages("inServerAddress");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mInServerPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                createErrorMessages("inServerPort");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                createErrorMessages("email");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                createErrorMessages("password");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDsiplayName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                createErrorMessages("displayName");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void createErrorMessages(String which) {
        if (which.equals("all") || which.equals("smtpAddress")) {
            if (mSmtpAddress.getText().toString().trim().equals(""))
                mSmtpAddressLayout.setError("Smtp address can't be blank");
            else
                mSmtpAddressLayout.setError(null);
        }
        if (which.equals("all") || which.equals("smtpPort")) {
            if (mSmtpPort.getText().toString().trim().equals(""))
                mSmtpPortLayout.setError("Smtp port can't be blank");
            else {
                try {
                    Integer.parseInt(mSmtpPort.getText().toString().trim());
                    mSmtpPortLayout.setError(null);
                }
                catch (Exception e) {
                    mSmtpPortLayout.setError("Smtp port must be number");
                }

            }
        }
        if (which.equals("all") || which.equals("inServerAddress")) {
            if (mInServerAddress.getText().toString().trim().equals(""))
                mInServerAddressLayout.setError("In server address can't be blank");
            else
                mInServerAddressLayout.setError(null);
        }
        if (which.equals("all") || which.equals("inServerPort")) {
            if (mInServerPort.getText().toString().trim().equals(""))
                mInServerPortLayout.setError("In server port can't be blank");
            else {
                try {
                    Integer.parseInt(mInServerPort.getText().toString().trim());
                    mInServerPortLayout.setError(null);
                }
                catch (Exception e) {
                    mInServerPortLayout.setError("In server port must be number");
                }

            }
        }
        if (which.equals("all") || which.equals("email")) {
            if (mEmail.getText().toString().trim().equals(""))
                mEmailLayout.setError("Email can't be blank");
            else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString().trim()).matches())
                mEmailLayout.setError("Invalid email address");
            else
                mEmailLayout.setError(null);
        }
        if (which.equals("all") || which.equals("password")) {
            if (mPassword.getText().toString().trim().equals(""))
                mPasswordLayout.setError("Password can't be blank");
            else
                mPasswordLayout.setError(null);
        }
        if (which.equals("all") || which.equals("displayName")) {
            if (mDsiplayName.getText().toString().trim().equals(""))
                mDsiplayNameLayout.setError("Display name can't be blank");
            else
                mDsiplayNameLayout.setError(null);
        }
    }

    private boolean validate() {
        createErrorMessages("all");
        Account.InServerType inServerType = null;
        switch (mRadioGroup.getCheckedRadioButtonId()) {
            case R.id.inServerType_POP3: {
                inServerType = Account.InServerType.POP3;
                break;
            }
            case R.id.inServerType_IMAP: {
                inServerType = Account.InServerType.IMAP;
                break;
            }
        }
        if ( mSmtpAddressLayout.getError() != null
                || mSmtpPortLayout.getError() != null
                || mInServerAddressLayout.getError() != null
                || mInServerPortLayout.getError() != null
                || mEmailLayout.getError() != null
                || mPasswordLayout.getError() != null
                || mDsiplayNameLayout.getError() != null
                || inServerType == null) {
            return false;
        }
        return true;
    }

    private void deleteAccount() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(R.string.delete_account)
                .setMessage(R.string.delete_account_alert_message)
                .setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Void> call = mService.removeAccount(mAccount.getId());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(getContext(),"Account deleted", Toast.LENGTH_SHORT).show();
                                    Long currentAccountId = mSharedPreferences.getLong("currentAccountId", 0);
                                    if (currentAccountId == mAccount.getId()) {
                                        mSharedPreferences.edit().putLong("currentAccountId", 0).putString("currentAccountEmail", "")
                                                .putString("currentAccountDisplayName", "").apply();
                                        ((MainActivity)getActivity()).mCurrentAccountDisplayName().setText("");
                                        ((MainActivity)getActivity()).mCurrentAccountEmail().setText("");
                                        ((MainActivity) getActivity()).chooseAccount();

                                    }
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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCurrentFragment(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
    }
}
