package com.example.projekat_pmsu2020_sf_1_5_28;

import android.os.StrictMode;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }
}
