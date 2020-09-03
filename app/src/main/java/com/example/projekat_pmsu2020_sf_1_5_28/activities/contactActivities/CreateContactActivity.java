package com.example.projekat_pmsu2020_sf_1_5_28.activities.contactActivities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Contact;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.service.ServiceUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateContactActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextInputEditText mFirstName, mLastName, mDisplayName, mEmail, mNote;
    private TextInputLayout mFirstNameLayout, mLastNameLayout, mDisplayNameLayout, mEmailLayout;
    private EmailClientService mService;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        setToolbar();

        mFirstName = findViewById(R.id.firstNameInput);
        mLastName = findViewById(R.id.lastNameInput);
        mDisplayName = findViewById(R.id.displayNameInput);
        mEmail = findViewById(R.id.emailInput);
        mNote = findViewById(R.id.noteInput);

        mFirstNameLayout = findViewById(R.id.firstNameInputLayout);
        mLastNameLayout = findViewById(R.id.lastNameInputLayout);
        mDisplayNameLayout = findViewById(R.id.displayNameInputLayout);
        mEmailLayout = findViewById(R.id.emailInputLayout);

        mService = ServiceUtils.emailClientService(this);
        mSharedPreferences = getSharedPreferences(ServiceUtils.PREFERENCES_NAME, MODE_PRIVATE);

        setTextChangeListeners();

    }

    private void setToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar_create_contact);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle(R.string.create_contact);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_contact_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.contact_save:
                Toast.makeText(CreateContactActivity.this,"Save contact",Toast.LENGTH_SHORT).show();
                createContact();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTextChangeListeners() {
        mFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                createErrorMessages("firstName");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                createErrorMessages("lastName");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDisplayName.addTextChangedListener(new TextWatcher() {
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
    }

    private void createErrorMessages(String which) {
        if (which.equals("all") || which.equals("firstName")) {
            if (mFirstName.getText().toString().trim().equals(""))
                mFirstNameLayout.setError("First Name can't be blank");
            else
                mFirstNameLayout.setError(null);
        }
        if (which.equals("all") || which.equals("lastName")) {
            if (mLastName.getText().toString().trim().equals(""))
                mLastNameLayout.setError("Last Name can't be blank");
            else
                mLastNameLayout.setError(null);
        }
        if (which.equals("all") || which.equals("displayName")) {
            if (mDisplayName.getText().toString().trim().equals(""))
                mDisplayNameLayout.setError("Display Name can't be blank");
            else
                mDisplayNameLayout.setError(null);
        }
        if (which.equals("all") || which.equals("email")) {
            if (mEmail.getText().toString().trim().equals(""))
                mEmailLayout.setError("Email can't be blank");
            else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString().trim()).matches())
                mEmailLayout.setError("Invalid email address");
            else
                mEmailLayout.setError(null);
        }
    }

    private void createContact() {
        if (validate()) {
            Contact contact = new Contact();
            contact.setFirstName(mFirstName.getText().toString().trim());
            contact.setLastName(mLastName.getText().toString().trim());
            contact.setDisplayName(mDisplayName.getText().toString().trim());
            contact.setEmail(mEmail.getText().toString().trim());
            contact.setNote(mNote.getText().toString().trim());
            contact.setPhotoPath("");

            Long userId = mSharedPreferences.getLong("userId", 0);
            if (userId != 0) {
                Call<Contact> call = mService.createContact(userId, contact);
                call.enqueue(new Callback<Contact>() {
                    @Override
                    public void onResponse(Call<Contact> call, Response<Contact> response){
                        if(response.code() == 201) {
                            Toast.makeText(CreateContactActivity.this,"Contact created",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(CreateContactActivity.this,"Something went wrong...",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Contact> call, Throwable t) {
                        Toast.makeText(CreateContactActivity.this,"Something went wrong...",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private boolean validate() {
        createErrorMessages("all");
        if ( mFirstNameLayout.getError() != null
                || mLastNameLayout.getError() != null
                || mDisplayNameLayout.getError() != null
                || mEmailLayout.getError() != null ) {
            return false;
        }
        return true;
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