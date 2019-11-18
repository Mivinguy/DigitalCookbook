package com.example.digitalcookbook;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        final RecyclerView.ViewHolder holder1 = holder;

        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "Ingrediants = " + recipeList.get(holder1.getAdapterPosition()).getIngredients());
                Log.d("TAG", "Steps = " + recipeList.get(holder1.getAdapterPosition()).getSteps());

                Intent intent = new Intent(v.getContext(), RecipeView.class);
                intent.putExtra("IngHashMap",recipeList.get(holder1.getAdapterPosition()).getIngredients());
                intent.putExtra("StepHashMap",recipeList.get(holder1.getAdapterPosition()).getSteps());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

}