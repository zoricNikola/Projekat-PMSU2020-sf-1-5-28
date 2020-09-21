package com.example.projekat_pmsu2020_sf_1_5_28.activities.emailActivities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Attachment;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Message;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Tag;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.Base64;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.BitmapUtil;
import com.example.projekat_pmsu2020_sf_1_5_28.tools.FileUtil;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {

    private Message mMessage;
    private TextView mEmailSubject;
    private ChipGroup mEmailTagsChipGroup;
    private ImageView mContactIcon;
    private TextView mSenderName, mDateTime, mEmailFrom, mEmailCC,
            mEmailBCC, mEmailTo, mEmailContent;
    private EmailClientService mService;
    private SharedPreferences mSharedPreferences;
    private Chip mAddChip;
    private List<Attachment> mAttachments;
    private TextView mAttachmentsCounter;
    String mDownloadsDir;

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
        mAddChip = getActivity().findViewById(R.id.addTagChip);

        Bundle emailData = this.getArguments();
        Message message = (Message) emailData.getSerializable("email");
        mMessage = message;
        mEmailSubject.setText(message.getSubject());
        mDateTime.setText(message.getDateTimeString());
        mEmailFrom.setText(message.getFrom());
        mEmailCC.setText(message.getCc());
        mEmailBCC.setText(message.getBcc());
        mEmailTo.setText(message.getTo());

        if (mMessage.getContactDisplayName() != null && !mMessage.getContactDisplayName().isEmpty()) {
            mSenderName.setText(mMessage.getContactDisplayName());
        }

        if (mMessage.getEncodedContactPhoto() != null && !mMessage.getEncodedContactPhoto().isEmpty()) {
            byte[] photoData = Base64.decode(mMessage.getEncodedContactPhoto());
            Bitmap bitmap = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
            mContactIcon.setImageBitmap(BitmapUtil.getCroppedBitmap(bitmap));
        }

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
        mEmailTagsChipGroup.removeView(mAddChip);
        for (Tag tag : message.getTags()) {
            Chip chip = new Chip(getContext());
            chip.setText(tag.getName());
            chip.setChipBackgroundColor(ColorStateList.valueOf(tag.getColor()));
            mEmailTagsChipGroup.addView(chip);
        }
        mEmailTagsChipGroup.addView(mAddChip);

        mAddChip.setOnClickListener(EmailFragment.this);

        mService = ((MainActivity) getActivity()).getEmailClientService();
        mSharedPreferences = ((MainActivity) getActivity()).getSharedPreferences();

        File dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        mDownloadsDir = dir.getPath();
        mAttachments = new ArrayList<>();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.email_fragment_menu, menu);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        final MenuItem item = menu.findItem(R.id.item_attachment);
        MenuItemCompat.setActionView(item, R.layout.menu_item_attachment_layout);
        RelativeLayout badgeLayout = (RelativeLayout) MenuItemCompat.getActionView(item);
        mAttachmentsCounter = (TextView) badgeLayout.findViewById(R.id.attachment_counter);
        View icon = badgeLayout.findViewById(R.id.attachment_icon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item);
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_attachment:
                Toast.makeText(getContext(),"Attachments", Toast.LENGTH_SHORT).show();
                openAttachmentsDialog();
                return true;
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
                openUpdateTagsDialog();
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
                        mMessage.setUnread(false);
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

        Call<List<Attachment>> callA = mService.getMessageAttachments(mMessage.getId());
        callA.enqueue(new Callback<List<Attachment>>() {
            @Override
            public void onResponse(Call<List<Attachment>> call, Response<List<Attachment>> response) {
                if (response.code() == 200) {
                    mAttachments.addAll(response.body());
                    mAttachmentsCounter.setText(mAttachments.size() > 0 ? String.valueOf(mAttachments.size()) : "");
                }
                else
                    Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Attachment>> call, Throwable t) {
                Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private UpdateTagsDialogFragment mUpdateDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addTagChip:
                openUpdateTagsDialog();
        }
    }

    private void openUpdateTagsDialog() {
        Long userId = mSharedPreferences.getLong("userId", 0);
        if (userId != 0) {
            Call<List<Tag>> call = mService.getUserTags(userId);
            call.enqueue(new Callback<List<Tag>>() {
                @Override
                public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                    if (response.code() == 200) {
                        List<Tag> allTags = response.body();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        UpdateTagsDialogFragment dialog = new UpdateTagsDialogFragment(EmailFragment.this, allTags, mMessage.getTags());
                        dialog.show(fragmentManager, "tagSelection");
                        mUpdateDialog = dialog;
                    }
                }

                @Override
                public void onFailure(Call<List<Tag>> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        List<Tag> selectedTags = mUpdateDialog.getSelectedTags();
        if (mMessage.getTags().containsAll(selectedTags) && selectedTags.containsAll(mMessage.getTags()))
            return;
        mMessage.getTags().removeAll(mMessage.getTags());
        mMessage.getTags().addAll(selectedTags);

        Call<Message> call = mService.updateMessageTags(mMessage.getId(), mMessage);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.code() == 201) {
                    Toast.makeText(getContext(),"Message tags updated", Toast.LENGTH_SHORT).show();
                    mMessage = response.body();
                    mEmailTagsChipGroup.removeAllViews();
                    for (Tag tag : mMessage.getTags()) {
                        Chip chip = new Chip(getContext());
                        chip.setText(tag.getName());
                        chip.setChipBackgroundColor(ColorStateList.valueOf(tag.getColor()));
                        mEmailTagsChipGroup.addView(chip);
                    }

                    mEmailTagsChipGroup.addView(mAddChip);
                }
                else {
                    Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openAttachmentsDialog() {
        int count = mAttachments.size();
        if (count > 0) {
            final String[] items = new String[count];
            for (int i = 0; i < count; i++) {
                Attachment current = mAttachments.get(i);
                String displayName = String.format("%s.%s", current.getName(), current.getMimeType());
                items[i] = displayName;
            }
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

            builder.setTitle("Attachments").setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getContext(),items[which], Toast.LENGTH_SHORT).show();
                    saveAttachment(mAttachments.get(which).getId());
                }
            }).create().show();
        }
        else
            Toast.makeText(getContext(),"There is no attachments", Toast.LENGTH_SHORT).show();
    }

    private void saveAttachment(Long attachmentId) {
        Call<Attachment> call = mService.getAttachmentById(attachmentId);
        call.enqueue(new Callback<Attachment>() {
            @Override
            public void onResponse(Call<Attachment> call, Response<Attachment> response) {
                if (response.code() == 200) {
                    Attachment attachment = response.body();
                    File file = FileUtil.saveAsFile(Base64.decode(attachment.getData()), mDownloadsDir, attachment.getName());
                    if (file != null) {
                        FileUtil.openFile(getContext(), file);
                    }
                }
                else
                    Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Attachment> call, Throwable t) {
                Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
