package com.example.digitalcookbook;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryRecipes extends AppCompatActivity {


    DatabaseReference db;
    RecyclerView recView;
    RecipeAdapter adapter;
    ArrayList<Recipe> recipeList = new ArrayList<>();
    DbHelper helper;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cat_recipe_list);

        // Get category chosen from bundle
        Bundle extras = getIntent().getExtras();
        final int cat = extras.getInt("Category");
        category = setCategory(cat);

        // RecyclerView
        recView = (RecyclerView) findViewById(R.id.recycler_view);
        recView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("Category","Category Chosen: " + category);
        Log.d("Category","recipeList length Before: " + recipeList.size());

        // RecyclerView Adapter
        adapter = new RecipeAdapter(this,recipeList);
        recView.setAdapter(adapter);

        // Firebase
        db= FirebaseDatabase.getInstance().getReference();
        helper=new DbHelper(db);
        recipeList = helper.read(adapter);
        Log.d("Category","recipeList length AFTER: " + recipeList.size());

    }

    public String setCategory(int num) {
        String category = "";
        switch(num)
        {
            case 0:
                category = "Breakfast and Brunch";
                break;
            case 1:
                category = "Lunch";
                break;
            case 2:
                category = "Dinner";
                break;
            case 3:
                category = "Appetizers and Snacks";
                break;
            case 4:
                category = "Desserts";
                break;
            case 5:
                category = "Drinks";
                break;
        }
        return category;
    }
}

//    public void synchronizeRecipes(){
//        db= FirebaseDatabase.getInstance().getReference(category);
//
//        final ArrayList<Recipe> updatedRecipes= new ArrayList<Recipe>();
//        db.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Recipe recipe = dataSnapshot.getValue(Recipe.class);
//                Log.d("TAG", "Title: " + recipe.getTitle());
//                Log.d("TAG", "Image: " + recipe.getImage());
//                Log.d("TAG", "Ingredients: " + recipe.getIngredients());
//                Log.d("TAG", "Steps: " + recipe.getSteps());
//                updatedRecipes.add(recipe);
//                setRecipes(updatedRecipes);
//                loadSampleData();
//            }
//
//            @Override
//            public void onCancelled(DatabaseReference firebaseError) { throw firebaseError.toException(); }
//        });
//    }

//public class MyAsyncTask extends AsyncTask<DatabaseReference ,Void,ArrayList<Recipe>> {
//    @Override
//    protected ArrayList<Recipe> doInBackground(DatabaseReference... db) {
//        DbHelper helper = new DbHelper(db[0]);
//        return helper.read();
//    }
//    @Override
//    protected void onPostExecute(ArrayList<Recipe> recipeArrayList) {
//        super.onPostExecute(recipeArrayList);
//
//    }
//}
