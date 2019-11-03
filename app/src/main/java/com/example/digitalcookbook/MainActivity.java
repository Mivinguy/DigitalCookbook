package com.example.digitalcookbook;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.gridlayout.widget.GridLayout;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    GridLayout mainGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set onClickListeners for category cards
        mainGrid = findViewById(R.id.grid);
        setSingleEvent(mainGrid);


        // Floating Action Button
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void setSingleEvent(GridLayout mainGrid) {
        // Set onClickListeners for each CardView
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView)mainGrid.getChildAt(i);
            final int selected = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, CategoryRecipes.class);
                        intent.putExtra("Category",selected);
//
//                    if (selected == 0) {
//                        intent.putExtra("Category",selected);
//                    } else if (selected == 1) {
//                        intent.putExtra("Category",selected);
//                    } else if (selected == 2) {
//                        intent.putExtra("Category",selected);
//                    } else if (selected == 3) {
//                        intent.putExtra("Category",selected);
//                    }
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

