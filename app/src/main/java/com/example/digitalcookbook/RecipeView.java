package com.example.digitalcookbook;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RecipeView extends AppCompatActivity {
    Recipe recipe;
    HashMap<String, String> ingredients = new HashMap<>();
    HashMap<String, String> steps = new HashMap<>();
    TextToSpeech currentStepTTS;
    Button readNextStep;
    int currentStepNum = 1;
    int stepNum = 1;
    int ingrNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        DatabaseReference mDatabase;
        readNextStep =(Button)findViewById(R.id.ttsButton);
        currentStepTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    currentStepTTS.setLanguage(Locale.UK);
                }
            }
        });
        //Intent getters
        Bundle extras = getIntent().getExtras();
        String categoryKey = extras.getString("Category");
        String recipeKey = extras.getString("RecipeKey");

        mDatabase = FirebaseDatabase.getInstance().getReference(categoryKey);
        mDatabase.child(recipeKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (!(dataSnapshot.getValue() instanceof Map<?, ?>)) {

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });


        mDatabase = FirebaseDatabase.getInstance().getReference(categoryKey);
        mDatabase.child(recipeKey).child("ingredients").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                TextView ShowIngredients = (TextView)findViewById(R.id.ingredientsList);
                ingredients.put("step"+ingrNum,"" + dataSnapshot.getValue());
                ShowIngredients.append("\n" + dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference(categoryKey);
        mDatabase.child(recipeKey).child("steps").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                TextView ShowSteps = (TextView)findViewById(R.id.stepsList);
                steps.put("step "+stepNum,"" + dataSnapshot.getValue());
                stepNum++;
                ShowSteps.append("\n\n" + dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }


            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        readNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if we want it to be loopable
                if (currentStepNum > steps.size()) {
                    currentStepNum = 1;
                }

                //dont talk over each other
                if (currentStepTTS.isSpeaking()) {
                    currentStepTTS.stop();
                }

                //what to say
                String toSpeak = steps.get("Step" + currentStepNum);

                currentStepTTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            }

        });
    }

    public void onPause(){
        if(currentStepTTS !=null){
            currentStepTTS.stop();
            currentStepTTS.shutdown();
        }
        super.onPause();
    }


}



