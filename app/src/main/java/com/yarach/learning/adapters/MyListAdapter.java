package com.yarach.learning.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yarach.learning.R;
import com.yarach.learning.holders.MyListHolder;

public class MyListAdapter extends RecyclerView.Adapter<MyListHolder>{

    private final Context mContext;

    public MyListAdapter(Context context) {
        this.mContext = context;
    }
    @NonNull
    @Override
    public MyListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_list_item, parent, false);
        return new MyListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyListHolder holder, int position) {
        // получение отображаемого элемента
        
        // установка значений в элемент списка
        holder.name.setText("Английский А2");
        holder.kind.setText("Английский");

        // слушатель на клик на строку
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // создание диалогового окна
                Context mContext = null;
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(mContext);
                // наполнение окна
                dialogBuilder.setMessage("Английский описание");
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}