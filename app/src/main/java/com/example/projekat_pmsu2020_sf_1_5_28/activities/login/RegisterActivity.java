package com.example.projekat_pmsu2020_sf_1_5_28.activities.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.model.LoginDTO;
import com.example.projekat_pmsu2020_sf_1_5_28.model.User;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.service.ServiceUtils;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText mFirstNameInput, mLastNameInput, mUsernameInput, mPasswordInput, mRepeatedPasswordInput;
    private EmailClientService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirstNameInput = findViewById(R.id.firstNameInput);
        mLastNameInput = findViewById(R.id.lastNameInput);
        mUsernameInput = findViewById(R.id.usernameInput);
        mPasswordInput = findViewById(R.id.passwordInput);
        mRepeatedPasswordInput = findViewById(R.id.repeatedPasswordInput);
        service = ServiceUtils.emailClientService(this);

    }

    public void onRegister(View view) {
        String firstName = mFirstNameInput.getText().toString().trim();
        String lastName = mLastNameInput.getText().toString().trim();
        String username = mUsernameInput.getText().toString().trim();
        String password = mPasswordInput.getText().toString().trim();
        String repeatedPassword = mRepeatedPasswordInput.getText().toString().trim();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !username.isEmpty()
                && !password.isEmpty() && !repeatedPassword.isEmpty()) {
            if (!password.equals(repeatedPassword))
                Toast.makeText(RegisterActivity.this, "Password and repeated password not same",Toast.LENGTH_SHORT).show();
            else {
                User user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setUsername(username);
                user.setPassword(password);
                doRegister(user);
            }
        }
        else {
            Toast.makeText(RegisterActivity.this, "Please fill all fields",Toast.LENGTH_SHORT).show();
        }
    }

    private void doRegister(User user) {
        Call<User> call = service.register(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 201) {
                    Toast.makeText(RegisterActivity.this, "Registration succesfull",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if (response.code() == 226) {
                    Toast.makeText(RegisterActivity.this, "Username already taken",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Something went wrong...",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Something went wrong...",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onCancel(View view) {
        finish();
    }
}
