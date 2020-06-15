package com.example.projekat_pmsu2020_sf_1_5_28.activities;

import android.content.Intent;
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
import com.example.projekat_pmsu2020_sf_1_5_28.activities.emailActivities.CreateEmailActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.settingActivities.SettingsActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.adapters.ContactsAdapter;
import com.example.projekat_pmsu2020_sf_1_5_28.adapters.EmailsAdapter;
import com.example.projekat_pmsu2020_sf_1_5_28.adapters.FoldersAdapter;
import com.example.projekat_pmsu2020_sf_1_5_28.fragments.ContactsFragment;
import com.example.projekat_pmsu2020_sf_1_5_28.fragments.EmailFragment;
import com.example.projekat_pmsu2020_sf_1_5_28.fragments.EmailsFragment;
import com.example.projekat_pmsu2020_sf_1_5_28.fragments.FoldersFragment;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.FragmentTransition;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.Mokap;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FoldersAdapter.OnFolderItemListener,
                                                            EmailsAdapter.OnEmailItemListener, ContactsAdapter.OnContactItemListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private boolean appStarting;

    private Fragment mEmailsFragment;
    private Fragment mFoldersFragment;
    private Fragment currentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
        setNavigationDrawer();
        mEmailsFragment = EmailsFragment.newInstance();
        mFoldersFragment = FoldersFragment.newInstance();

        appStarting = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (appStarting)
            navigationItemClicked(R.id.item_emails);
    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setTitle(null);
    }

    private void setNavigationDrawer() {
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mNavigationView = findViewById(R.id.navigationView);
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
                    startFoldersFragment();
                    break;
                case R.id.item_contacts:
                    startContactsFragment();
                    break;
                case R.id.item_settings:
                    startSettingsActivity();
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

    private void  startFoldersFragment() {
        boolean addToBackStack = true;
        FragmentTransition.to(mFoldersFragment, MainActivity.this,addToBackStack);
    }

    private void startContactsFragment() {
        FragmentTransition.to(new ContactsFragment(), MainActivity.this, true);
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
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
                if(currentFragment instanceof EmailsFragment ||
                    currentFragment instanceof ContactsFragment ||
                    currentFragment instanceof FoldersFragment)
                    mDrawerLayout.openDrawer(GravityCompat.START);
                else
                    super.onBackPressed();
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
        Bundle emailData = new Bundle();
        emailData.putSerializable("email", Mokap.getEmails().get(position));
        EmailFragment emailFragment = EmailFragment.newInstance();
        emailFragment.setArguments(emailData);
        FragmentTransition.to(emailFragment, MainActivity.this, true);
    }

    @Override
    public void onContactItemClick(int position) {
        Toast.makeText(MainActivity.this, "Contact " + position, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFolderItemClick(int position) {
        Toast.makeText(MainActivity.this, "Folder " + position, Toast.LENGTH_LONG).show();
//        Bundle folderData = new Bundle();
//        folderData.putSerializable("folder",Mokap.getFolders().get(position));
//        FoldersFragment foldersFragment = (FoldersFragment) FoldersFragment.newInstance();
//        foldersFragment.setArguments(folderData);
//        FragmentTransition.to(foldersFragment, MainActivity.this, true);

    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
        if (currentFragment instanceof EmailsFragment || currentFragment instanceof EmailFragment)
            mNavigationView.getMenu().findItem(R.id.item_emails).setChecked(true);
        else if (currentFragment instanceof  FoldersFragment)
            mNavigationView.getMenu().findItem(R.id.item_folders).setChecked(true);
        else if (currentFragment instanceof  ContactsFragment)
            mNavigationView.getMenu().findItem(R.id.item_contacts).setChecked(true);


    }
}
