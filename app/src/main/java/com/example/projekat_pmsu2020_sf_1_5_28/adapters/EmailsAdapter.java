package com.example.projekat_pmsu2020_sf_1_5_28.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Message;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Tag;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class EmailsAdapter extends RecyclerView.Adapter<EmailsAdapter.EmailViewHolder> {

    private Context mContext;
    private List<Message> mItems = new ArrayList<Message>();
    private LayoutInflater inflater;
    private OnEmailItemListener mOnEmailItemListener;

    public EmailsAdapter(Context context, List<Message> items, OnEmailItemListener mOnEmailItemListener) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mItems.addAll(items);
        this.mOnEmailItemListener = mOnEmailItemListener;
    }

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_email, parent, false);
        EmailViewHolder holder = new EmailViewHolder(view, mOnEmailItemListener);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, int position) {
        Message current = mItems.get(position);
        holder.setData(current, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateItems(List<Message> items) {
        this.mItems.clear();
        this.mItems.addAll(items);
        this.notifyDataSetChanged();
    }

    class EmailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Message current;
        private CardView mCardItem;
        private ImageView mContactIcon;
        private TextView mSenderName, mDateTime, mSubject, mContent;
        private ChipGroup mEmailTagsChipGroup;
        private OnEmailItemListener mOnEmailItemListener;

        public EmailViewHolder(@NonNull View itemView, OnEmailItemListener listener) {
            super(itemView);
            mCardItem = itemView.findViewById(R.id.cardEmailListItem);
            mContactIcon = itemView.findViewById(R.id.contact_icon);
            mSenderName = itemView.findViewById(R.id.senderName);
            mDateTime = itemView.findViewById(R.id.dateTime);
            mSubject = itemView.findViewById(R.id.subject);
            mContent = itemView.findViewById(R.id.content);
            mEmailTagsChipGroup = itemView.findViewById(R.id.emailTagsChipGroup);
            mOnEmailItemListener = listener;

            itemView.setOnClickListener(this);
        }

        @SuppressLint("ResourceAsColor")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setData (Message current, int position) {
            this.current = current;

            mSenderName.setText(current.getFrom());

            mDateTime.setText(current.getDateTimeString());

            mSubject.setText(current.getSubject());

            try {
                mContent.setText(current.getContent().substring(0, 40));
            }
            catch (IndexOutOfBoundsException e) {
                mContent.setText(current.getContent());
            }
            catch (Exception e) {
//            ??
            }

            mEmailTagsChipGroup.removeAllViews();

            for (Tag tag : current.getTags()) {
                Chip chip = new Chip(mContext);
                chip.setText(tag.getName());
                chip.setChipBackgroundColor(ColorStateList.valueOf(R.color.colorAccent));
                mEmailTagsChipGroup.addView(chip);
            }
        }

        @Override
        public void onClick(View v) {
            mOnEmailItemListener.onEmailItemClick(this.current);
        }
    }

    public interface OnEmailItemListener {
        void onEmailItemClick(Message email);
    }

}
