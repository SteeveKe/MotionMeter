package com.capucinetulipe.motionmeter.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.capucinetulipe.motionmeter.R;

public class FolderViewHolder extends RecyclerView.ViewHolder {
    private final TextView folderNameViewItem;
    private final ImageView folderIconViewItem;

    public FolderViewHolder(@NonNull View itemView) {
        super(itemView);
        folderNameViewItem = itemView.findViewById(R.id.folderNameItem);
        folderIconViewItem = itemView.findViewById(R.id.folderIconItem);
    }

    public void bind(String text, int rID){
        folderNameViewItem.setText(text);
        folderIconViewItem.setImageResource(rID);
    }

    static FolderViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.folder_recycler_item, parent, false);
        return new FolderViewHolder(view);
    }
}
