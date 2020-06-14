package com.example.projekat_pmsu2020_sf_1_5_28.adapters;

import android.content.Context;
import android.net.sip.SipSession;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekat_pmsu2020_sf_1_5_28.R;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Folder;

import java.util.List;

public class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.FolderViewHolder> {

    private List<Folder> mFolders;
    private LayoutInflater inflater;
    private OnFolderItemListener onFolderItemListener;


    public FoldersAdapter(Context context, List<Folder> folders, OnFolderItemListener onFolderItemListener){
        this.inflater = LayoutInflater.from(context);
        this.mFolders = folders;
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
        Folder current = mFolders.get(position);
        holder.setData(current, position);
    }

    @Override
    public int getItemCount() {
        return mFolders.size();
    }

    class FolderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView folderName;
        private TextView folderCounter;
        private OnFolderItemListener onFolderItemListener;

        public FolderViewHolder(@NonNull View itemView, OnFolderItemListener listener) {
            super(itemView);
            folderName = (TextView) itemView.findViewById(R.id.folderName);
            folderCounter = (TextView) itemView.findViewById(R.id.folderCounter);
            onFolderItemListener = listener;
            itemView.setOnClickListener(this);
        }

        public void setData(Folder current, int position) {

            this.folderName.setText(current.getName());
            //this.folderCounter.setText(current.get);

        }

        @Override
        public void onClick(View v) {
            onFolderItemListener.onFolderItemClick(getAdapterPosition());
        }
    }

    public interface OnFolderItemListener{
        void onFolderItemClick(int position);
    }


}
