package com.example.digitalcookbook;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeListViewHolder> {
    Context c;
    ArrayList<Recipe> recipeList;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
        this.c = context;
    }

    @Override
    public RecipeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.recipe_row,parent,false);
        return new RecipeListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecipeListViewHolder holder, int position) {
        holder.recipe_title.setText(recipeList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

}