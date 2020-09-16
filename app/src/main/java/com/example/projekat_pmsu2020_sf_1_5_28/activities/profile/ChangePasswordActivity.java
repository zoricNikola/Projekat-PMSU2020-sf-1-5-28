package com.example.projekat_pmsu2020_sf_1_5_28.activities.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.contactActivities.CreateContactActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.model.LoginDTO;
import com.example.projekat_pmsu2020_sf_1_5_28.model.TokenDTO;
import com.example.projekat_pmsu2020_sf_1_5_28.model.User;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.service.ServiceUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText mCurrentPassword, mNewPassword, mRepeatedNewPassword;
    private TextInputLayout mCurrentPasswordLayout, mNewPasswordLayout, mRepeatedNewPasswordLayout;
    private EmailClientService mService;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mCurrentPassword = findViewById(R.id.currentPasswordInput);
        mNewPassword = findViewById(R.id.newPasswordInput);
        mRepeatedNewPassword = findViewById(R.id.repeatedPasswordInput);

        mCurrentPasswordLayout = findViewById(R.id.currentPasswordInputLayout);
        mNewPasswordLayout = findViewById(R.id.newPasswordInputLayout);
        mRepeatedNewPasswordLayout = findViewById(R.id.repeatedPasswordInputLayout);

        mService = ServiceUtils.emailClientService(this);
        mSharedPreferences = getSharedPreferences(ServiceUtils.PREFERENCES_NAME, MODE_PRIVATE);

        setTextChangeListeners();
    }

    private void setTextChangeListeners() {
        mCurrentPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                createErrorMessages("currentPassword");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                createErrorMessages("newPassword");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRepeatedNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                createErrorMessages("repeatedNewPassword");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void createErrorMessages(String which) {
        if (which.equals("all") || which.equals("currentPassword")) {
            if (mCurrentPassword.getText().toString().trim().isEmpty())
                mCurrentPasswordLayout.setError("Current password can't be blank");
            else
                mCurrentPasswordLayout.setError(null);
        }
        if (which.equals("all") || which.equals("newPassword")) {
            if (mNewPassword.getText().toString().trim().isEmpty())
                mNewPasswordLayout.setError("New password can't be blank");
            else
                mNewPasswordLayout.setError(null);
        }
        if (which.equals("all") || which.equals("repeatedNewPassword")) {
            if (mRepeatedNewPassword.getText().toString().trim().isEmpty())
                mRepeatedNewPasswordLayout.setError("Repeated password can't be blank");
            else if (!mRepeatedNewPassword.getText().toString().trim().equals(mNewPassword.getText().toString().trim()))
                mRepeatedNewPasswordLayout.setError("Repeated password must be same as new password");
            else
                mRepeatedNewPasswordLayout.setError(null);
        }
    }

    public void onCancel(View view) {
        finish();
    }

    public void onConfirm(View view) {
        Toast.makeText(ChangePasswordActivity.this,"Change password",Toast.LENGTH_SHORT).show();
        changePassword();
    }

    private void changePassword() {
        if (validate()) {
            String username = mSharedPreferences.getString("username", "");

            if (!username.isEmpty()) {
                LoginDTO login = new LoginDTO(username, mCurrentPassword.getText().toString().trim());
                Call<TokenDTO> call = mService.login(login);
                call.enqueue(new Callback<TokenDTO>() {
                    @Override
                    public void onResponse(Call<TokenDTO> call, Response<TokenDTO> response) {
                        if (response.code() == 200) {
                            doChangePassword();
                        }
                        else {
                            Toast.makeText(ChangePasswordActivity.this, "Wrong current password...", Toast.LENGTH_SHORT).show();
                            mCurrentPasswordLayout.setError("Wrong current password");
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenDTO> call, Throwable t) {
                        Toast.makeText(ChangePasswordActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void doChangePassword() {
        User user = new User();
        user.setPassword(mNewPassword.getText().toString().trim());
        Long userId = mSharedPreferences.getLong("userId", 0);
        if (userId != 0) {
            Call<User> call = mService.updateUser(userId, user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        Toast.makeText(ChangePasswordActivity.this, "Password changed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(ChangePasswordActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(ChangePasswordActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validate() {
        createErrorMessages("all");
        if ( mCurrentPasswordLayout.getError() != null
                || mNewPasswordLayout.getError() != null
                || mRepeatedNewPasswordLayout.getError() != null) {
            return false;
        }
        return true;
    }
}
