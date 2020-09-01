package com.example.projekat_pmsu2020_sf_1_5_28.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Rule;

import java.util.ArrayList;
import java.util.List;

public class RulesAdapter extends RecyclerView.Adapter<RulesAdapter.RuleViewHolder> {

    private List<Rule> mItems;
    private LayoutInflater inflater;
    private OnRuleItemListener mOnRuleItemListener;

    public RulesAdapter (Context context, List<Rule> items, OnRuleItemListener onRuleItemListener) {
        this.inflater = LayoutInflater.from(context);
        this.mItems = items;
        this.mOnRuleItemListener = onRuleItemListener;
    }

    @NonNull
    @Override
    public RuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_rule, parent, false);
        RuleViewHolder holder = new RuleViewHolder(view, mOnRuleItemListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RuleViewHolder holder, int position) {
        Rule current = mItems.get(position);
        holder.setData(current, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateItems(List<Rule> items) {
        this.mItems.clear();
        this.mItems.addAll(items);
        this.notifyDataSetChanged();
    }

    public void addItem(Rule rule) {
        this.mItems.add(rule);
        this.notifyDataSetChanged();
    }

    public void removeItem(Rule rule) {
        this.mItems.remove(rule);
        this.notifyDataSetChanged();
    }

    public List<Rule> getItems() { return this.mItems; }

    class RuleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Rule current;
        private TextView mRuleValue;
        private TextView mRuleCondition;
        private TextView mRuleOperation;
        private TextView mDelete;
        private OnRuleItemListener mOnRuleItemListener;

        public RuleViewHolder(@NonNull View itemView, OnRuleItemListener listener) {
            super(itemView);
            mRuleValue = itemView.findViewById(R.id.ruleValue);
            mRuleCondition = itemView.findViewById(R.id.ruleCondition);
            mRuleOperation = itemView.findViewById(R.id.ruleOperation);
            mDelete = itemView.findViewById(R.id.ruleDelete);
            mOnRuleItemListener = listener;

            mDelete.setOnClickListener(this);
        }

        public void setData (Rule current, int position) {
            this.current = current;
            this.mRuleValue.setText(current.getValue());
            this.mRuleCondition.setText(current.getCondition().toString());
            this.mRuleOperation.setText(current.getOperation().toString());
        }

        @Override
        public void onClick(View v) {
            mOnRuleItemListener.onRuleItemDelete(current);
        }
    }

    public interface OnRuleItemListener {
        void onRuleItemDelete(Rule rule);
    }
}
