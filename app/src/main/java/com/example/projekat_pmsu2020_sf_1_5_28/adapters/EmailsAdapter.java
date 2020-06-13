package com.example.projekat_pmsu2020_sf_1_5_28.adapters;

import android.content.Context;
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
import com.example.projekat_pmsu2020_sf_1_5_28.model.Email;

import java.util.List;

public class EmailsAdapter extends RecyclerView.Adapter<EmailsAdapter.EmailViewHolder> {

    private List<Email> mItems;
    private LayoutInflater inflater;
    private OnEmailItemListener mOnEmailItemListener;

    public EmailsAdapter(Context context, List<Email> items, OnEmailItemListener mOnEmailItemListener) {
        this.inflater = LayoutInflater.from(context);
        this.mItems = items;
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
        Email current = mItems.get(position);
        holder.setData(current, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class EmailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CardView mCardItem;
        private ImageView mContactIcon;
        private TextView mSenderName, mDateTime, mSubject, mContent;
        private OnEmailItemListener mOnEmailItemListener;

        public EmailViewHolder(@NonNull View itemView, OnEmailItemListener listener) {
            super(itemView);
            mCardItem = (CardView) itemView.findViewById(R.id.cardEmailListItem);
            mContactIcon = (ImageView) itemView.findViewById(R.id.contact_icon);
            mSenderName = (TextView) itemView.findViewById(R.id.senderName);
            mDateTime = (TextView) itemView.findViewById(R.id.dateTime);
            mSubject = (TextView) itemView.findViewById(R.id.subject);
            mContent = (TextView) itemView.findViewById(R.id.content);
            mOnEmailItemListener = listener;

            itemView.setOnClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void setData (Email current, int position) {
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
        }

        @Override
        public void onClick(View v) {
            mOnEmailItemListener.onEmailItemClick(getAdapterPosition());
        }
    }

    public interface OnEmailItemListener {
        void onEmailItemClick(int position);
    }

}
