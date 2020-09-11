package com.criddam.medicine.rubelportion;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Noteadapter extends RecyclerView.Adapter<Noteadapter.Notcustclass> {

    Context context;
    String[] data;

    public Noteadapter(Context context, String[] data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public Notcustclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Notcustclass holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class Notcustclass extends RecyclerView.ViewHolder {
        public Notcustclass(@NonNull View itemView) {
            super(itemView);
        }
    }
}
