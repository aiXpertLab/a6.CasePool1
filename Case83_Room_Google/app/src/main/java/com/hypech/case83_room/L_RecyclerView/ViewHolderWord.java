package com.hypech.case83_room.L_RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hypech.case83_room.R;

class ViewHolderWord extends RecyclerView.ViewHolder {
        private final TextView wordItemView;

        private ViewHolderWord(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
        }

        public void bind(String text) {
            wordItemView.setText(text);
        }

        static ViewHolderWord create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_item, parent, false);
            return new ViewHolderWord(view);
        }
}
