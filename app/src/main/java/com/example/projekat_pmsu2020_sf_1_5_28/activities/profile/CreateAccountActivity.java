package com.example.projekat_pmsu2020_sf_1_5_28.activities.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.contactActivities.CreateContactActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Account;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Rule;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.service.ServiceUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountActivity extends AppCompatActivity {

    private TextInputEditText mSmtpAddress, mSmtpPort, mInServerAddress, mInServerPort, mEmail, mPassword, mDsiplayName;
    private TextInputLayout mSmtpAddressLayout, mSmtpPortLayout, mInServerAddressLayout, mInServerPortLayout, mEmailLayout, mPasswordLayout, mDsiplayNameLayout;
    private RadioGroup mRadioGroup;
    private EmailClientService mService;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mSmtpAddress = findViewById(R.id.smtpAddressInput);
        mSmtpPort = findViewById(R.id.smtpPortInput);
        mInServerAddress = findViewById(R.id.inServerAddressInput);
        mInServerPort = findViewById(R.id.inServerPortInput);
        mEmail = findViewById(R.id.emailInput);
        mPassword = findViewById(R.id.passwordInput);
        mDsiplayName = findViewById(R.id.displayNameInput);

        mSmtpAddressLayout = findViewById(R.id.smtpAddressInputLayout);
        mSmtpPortLayout = findViewById(R.id.smtpPortInputLayout);
        mInServerAddressLayout = findViewById(R.id.inServerAddressInputLayout);
        mInServerPortLayout = findViewById(R.id.inServerPortInputLayout);
        mEmailLayout = findViewById(R.id.emailInputLayout);
        mPasswordLayout = findViewById(R.id.passwordInputLayout);
        mDsiplayNameLayout = findViewById(R.id.displayNameInputLayout);

        mRadioGroup = findViewById(R.id.inServerTypeRadioGroup);

        mService = ServiceUtils.emailClientService(this);
        mSharedPreferences = getSharedPreferences(ServiceUtils.PREFERENCES_NAME, MODE_PRIVATE);

        setTextChangeListeners();

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

    public void onCancel(View view) {
        finish();
    }

    public void onSave(View view) {
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

            Account account = new Account();
            account.setSmtpAddress(mSmtpAddress.getText().toString().trim());
            account.setSmtpPort(Integer.parseInt(mSmtpPort.getText().toString().trim()));
            account.setInServerType(inServerType);
            account.setInServerAddress(mInServerAddress.getText().toString().trim());
            account.setInServerPort(Integer.parseInt(mInServerPort.getText().toString().trim()));
            account.setUsername(mEmail.getText().toString().trim());
            account.setPassword(mPassword.getText().toString().trim());
            account.setDisplayName(mDsiplayName.getText().toString().trim());

            Long userId = mSharedPreferences.getLong("userId", 0);
            if (userId != 0) {
                Call<Account> call = mService.createAccount(userId, account);
                call.enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if (response.code() == 201) {
                            Toast.makeText(CreateAccountActivity.this,"Account created",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(CreateAccountActivity.this,"Something went wrong...",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        Toast.makeText(CreateAccountActivity.this,"Something went wrong...",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
