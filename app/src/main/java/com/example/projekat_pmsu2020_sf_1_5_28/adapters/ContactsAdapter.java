package com.example.projekat_pmsu2020_sf_1_5_28.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Contact;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private List<Contact> mItems;
    private LayoutInflater inflater;
    private OnContactItemListener mOnContactItemListener;

    public ContactsAdapter (Context context, List<Contact> items, OnContactItemListener onContactItemListener) {
        this.inflater = LayoutInflater.from(context);
        this.mItems = items;
        this.mOnContactItemListener = onContactItemListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_contact, parent, false);
        ContactViewHolder holder = new ContactViewHolder(view, mOnContactItemListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact current = mItems.get(position);
        holder.setData(current, position);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mContactIcon;
        private TextView mContactName;
        private OnContactItemListener mOnContactItemListener;

        public ContactViewHolder(@NonNull View itemView, OnContactItemListener listener) {
            super(itemView);
            mContactIcon = itemView.findViewById(R.id.contact_icon);
            mContactName = itemView.findViewById(R.id.contactName);
            mOnContactItemListener = listener;

            itemView.setOnClickListener(this);
        }

        public void setData(Contact current, int position) {
            this.mContactName.setText(current.getDisplayName());
        }

        @Override
        public void onClick(View v) {
            mOnContactItemListener.onContactItemClick(getAdapterPosition());
        }
    }

    public interface OnContactItemListener {
        void onContactItemClick(int position);
    }
}
