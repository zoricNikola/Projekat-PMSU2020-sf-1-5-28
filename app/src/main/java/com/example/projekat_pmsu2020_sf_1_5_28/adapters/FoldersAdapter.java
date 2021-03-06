package com.example.projekat_pmsu2020_sf_1_5_28.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Contact;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Folder;

import java.util.List;

public class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.FolderViewHolder> {

    private List<Folder> mItems;
    private LayoutInflater inflater;
    private OnFolderItemListener onFolderItemListener;


    public FoldersAdapter(Context context, List<Folder> folders, OnFolderItemListener onFolderItemListener){
        this.inflater = LayoutInflater.from(context);
        this.mItems = folders;
        this.onFolderItemListener = onFolderItemListener;

    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_folder, parent, false);
        FolderViewHolder holder = new FolderViewHolder(view,onFolderItemListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        Folder current = mItems.get(position);
        holder.setData(current, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateItems(List<Folder> items) {
        this.mItems.clear();
        this.mItems.addAll(items);
        this.notifyDataSetChanged();
    }

    class FolderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Folder current;
        private TextView folderName;
        private TextView folderCounter;
        private OnFolderItemListener onFolderItemListener;

        public FolderViewHolder(@NonNull View itemView, OnFolderItemListener listener) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folderName);
            folderCounter = itemView.findViewById(R.id.folderCounter);
            onFolderItemListener = listener;
            itemView.setOnClickListener(this);
        }

        public void setData(Folder current, int position) {
            this.current = current;
            this.folderName.setText(current.getName());
            this.folderCounter.setText(String.valueOf(current.getNumberOfMessages()));

        }

        @Override
        public void onClick(View v) {
            onFolderItemListener.onFolderItemClick(this.current);
        }
    }

    public interface OnFolderItemListener{
        void onFolderItemClick(Folder folder);
    }


}
