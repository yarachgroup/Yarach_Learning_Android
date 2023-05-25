package com.yarach.learning.holders;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yarach.learning.R;

public class MyListHolder extends RecyclerView.ViewHolder{

    public TextView name;
    public TextView kind;

    public MyListHolder(@NonNull View itemView) {
        super(itemView);
        // получение всех элементов
        name = (TextView) itemView.findViewById(R.id.list_item_text);
        kind = (TextView) itemView.findViewById(R.id.list_item_secondary_text);
    }
}