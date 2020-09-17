package com.example.projekat_pmsu2020_sf_1_5_28.activities.contactActivities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Contact;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.Base64;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.BitmapUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContactFragment extends Fragment implements View.OnClickListener {

    private Contact mContact;
    private ImageView mPhotoHolder, mAddPhoto;
    private TextInputEditText mFirstName, mLastName, mDisplayName, mEmail, mNote;
    private TextInputLayout mFirstNameLayout, mLastNameLayout, mDisplayNameLayout, mEmailLayout;
    private EmailClientService mService;

    private byte[] mNewPicture;

    public static ContactFragment newInstance() {return new ContactFragment();}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.contact_fragment_menu, menu);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPhotoHolder = getActivity().findViewById(R.id.contact_icon);
        mFirstName = getActivity().findViewById(R.id.firstNameInput);
        mLastName = getActivity().findViewById(R.id.lastNameInput);
        mDisplayName = getActivity().findViewById(R.id.displayNameInput);
        mEmail = getActivity().findViewById(R.id.emailInput);
        mNote = getActivity().findViewById(R.id.noteInput);

        mFirstNameLayout = getActivity().findViewById(R.id.firstNameInputLayout);
        mLastNameLayout = getActivity().findViewById(R.id.lastNameInputLayout);
        mDisplayNameLayout = getActivity().findViewById(R.id.displayNameInputLayout);
        mEmailLayout = getActivity().findViewById(R.id.emailInputLayout);

        mService = ((MainActivity) getActivity()).getEmailClientService();

        setTextChangeListeners();

        Bundle contactData = this.getArguments();
        Contact contact = (Contact) contactData.getSerializable("contact");
        this.getArguments().clear();

        mContact = contact;
        if (contact.getEncodedPhotoData() != null && !contact.getEncodedPhotoData().isEmpty()) {
            Bitmap bitmap = BitmapUtil.readBitmapFromBytes(Base64.decode(contact.getEncodedPhotoData()));
            mPhotoHolder.setImageBitmap(bitmap);
        }
        mFirstName.setText(contact.getFirstName());
        mLastName.setText(contact.getLastName());
        mDisplayName.setText(contact.getDisplayName());
        mEmail.setText(contact.getEmail());
        mNote.setText(contact.getNote());

        mAddPhoto = getActivity().findViewById(R.id.add_photo);
        mAddPhoto.setOnClickListener(ContactFragment.this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_saveContact:
                Toast.makeText(getContext(),"Save contact", Toast.LENGTH_SHORT).show();
                saveContact();
                return true;
            case R.id.item_deleteContact:
                Toast.makeText(getContext(),"Delete contact", Toast.LENGTH_SHORT).show();
                deleteContact();
                return true;
        }
        return false;
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

    private void saveContact() {
        if (validate()) {

            if (mNewPicture != null)
                mContact.setEncodedPhotoData(Base64.encodeToString(mNewPicture));
            mContact.setFirstName(mFirstName.getText().toString().trim());
            mContact.setLastName(mLastName.getText().toString().trim());
            mContact.setDisplayName(mDisplayName.getText().toString().trim());
            mContact.setEmail(mEmail.getText().toString().trim());
            mContact.setNote(mNote.getText().toString().trim());

            Call<Contact> call = mService.updateContact(mContact.getId(), mContact);
            call.enqueue(new Callback<Contact>() {
                @Override
                public void onResponse(Call<Contact> call, Response<Contact> response) {
                    if (response.code() == 201) {
                        Toast.makeText(getContext(),"Contact updated", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Contact> call, Throwable t) {
                    Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });

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

    private void deleteContact() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(R.string.delete_contact)
                .setMessage(R.string.delete_contact_alert_message)
                .setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Void> call = mService.removeContact(mContact.getId());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(getContext(),"Contact deleted", Toast.LENGTH_SHORT).show();
                                    getActivity().onBackPressed();
                                }
                                else {
                                    Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCurrentFragment(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
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
