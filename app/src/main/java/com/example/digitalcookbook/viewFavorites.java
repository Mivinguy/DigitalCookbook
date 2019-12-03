package com.example.digitalcookbook;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class viewFavorites extends AppCompatActivity {

    private List<favRecipe> mFavList;
    private favoritesDB mFavDB;
    private favRecipe mRecipe;
    TextView mText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fav_activity);
        mText = (TextView) findViewById(R.id.item);

        mFavDB = favoritesDB.getInstance(getApplicationContext());

        mFavList = mFavDB.getFavs();
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < mFavList.size(); i++){
            mRecipe = mFavList.get(i);
            result.append(mRecipe.getTitle());
            result.append(mRecipe.getImageFileName());
            result.append(mRecipe.getIngredients());
            result.append(mRecipe.getSteps());
            result.append("\n\n");
        }
        mText.setText(result);
    }
}
