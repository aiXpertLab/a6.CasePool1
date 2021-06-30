package com.hypech.case83_room.L_RecyclerView;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.hypech.case83_room.L1_Entity;

public class AdapterWordList extends ListAdapter<L1_Entity, ViewHolderWord> {

    public AdapterWordList(@NonNull DiffUtil.ItemCallback<L1_Entity> diffCallback) {
        super(diffCallback);
    }

    @Override
    public ViewHolderWord onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolderWord.create(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolderWord holder, int position) {
        L1_Entity current = getItem(position);
        holder.bind(current.getWord());
    }

    public static class WordDiff extends DiffUtil.ItemCallback<L1_Entity> {

        @Override
        public boolean areItemsTheSame(@NonNull L1_Entity oldItem, @NonNull L1_Entity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull L1_Entity oldItem, @NonNull L1_Entity newItem) {
            return oldItem.getWord().equals(newItem.getWord());
        }
    }
}
