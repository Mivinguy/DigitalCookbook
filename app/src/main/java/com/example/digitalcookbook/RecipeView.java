package com.example.digitalcookbook;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class RecipeView extends AppCompatActivity {
    TextToSpeech currentStepTTS;
    View ingredientSide;
    View stepsSide;
    Boolean showBack = false;
    Button readNextStep;
    Button readPrevStep;
    Button readThisStep;

    List<String> steps = new ArrayList<String>();
    int currentStepNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);


        readNextStep =(Button)findViewById(R.id.readNextStep);
        readPrevStep =(Button)findViewById(R.id.readPrevStep);
        readThisStep =(Button)findViewById(R.id.readThisStep);
        ingredientSide = (View) findViewById(R.id.front_recipe);
        stepsSide = findViewById(R.id.back_recipe);

        currentStepTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    currentStepTTS.setLanguage(Locale.UK);
                }
            }
        });

        Intent intent = getIntent();
        HashMap<String, String> IngHashMap = (HashMap<String, String>) intent.getSerializableExtra("IngHashMap");
        final HashMap<String, String> StepHashMap = (HashMap<String, String>) intent.getSerializableExtra("StepHashMap");

        TextView ShowIngs = (TextView)findViewById(R.id.ingredientsList);
        for(Map.Entry<String,String > entry : IngHashMap.entrySet()){
            ShowIngs.setText(entry.getValue()+ "\n" + ShowIngs.getText());
        }

        TextView ShowSteps = (TextView)findViewById(R.id.stepsList);
        for(Map.Entry<String,String > entry : StepHashMap.entrySet()){
            ShowSteps.setText(entry.getValue()+ "\n" +ShowSteps.getText());
        }

        CharSequence charSequence = ShowSteps.getText();
        final StringBuilder sb = new StringBuilder(charSequence.length());
        sb.append(charSequence);
        String scannerIn =sb.toString();
        Scanner s = new Scanner(scannerIn);
        while(s.hasNextLine()) {
            steps.add(s.nextLine());
        }


        readNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentStepNum < steps.size()) {
                    currentStepNum++;
                    if (currentStepTTS.isSpeaking()) {
                        currentStepTTS.stop();
                    }
                    String toSpeak = steps.get(currentStepNum);
                    currentStepTTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);

                }
            }
        });

        readPrevStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentStepNum > 0 ) {
                    currentStepNum--;
                    if (currentStepTTS.isSpeaking()) {
                        currentStepTTS.stop();
                    }
                    String toSpeak = steps.get(currentStepNum);
                    currentStepTTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });

        readThisStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStepTTS.isSpeaking()) {
                    currentStepTTS.stop();
                }
                String toSpeak = steps.get(currentStepNum);
                currentStepTTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                }

        });
    }
    public void flipper(View v) {
        currentStepTTS.stop();
        if (showBack) {
            ingredientSide.setVisibility(View.VISIBLE);
            stepsSide.setVisibility(View.GONE);
            showBack = false;
        } else {
            ingredientSide.setVisibility(View.GONE);
            stepsSide.setVisibility(View.VISIBLE);
            showBack = true;
        }
    }
    public void onPause(){
        if(currentStepTTS !=null){
            currentStepTTS.stop();
            currentStepTTS.shutdown();
        }
        super.onPause();
    }
}



