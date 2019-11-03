package com.example.digitalcookbook;

import android.view.View;
import android.widget.TextView;

import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;


public class RecipeListViewHolder extends RecyclerView.ViewHolder {
    TextView recipe_title;

    public RecipeListViewHolder(View itemView) {
        super(itemView);
        recipe_title = (TextView) itemView.findViewById(R.id.row_title);
    }
}
