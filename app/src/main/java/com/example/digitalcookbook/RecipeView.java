package com.example.digitalcookbook;

import android.content.Intent;
import android.hardware.SensorEvent;
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
    HashMap<String, String> steps = new HashMap<>();
    TextToSpeech currentStepTTS;
    Button readNextStep;
    int currentStepNum = 1;
    int stepNum = 1;
    int ingrNum = 1;

    SensorManager sensorManager;
    Sensor accelerometer;
    Vibrator vibrator;
    private final String TAG = "GestureDemo";
    private GestureDetectorCompat mDetector;
    private static final int SHAKE_THRESHOLD = 100;
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

        Intent intent = getIntent();
        HashMap<String, String> IngHashMap = (HashMap<String, String>) intent.getSerializableExtra("IngHashMap");
        HashMap<String, String> StepHashMap = (HashMap<String, String>) intent.getSerializableExtra("StepHashMap");

        TextView ShowIngs = (TextView)findViewById(R.id.ingredientsList);
        for(Map.Entry<String,String > entry : IngHashMap.entrySet()){
            ShowIngs.setText(ShowIngs.getText() + "\n" + entry.getValue());
        }

        TextView ShowSteps = (TextView)findViewById(R.id.stepsList);
        for(Map.Entry<String,String > entry : StepHashMap.entrySet()){
            ShowSteps.setText(ShowSteps.getText() + "\n" + entry.getValue());
        }

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
                buttonPressed = true; // turns on sensor

                //if we want it to be loopable
                if (currentStepNum > steps.size()) {
                    currentStepNum = 1;
                }

                //dont talk over each other
                if (currentStepTTS.isSpeaking()) {
                    currentStepTTS.stop();
                }

                //what to say
                TextView ShowSteps = (TextView)findViewById(R.id.stepsList);
                CharSequence charSequence = ShowSteps.getText();
                final StringBuilder sb = new StringBuilder(charSequence.length());
                sb.append(charSequence);
                String toSpeak = sb.toString();
                currentStepTTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            }

        });
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
            if(buttonPressed = true) {
                long currTime = System.currentTimeMillis();
                // check in intervals of 25 milliseconds
                if ((currTime - lastUpdate) > 25) {
                    long diffTime = (currTime - lastUpdate);
                    lastUpdate = currTime;

                    float speed = Math.abs(ax + ay + az - prevX - prevY - prevZ) / diffTime * 10000;

                    if (speed > SHAKE_THRESHOLD) {
                        Toast.makeText(this, "Device was shaken", Toast.LENGTH_SHORT).show();
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



