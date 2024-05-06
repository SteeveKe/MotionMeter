package com.capucinetulipe.motionmeter.viewHolders;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.capucinetulipe.motionmeter.R;
import com.capucinetulipe.motionmeter.database.FolderDAO;
import com.capucinetulipe.motionmeter.database.entities.Folder;

public class FolderAdapter extends ListAdapter<Folder, FolderViewHolder> {

    public FolderAdapter(@NonNull DiffUtil.ItemCallback<Folder> folderItemCallback){
        super(folderItemCallback);
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return FolderViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        Folder current = getItem(position);
        int rID;
        if (current.isGlobal()){
            rID = R.drawable.baseline_folder_special_24;
        }
        else {
            rID = R.drawable.baseline_folder_shared_24;
        }
        holder.bind(current.getFolderName(), rID);
    }

    public static class FolderDiff extends DiffUtil.ItemCallback<Folder>{

        @Override
        public boolean areItemsTheSame(@NonNull Folder oldItem, @NonNull Folder newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Folder oldItem, @NonNull Folder newItem) {
            return oldItem.equals(newItem);
        }
    }
}
