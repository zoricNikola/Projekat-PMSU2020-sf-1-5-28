package com.example.projekat_pmsu2020_sf_1_5_28.activities.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.login.LoginActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.model.User;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.service.ServiceUtils;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.Base64;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.BitmapUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private String mDirPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        mDirPath = dir.getPath();

        if(isOnline()){
            int splashTime = 1000;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    SharedPreferences sharedPreferences = getSharedPreferences(ServiceUtils.PREFERENCES_NAME, MODE_PRIVATE);
                    String jwt = sharedPreferences.getString("jwt", null);
                    Long userId = sharedPreferences.getLong("userId", 0);
                    if (jwt != null && userId != 0) {
                        EmailClientService service = ServiceUtils.emailClientService(SplashActivity.this);
                        Call<User> call = service.getUserById(userId);
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
//                              JWT exists and is valid
                                if (response.code() == 200) {
                                    String encodedAvatar = response.body().getEncodedAvatarData();
                                    if (encodedAvatar!= null && !encodedAvatar.isEmpty())
                                        BitmapUtil.saveUserAvatar(Base64.decode(encodedAvatar), mDirPath);
                                    startMainActivity();
                                }
                                else
                                    startLoginActivity();
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                startLoginActivity();
                            }
                        });
                    }
                    else
                        startLoginActivity();
                }
            }, splashTime);
        }else{
            Toast.makeText(this, "No network connection",Toast.LENGTH_SHORT).show();
        }
    }

    protected boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }

    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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
