package com.example.projekat_pmsu2020_sf_1_5_28.fragments;

import android.os.Build;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Email;

public class EmailFragment extends Fragment {

    private Email mEmail;
    private TextView mEmailSubject;
    private RecyclerView mLabelsRecyclerView;
    private ImageView mContactIcon;
    private TextView mSenderName, mDateTime, mEmailFrom, mEmailCC,
            mEmailBCC, mEmailTo, mEmailContent;

    public static EmailFragment newInstance() { return new EmailFragment();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstanceState){
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_email, vg, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEmailSubject = getActivity().findViewById(R.id.emailSubject);
        mLabelsRecyclerView = getActivity().findViewById(R.id.emailLabelsHolder);
        mContactIcon = getActivity().findViewById(R.id.contact_icon);
        mSenderName = getActivity().findViewById(R.id.senderName);
        mDateTime = getActivity().findViewById(R.id.emailDateTime);
        mEmailFrom = getActivity().findViewById(R.id.emailFrom);
        mEmailCC = getActivity().findViewById(R.id.emailCC);
        mEmailBCC = getActivity().findViewById(R.id.emailBCC);
        mEmailTo = getActivity().findViewById(R.id.emailTo);
        mEmailContent = getActivity().findViewById(R.id.emailContent);

        Bundle emailData = this.getArguments();
        Email email = (Email) emailData.getSerializable("email");
        mEmail = email;
        mEmailSubject.setText(email.getSubject());
        mDateTime.setText(email.getDateTimeString());
        mEmailFrom.setText(email.getFrom());
        mEmailCC.setText(email.getCc());
        mEmailBCC.setText(email.getBcc());
        mEmailTo.setText(email.getTo());
        mEmailContent.setText(email.getContent());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.email_fragment_menu, menu);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_deleteEmail:
                Toast.makeText(getContext(),"Delete email", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item_moveEmails:
                Toast.makeText(getContext(),"Move email", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item_changeTags:
                Toast.makeText(getContext(),"Change tag", Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCurrentFragment(this);
    }
}
