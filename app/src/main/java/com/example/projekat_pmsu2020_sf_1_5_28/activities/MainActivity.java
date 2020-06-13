package com.example.projekat_pmsu2020_sf_1_5_28.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.adapters.EmailsAdapter;
import com.example.projekat_pmsu2020_sf_1_5_28.fragments.EmailsFragment;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.FragmentTransition;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.Mokap;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                            EmailsAdapter.OnEmailItemListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private boolean appStarting;

    private Fragment mEmailsFragment;
    public Fragment currentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
        setNavigationDrawer();
        mEmailsFragment = EmailsFragment.newInstance();

        appStarting = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationItemClicked(R.id.item_emails);
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setTitle(null);
    }

    private void setNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationView);
        mNavigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        navigationItemClicked(item.getItemId());

        return true;
    }

    private void navigationItemClicked(int itemId) {

        MenuItem item = mNavigationView.getMenu().findItem(itemId);

        if (!item.isChecked()) {
            if (appStarting)
                item.setChecked(true);
            Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
            mDrawerLayout.closeDrawer(GravityCompat.START);

            switch (itemId) {
                case R.id.item_emails:
                    startEmailsFragment();
                    break;
                case R.id.item_folders:
                    // folders
                    break;
                case R.id.item_contacts:
                    // contacts
                    break;
                case R.id.item_settings:
                    // settings
                    break;
            }
        }
        else {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

    }

    private void startEmailsFragment() {
        boolean addToBackStack = true;
        if (appStarting) {
            addToBackStack = false;
            appStarting = false;
        }
        FragmentTransition.to(mEmailsFragment, MainActivity.this, addToBackStack);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.item_search:
                Toast.makeText(MainActivity.this, "Search clicked", Toast.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    public void onUserAvatarClick(View view) {
        Toast.makeText(MainActivity.this, "User avatar clicked", Toast.LENGTH_LONG).show();
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onEmailItemClick(int position) {
        Toast.makeText(MainActivity.this, "Email " + position, Toast.LENGTH_LONG).show();
    }
}
