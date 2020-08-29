package com.example.projekat_pmsu2020_sf_1_5_28.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.projekat_pmsu2020_sf_1_5_28.R;

import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.model.LoginDTO;
import com.example.projekat_pmsu2020_sf_1_5_28.model.TokenDTO;
import com.example.projekat_pmsu2020_sf_1_5_28.model.User;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.service.ServiceUtils;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText mUsernameInput, mPasswordInput;
    private Button mLoginButton, mRegisterButton;
    private EmailClientService service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsernameInput = findViewById(R.id.usernameInput);
        mPasswordInput = findViewById(R.id.passwordInput);
        mLoginButton = findViewById(R.id.loginButton);
        mRegisterButton = findViewById(R.id.registerButton);
        service = ServiceUtils.emailClientService(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLoginButton.setOnClickListener(new View.OnClickListener()  {
           @Override
           public void onClick(View v) {
               String username = mUsernameInput.getText().toString().trim();
               String password = mPasswordInput.getText().toString().trim();
               LoginDTO login = new LoginDTO(username, password);
               doLogin(login);
           }
        });
    }

    private void doLogin(LoginDTO login) {
        Call<TokenDTO> call = service.login(login);
        call.enqueue(new Callback<TokenDTO>() {
            @Override
            public void onResponse(Call<TokenDTO> call, Response<TokenDTO> response) {
                if (response.code() == 200) {
                    TokenDTO tokenHolder = response.body();
                    Log.d("REST - LOGIN", "Success login " + tokenHolder);

                    SharedPreferences sharedPreferences = getSharedPreferences(ServiceUtils.PREFERENCES_NAME, MODE_PRIVATE);
                    sharedPreferences.edit().putString("jwt", tokenHolder.getJwt())
                            .putLong("userId", tokenHolder.getUser().getId())
                            .putString("username", tokenHolder.getUser().getUsername())
                            .putString("firstName", tokenHolder.getUser().getFirstName())
                            .putString("lastName", tokenHolder.getUser().getLastName()).apply();

                    startMainActivity();
                }
                else if (response.code() == 404) {
                    Log.d("REST - LOGIN", "Bad credentails - username");
                    Toast.makeText(LoginActivity.this, "Invalid username",Toast.LENGTH_SHORT).show();
                }
                else if (response.code() == 401) {
                    Log.d("REST - LOGIN", "Bad credentails - password");
                    Toast.makeText(LoginActivity.this, "Wrong password",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("REST - LOGIN", "Something went wrong " + response.errorBody());
                    Toast.makeText(LoginActivity.this, "Something went wrong...",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenDTO> call, Throwable t) {
                Log.d("REST - LOGIN", "Login failed " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Something went wrong...",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
