package com.example.projekat_pmsu2020_sf_1_5_28.activities.emailActivities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
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
import androidx.arch.core.internal.FastSafeIterableMap;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Email;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Message;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Tag;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.service.ServiceUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailFragment extends Fragment {

    private Message mMessage;
    private TextView mEmailSubject;
    private ChipGroup mEmailTagsChipGroup;
    private ImageView mContactIcon;
    private TextView mSenderName, mDateTime, mEmailFrom, mEmailCC,
            mEmailBCC, mEmailTo, mEmailContent;
    private EmailClientService mService;

    public static EmailFragment newInstance() { return new EmailFragment();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstanceState){
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_email, vg, false);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEmailSubject = getActivity().findViewById(R.id.emailSubject);
        mEmailTagsChipGroup = getActivity().findViewById(R.id.emailTagsChipGroup);
        mContactIcon = getActivity().findViewById(R.id.contact_icon);
        mSenderName = getActivity().findViewById(R.id.senderName);
        mDateTime = getActivity().findViewById(R.id.emailDateTime);
        mEmailFrom = getActivity().findViewById(R.id.emailFrom);
        mEmailCC = getActivity().findViewById(R.id.emailCC);
        mEmailBCC = getActivity().findViewById(R.id.emailBCC);
        mEmailTo = getActivity().findViewById(R.id.emailTo);
        mEmailContent = getActivity().findViewById(R.id.emailContent);

        Bundle emailData = this.getArguments();
        Message message = (Message) emailData.getSerializable("email");
        mMessage = message;
        mEmailSubject.setText(message.getSubject());
        mDateTime.setText(message.getDateTimeString());
        mEmailFrom.setText(message.getFrom());
        mEmailCC.setText(message.getCc());
        mEmailBCC.setText(message.getBcc());
        mEmailTo.setText(message.getTo());

        String content = message.getContent();
        if (content.startsWith("<!DOCTYPE HTML>") || content.startsWith("<!DOCTYPE html>")
            || content.startsWith("<!doctype html>") || content.startsWith("<!doctype HTML>")) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Spanned sp = Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY);
                mEmailContent.setText(sp);
            } else {
                Spanned sp = Html.fromHtml(content);
                mEmailContent.setText(sp);
            }

            mEmailContent.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT));
        }
        else {
            mEmailContent.setText(content);
        }

        for (Tag tag : message.getTags()) {
            Chip chip = new Chip(getContext());
            chip.setText(tag.getName());
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            chip.setChipBackgroundColor(ColorStateList.valueOf(color));
            mEmailTagsChipGroup.addView(chip);
        }

        mService = ((MainActivity) getActivity()).getEmailClientService();
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
            case R.id.item_reply:
                Toast.makeText(getContext(),"Reply", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_replyAll:
                Toast.makeText(getContext(),"Reply to all", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_forward:
                Toast.makeText(getContext(),"Forward", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_deleteEmail:
                Toast.makeText(getContext(),"Delete email", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_moveEmails:
                Toast.makeText(getContext(),"Move email", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_changeTags:
                Toast.makeText(getContext(),"Change tag", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCurrentFragment(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);

        if (mMessage.isUnread()) {
            Call<Boolean> call = mService.markMessageAsRead(mMessage.getId());
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.code() == 200 && response.body()) {
                        Toast.makeText(getContext(),"Message marked as read", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getContext(),"Error marking as read", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(getContext(),"Error marking as read", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
