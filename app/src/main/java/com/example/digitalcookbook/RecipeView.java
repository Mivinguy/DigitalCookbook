package com.example.digitalcookbook;

import android.content.Intent;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import android.os.Vibrator;
import androidx.core.view.GestureDetectorCompat;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.widget.Toast;
import java.lang.StrictMath;
import android.view.GestureDetector;

public class RecipeView extends AppCompatActivity implements SensorEventListener {
    Recipe recipe;
    HashMap<String, String> ingredients = new HashMap<>();
    //HashMap<String, String> steps = new HashMap<>();

    TextToSpeech currentStepTTS;
    View ingredientSide;
    View stepsSide;
    Boolean showBack = false;
    Button readNextStep;
    Button readPrevStep;
    Button readThisStep;

    List<String> steps = new ArrayList<String>();
    int currentStepNum = 0;

    SensorManager sensorManager;
    Sensor accelerometer;
    Vibrator vibrator;
    private final String TAG = "GestureDemo";
    private GestureDetectorCompat mDetector;
    private static final int SHAKE_THRESHOLD = 12;
    long lastUpdate;
    float threshold;
    float prevX;
    float prevY;
    float prevZ;

    boolean buttonPressed = false;

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

        String imageFileName = (String) intent.getSerializableExtra("ImageFileName");


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
        
        ImageView img = (ImageView)findViewById(R.id.recipe_image);
        imageFileName = imageFileName.substring(0, imageFileName.lastIndexOf('.'));
        int id = getResources().getIdentifier(imageFileName, "drawable", getPackageName());
        img.setImageResource(id);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            threshold = accelerometer.getMaximumRange()/8;
        }
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        mDetector = new GestureDetectorCompat(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });

        readNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentStepNum < steps.size()-1) {
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
                    buttonPressed = true; // turns on sensor
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


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause(){
        if(currentStepTTS !=null){
            currentStepTTS.stop();
            currentStepTTS.shutdown();
        }
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    private class MyGestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.d(TAG, "onDown");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d(TAG, "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d(TAG, "onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d(TAG, "onScroll");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d(TAG, "onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(TAG, "onFling");
            return false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float ax, ay, az;

        ax = event.values[0];
        ay = event.values[1];
        az = event.values[2];

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if(buttonPressed == true) {
                long currTime = System.currentTimeMillis();
                // check in intervals of 25 milliseconds
                if ((currTime - lastUpdate) > 50) {
                    long diffTime = (currTime - lastUpdate);
                    lastUpdate = currTime;

                    //float speed = Math.abs(ax + ay + az - prevX - prevY - prevZ) / diffTime * 10000;
                    float speed = Math.abs(ax + az - prevX - prevZ) / diffTime * 10000;


                    if (speed > SHAKE_THRESHOLD) {
                        Toast.makeText(this, "Device was shaken", Toast.LENGTH_SHORT).show();
                        buttonPressed = false;
                    }

                    prevX = ax;
                    prevY = ay;
                    prevZ = az;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}



