package com.example.projekat_pmsu2020_sf_1_5_28.activities.contactActivities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Contact;

public class ContactFragment extends Fragment {

    Contact mContact;
    private TextView mFirstName, mLastName, mDisplayName, mEmail;

    public static ContactFragment newInstance() {return new ContactFragment();}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFirstName = getActivity().findViewById(R.id.firstName);
        mLastName = getActivity().findViewById(R.id.lastName);
        mDisplayName = getActivity().findViewById(R.id.displayName);
        mEmail = getActivity().findViewById(R.id.contactEmail);

        Bundle contactData = this.getArguments();
        Contact contact = (Contact) contactData.getSerializable("contact");

        mContact = contact;
        mFirstName.setText(contact.getFirstName());
        mLastName.setText(contact.getLastName());
        mDisplayName.setText(contact.getDisplayName());
        mEmail.setText(contact.getEmail());

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCurrentFragment(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
    }
}
