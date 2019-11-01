package com.example.digitalcookbook;

import java.util.HashMap;

public class Recipe {
    private String image;
    private HashMap<String, String> ingredients;
    private HashMap<String, String> steps;
    private String title;

    public Recipe() {

    }

    public Recipe(String image, HashMap<String, String> ingredients, HashMap<String, String> steps, String title){
        this.image = image;
        this.ingredients = ingredients;
        this.steps = steps;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public HashMap<String, String> getIngredients() {
        return ingredients;
    }

    public HashMap<String, String> getSteps() {
        return steps;
    }

    public String getTitle() {
        return title;
    }

}
