package com.example.projekat_pmsu2020_sf_1_5_28.activities.profile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.accountActivities.CreateAccountActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.contactActivities.ContactFragment;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.splash.SplashActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Account;
import com.example.projekat_pmsu2020_sf_1_5_28.model.User;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.Base64;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.BitmapUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private ImageView mPhotoHolder, mAddPhoto;
    private TextView mUsername, mFirstName, mLastName, mDisplayName, mEmail;
    private EmailClientService mService;
    private SharedPreferences sharedPreferences;
    private String mDirPath;

    byte[] mNewPicture;

    public static ProfileFragment newInstance() {return new ProfileFragment();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPhotoHolder = getActivity().findViewById(R.id.userAvatarPicture);
        mUsername = getActivity().findViewById(R.id.usernameInput);
        mFirstName = getActivity().findViewById(R.id.firstNameInput);
        mLastName = getActivity().findViewById(R.id.lastNameInput);
        mDisplayName = getActivity().findViewById(R.id.displayNameInput);
        mEmail = getActivity().findViewById(R.id.emailInput);
        sharedPreferences = ((MainActivity) getActivity()).getSharedPreferences();
        mService = ((MainActivity) getActivity()).getEmailClientService();

        String username = sharedPreferences.getString("username", "");
        String firstName = sharedPreferences.getString("firstName", "");
        String lastName = sharedPreferences.getString("lastName", "");
        String currentAccountDisplayName = sharedPreferences.getString("currentAccountDisplayName", "");
        String currentAccountEmail = sharedPreferences.getString("currentAccountEmail", "");
        mDirPath = ((MainActivity) getActivity()).getmDirPath();

        Bitmap avatar;
        if ((avatar = BitmapUtil.getUserAvatar(mDirPath)) != null) {
            mPhotoHolder.setImageBitmap(avatar);
        }
        mUsername.setText(username);
        mFirstName.setText(firstName);
        mLastName.setText(lastName);
        mDisplayName.setText(currentAccountDisplayName);
        mEmail.setText(currentAccountEmail);

        mAddPhoto = getActivity().findViewById(R.id.add_photo);
        mAddPhoto.setOnClickListener(ProfileFragment.this);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.profile_fragment_menu, menu);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_save:
                Toast.makeText(getContext(),"Save user", Toast.LENGTH_SHORT).show();
                saveUser();
                return true;
            case R.id.item_changePassword:
                Toast.makeText(getContext(),"Change password", Toast.LENGTH_SHORT).show();
                changePassword();
                return true;
            case R.id.item_switch_account:
                Toast.makeText(getContext(),"Switch account", Toast.LENGTH_SHORT).show();
                chooseAccount();
                return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCurrentFragment(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
    }

    private void chooseAccount() {
        Long userId = sharedPreferences.getLong("userId", 0);
        Call<List<Account>> call = mService.getUserAccounts(userId);
        call.enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.code() == 200) {
                    List<Account> accounts = response.body();
                    Log.d("Accounts", accounts.toString());
                    chooseAccountDialog(accounts);
                }
                else if (response.code() == 401) {
                    //jwt owner different than saved user
                    //if ever happens just make the user login again
                    Toast.makeText(getContext(), "Please login again", Toast.LENGTH_SHORT).show();
                    logout();
                }
                else {
                    Toast.makeText(getContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                    //kill app ?
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                //kill app ?
            }
        });
    }

    private void chooseAccountDialog(final List<Account> accountsList) {
        final String[] accounts = new String[accountsList.size()];
        for (int i = 0; i < accountsList.size(); i++)
            accounts[i] = accountsList.get(i).getUsername();
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Choose account")
                .setItems(accounts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Account acc : accountsList)
                            if (acc.getUsername().equals(accounts[which])) {
                                sharedPreferences.edit().putLong("currentAccountId", acc.getId())
                                        .putString("currentAccountEmail", acc.getUsername())
                                        .putString("currentAccountDisplayName", acc.getDisplayName()).apply();
                                mDisplayName.setText(acc.getDisplayName());
                                mEmail.setText(acc.getUsername());
                                ((MainActivity)getActivity()).mCurrentAccountDisplayName().setText(acc.getDisplayName());
                                ((MainActivity)getActivity()).mCurrentAccountEmail().setText(acc.getUsername());
                            }
                    }
                }).show();
    }

    private void logout() {
        sharedPreferences.edit().remove("jwt").remove("userId").remove("username")
                .remove("firstName").remove("lastName").remove("currentAccountId").apply();
        Intent intent = new Intent(getContext(), SplashActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void startCreateAccountActivity() {
        Intent intent = new Intent(getContext(), CreateAccountActivity.class);
        startActivity(intent);
    }

    private void saveUser() {
        if (validate()) {
            User user = new User();
            if (mNewPicture != null)
                user.setEncodedAvatarData(Base64.encodeToString(mNewPicture));
            user.setFirstName(mFirstName.getText().toString().trim());
            user.setLastName(mLastName.getText().toString().trim());

            Long userId = sharedPreferences.getLong("userId", 0);
            if (userId != 0) {
                Call<User> call = mService.updateUser(userId, user);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getContext(), "User updated", Toast.LENGTH_SHORT).show();
                            if (mNewPicture != null) {
                                BitmapUtil.saveUserAvatar(mNewPicture, mDirPath);
                            }
                            ((ImageView) getActivity().findViewById(R.id.userAvatar)).setImageBitmap(BitmapUtil.getCroppedBitmapFromByteArray(mNewPicture));
                        }
                        else {
                            Toast.makeText(getContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private boolean validate() {
        if (mFirstName.getText().toString().trim().isEmpty()
                || mLastName.getText().toString().trim().isEmpty())
            return false;

        return true;
    }

    private void changePassword() {
        Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(),"onClick from fragment", Toast.LENGTH_SHORT).show();
        switch (v.getId()) {
            case R.id.add_photo:
                pickImage();
                break;
        }
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, BitmapUtil.PICK_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BitmapUtil.PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                InputStream is = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapUtil.readBitmapFromInputStream(is, 200, 200);
                mPhotoHolder.setImageBitmap(bitmap);
                mNewPicture = BitmapUtil.readBitmapBytes(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
