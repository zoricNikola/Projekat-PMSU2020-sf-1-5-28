package com.example.projekat_pmsu2020_sf_1_5_28.activities.emailActivities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Tag;
import com.example.projekat_pmsu2020_sf_1_5_28.service.EmailClientService;
import com.example.projekat_pmsu2020_sf_1_5_28.service.ServiceUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTagsDialogFragment extends DialogFragment {

    private DialogInterface.OnClickListener mListener;
    private List<Tag> allTags;

    private List<Tag> selectedTags;

    public List<Tag> getSelectedTags() {
        return selectedTags;
    }

    public UpdateTagsDialogFragment(DialogInterface.OnClickListener listener, List<Tag> allTags, List<Tag> selectedTags) {
        this.mListener = listener;
        this.allTags = allTags;
        this.selectedTags = new ArrayList<>();
        this.selectedTags.addAll(selectedTags);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] items = new String[allTags.size()];
        final boolean[] checkedItems = new boolean[allTags.size()];

        for (int i = 0; i < allTags.size(); i++) {
            items[i] = allTags.get(i).getName();
            checkedItems[i] = false;
            for (Tag tag : selectedTags) {
                if (tag.getName().equalsIgnoreCase(allTags.get(i).getName())) {
                    checkedItems[i] = true;
                }
            }
        }
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

        builder.setTitle("Choose tags").setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    selectedTags.add(allTags.get(which));
                }
                else {
                    for (Tag tag : selectedTags) {
                        if (tag.getName().equalsIgnoreCase(allTags.get(which).getName())) {
                            selectedTags.remove(tag);
                            break;
                        }
                    }
                }
            }
        }).setNeutralButton("New tag", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                input.setHint("New tag name");
                builder.setTitle("Create new tag").setView(input).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!input.getText().toString().trim().isEmpty()) {
                            Tag tag = new Tag();
                            tag.setName(input.getText().toString().trim());
                            Random rnd = new Random();
                            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                            tag.setColor(color);
                            selectedTags.add(tag);
                            mListener.onClick(dialog, which);
                        }
                    }
                });
                builder.create().show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton("Save", mListener);

        return builder.create();
    }


}
