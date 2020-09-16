package com.example.projekat_pmsu2020_sf_1_5_28.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Account;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountViewHolder> {

    private List<Account> mItems = new ArrayList<Account>();
    private LayoutInflater inflater;
    private AccountsAdapter.OnAccountItemListener mOnAccountItemListener;

    public AccountsAdapter (Context context, List<Account> items, AccountsAdapter.OnAccountItemListener onAccountItemListener) {
        this.inflater = LayoutInflater.from(context);
        this.mItems = items;
        this.mOnAccountItemListener = onAccountItemListener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_account, parent, false);
        AccountsAdapter.AccountViewHolder holder = new AccountsAdapter.AccountViewHolder(view, mOnAccountItemListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        Account current = mItems.get(position);
        holder.setData(current, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateItems(List<Account> items) {
        this.mItems.clear();
        this.mItems.addAll(items);
        this.notifyDataSetChanged();
    }

    class AccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Account current;
        private TextView mDisplayName;
        private TextView mAccountUsername;
        private OnAccountItemListener mOnAccountItemListener;

        public AccountViewHolder(@NonNull View itemView, OnAccountItemListener listener) {
            super(itemView);
            mDisplayName = itemView.findViewById(R.id.accountDisplayName);
            mAccountUsername = itemView.findViewById(R.id.accountUsername);
            mOnAccountItemListener = listener;

            itemView.setOnClickListener(this);
        }

        public void setData(Account current, int position) {
            this.current = current;
            this.mDisplayName.setText(current.getDisplayName());
            this.mAccountUsername.setText(current.getUsername());
        }

        @Override
        public void onClick(View v) {
            mOnAccountItemListener.onAccountItemClick(current);
        }
    }

    public interface OnAccountItemListener {
        void onAccountItemClick(Account account);
    }
}
