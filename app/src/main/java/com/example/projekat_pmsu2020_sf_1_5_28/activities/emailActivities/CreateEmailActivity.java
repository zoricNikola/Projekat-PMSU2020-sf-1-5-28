package com.example.projekat_pmsu2020_sf_1_5_28.activities.emailActivities;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Message;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.service.ServiceUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEmailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputEditText mFrom, mTo, mCc, mBcc, mSubject, mContent;
    private EmailClientService mService;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_email);
        setToolbar();

        mFrom = findViewById(R.id.inputEmailFrom);
        mTo = findViewById(R.id.inputEmailTo);
        mCc = findViewById(R.id.inputEmailCC);
        mBcc = findViewById(R.id.inputEmailBCC);
        mSubject = findViewById(R.id.inputEmailSubject);
        mContent = findViewById(R.id.inputEmailContent);

        mService = ServiceUtils.emailClientService(this);
        mSharedPreferences = getSharedPreferences(ServiceUtils.PREFERENCES_NAME, MODE_PRIVATE);
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle(R.string.Compose);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_email_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                Toast.makeText(CreateEmailActivity.this, "Back clicked", Toast.LENGTH_SHORT).show();
                onBackPressed();
                return true;
            case R.id.item_send:
                Toast.makeText(CreateEmailActivity.this, "Send clicked", Toast.LENGTH_SHORT).show();
                sendMessage();
                return true;
            case R.id.item_attachment:
                Toast.makeText(CreateEmailActivity.this, "Attachment clicked", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendMessage() {
        if (validate()) {
            Message message = new Message();
            message.setFrom(mFrom.getText().toString().trim());
            message.setTo(mTo.getText().toString().trim());
            message.setCc(mCc.getText().toString().trim());
            message.setBcc(mBcc.getText().toString().trim());
            message.setSubject(mSubject.getText().toString().trim());
            message.setContent(mContent.getText().toString().trim());

            Long accountId = mSharedPreferences.getLong("currentAccountId", 0);
            Call<Message> call = mService.createMessage(accountId, message);
            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if (response.code() == 201) {
                        Toast.makeText(CreateEmailActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(CreateEmailActivity.this, "Error sending message", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    Toast.makeText(CreateEmailActivity.this, "Error sending message", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private boolean validate() {
        if (mFrom.getText().toString().trim().equals("")
                || !Patterns.EMAIL_ADDRESS.matcher(mFrom.getText().toString().trim()).matches()
                || mTo.getText().toString().trim().equals("")
                || !Patterns.EMAIL_ADDRESS.matcher(mTo.getText().toString().trim()).matches()
                || mCc.getText().toString().trim().equals("")
                || !Patterns.EMAIL_ADDRESS.matcher(mCc.getText().toString().trim()).matches()
                || mBcc.getText().toString().trim().equals("")
                || !Patterns.EMAIL_ADDRESS.matcher(mBcc.getText().toString().trim()).matches()
                || mSubject.getText().toString().trim().equals("")
                || mContent.getText().toString().trim().equals("")) {
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
        String email = mSharedPreferences.getString("currentAccountEmail", "");
        mFrom.setText(email);
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
